package gg.warcraft.monolith.api.player.statistic

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.types.DatabaseContext
import gg.warcraft.monolith.api.util.chaining._
import gg.warcraft.monolith.api.util.future._

import scala.collection.concurrent
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success}
import scala.util.chaining._

class StatisticService(implicit
    logger: Logger,
    executionContext: ExecutionContext,
    database: DatabaseContext,
    taskService: TaskService
) {
  import database._

  private val _statistics: concurrent.Map[UUID, Statistics] =
    new ConcurrentHashMap[UUID, Statistics]().asScala

  private[statistic] def loadStatistics(playerId: UUID): Unit =
    Await.result(statisticsFuture(playerId), 1000.millis)
      .tap { _statistics.update(playerId, _) }

  private[statistic] def invalidateStatistics(playerId: UUID): Unit =
    _statistics.remove(playerId)

  def statistics(playerId: UUID): Statistics =
    statisticsFuture(playerId).getOrThrow

  def statisticsFuture(playerId: UUID): Future[Statistics] =
    _statistics.get(playerId) match {
      case Some(statistics) => Future.successful(statistics)
      case None =>
        Future {
          database.run { query[Statistic].filter { _.playerId == lift(playerId) } }
            .iterator.map { it => it.statistic -> it }
            .toMap.pipe { new Statistics(_) }
          // don't update cache for offline players
        }
    }

  private def updateStatistic(
      transform: Statistic => Statistic,
      deltaStatistics: Statistic*
  ): Future[Unit] = {
    deltaStatistics.foreach { it =>
      _statistics.updateWith(it.playerId) {
        case Some(statistics) =>
          val playerStatistic = statistics.all.get(it.statistic) match {
            case Some(statistic) => statistic
            case None            => Statistic(it.playerId, it.statistic)
          }
          val newStatistic = playerStatistic |> transform |> { it.statistic -> _ }
          new Statistics(statistics.all + newStatistic) |> Some.apply
        case None => None // don't update cache for offline players
      }
    }

    Future {
      database.run {
        liftQuery(deltaStatistics).foreach { statistic =>
          query[Statistic]
            .insert { statistic }
            .onConflictUpdate(_.playerId, _.statistic) {
              (t, e) => t.value -> (t.value + e.value)
            }
        }
      }
    }
  }

  /** Increases a player's statistic by amount. Statistics are permanent. For daily
    * or weekly statistics that need to be reset append the statistic's name with a
    * unique identifier. */
  def increaseStatistic(statistic: String, amount: Long, playerIds: UUID*): Unit = {
    val deltaStatistic = playerIds.map { Statistic(_, statistic, amount) }
    updateStatistic(_.increase(amount), deltaStatistic: _*).immediatelyOrOnComplete {
      case Success(_) => // TODO fire statistic events
      case Failure(exception) =>
        logger.severe {
          s"""Failed to increase $statistic by $amount for ${playerIds.size} players!
             |${exception.getMessage}
             |${playerIds.mkString("\n")}""".stripMargin
        }
    }
  }

  def increaseStatistic(playerId: UUID, amount: Long, statistics: String*): Unit = {
    val deltaStatistic = statistics.map { Statistic(playerId, _, amount) }
    updateStatistic(_.increase(amount), deltaStatistic: _*).immediatelyOrOnComplete {
      case Success(_) => // TODO fire statistic events
      case Failure(exception) =>
        logger.severe {
          s"""Failed to increase ${statistics.size} statistics by $amount for $playerId!
             |${exception.getMessage}
             |${statistics.mkString("\n")}""".stripMargin
        }
    }
  }
}

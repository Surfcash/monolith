package gg.warcraft.monolith.api.entity.status

import java.util.UUID

import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.event.{Event, EventService}
import gg.warcraft.monolith.api.player.{PlayerConnectEvent, PlayerDisconnectEvent}

import scala.collection.mutable

private object StatusService {
  private val statuses = mutable.Map.empty[UUID, Status]
}

class StatusService(
    private implicit val eventService: EventService,
    private implicit val taskService: TaskService
) extends Event.Handler {
  import StatusService._

  def getStatus(entityId: UUID): Status =
    statuses.getOrElseUpdate(entityId, new Status)

  def addEffect(entityId: UUID, effects: StatusEffect*): Unit = {
    val status = getStatus(entityId)
    effects.foreach(effect => {
      if (status.effects.add(effect)) {
        effect.duration.foreach(it => {
          taskService.runLater(it, () => removeEffect(entityId, effect))
        })

        val event = StatusEffectGainedEvent(entityId, effect)
        eventService.publish(event)
      }
    })
  }

  def removeEffect(entityId: UUID, effects: StatusEffect*): Unit = {
    val status = getStatus(entityId)
    effects.foreach(effect => {
      if (status.effects.remove(effect)) {
        val event = StatusEffectLostEvent(entityId, effect)
        eventService.publish(event)
      }
    })
  }

  override def handle(event: Event): Unit = event match {
    case it: PlayerConnectEvent    => statuses.put(it.playerId, new Status)
    case it: PlayerDisconnectEvent => statuses.remove(it.playerId)
  }

  // TODO create/delete statuses for non-player Entities
}

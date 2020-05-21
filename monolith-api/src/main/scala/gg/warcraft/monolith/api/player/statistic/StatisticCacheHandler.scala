package gg.warcraft.monolith.api.player.statistic

import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.player.{PlayerDisconnectEvent, PlayerPreConnectEvent}

private[monolith] class StatisticCacheHandler(implicit
    service: StatisticService
) extends Event.Handler {
  override def handle(event: PreEvent): Unit = event match {
    case PlayerPreConnectEvent(id, _, _, _) => service.loadStatistics(id)
    case _                                  =>
  }

  override def handle(event: Event): Unit = event match {
    case PlayerDisconnectEvent(player) => service.invalidateStatistics(player.id)
    case _                             =>
  }
}

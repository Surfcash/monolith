/*
 * MIT License
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gg.warcraft.monolith.api.entity.team

import gg.warcraft.monolith.api.core.auth.{AuthService, Principal}
import gg.warcraft.monolith.api.core.command.{Command, CommandService}
import gg.warcraft.monolith.api.core.event.{Event, EventService}
import gg.warcraft.monolith.api.entity.EntityTeamChangedEvent
import gg.warcraft.monolith.api.player.Player
import gg.warcraft.monolith.api.player.data.PlayerDataService

class TeamStaffCommandHandler(implicit
    eventService: EventService,
    commandService: CommandService,
    authService: AuthService,
    teamService: TeamService,
    playerDataService: PlayerDataService
) extends Command.Handler
    with Event.Handler {
  import teamService._

  override def handle(
      principal: Principal,
      command: Command,
      args: String*
  ): Command.Result = principal match {
    case player: Player =>
      if (!authService.isStaff(player)) Command.invalid
      else if (args.nonEmpty) Command.invalid
      else if (!teams.contains(command.name)) Command.invalid
      else {
        val team = teams.get(command.name)
        val oldData = player.data
        if (oldData.team != team) {
          val newData = oldData.copy(team = team)
          playerDataService.setPlayerData(newData)

          // TODO fire this event in an appropriate place
          eventService.publish(
            EntityTeamChangedEvent(
              player,
              oldData.team,
              newData.team
            )
          )
        }
        Command.success
      }
    case _ => Command.playersOnly
  }

  override def handle(event: Event): Unit = event match {
    case TeamRegisteredEvent(name, _, _) =>
      val command = Command(name, Nil)
      commandService.registerCommand(command, this)
    case _ =>
  }
}

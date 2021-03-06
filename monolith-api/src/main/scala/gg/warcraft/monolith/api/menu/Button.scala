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

package gg.warcraft.monolith.api.menu

import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.item.{ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.player.Player

case class Button(
    icon: ItemTypeOrVariant,
    title: String,
    tooltip: List[String]
)(val action: Click => Unit) {
  lazy val formattedTitle: String = s"$WHITE$title"
  lazy val formattedTooltip: List[String] = tooltip.map { it => s"$GRAY$it" }
}

trait SkullButton extends Button {
  val playerName: String
}

object Button {
  def skull(
      name: String,
      title: String,
      tooltip: List[String]
  )(action: Click => Unit): Button =
    new Button(ItemType.MOB_HEAD, title, tooltip)(action) with SkullButton {
      override val playerName: String = name
    }

  def back(menu: Player => Menu)(implicit
      menuService: MenuService
  ): Button = {
    val tooltip = "[CLICK] To go back to" :: "the previous menu" :: Nil
    Button(ItemType.DOOR, "Back", tooltip) { click =>
      menuService.showMenu(click.player.id, menu(click.player))
    }
  }
}

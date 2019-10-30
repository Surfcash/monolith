package gg.warcraft.monolith.spigot.world

import org.bukkit.block.{ Block, BlockState, Sign }
import org.bukkit.block.data.{ BlockData, Rail }
import org.bukkit.block.data.`type`._

package object block {

  // Block meta types
  type SpigotBlock = Block
  type SpigotBlockData = BlockData
  type SpigotBlockState = BlockState

  // Spigot block types
  type SpigotBamboo = Bamboo
  type SpigotBed = Bed
  type SpigotBubbleColumn = BubbleColumn
  type SpigotCake = Cake
  type SpigotCampfire = Campfire
  type SpigotCommandBlock = CommandBlock
  type SpigotComparator = Comparator
  type SpigotDoor = Door
  type SpigotEndPortalFrame = EndPortalFrame
  type SpigotHopper = Hopper
  type SpigotJukebox = Jukebox
  type SpigotLantern = Lantern
  type SpigotLectern = Lectern
  type SpigotNoteBlock = NoteBlock
  type SpigotPiston = Piston
  type SpigotRails = Rail
  type SpigotRepeater = Repeater
  type SpigotSapling = Sapling
  type SpigotSeaPickle = SeaPickle
  type SpigotSign = Sign
  type SpigotStairs = Stairs
  type SpigotStructureBlock = StructureBlock
  type SpigotTNT = TNT
  type SpigotTurtleEgg = TurtleEgg

  // Mapper utility functions
  def dataAs[T <: SpigotBlockData](implicit data: SpigotBlockData): T =
    data.asInstanceOf[T]
}

package gg.warcraft.monolith.api.world.block.`type`

import gg.warcraft.monolith.api.world.BlockLocation
import gg.warcraft.monolith.api.world.block._

case class Trapdoor(
  location: BlockLocation,
  material: TrapdoorMaterial,
  direction: BlockFace,
  section: BlockBisection,
  powered: Boolean,
  flooded: Boolean,
  open: Boolean
) extends MaterialBlock[TrapdoorMaterial] with DirectedBlock with BisectedBlock
  with PowerableBlock with FloodableBlock with OpenableBlock {
  override val kind = BlockType.TRAPDOOR

  /* Java interop */

  override def withLocation(loc: BlockLocation): Trapdoor = copy(location = loc)
  override def withMaterial(mat: TrapdoorMaterial): Trapdoor = copy(material = mat)
  override def withDirection(facing: BlockFace): Trapdoor = copy(direction = facing)
  override def withSection(section: BlockBisection): Trapdoor = copy(section = section)
  override def withPowered(powered: Boolean): Trapdoor = copy(powered = powered)
  override def withFlooded(flooded: Boolean): Trapdoor = copy(flooded = flooded)
  override def withOpen(open: Boolean): Trapdoor = copy(open = open)
}

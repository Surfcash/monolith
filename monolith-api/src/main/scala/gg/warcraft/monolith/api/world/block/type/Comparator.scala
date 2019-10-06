package gg.warcraft.monolith.api.world.block.`type`

import gg.warcraft.monolith.api.world.block.state.ComparatorState
import gg.warcraft.monolith.api.world.BlockLocation
import gg.warcraft.monolith.api.world.block._

case class Comparator(
  location: BlockLocation,
  state: ComparatorState,
  direction: BlockFace,
  powered: Boolean
) extends StatefulBlock[ComparatorState] with DirectedBlock with PowerableBlock {
  override val kind = BlockType.COMPARATOR

  /* Java interop */

  override def withLocation(loc: BlockLocation): Comparator = copy(location = loc)
  override def withState(state: ComparatorState): Comparator = copy(state = state)
  override def withDirection(facing: BlockFace): Comparator = copy(direction = facing)
  override def withPowered(powered: Boolean): Comparator = copy(powered = powered)
}

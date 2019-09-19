package gg.warcraft.monolith.spigot.world.block

import com.google.inject.Inject
import gg.warcraft.monolith.api.world.block._
import gg.warcraft.monolith.api.world.block.state._
import gg.warcraft.monolith.spigot.world.SpigotLocationMapper
import org.bukkit.Material
import org.bukkit.block.{ Block => SpigotBlock }
import org.bukkit.block.data.{ Ageable, AnaloguePowerable, Levelled }
import org.bukkit.block.data.`type`.{ Cake => SpigotCake, Comparator => SpigotComparator, Sapling => SpigotSapling, StructureBlock => SpigotStructureBlock }

class SpigotBlockStateMapper @Inject()(
  private val locationMapper: SpigotLocationMapper
) {

  def map(block: SpigotBlock): BlockState = {
    val state = block.getState

    lazy val age = s"AGE_${ block.getState.asInstanceOf[Ageable].getAge }"
    lazy val level = s"LEVEL_${ block.getState.asInstanceOf[Levelled].getLevel }"
    lazy val power = s"POWER_${ block.getState.asInstanceOf[AnaloguePowerable].getPower }"

    block.getType match {
      case Material.BAMBOO => BambooState.valueOf(age)
      case Material.BEETROOTS => BeetrootState.valueOf(age)
      case Material.CACTUS => CactusState.valueOf(age)
      case Material.CHORUS_FLOWER => ChorusFlowerState.valueOf(age)
      case Material.COCOA => CocoaState.valueOf(age)
      case Material.COMPOSTER => ComposterState.valueOf(level)
      case Material.KELP_PLANT => KelpState.valueOf(age)
      case Material.LAVA => LavaState.valueOf(level)
      case Material.NETHER_WART => NetherWartState.valueOf(age)
      case Material.POTATOES => PotatoState.valueOf(age)
      case Material.REDSTONE_WIRE => RedstoneWireState.values()
      case Material.REPEATER => RepeaterState.values()
      case Material.SEA_PICKLE => SeaPickleState.values()
      case Material.SUGAR_CANE => SugarCaneState.valueOf(age)
      case Material.SWEET_BERRY_BUSH => SweetBerryState.valueOf(age)
      case Material.TURTLE_EGG => TurtleEggState.values()
      case Material.WATER => WaterState.valueOf(level)
      case Material.WHEAT => WheatState.valueOf(age)

      // ANVIL
      case Material.ANVIL => AnvilState.PRISTINE
      case Material.CHIPPED_ANVIL => AnvilState.CHIPPED
      case Material.DAMAGED_ANVIL => AnvilState.DAMAGED

      // CAKE
      case Material.CAKE =>
        val bites = state.asInstanceOf[SpigotCake].getBites
        CakeState.valueOf(s"EATEN_$bites")

      // COMPARATOR
      case Material.COMPARATOR =>
        val mode = state.asInstanceOf[SpigotComparator].getMode
        mapComparatorMode(mode)

      // MELON_STEM
      case Material.MELON_STEM | Material.ATTACHED_MELON_STEM =>
        MelonStemState.valueOf(age)

      // NOTE_BLOCK
      case Material.NOTE_BLOCK =>
        NoteBlockState.values()

      // PUMPKIN_STEM
      case Material.PUMPKIN_STEM | Material.ATTACHED_PUMPKIN_STEM =>
        PumpkinStemState.valueOf(age)

      // RAIL
      case Material.RAIL | Material.ACTIVATOR_RAIL | Material.DETECTOR_RAIL | Material.POWERED_RAIL =>
        RailsState.values()

      // SANDSTONE TODO add slab stairs wall etc
      case Material.SANDSTONE | Material.RED_SANDSTONE =>
        SandstoneState.NORMAL

      case Material.CHISELED_SANDSTONE | Material.CHISELED_RED_SANDSTONE =>
        SandstoneState.CHISELED

      case Material.CUT_SANDSTONE | Material.CUT_RED_SANDSTONE =>
        SandstoneState.CUT

      case Material.SMOOTH_SANDSTONE | Material.SMOOTH_RED_SANDSTONE =>
        SandstoneState.SMOOTH

      // SAPLING
      case Material.BAMBOO_SAPLING |
           Material.ACACIA_SAPLING | Material.BIRCH_SAPLING | Material.DARK_OAK_SAPLING |
           Material.JUNGLE_SAPLING | Material.OAK_SAPLING | Material.SPRUCE_SAPLING =>
        val stage = s"AGE_${ state.asInstanceOf[SpigotSapling].getStage }"
        SaplingState.valueOf(stage)

      // STRUCTURE_BLOCK
      case Material.STRUCTURE_BLOCK =>
        val mode = state.asInstanceOf[SpigotStructureBlock].getMode
        mapStructureBlockMode(mode)

      // WEIGHTED_PRESSURE_PLATE
      case Material.LIGHT_WEIGHTED_PRESSURE_PLATE | Material.HEAVY_WEIGHTED_PRESSURE_PLATE =>
        WeightedPressurePlateState.valueOf(power)

      case _ => throw new IllegalArgumentException(s"Failed to map state for material: ${ block.getType }")
    }
  }

  def mapComparatorMode(mode: SpigotComparator.Mode): ComparatorState = mode match {
    case SpigotComparator.Mode.COMPARE => ComparatorState.COMPARE
    case SpigotComparator.Mode.SUBTRACT => ComparatorState.SUBTRACT
  }

  def mapStructureBlockMode(mode: SpigotStructureBlock.Mode): StructureBlockState = mode match {
    case SpigotStructureBlock.Mode.CORNER => StructureBlockState.CORNER
    case SpigotStructureBlock.Mode.DATA => StructureBlockState.DATA
    case SpigotStructureBlock.Mode.LOAD => StructureBlockState.LOAD
    case SpigotStructureBlock.Mode.SAVE => StructureBlockState.SAVE
  }
}

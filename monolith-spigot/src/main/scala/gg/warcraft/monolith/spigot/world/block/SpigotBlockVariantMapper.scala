package gg.warcraft.monolith.spigot.world.block

import java.util

import gg.warcraft.monolith.api.core.Extensions._
import gg.warcraft.monolith.api.world.block._
import gg.warcraft.monolith.api.world.block.material.{ BrickMaterial, CobblestoneMaterial,
  EndStoneMaterial, PrismarineMaterial, PurpurMaterial, QuartzMaterial, SandstoneMaterial,
  StoneMaterial, StoniteMaterial, WoodMaterial }
import gg.warcraft.monolith.api.world.block.variant._
import org.bukkit.{ Material, Instrument => SpigotInstrument }
import org.bukkit.block.data.`type`.Bamboo.{ Leaves => SpigotBambooLeaves }
import org.bukkit.block.data.`type`.Comparator.{ Mode => SpigotComparatorMode }
import org.bukkit.block.data.`type`.StructureBlock.{ Mode => SpigotStructureBlockMode }

class SpigotBlockVariantMapper {

  private val cache =
    new util.EnumMap[Material, BlockVariant](classOf[Material])

  def map(block: SpigotBlock): BlockVariant = {
    implicit val data: SpigotBlockData = block.getState.getBlockData

    block.getType match {
      case Material.BAMBOO => dataAs[SpigotBamboo].getLeaves match {
        case SpigotBambooLeaves.NONE  => BambooVariant.NO_LEAVES
        case SpigotBambooLeaves.SMALL => BambooVariant.SMALL_LEAVES
        case SpigotBambooLeaves.LARGE => BambooVariant.LARGE_LEAVES
      }

      case Material.COMPARATOR => dataAs[SpigotComparator].getMode match {
        case SpigotComparatorMode.COMPARE  => ComparatorVariant.COMPARE
        case SpigotComparatorMode.SUBTRACT => ComparatorVariant.SUBTRACT
      }

      case Material.NOTE_BLOCK => dataAs[SpigotNoteBlock].getInstrument match {
        case SpigotInstrument.BANJO          => NoteBlockVariant.BANJO
        case SpigotInstrument.BASS_DRUM      => NoteBlockVariant.BASS_DRUM
        case SpigotInstrument.BASS_GUITAR    => NoteBlockVariant.BASS_GUITAR
        case SpigotInstrument.BELL           => NoteBlockVariant.BELL
        case SpigotInstrument.BIT            => NoteBlockVariant.BIT
        case SpigotInstrument.CHIME          => NoteBlockVariant.CHIME
        case SpigotInstrument.COW_BELL       => NoteBlockVariant.COW_BELL
        case SpigotInstrument.DIDGERIDOO     => NoteBlockVariant.DIDGERIDOO
        case SpigotInstrument.FLUTE          => NoteBlockVariant.FLUTE
        case SpigotInstrument.GUITAR         => NoteBlockVariant.GUITAR
        case SpigotInstrument.IRON_XYLOPHONE => NoteBlockVariant.IRON_XYLOPHONE
        case SpigotInstrument.PIANO          => NoteBlockVariant.HAT
        case SpigotInstrument.PLING          => NoteBlockVariant.PLING
        case SpigotInstrument.SNARE_DRUM     => NoteBlockVariant.SNARE_DRUM
        case SpigotInstrument.STICKS         => NoteBlockVariant.HARP
        case SpigotInstrument.XYLOPHONE      => NoteBlockVariant.XYLOPHONE
      }

      case Material.STRUCTURE_BLOCK => dataAs[SpigotStructureBlock].getMode match {
        case SpigotStructureBlockMode.CORNER => StructureBlockVariant.CORNER
        case SpigotStructureBlockMode.DATA   => StructureBlockVariant.DATA
        case SpigotStructureBlockMode.LOAD   => StructureBlockVariant.LOAD
        case SpigotStructureBlockMode.SAVE   => StructureBlockVariant.SAVE
      }

      case _ => cache.computeIfAbsent(block.getType, _ => map(block.getType))
    }
  }

  def map(material: Material): BlockVariant = material match {

    // AIR
    case Material.AIR      => AirVariant.NORMAL
    case Material.CAVE_AIR => AirVariant.CAVE
    case Material.VOID_AIR => AirVariant.VOID

    // ANVIL
    case Material.ANVIL         => AnvilVariant.NORMAL
    case Material.CHIPPED_ANVIL => AnvilVariant.CHIPPED
    case Material.DAMAGED_ANVIL => AnvilVariant.DAMAGED

    // CHEST
    case Material.CHEST         => ChestVariant.NORMAL
    case Material.ENDER_CHEST   => ChestVariant.ENDER
    case Material.TRAPPED_CHEST => ChestVariant.TRAPPED

    // COMMAND_BLOCK
    case Material.COMMAND_BLOCK           => CommandBlockVariant.NORMAL
    case Material.CHAIN_COMMAND_BLOCK     => CommandBlockVariant.CHAIN
    case Material.REPEATING_COMMAND_BLOCK => CommandBlockVariant.REPEATING

    // FLOWER
    case Material.ALLIUM             => FlowerVariant.ALLIUM
    case Material.AZURE_BLUET        => FlowerVariant.AZURE_BLUET
    case Material.BLUE_ORCHID        => FlowerVariant.BLUE_ORCHID
    case Material.CORNFLOWER         => FlowerVariant.CORNFLOWER
    case Material.DANDELION          => FlowerVariant.DANDELION
    case Material.LILY_OF_THE_VALLEY => FlowerVariant.LILY_OF_THE_VALLEY
    case Material.ORANGE_TULIP       => FlowerVariant.ORANGE_TULIP
    case Material.OXEYE_DAISY        => FlowerVariant.OXEYE_DAISY
    case Material.PINK_TULIP         => FlowerVariant.PINK_TULIP
    case Material.POPPY              => FlowerVariant.POPPY
    case Material.RED_TULIP          => FlowerVariant.RED_TULIP
    case Material.WHITE_TULIP        => FlowerVariant.WHITE_TULIP
    case Material.WITHER_ROSE        => FlowerVariant.WITHER_ROSE

    // FLOWER_POT
    case Material.POTTED_ALLIUM             => FlowerVariant.ALLIUM
    case Material.POTTED_AZURE_BLUET        => FlowerVariant.AZURE_BLUET
    case Material.POTTED_BLUE_ORCHID        => FlowerVariant.BLUE_ORCHID
    case Material.POTTED_CORNFLOWER         => FlowerVariant.CORNFLOWER
    case Material.POTTED_DANDELION          => FlowerVariant.DANDELION
    case Material.POTTED_ORANGE_TULIP       => FlowerVariant.ORANGE_TULIP
    case Material.POTTED_OXEYE_DAISY        => FlowerVariant.OXEYE_DAISY
    case Material.POTTED_PINK_TULIP         => FlowerVariant.PINK_TULIP
    case Material.POTTED_POPPY              => FlowerVariant.POPPY
    case Material.POTTED_RED_TULIP          => FlowerVariant.RED_TULIP
    case Material.POTTED_WHITE_TULIP        => FlowerVariant.WHITE_TULIP
    case Material.POTTED_WITHER_ROSE        => FlowerVariant.WITHER_ROSE
    case Material.POTTED_LILY_OF_THE_VALLEY => FlowerVariant.LILY_OF_THE_VALLEY

    case Material.POTTED_BROWN_MUSHROOM => MushroomVariant.BROWN
    case Material.POTTED_RED_MUSHROOM   => MushroomVariant.RED

    case Material.POTTED_CACTUS    => PlantVariant.CACTUS
    case Material.POTTED_DEAD_BUSH => PlantVariant.DEAD_BUSH
    case Material.POTTED_FERN      => PlantVariant.FERN

    case Material.POTTED_BAMBOO           => SaplingVariant.BAMBOO
    case Material.POTTED_ACACIA_SAPLING   => SaplingVariant.ACACIA
    case Material.POTTED_BIRCH_SAPLING    => SaplingVariant.BIRCH
    case Material.POTTED_DARK_OAK_SAPLING => SaplingVariant.DARK_OAK
    case Material.POTTED_JUNGLE_SAPLING   => SaplingVariant.JUNGLE
    case Material.POTTED_OAK_SAPLING      => SaplingVariant.OAK
    case Material.POTTED_SPRUCE_SAPLING   => SaplingVariant.SPRUCE

    // ICE
    case Material.ICE        => IceVariant.NORMAL
    case Material.BLUE_ICE   => IceVariant.BLUE
    case Material.PACKED_ICE => IceVariant.PACKED

    // INFESTED_BLOCK
    case Material.INFESTED_COBBLESTONE           => CobblestoneVariant.NORMAL
    case Material.INFESTED_STONE                 => StoneVariant.NORMAL
    case Material.INFESTED_STONE_BRICKS          => StoneVariant.NORMAL
    case Material.INFESTED_CHISELED_STONE_BRICKS => StoneVariant.CHISELED
    case Material.INFESTED_CRACKED_STONE_BRICKS  => StoneVariant.CRACKED
    case Material.INFESTED_MOSSY_STONE_BRICKS    => StoneVariant.MOSSY

    // MUSHROOM
    case Material.BROWN_MUSHROOM => MushroomVariant.BROWN
    case Material.RED_MUSHROOM   => MushroomVariant.RED

    // MUSHROOM_BLOCK
    case Material.BROWN_MUSHROOM_BLOCK => MushroomBlockVariant.BROWN
    case Material.RED_MUSHROOM_BLOCK   => MushroomBlockVariant.RED
    case Material.MUSHROOM_STEM        => MushroomBlockVariant.STEM

    // RAILS
    case Material.RAIL           => RailsVariant.NORMAL
    case Material.ACTIVATOR_RAIL => RailsVariant.ACTIVATOR
    case Material.DETECTOR_RAIL  => RailsVariant.DETECTOR
    case Material.POWERED_RAIL   => RailsVariant.POWERED

    // SAPLING
    case Material.BAMBOO_SAPLING => SaplingVariant.BAMBOO

    case Material.ACACIA_SAPLING   => SaplingVariant.ACACIA
    case Material.BIRCH_SAPLING    => SaplingVariant.BIRCH
    case Material.DARK_OAK_SAPLING => SaplingVariant.DARK_OAK
    case Material.JUNGLE_SAPLING   => SaplingVariant.JUNGLE
    case Material.OAK_SAPLING      => SaplingVariant.OAK
    case Material.SPRUCE_SAPLING   => SaplingVariant.SPRUCE

    // WEIGHTED_PRESSURE_PLATE
    case Material.LIGHT_WEIGHTED_PRESSURE_PLATE => WeightedPressurePlateVariant.LIGHT
    case Material.HEAVY_WEIGHTED_PRESSURE_PLATE => WeightedPressurePlateVariant.HEAVY

    case _ => material.name match {

      // COBBLESTONE
      case r"COBBLESTONE.*"       => CobblestoneVariant.NORMAL
      case r"MOSSY_COBBLESTONE.*" => CobblestoneVariant.MOSSY

      // CORAL
      case r"BRAIN_CORAL.*"  => CoralVariant.BRAIN
      case r"BUBBLE_CORAL.*" => CoralVariant.BUBBLE
      case r"FIRE_CORAL.*"   => CoralVariant.FIRE
      case r"HORN_CORAL.*"   => CoralVariant.HORN
      case r"TUBE_CORAL.*"   => CoralVariant.TUBE

      case r"DEAD_BRAIN_CORAL.*"  => CoralVariant.DEAD_BRAIN
      case r"DEAD_BUBBLE_CORAL.*" => CoralVariant.DEAD_BUBBLE
      case r"DEAD_FIRE_CORAL.*"   => CoralVariant.DEAD_FIRE
      case r"DEAD_HORN_CORAL.*"   => CoralVariant.DEAD_HORN
      case r"DEAD_TUBE_CORAL.*"   => CoralVariant.DEAD_TUBE

      // MOB_HEAD
      case r"CREEPER.*"         => MobHeadVariant.CREEPER
      case r"DRAGON.*"          => MobHeadVariant.DRAGON
      case r"PLAYER.*"          => MobHeadVariant.PLAYER
      case r"SKELETON.*"        => MobHeadVariant.SKELETON
      case r"WITHER_SKELETON.*" => MobHeadVariant.WITHER_SKELETON
      case r"ZOMBIE.*"          => MobHeadVariant.ZOMBIE

      // QUARTZ
      case r"QUARTZ.*"          => QuartzVariant.NORMAL
      case r"CHISELED_QUARTZ.*" => QuartzVariant.CHISELED
      case r"SMOOTH_QUARTZ.*"   => QuartzVariant.SMOOTH

      // SANDSTONE
      case r"SANDSTONE.*"     => SandstoneVariant.NORMAL
      case r"RED_SANDSTONE.*" => SandstoneVariant.NORMAL

      case r"CHISELED_SANDSTONE.*"     => SandstoneVariant.CHISELED
      case r"CHISELED_RED_SANDSTONE.*" => SandstoneVariant.CHISELED

      case r"CUT_SANDSTONE.*"     => SandstoneVariant.CUT
      case r"CUT_RED_SANDSTONE.*" => SandstoneVariant.CUT

      case r"SMOOTH_SANDSTONE.*"     => SandstoneVariant.SMOOTH
      case r"SMOOTH_RED_SANDSTONE.*" => SandstoneVariant.SMOOTH

      // STONE
      case r"STONE_BRICK.*" => StoneVariant.NORMAL
      case r"STONE.*"       => StoneVariant.NORMAL

      case r"CHISELED_STONE_BRICK.*" => StoneVariant.CHISELED
      case r"CHISELED_STONE.*"       => StoneVariant.CHISELED

      case r"CRACKED_STONE_BRICK.*" => StoneVariant.CRACKED
      case r"CRACKED_STONE.*"       => StoneVariant.CRACKED

      case r"MOSSY_STONE_BRICK.*" => StoneVariant.MOSSY
      case r"MOSSY_STONE.*"       => StoneVariant.MOSSY

      case r"SMOOTH_STONE_BRICK.*" => StoneVariant.SMOOTH
      case r"SMOOTH_STONE.*"       => StoneVariant.SMOOTH

      // STONITE
      case r"ANDESITE.*" => StoniteVariant.NORMAL
      case r"DIORITE.*"  => StoniteVariant.NORMAL
      case r"GRANITE.*"  => StoniteVariant.NORMAL

      case r"POLISHED_ANDESITE.*" => StoniteVariant.POLISHED
      case r"POLISHED_DIORITE.*"  => StoniteVariant.POLISHED
      case r"POLISHED_GRANITE.*"  => StoniteVariant.POLISHED

      case _ => throw new IllegalArgumentException(s"$material")
    }
  }

  def map(block: VariedBlock[_ <: BlockVariant]): Material = block match {

    // AIR
    case Air(_, AirVariant.NORMAL) => Material.AIR
    case Air(_, AirVariant.CAVE)   => Material.CAVE_AIR
    case Air(_, AirVariant.VOID)   => Material.STRUCTURE_VOID

    // ANVIL
    case Anvil(_, AnvilVariant.NORMAL, _)  => Material.ANVIL
    case Anvil(_, AnvilVariant.CHIPPED, _) => Material.CHIPPED_ANVIL
    case Anvil(_, AnvilVariant.DAMAGED, _) => Material.DAMAGED_ANVIL

    // BAMBOO
    case _: Bamboo => Material.BAMBOO

    // CHEST
    case Chest(_, ChestVariant.NORMAL, _)  => Material.CHEST
    case Chest(_, ChestVariant.ENDER, _)   => Material.ENDER_CHEST
    case Chest(_, ChestVariant.TRAPPED, _) => Material.TRAPPED_CHEST

    // COBBLESTONE
    case Cobblestone(_, CobblestoneVariant.NORMAL) => Material.COBBLESTONE
    case Cobblestone(_, CobblestoneVariant.MOSSY)  => Material.MOSSY_COBBLESTONE

    // COMMAND_BLOCK
    case CommandBlock(_, CommandBlockVariant.NORMAL, _, _)    => Material.COMMAND_BLOCK
    case CommandBlock(_, CommandBlockVariant.CHAIN, _, _)     => Material.CHAIN_COMMAND_BLOCK
    case CommandBlock(_, CommandBlockVariant.REPEATING, _, _) => Material.REPEATING_COMMAND_BLOCK

    // COMPARATOR
    case _: Comparator => Material.COMPARATOR

    // COMMAND_BLOCK
    case CommandBlock(_, variant, _, _) => variant match {
      case CommandBlockVariant.NORMAL    => Material.COMMAND_BLOCK
      case CommandBlockVariant.CHAIN     => Material.CHAIN_COMMAND_BLOCK
      case CommandBlockVariant.REPEATING => Material.REPEATING_COMMAND_BLOCK
    }

    // CORAL
    case Coral(_, CoralVariant.BRAIN, _)  => Material.BRAIN_CORAL
    case Coral(_, CoralVariant.BUBBLE, _) => Material.BUBBLE_CORAL
    case Coral(_, CoralVariant.FIRE, _)   => Material.FIRE_CORAL
    case Coral(_, CoralVariant.HORN, _)   => Material.HORN_CORAL
    case Coral(_, CoralVariant.TUBE, _)   => Material.TUBE_CORAL

    case Coral(_, CoralVariant.DEAD_BRAIN, _)  => Material.DEAD_BRAIN_CORAL
    case Coral(_, CoralVariant.DEAD_BUBBLE, _) => Material.DEAD_BUBBLE_CORAL
    case Coral(_, CoralVariant.DEAD_FIRE, _)   => Material.DEAD_FIRE_CORAL
    case Coral(_, CoralVariant.DEAD_HORN, _)   => Material.DEAD_HORN_CORAL
    case Coral(_, CoralVariant.DEAD_TUBE, _)   => Material.DEAD_TUBE_CORAL

    // CORAL_BLOCK
    case CoralBlock(_, CoralVariant.BRAIN)  => Material.BRAIN_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.BUBBLE) => Material.BUBBLE_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.FIRE)   => Material.FIRE_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.HORN)   => Material.HORN_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.TUBE)   => Material.TUBE_CORAL_BLOCK

    case CoralBlock(_, CoralVariant.DEAD_BRAIN)  => Material.DEAD_BRAIN_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.DEAD_BUBBLE) => Material.DEAD_BUBBLE_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.DEAD_FIRE)   => Material.DEAD_FIRE_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.DEAD_HORN)   => Material.DEAD_HORN_CORAL_BLOCK
    case CoralBlock(_, CoralVariant.DEAD_TUBE)   => Material.DEAD_TUBE_CORAL_BLOCK

    // CORAL_FAN
    case CoralFan(_, CoralVariant.BRAIN, dir, _) => dir match {
      case None    => Material.BRAIN_CORAL_FAN
      case Some(_) => Material.BRAIN_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.BUBBLE, dir, _) => dir match {
      case None    => Material.BUBBLE_CORAL_FAN
      case Some(_) => Material.BUBBLE_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.FIRE, dir, _) => dir match {
      case None    => Material.FIRE_CORAL_FAN
      case Some(_) => Material.FIRE_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.HORN, dir, _) => dir match {
      case None    => Material.HORN_CORAL_FAN
      case Some(_) => Material.HORN_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.TUBE, dir, _) => dir match {
      case None    => Material.TUBE_CORAL_FAN
      case Some(_) => Material.TUBE_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.DEAD_BRAIN, dir, _) => dir match {
      case None    => Material.DEAD_BRAIN_CORAL_FAN
      case Some(_) => Material.DEAD_BRAIN_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.DEAD_BUBBLE, dir, _) => dir match {
      case None    => Material.DEAD_BUBBLE_CORAL_FAN
      case Some(_) => Material.DEAD_BUBBLE_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.DEAD_FIRE, dir, _) => dir match {
      case None    => Material.DEAD_FIRE_CORAL_FAN
      case Some(_) => Material.DEAD_FIRE_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.DEAD_HORN, dir, _) => dir match {
      case None    => Material.DEAD_HORN_CORAL_FAN
      case Some(_) => Material.DEAD_HORN_CORAL_WALL_FAN
    }

    case CoralFan(_, CoralVariant.DEAD_TUBE, dir, _) => dir match {
      case None    => Material.DEAD_TUBE_CORAL_FAN
      case Some(_) => Material.DEAD_TUBE_CORAL_WALL_FAN
    }

    // FLOWER
    case Flower(_, variant, _, _) => variant match {
      case FlowerVariant.ALLIUM             => Material.ALLIUM
      case FlowerVariant.AZURE_BLUET        => Material.AZURE_BLUET
      case FlowerVariant.BLUE_ORCHID        => Material.BLUE_ORCHID
      case FlowerVariant.CORNFLOWER         => Material.CORNFLOWER
      case FlowerVariant.DANDELION          => Material.DANDELION
      case FlowerVariant.LILY_OF_THE_VALLEY => Material.LILY_OF_THE_VALLEY
      case FlowerVariant.ORANGE_TULIP       => Material.ORANGE_TULIP
      case FlowerVariant.OXEYE_DAISY        => Material.OXEYE_DAISY
      case FlowerVariant.PINK_TULIP         => Material.PINK_TULIP
      case FlowerVariant.POPPY              => Material.POPPY
      case FlowerVariant.RED_TULIP          => Material.RED_TULIP
      case FlowerVariant.WHITE_TULIP        => Material.WHITE_TULIP
      case FlowerVariant.WITHER_ROSE        => Material.WITHER_ROSE
    }

    // INFESTED_BLOCK
    case InfestedBlock(_, CobblestoneMaterial.COBBLESTONE, _) => Material.INFESTED_COBBLESTONE
    case InfestedBlock(_, StoneMaterial.STONE, _)             => Material.INFESTED_STONE
    case InfestedBlock(_, StoneMaterial.STONE_BRICK, variant) => variant match {
      case StoneVariant.CHISELED => Material.INFESTED_CHISELED_STONE_BRICKS
      case StoneVariant.CRACKED  => Material.INFESTED_CRACKED_STONE_BRICKS
      case StoneVariant.MOSSY    => Material.INFESTED_MOSSY_STONE_BRICKS
      case _                     => Material.INFESTED_STONE_BRICKS
    }

    // ICE
    case Ice(_, variant) => variant match {
      case IceVariant.NORMAL => Material.ICE
      case IceVariant.PACKED => Material.PACKED_ICE
      case IceVariant.BLUE   => Material.BLUE_ICE
    }

    // MOB_HEAD
    case MobHead(_, MobHeadVariant.CREEPER, dir, _) => dir match {
      case None    => Material.CREEPER_HEAD
      case Some(_) => Material.CREEPER_WALL_HEAD
    }

    case MobHead(_, MobHeadVariant.DRAGON, dir, _) => dir match {
      case None    => Material.DRAGON_HEAD
      case Some(_) => Material.DRAGON_WALL_HEAD
    }

    case MobHead(_, MobHeadVariant.PLAYER, dir, _) => dir match {
      case None    => Material.PLAYER_HEAD
      case Some(_) => Material.PLAYER_WALL_HEAD
    }

    case MobHead(_, MobHeadVariant.SKELETON, dir, _) => dir match {
      case None    => Material.SKELETON_SKULL
      case Some(_) => Material.SKELETON_WALL_SKULL
    }

    case MobHead(_, MobHeadVariant.WITHER_SKELETON, dir, _) => dir match {
      case None    => Material.WITHER_SKELETON_SKULL
      case Some(_) => Material.WITHER_SKELETON_WALL_SKULL
    }

    case MobHead(_, MobHeadVariant.ZOMBIE, dir, _) => dir match {
      case None    => Material.ZOMBIE_HEAD
      case Some(_) => Material.ZOMBIE_WALL_HEAD
    }

    // MUSHROOM
    case Mushroom(_, variant) => variant match {
      case MushroomVariant.BROWN => Material.BROWN_MUSHROOM
      case MushroomVariant.RED   => Material.RED_MUSHROOM
    }

    // MUSHROOM_BLOCK
    case MushroomBlock(_, variant) => variant match {
      case MushroomBlockVariant.BROWN => Material.BROWN_MUSHROOM_BLOCK
      case MushroomBlockVariant.RED   => Material.RED_MUSHROOM_BLOCK
      case MushroomBlockVariant.STEM  => Material.MUSHROOM_STEM
    }

    // NOTE_BLOCK
    case _: NoteBlock => Material.NOTE_BLOCK

    // QUARTZ
    case Quartz(_, QuartzVariant.NORMAL)   => Material.QUARTZ_BLOCK
    case Quartz(_, QuartzVariant.CHISELED) => Material.CHISELED_QUARTZ_BLOCK
    case Quartz(_, QuartzVariant.SMOOTH)   => Material.SMOOTH_QUARTZ

    // RAILS
    case Rails(_, RailsVariant.NORMAL, _, _)    => Material.RAIL
    case Rails(_, RailsVariant.ACTIVATOR, _, _) => Material.ACTIVATOR_RAIL
    case Rails(_, RailsVariant.DETECTOR, _, _)  => Material.DETECTOR_RAIL
    case Rails(_, RailsVariant.POWERED, _, _)   => Material.POWERED_RAIL

    // SANDSTONE
    case Sandstone(_, SandstoneMaterial.SANDSTONE, variant) => variant match {
      case SandstoneVariant.NORMAL   => Material.SANDSTONE
      case SandstoneVariant.CHISELED => Material.CHISELED_SANDSTONE
      case SandstoneVariant.CUT      => Material.CUT_SANDSTONE
      case SandstoneVariant.SMOOTH   => Material.SMOOTH_SANDSTONE
    }

    case Sandstone(_, SandstoneMaterial.RED_SANDSTONE, v) => v match {
      case SandstoneVariant.NORMAL   => Material.RED_SANDSTONE
      case SandstoneVariant.CHISELED => Material.CHISELED_RED_SANDSTONE
      case SandstoneVariant.CUT      => Material.CUT_RED_SANDSTONE
      case SandstoneVariant.SMOOTH   => Material.SMOOTH_RED_SANDSTONE
    }

    // SAPLING
    case Sapling(_, variant, _) => variant match {
      case SaplingVariant.BAMBOO   => Material.BAMBOO_SAPLING
      case SaplingVariant.ACACIA   => Material.ACACIA_SAPLING
      case SaplingVariant.BIRCH    => Material.BIRCH_SAPLING
      case SaplingVariant.DARK_OAK => Material.DARK_OAK_SAPLING
      case SaplingVariant.JUNGLE   => Material.JUNGLE_SAPLING
      case SaplingVariant.OAK      => Material.OAK_SAPLING
      case SaplingVariant.SPRUCE   => Material.SPRUCE_SAPLING
    }

    // STONE
    case Stone(_, StoneMaterial.STONE, variant) => variant match {
      case StoneVariant.SMOOTH => Material.SMOOTH_STONE
      case _                   => Material.STONE
    }

    case Stone(_, StoneMaterial.STONE_BRICK, variant) => variant match {
      case StoneVariant.CHISELED => Material.CHISELED_STONE_BRICKS
      case StoneVariant.CRACKED  => Material.CRACKED_STONE_BRICKS
      case StoneVariant.MOSSY    => Material.MOSSY_STONE_BRICKS
      case _                     => Material.STONE_BRICKS
    }

    // STONITE
    case Stonite(_, StoniteMaterial.ANDESITE, variant) => variant match {
      case StoniteVariant.NORMAL   => Material.ANDESITE
      case StoniteVariant.POLISHED => Material.POLISHED_ANDESITE
    }

    case Stonite(_, StoniteMaterial.DIORITE, variant) => variant match {
      case StoniteVariant.NORMAL   => Material.DIORITE
      case StoniteVariant.POLISHED => Material.POLISHED_DIORITE
    }

    case Stonite(_, StoniteMaterial.GRANITE, variant) => variant match {
      case StoniteVariant.NORMAL   => Material.GRANITE
      case StoniteVariant.POLISHED => Material.POLISHED_GRANITE
    }

    // STRUCTURE_BLOCK
    case StructureBlock(_, StructureBlockVariant.VOID) => Material.STRUCTURE_VOID
    case StructureBlock(_, _)                          => Material.STRUCTURE_BLOCK

    // WEIGHTED_PRESSURE_PLATE
    case WeightedPressurePlate(_, WeightedPressurePlateVariant.LIGHT, _) =>
      Material.LIGHT_WEIGHTED_PRESSURE_PLATE
    case WeightedPressurePlate(_, WeightedPressurePlateVariant.HEAVY, _) =>
      Material.HEAVY_WEIGHTED_PRESSURE_PLATE
  }

  def map(block: VariableBlock[_ <: BlockVariant]): Material = block match {

    // FLOWER_POT
    case FlowerPot(_, None)          => Material.FLOWER_POT
    case FlowerPot(_, Some(variant)) => variant match {
      case FlowerVariant.ALLIUM             => Material.POTTED_ALLIUM
      case FlowerVariant.AZURE_BLUET        => Material.POTTED_AZURE_BLUET
      case FlowerVariant.BLUE_ORCHID        => Material.POTTED_BLUE_ORCHID
      case FlowerVariant.CORNFLOWER         => Material.POTTED_CORNFLOWER
      case FlowerVariant.DANDELION          => Material.POTTED_DANDELION
      case FlowerVariant.ORANGE_TULIP       => Material.POTTED_ORANGE_TULIP
      case FlowerVariant.OXEYE_DAISY        => Material.POTTED_OXEYE_DAISY
      case FlowerVariant.PINK_TULIP         => Material.POTTED_PINK_TULIP
      case FlowerVariant.POPPY              => Material.POTTED_POPPY
      case FlowerVariant.RED_TULIP          => Material.POTTED_RED_TULIP
      case FlowerVariant.WHITE_TULIP        => Material.POTTED_WHITE_TULIP
      case FlowerVariant.WITHER_ROSE        => Material.POTTED_WITHER_ROSE
      case FlowerVariant.LILY_OF_THE_VALLEY =>
        Material.POTTED_LILY_OF_THE_VALLEY

      case MushroomVariant.BROWN => Material.POTTED_BROWN_MUSHROOM
      case MushroomVariant.RED   => Material.POTTED_RED_MUSHROOM

      case PlantVariant.CACTUS    => Material.POTTED_CACTUS
      case PlantVariant.DEAD_BUSH => Material.POTTED_DEAD_BUSH
      case PlantVariant.FERN      => Material.POTTED_FERN

      case SaplingVariant.BAMBOO   => Material.POTTED_BAMBOO
      case SaplingVariant.ACACIA   => Material.POTTED_ACACIA_SAPLING
      case SaplingVariant.BIRCH    => Material.POTTED_BIRCH_SAPLING
      case SaplingVariant.DARK_OAK => Material.POTTED_DARK_OAK_SAPLING
      case SaplingVariant.JUNGLE   => Material.POTTED_JUNGLE_SAPLING
      case SaplingVariant.OAK      => Material.POTTED_OAK_SAPLING
      case SaplingVariant.SPRUCE   => Material.POTTED_SPRUCE_SAPLING
    }

    case Slab(_, material, variant, _) => material match {

      // BRICK
      case BrickMaterial.BRICK            => Material.BRICK_SLAB
      case BrickMaterial.NETHER_BRICK     => Material.NETHER_BRICK_SLAB
      case BrickMaterial.RED_NETHER_BRICK => Material.RED_NETHER_BRICK_SLAB

      // COBBLESTONE
      case _: CobblestoneMaterial => variant match {
        case Some(CobblestoneVariant.MOSSY) => Material.MOSSY_COBBLESTONE_SLAB
        case _                              => Material.COBBLESTONE_SLAB
      }

      // END_STONE
      case _: EndStoneMaterial => Material.END_STONE_BRICK_SLAB

      // PRISMARINE
      case PrismarineMaterial.PRISMARINE       => Material.PRISMARINE_SLAB
      case PrismarineMaterial.PRISMARINE_BRICK => Material.PRISMARINE_BRICK_SLAB
      case PrismarineMaterial.DARK_PRISMARINE  => Material.DARK_PRISMARINE_SLAB

      // PURPUR
      case _: PurpurMaterial => Material.PURPUR_SLAB

      // QUARTZ
      case QuartzMaterial.QUARTZ => variant match {
        case Some(QuartzVariant.SMOOTH) => Material.SMOOTH_QUARTZ_SLAB
        case _                          => Material.QUARTZ_SLAB
      }

      // SANDSTONE
      case SandstoneMaterial.SANDSTONE => variant match {
        case Some(SandstoneVariant.CUT)    => Material.CUT_SANDSTONE_SLAB
        case Some(SandstoneVariant.SMOOTH) => Material.SMOOTH_SANDSTONE_SLAB
        case _                             => Material.SANDSTONE_SLAB
      }

      case SandstoneMaterial.RED_SANDSTONE => variant match {
        case Some(SandstoneVariant.CUT)    => Material.CUT_RED_SANDSTONE_SLAB
        case Some(SandstoneVariant.SMOOTH) => Material.SMOOTH_RED_SANDSTONE_SLAB
        case _                             => Material.RED_SANDSTONE_SLAB
      }

      // STONE
      case StoneMaterial.STONE => variant match {
        case Some(StoneVariant.SMOOTH) => Material.SMOOTH_STONE_SLAB
        case _                         => Material.STONE_SLAB
      }

      case StoneMaterial.STONE_BRICK => variant match {
        case Some(StoneVariant.MOSSY) => Material.MOSSY_STONE_BRICK_SLAB
        case _                        => Material.STONE_BRICK_SLAB
      }

      // STONITE
      case StoniteMaterial.ANDESITE => variant match {
        case Some(StoniteVariant.POLISHED) => Material.POLISHED_ANDESITE_SLAB
        case _                             => Material.ANDESITE_SLAB
      }

      case StoniteMaterial.DIORITE => variant match {
        case Some(StoniteVariant.POLISHED) => Material.POLISHED_DIORITE_SLAB
        case _                             => Material.DIORITE_SLAB
      }

      case StoniteMaterial.GRANITE => variant match {
        case Some(StoniteVariant.POLISHED) => Material.POLISHED_GRANITE_SLAB
        case _                             => Material.GRANITE_SLAB
      }

      // WOOD
      case WoodMaterial.ACACIA   => Material.ACACIA_SLAB
      case WoodMaterial.BIRCH    => Material.BIRCH_SLAB
      case WoodMaterial.DARK_OAK => Material.DARK_OAK_SLAB
      case WoodMaterial.JUNGLE   => Material.JUNGLE_SLAB
      case WoodMaterial.OAK      => Material.OAK_SLAB
      case WoodMaterial.SPRUCE   => Material.SPRUCE_SLAB
    }

    case Stairs(_, material, variant, _, _, _, _) => material match {

      // BRICK
      case BrickMaterial.BRICK            => Material.BRICK_STAIRS
      case BrickMaterial.NETHER_BRICK     => Material.NETHER_BRICK_STAIRS
      case BrickMaterial.RED_NETHER_BRICK => Material.RED_NETHER_BRICK_STAIRS

      // COBBLESTONE
      case _: CobblestoneMaterial => variant match {
        case Some(CobblestoneVariant.MOSSY) => Material.MOSSY_COBBLESTONE_STAIRS
        case _                              => Material.COBBLESTONE_STAIRS
      }

      // END_STONE
      case _: EndStoneMaterial => Material.END_STONE_BRICK_STAIRS

      // PRISMARINE
      case PrismarineMaterial.PRISMARINE       => Material.PRISMARINE_STAIRS
      case PrismarineMaterial.DARK_PRISMARINE  => Material.DARK_PRISMARINE_STAIRS
      case PrismarineMaterial.PRISMARINE_BRICK =>
        Material.PRISMARINE_BRICK_STAIRS

      // PURPUR
      case _: PurpurMaterial => Material.PURPUR_STAIRS

      // QUARTZ
      case QuartzMaterial.QUARTZ => variant match {
        case Some(QuartzVariant.SMOOTH) => Material.SMOOTH_QUARTZ_STAIRS
        case _                          => Material.QUARTZ_STAIRS
      }

      // SANDSTONE
      case SandstoneMaterial.SANDSTONE => variant match {
        case Some(SandstoneVariant.SMOOTH) => Material.SMOOTH_SANDSTONE_STAIRS
        case _                             => Material.SANDSTONE_STAIRS
      }

      case SandstoneMaterial.RED_SANDSTONE => variant match {
        case Some(SandstoneVariant.SMOOTH) =>
          Material.SMOOTH_RED_SANDSTONE_STAIRS
        case _                             => Material.RED_SANDSTONE_STAIRS
      }

      // STONE
      case StoneMaterial.STONE       => Material.STONE_STAIRS
      case StoneMaterial.STONE_BRICK => variant match {
        case Some(StoneVariant.MOSSY) => Material.MOSSY_STONE_BRICK_STAIRS
        case _                        => Material.STONE_BRICK_STAIRS
      }

      // STONITE
      case StoniteMaterial.ANDESITE => Material.ANDESITE_STAIRS
      case StoniteMaterial.DIORITE  => Material.DIORITE_STAIRS
      case StoniteMaterial.GRANITE  => Material.GRANITE_STAIRS

      // WOOD
      case WoodMaterial.ACACIA   => Material.ACACIA_STAIRS
      case WoodMaterial.BIRCH    => Material.BIRCH_STAIRS
      case WoodMaterial.DARK_OAK => Material.DARK_OAK_STAIRS
      case WoodMaterial.JUNGLE   => Material.JUNGLE_STAIRS
      case WoodMaterial.OAK      => Material.OAK_STAIRS
      case WoodMaterial.SPRUCE   => Material.SPRUCE_STAIRS
    }

    case Wall(_, material, variant, _) => material match {

      // BRICK
      case BrickMaterial.BRICK            => Material.BRICK_WALL
      case BrickMaterial.NETHER_BRICK     => Material.NETHER_BRICK_WALL
      case BrickMaterial.RED_NETHER_BRICK => Material.RED_NETHER_BRICK_WALL

      // COBBLESTONE
      case _: CobblestoneMaterial => variant match {
        case Some(CobblestoneVariant.MOSSY) => Material.MOSSY_COBBLESTONE_WALL
        case _                              => Material.COBBLESTONE_WALL
      }

      // END_STONE
      case _: EndStoneMaterial => Material.END_STONE_BRICK_WALL

      // PRISMARINE
      case _: PrismarineMaterial => Material.PRISMARINE_WALL

      // SANDSTONE
      case SandstoneMaterial.SANDSTONE     => Material.SANDSTONE_WALL
      case SandstoneMaterial.RED_SANDSTONE => Material.RED_SANDSTONE_WALL

      // STONE
      case _: StoneMaterial => variant match {
        case Some(StoneVariant.MOSSY) => Material.MOSSY_STONE_BRICK_WALL
        case _                        => Material.STONE_BRICK_WALL
      }

      // STONITE
      case StoniteMaterial.ANDESITE => Material.ANDESITE_WALL
      case StoniteMaterial.DIORITE  => Material.DIORITE_WALL
      case StoniteMaterial.GRANITE  => Material.GRANITE_WALL
    }
  }

  def map(
      block: VariedBlock[_ <: BlockVariant],
      spigotData: SpigotBlockData
  ): Unit = {
    implicit val data: SpigotBlockData = spigotData

    block match {
      case Bamboo(_, variant, _, _) => dataAs[SpigotBamboo].setLeaves(variant match {
        case BambooVariant.NO_LEAVES    => SpigotBambooLeaves.NONE
        case BambooVariant.SMALL_LEAVES => SpigotBambooLeaves.SMALL
        case BambooVariant.LARGE_LEAVES => SpigotBambooLeaves.LARGE
      })

      case Comparator(_, variant, _, _) => dataAs[SpigotComparator].setMode(variant match {
        case ComparatorVariant.COMPARE  => SpigotComparatorMode.COMPARE
        case ComparatorVariant.SUBTRACT => SpigotComparatorMode.SUBTRACT
      })

      case NoteBlock(_, variant, _, _) => dataAs[SpigotNoteBlock].setInstrument(variant match {
        case NoteBlockVariant.BANJO          => SpigotInstrument.BANJO
        case NoteBlockVariant.BASS_DRUM      => SpigotInstrument.BASS_DRUM
        case NoteBlockVariant.BASS_GUITAR    => SpigotInstrument.BASS_GUITAR
        case NoteBlockVariant.BELL           => SpigotInstrument.BELL
        case NoteBlockVariant.BIT            => SpigotInstrument.BIT
        case NoteBlockVariant.CHIME          => SpigotInstrument.CHIME
        case NoteBlockVariant.COW_BELL       => SpigotInstrument.COW_BELL
        case NoteBlockVariant.DIDGERIDOO     => SpigotInstrument.DIDGERIDOO
        case NoteBlockVariant.FLUTE          => SpigotInstrument.FLUTE
        case NoteBlockVariant.GUITAR         => SpigotInstrument.GUITAR
        case NoteBlockVariant.IRON_XYLOPHONE => SpigotInstrument.IRON_XYLOPHONE
        case NoteBlockVariant.HAT            => SpigotInstrument.PIANO
        case NoteBlockVariant.PLING          => SpigotInstrument.PLING
        case NoteBlockVariant.SNARE_DRUM     => SpigotInstrument.SNARE_DRUM
        case NoteBlockVariant.HARP           => SpigotInstrument.STICKS
        case NoteBlockVariant.XYLOPHONE      => SpigotInstrument.XYLOPHONE
      })

      case StructureBlock(_, variant) => if (variant != StructureBlockVariant.VOID) {
        dataAs[SpigotStructureBlock].setMode(variant match {
          case StructureBlockVariant.CORNER => SpigotStructureBlockMode.CORNER
          case StructureBlockVariant.DATA   => SpigotStructureBlockMode.DATA
          case StructureBlockVariant.LOAD   => SpigotStructureBlockMode.LOAD
          case StructureBlockVariant.SAVE   => SpigotStructureBlockMode.SAVE
        })
      }
    }
  }
}

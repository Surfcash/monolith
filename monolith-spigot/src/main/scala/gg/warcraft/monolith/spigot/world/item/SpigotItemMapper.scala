package gg.warcraft.monolith.spigot.world.item

import gg.warcraft.monolith.api.world.block.variant._
import gg.warcraft.monolith.api.world.item._
import gg.warcraft.monolith.api.world.item.variant._
import gg.warcraft.monolith.spigot.Extensions._
import javax.inject.Inject
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag

class SpigotItemMapper @Inject() (
    private val colorMapper: SpigotItemColorMapper,
    private val variantMapper: SpigotItemVariantMapper
) {
  def map(item: SpigotItemStack): Option[Item] = {
    // return None if Air
    val material = item.getType
    if (material.name.endsWith("AIR")) return None

    // Set common item data
    val meta = item.getItemMeta
    val name = meta.getDisplayName
    val tooltip: Array[String] = Array() // TODO item.getItemMeta.getLore
    val attr = Set.empty[String] // TODO map attributes
    val hideAttr = meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)

    // Lazily compute generic item data
    lazy val color = colorMapper.map(material)
    lazy val count = item.getAmount
    lazy val durability = item.getDurability.toInt // TODO keep int or let be short?
    lazy val unbreakable = meta.isUnbreakable
    lazy val hideUnbreakable = meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)
    lazy val variant = variantMapper.map(item)

    // Lazily construct tuples for all the different types of parameter sets
    lazy val params = (name, tooltip, count, attr, hideAttr)
    lazy val colorParams = (color, name, tooltip, count, attr, hideAttr)
    lazy val colorableParams = (Option(color), name, tooltip, count, attr, hideAttr)
    lazy val singleParams = (name, tooltip, attr, hideAttr)
    lazy val durableParams =
      (name, tooltip, attr, hideAttr, durability, unbreakable, hideUnbreakable)
    def v[T <: ItemVariant] =
      (variant.asInstanceOf[T], name, tooltip, count, attr, hideAttr)

    // Map item
    Some(material match {
      case Material.APPLE                  => Apple.tupled(params)
      case Material.ARMOR_STAND            => ArmorStand.tupled(params)
      case Material.BAMBOO                 => Bamboo.tupled(params)
      case Material.BARREL                 => Barrel.tupled(params)
      case Material.BARRIER                => Barrier.tupled(params)
      case Material.BEACON                 => Beacon.tupled(params)
      case Material.BEDROCK                => Bedrock.tupled(params)
      case Material.BEETROOT               => Beetroot.tupled(params)
      case Material.BELL                   => Bell.tupled(params)
      case Material.BLAST_FURNACE          => BlastFurnace.tupled(params)
      case Material.BLAZE_POWDER           => BlazeRod.tupled(params)
      case Material.BLAZE_ROD              => BlazeRod.tupled(params)
      case Material.BONE                   => Bone.tupled(params)
      case Material.BONE_BLOCK             => BoneBlock.tupled(params)
      case Material.BONE_MEAL              => BoneMeal.tupled(params)
      case Material.BOOK                   => Book.tupled(params)
      case Material.BOOKSHELF              => Bookshelf.tupled(params)
      case Material.BOW                    => Bow.tupled(durableParams)
      case Material.BOWL                   => Bowl.tupled(params)
      case Material.BREAD                  => Bread.tupled(params)
      case Material.BREWING_STAND          => BrewingStand.tupled(params)
      case Material.CACTUS                 => Cactus.tupled(params)
      case Material.CAKE                   => Cake.tupled(singleParams)
      case Material.CAMPFIRE               => Campfire.tupled(params)
      case Material.CARROT                 => Carrot.tupled(params)
      case Material.CARROT_ON_A_STICK      => CarrotOnAStick.tupled(durableParams)
      case Material.CARTOGRAPHY_TABLE      => CartographyTable.tupled(params)
      case Material.CAULDRON               => Cauldron.tupled(params)
      case Material.CHARCOAL               => Charcoal.tupled(params)
      case Material.CHORUS_FLOWER          => ChorusFlower.tupled(params)
      case Material.CHORUS_PLANT           => ChorusPlant.tupled(params)
      case Material.CLAY                   => ClayBlock.tupled(params)
      case Material.CLAY_BALL              => Clay.tupled(params)
      case Material.CLOCK                  => Clock.tupled(params)
      case Material.COAL                   => Coal.tupled(params)
      case Material.COAL_BLOCK             => CoalBlock.tupled(params)
      case Material.COAL_ORE               => CoalOre.tupled(params)
      case Material.COBWEB                 => Cobweb.tupled(params)
      case Material.COCOA_BEANS            => CocoaBeans.tupled(params)
      case Material.COMPARATOR             => Comparator.tupled(params)
      case Material.COMPASS                => Compass.tupled(params)
      case Material.COMPOSTER              => Composter.tupled(params)
      case Material.CONDUIT                => Conduit.tupled(params)
      case Material.COOKIE                 => Cookie.tupled(params)
      case Material.CRAFTING_TABLE         => CraftingTable.tupled(params)
      case Material.CROSSBOW               => Crossbow.tupled(durableParams)
      case Material.DAYLIGHT_DETECTOR      => DaylightDetector.tupled(params)
      case Material.DEAD_BUSH              => DeadBush.tupled(params)
      case Material.DEBUG_STICK            => DebugStick.tupled(singleParams)
      case Material.DIAMOND                => Diamond.tupled(params)
      case Material.DIAMOND_BLOCK          => DiamondBlock.tupled(params)
      case Material.DIAMOND_ORE            => DiamondOre.tupled(params)
      case Material.DISPENSER              => Dispenser.tupled(params)
      case Material.DRAGON_BREATH          => DragonBreath.tupled(params)
      case Material.DRAGON_EGG             => DragonEgg.tupled(params)
      case Material.DRIED_KELP             => DriedKelp.tupled(params)
      case Material.DRIED_KELP_BLOCK       => DriedKelpBlock.tupled(params)
      case Material.DROPPER                => Dropper.tupled(params)
      case Material.EGG                    => Egg.tupled(params)
      case Material.ELYTRA                 => Elytra.tupled(durableParams)
      case Material.EMERALD                => Emerald.tupled(params)
      case Material.EMERALD_BLOCK          => EmeraldBlock.tupled(params)
      case Material.EMERALD_ORE            => EmeraldOre.tupled(params)
      case Material.ENCHANTED_BOOK         => EnchantedBook.tupled(singleParams)
      case Material.ENCHANTING_TABLE       => EnchantingTable.tupled(params)
      case Material.ENDER_EYE              => EnderEye.tupled(params)
      case Material.ENDER_PEARL            => EnderPearl.tupled(params)
      case Material.END_CRYSTAL            => EndCrystal.tupled(params)
      case Material.END_PORTAL_FRAME       => EndPortalFrame.tupled(params)
      case Material.END_ROD                => EndRod.tupled(params)
      case Material.EXPERIENCE_BOTTLE      => BottleOfEnchanting.tupled(params)
      case Material.FARMLAND               => Farmland.tupled(params)
      case Material.FEATHER                => Feather.tupled(params)
      case Material.FISHING_ROD            => FishingRod.tupled(durableParams)
      case Material.FIREWORK_ROCKET        => FireworkRocket.tupled(params)
      case Material.FIREWORK_STAR          => FireworkStar.tupled(colorParams)
      case Material.FIRE_CHARGE            => FireCharge.tupled(params)
      case Material.FLETCHING_TABLE        => FletchingTable.tupled(params)
      case Material.FLINT                  => Flint.tupled(params)
      case Material.FLINT_AND_STEEL        => FlintAndSteel.tupled(durableParams)
      case Material.FLOWER_POT             => FlowerPot.tupled(params)
      case Material.FURNACE                => Furnace.tupled(params)
      case Material.GHAST_TEAR             => GhastTear.tupled(params)
      case Material.GLASS_BOTTLE           => GlassBottle.tupled(params)
      case Material.GLISTERING_MELON_SLICE => GoldenMelonSlice.tupled(params)
      case Material.GLOWSTONE              => Glowstone.tupled(params)
      case Material.GLOWSTONE_DUST         => GlowstoneDust.tupled(params)
      case Material.GOLDEN_CARROT          => GoldenCarrot.tupled(params)
      case Material.GOLD_BLOCK             => GoldBlock.tupled(params)
      case Material.GOLD_INGOT             => GoldIngot.tupled(params)
      case Material.GOLD_NUGGET            => GoldNugget.tupled(params)
      case Material.GOLD_ORE               => GoldOre.tupled(params)
      case Material.GRASS_BLOCK            => GrassBlock.tupled(params)
      case Material.GRASS_PATH             => GrassPath.tupled(params)
      case Material.GRAVEL                 => Gravel.tupled(params)
      case Material.GRINDSTONE             => Grindstone.tupled(params)
      case Material.GUNPOWDER              => Gunpowder.tupled(params)
      case Material.HAY_BLOCK              => HayBale.tupled(params)
      case Material.HEART_OF_THE_SEA       => HeartOfTheSea.tupled(params)
      case Material.HOPPER                 => Hopper.tupled(params)
      case Material.INK_SAC                => InkSac.tupled(params)
      case Material.IRON_BARS              => IronBars.tupled(params)
      case Material.IRON_BLOCK             => IronBlock.tupled(params)
      case Material.IRON_INGOT             => IronIngot.tupled(params)
      case Material.IRON_NUGGET            => IronNugget.tupled(params)
      case Material.IRON_ORE               => IronOre.tupled(params)
      case Material.ITEM_FRAME             => ItemFrame.tupled(params)
      case Material.JACK_O_LANTERN         => JackOfTheLantern.tupled(params)
      case Material.JIGSAW                 => JigsawBlock.tupled(params)
      case Material.JUKEBOX                => Jukebox.tupled(params)
      case Material.KELP                   => Kelp.tupled(params)
      case Material.KNOWLEDGE_BOOK         => KnowledgeBook.tupled(singleParams)
      case Material.LADDER                 => Ladder.tupled(params)
      case Material.LANTERN                => Lantern.tupled(params)
      case Material.LAPIS_BLOCK            => LapisBlock.tupled(params)
      case Material.LAPIS_LAZULI           => Lapis.tupled(params)
      case Material.LAPIS_ORE              => LapisOre.tupled(params)
      case Material.LEAD                   => Lead.tupled(params)
      case Material.LEATHER                => Leather.tupled(params)
      case Material.LECTERN                => Lectern.tupled(params)
      case Material.LEVER                  => Lever.tupled(params)
      case Material.LILY_PAD               => LilyPad.tupled(params)
      case Material.LOOM                   => Loom.tupled(params)
      case Material.MAGMA_BLOCK            => MagmaBlock.tupled(params)
      case Material.MAGMA_CREAM            => MagmaCream.tupled(params)
      case Material.MELON                  => Melon.tupled(params)
      case Material.MELON_SLICE            => MelonSlice.tupled(params)
      case Material.MYCELIUM               => Mycelium.tupled(params)
      case Material.NAME_TAG               => NameTag.tupled(params)
      case Material.NAUTILUS_SHELL         => NautilusShell.tupled(params)
      case Material.NETHERRACK             => Netherrack.tupled(params)
      case Material.NETHER_QUARTZ_ORE      => QuartzOre.tupled(params)
      case Material.NETHER_STAR            => NetherStar.tupled(params)
      case Material.NETHER_WART            => NetherWart.tupled(params)
      case Material.NETHER_WART_BLOCK      => NetherWartBlock.tupled(params)
      case Material.NOTE_BLOCK             => NoteBlock.tupled(params)
      case Material.OBSERVER               => Observer.tupled(params)
      case Material.OBSIDIAN               => Obsidian.tupled(params)
      case Material.PAINTING               => Painting.tupled(params)
      case Material.PAPER                  => Paper.tupled(params)
      case Material.PHANTOM_MEMBRANE       => PhantomMembrane.tupled(params)
      case Material.PODZOL                 => Podzol.tupled(params)
      case Material.POISONOUS_POTATO       => PoisonousPotato.tupled(params)
      case Material.PRISMARINE_CRYSTALS    => PrismarineCrystals.tupled(params)
      case Material.PRISMARINE_SHARD       => PrismarineShard.tupled(params)
      case Material.PUFFERFISH             => Pufferfish.tupled(params)
      case Material.PUMPKIN_PIE            => PumpkinPie.tupled(params)
      case Material.PURPUR_BLOCK           => PurpurBlock.tupled(params)
      case Material.QUARTZ                 => Quartz.tupled(params)
      case Material.RABBIT_FOOT            => RabbitFoot.tupled(params)
      case Material.RABBIT_HIDE            => RabbitHide.tupled(params)
      case Material.REDSTONE               => Redstone.tupled(params)
      case Material.REDSTONE_BLOCK         => RedstoneBlock.tupled(params)
      case Material.REDSTONE_LAMP          => RedstoneLamp.tupled(params)
      case Material.REDSTONE_ORE           => RedstoneOre.tupled(params)
      case Material.REDSTONE_TORCH         => RedstoneTorch.tupled(params)
      case Material.REPEATER               => Repeater.tupled(params)
      case Material.ROTTEN_FLESH           => RottenFlesh.tupled(params)
      case Material.SADDLE                 => Saddle.tupled(singleParams)
      case Material.SCAFFOLDING            => Scaffolding.tupled(params)
      case Material.SCUTE                  => Scute.tupled(params)
      case Material.SEAGRASS               => Seagrass.tupled(params)
      case Material.SEA_LANTERN            => SeaLantern.tupled(params)
      case Material.SEA_PICKLE             => SeaPickle.tupled(params)
      case Material.SHEARS                 => Shears.tupled(durableParams)
      case Material.SHIELD                 => Shield.tupled(durableParams)
      case Material.SHULKER_SHELL          => ShulkerShell.tupled(params)
      case Material.SLIME_BALL             => Slimeball.tupled(params)
      case Material.SLIME_BLOCK            => SlimeBlock.tupled(params)
      case Material.SMITHING_TABLE         => SmithingTable.tupled(params)
      case Material.SMOKER                 => Smoker.tupled(params)
      case Material.SNOW                   => Snow.tupled(params)
      case Material.SNOWBALL               => Snowball.tupled(params)
      case Material.SNOW_BLOCK             => SnowBlock.tupled(params)
      case Material.SOUL_SAND              => SoulSand.tupled(params)
      case Material.SPAWNER                => Spawner.tupled(params)
      case Material.STICK                  => Stick.tupled(params)
      case Material.STONECUTTER            => Stonecutter.tupled(params)
      case Material.STRING                 => PieceOfString.tupled(params)
      case Material.SUGAR                  => Sugar.tupled(params)
      case Material.SUGAR_CANE             => SugarCane.tupled(params)
      case Material.SWEET_BERRIES          => SweetBerries.tupled(params)
      case Material.TNT                    => TNT.tupled(params)
      case Material.TORCH                  => Torch.tupled(params)
      case Material.TOTEM_OF_UNDYING       => TotemOfUndying.tupled(singleParams)
      case Material.TRIDENT                => Trident.tupled(durableParams)
      case Material.TRIPWIRE_HOOK          => TripwireHook.tupled(params)
      case Material.TROPICAL_FISH          => TropicalFish.tupled(params)
      case Material.TURTLE_EGG             => TurtleEgg.tupled(params)
      case Material.TURTLE_HELMET          => TurtleHelmet.tupled(durableParams)
      case Material.VINE                   => Vine.tupled(params)
      case Material.WHEAT                  => Wheat.tupled(params)
      case Material.WRITABLE_BOOK          => BookAndQuill.tupled(singleParams)
      case Material.WRITTEN_BOOK           => WrittenBook.tupled(params)

      // TODO andesite etc are split in items, but merged in block, choose

      //      case Material.MUTTON  => Mutton(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.BAKED_POTATO => Potato(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.BEEF => Beef(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.CARVED_PUMPKIN => Pumpkin(carved = true, name, tooltip, count, attr, hideAttr)
      //      case Material.CHICKEN => Chicken(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.CHORUS_FRUIT => ChorusFruit(popped = false, name, tooltip, count, attr, hideAttr)
      //      case Material.COARSE_DIRT => Dirt(coarse = true, name, tooltip, count, attr, hideAttr)
      //      case Material.COD => Cod(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.COOKED_BEEF => Beef(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.COOKED_CHICKEN => Chicken(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.COOKED_COD => Cod(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.COOKED_MUTTON => Mutton(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.COOKED_PORKCHOP => Porkchop(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.COOKED_RABBIT => Rabbit(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.COOKED_SALMON => Salmon(cooked = true, name, tooltip, count, attr, hideAttr)
      //      case Material.DIRT => Dirt(coarse = false, name, tooltip, count, attr, hideAttr)
      //      case Material.ENCHANTED_GOLDEN_APPLE => GoldenApple(enchanted = true, name, tooltip, count, attr, hideAttr)
      //      case Material.FERMENTED_SPIDER_EYE => SpiderEye(fermented = true, name, tooltip, count, attr, hideAttr)
      //      case Material.FERN       => Fern(tall = false, name, tooltip, count, attr, hideAttr)
      //      case Material.FILLED_MAP => Map(filled = true, name, tooltip, count, attr, hideAttr)
      //      case Material.GOLDEN_APPLE => GoldenApple(enchanted = false, name, tooltip, count, attr, hideAttr)
      //      case Material.GRASS      => Grass(tall = false, name, tooltip, count, attr, hideAttr)
      //      case Material.LARGE_FERN => Fern(tall = true, name, tooltip, count, attr, hideAttr)
      //      case Material.MAP        => Map(filled = false, name, tooltip, count, attr, hideAttr)
      //      case Material.PISTON => Piston(sticky = false, name, tooltip, count, attr, hideAttr)
      //      case Material.POPPED_CHORUS_FRUIT => ChorusFruit(popped = true, name, tooltip, count, attr, hideAttr)
      //      case Material.PORKCHOP => Porkchop(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.POTATO => Potato(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.PUMPKIN => Pumpkin(carved = false, name, tooltip, count, attr, hideAttr)
      //      case Material.RABBIT => Rabbit(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.SALMON => Salmon(cooked = false, name, tooltip, count, attr, hideAttr)
      //      case Material.SPIDER_EYE => SpiderEye(fermented = false, name, tooltip, count, attr, hideAttr)
      //      case Material.SPONGE => Sponge(wet = false, name, tooltip, count, attr, hideAttr)
      //      case Material.STICKY_PISTON => Piston(sticky = true, name, tooltip, count, attr, hideAttr)
      //      case Material.TALL_GRASS => Grass(tall = true, name, tooltip, count, attr, hideAttr)
      //      case Material.WET_SPONGE => Sponge(wet = true, name, tooltip, count, attr, hideAttr)

      case m if m.isAnvil            => Anvil.tupled(v[AnvilVariant])
      case m if m.isArrow            => Arrow.tupled(v[ArrowVariant])
      case m if m.isBanner           => Banner.tupled(colorParams)
      case m if m.isBed              => Bed.tupled(colorParams)
      case m if m.isBoat             => Boat.tupled(v[BoatVariant])
      case m if m.isBrick            => Brick.tupled(v[BrickVariant])
      case m if m.isBrickBlock       => BrickBlock.tupled(v[BrickBlockVariant])
      case m if m.isBucket           => Bucket.tupled(v[BucketVariant])
      case m if m.isButton           => Button.tupled(v[ButtonVariant])
      case m if m.isCarpet           => Carpet.tupled(colorParams)
      case m if m.isCobblestone      => Cobblestone.tupled(v[CobblestoneVariant])
      case m if m.isChest            => Chest.tupled(v[ChestVariant])
      case m if m.isConcrete         => Concrete.tupled(colorParams)
      case m if m.isConcretePowder   => ConcretePowder.tupled(colorParams)
      case m if m.isCoral            => Coral.tupled(v[CoralVariant])
      case m if m.isCoralBlock       => CoralBlock.tupled(v[CoralBlockVariant])
      case m if m.isCoralFan         => CoralFan.tupled(v[CoralFanVariant])
      case m if m.isDye              => Dye.tupled(colorParams)
      case m if m.isEndStone         => EndStone.tupled(v[EndStoneVariant])
      case m if m.isFence            => Fence.tupled(v[FenceVariant])
      case m if m.isFenceGate        => FenceGate.tupled(v[FenceGateVariant])
      case m if m.isFlower           => Flower.tupled(v[FlowerVariant])
      case m if m.isGlass            => Glass.tupled(colorableParams)
      case m if m.isGlassPane        => GlassPane.tupled(colorableParams)
      case m if m.isGlazedTerracotta => GlazedTerracotta.tupled(colorParams)
      case m if m.isHorseArmor       => HorseArmor.tupled(v[HorseArmorVariant])
      case m if m.isIce              => Ice.tupled(v[IceVariant])
      case m if m.isLeaves           => Leaves.tupled(v[LeavesVariant])
      case m if m.isLog              => Log.tupled(v[LogVariant])
      case m if m.isMinecart         => Minecart.tupled(v[MinecartVariant])
      case m if m.isMobHead          => MobHead.tupled(v[MobHeadVariant])
      case m if m.isMushroom         => Mushroom.tupled(v[MushroomVariant])
      case m if m.isMusicDisc        => MusicDisc.tupled(v[MusicDiscVariant])
      case m if m.isPillar           => Pillar.tupled(v[PillarVariant])
      case m if m.isPlanks           => Planks.tupled(v[PlanksVariant])
      case m if m.isPlant            => Plant.tupled(v[PlantVariant])
      case m if m.isPrismarine       => Prismarine.tupled(v[PrismarineVariant])
      case m if m.isQuartzBlock      => QuartzBlock.tupled(v[QuartzBlockVariant])
      case m if m.isRail             => Rail.tupled(v[RailVariant])
      case m if m.isSand             => Sand.tupled(v[SandVariant])
      case m if m.isSandstone        => Sandstone.tupled(v[SandstoneVariant])
      case m if m.isSeeds            => Seeds.tupled(v[SeedsVariant])
      case m if m.isShulkerBox       => ShulkerBox.tupled(colorableParams)
      case m if m.isSlab             => Slab.tupled(v[SlabVariant])
      case m if m.isSpawnEgg         => SpawnEgg.tupled(v[SpawnEggVariant])
      case m if m.isStew             => Stew.tupled(v[StewVariant])
      case m if m.isStone            => Stone.tupled(v[StoneVariant])
      case m if m.isTerracotta       => Terracotta.tupled(colorableParams)
      case m if m.isWall             => Wall.tupled(v[WallVariant])
      case m if m.isWood             => Wood.tupled(v[WoodVariant])
      case m if m.isWool             => Wool.tupled(colorParams)

      // TODO
      // case m if m.isSapling          => Sapling.tupled(params[SaplingVariant])
      // case m if m.isStonite          => Stonite(loc, v[StoniteVariant])

      case m if m.isBannerPattern =>
        val _variant = variant.asInstanceOf[BannerPatternVariant]
        BannerPattern(_variant, name, tooltip, attr, hideAttr)

      case m if m.isInfestedBlock =>
        InfestedBlock.tupled(v[InfestedBlockVariant])

      case m if m.isMushroomBlock =>
        MushroomBlock.tupled(v[MushroomBlockVariant])

      case m if m.isPotion =>
        val _variant = variant.asInstanceOf[PotionVariant]
        val hideEffects = meta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
        Potion(_variant, hideEffects, name, tooltip, attr, hideAttr)

      case m if m.isStructureBlock =>
        StructureBlock.tupled(v[StructureBlockVariant])

      /* TODO
     case r".*PICKAXE" =>
       case r".*AXE" =>
       case r".*BOOTS" =>
       case r".*CHESTPLATE" =>
       case r".*HELMET" =>
       case r".*HOE" =>
       case r".*LEGGINGS" =>
       case r".*SHOVEL" =>
       case r".*SWORD" =>
     */
    })
  }

  def map(item: Item): SpigotItemStack = {
    val material = item match {
      case it: ColoredItem     => colorMapper.map(it)
      case it: VariableItem[_] => variantMapper.map(it)

      // TODO add item types, mostly only blocks atm

      case _: Barrel           => Material.BARREL
      case _: Barrier          => Material.BARRIER
      case _: Beacon           => Material.BEACON
      case _: Bedrock          => Material.BEDROCK
      case _: Beetroot         => Material.BEETROOT
      case _: Bell             => Material.BELL
      case _: BlastFurnace     => Material.BLAST_FURNACE
      case _: Bone             => Material.BONE_BLOCK
      case _: Bookshelf        => Material.BOOKSHELF
      case _: BrewingStand     => Material.BREWING_STAND
      case _: Cactus           => Material.CACTUS
      case _: Cake             => Material.CAKE
      case _: Campfire         => Material.CAMPFIRE
      case _: Carrot           => Material.CARROT
      case _: CartographyTable => Material.CARTOGRAPHY_TABLE
      case _: Cauldron         => Material.CAULDRON
      case _: ChorusFlower     => Material.CHORUS_FLOWER
      case _: ChorusPlant      => Material.CHORUS_PLANT
      case _: Clay             => Material.CLAY
      case _: Coal             => Material.COAL_BLOCK
      case _: CoalOre          => Material.COAL_ORE
      case _: Cobweb           => Material.COBWEB
      case _: Composter        => Material.COMPOSTER
      case _: Conduit          => Material.CONDUIT
      case _: CraftingTable    => Material.CRAFTING_TABLE
      case _: DaylightDetector => Material.DAYLIGHT_DETECTOR
      case _: DeadBush         => Material.DEAD_BUSH
      case _: Diamond          => Material.DIAMOND
      case _: DiamondBlock     => Material.DIAMOND_BLOCK
      case _: DiamondOre       => Material.DIAMOND_ORE
      case _: Dirt             => Material.DIRT
      case _: Dispenser        => Material.DISPENSER
      case _: DragonEgg        => Material.DRAGON_EGG
      case _: DriedKelp        => Material.DRIED_KELP_BLOCK
      case _: Dropper          => Material.DROPPER
      case _: Emerald          => Material.EMERALD_BLOCK
      case _: EmeraldOre       => Material.EMERALD_ORE
      case _: EnchantingTable  => Material.ENCHANTING_TABLE
      case _: EndPortalFrame   => Material.END_PORTAL_FRAME
      case _: EndRod           => Material.END_ROD
      case _: Farmland         => Material.FARMLAND
      case _: FletchingTable   => Material.FLETCHING_TABLE
      case _: Furnace          => Material.FURNACE
      case _: Glowstone        => Material.GLOWSTONE
      case _: GoldBlock        => Material.GOLD_BLOCK
      case _: GoldOre          => Material.GOLD_ORE
      case _: GrassBlock       => Material.GRASS_BLOCK
      case _: GrassPath        => Material.GRASS_PATH
      case _: Gravel           => Material.GRAVEL
      case _: Grindstone       => Material.GRINDSTONE
      case _: HayBale          => Material.HAY_BLOCK
      case _: Hopper           => Material.HOPPER
      case _: IronBlock        => Material.IRON_BLOCK
      case _: IronBars         => Material.IRON_BARS
      case _: IronOre          => Material.IRON_ORE
      case _: JigsawBlock      => Material.JIGSAW
      case _: Jukebox          => Material.JUKEBOX
      case _: Kelp             => Material.KELP
      case _: Ladder           => Material.LADDER
      case _: Lantern          => Material.LANTERN
      case _: Lapis            => Material.LAPIS_BLOCK
      case _: LapisOre         => Material.LAPIS_ORE
      case _: Lectern          => Material.LECTERN
      case _: Lever            => Material.LEVER
      case _: LilyPad          => Material.LILY_PAD
      case _: Loom             => Material.LOOM
      case _: MagmaBlock       => Material.MAGMA_BLOCK
      case _: Melon            => Material.MELON
      case _: Mycelium         => Material.MYCELIUM
      case _: Netherrack       => Material.NETHERRACK
      case _: NetherWart       => Material.NETHER_WART
      case _: NetherWartBlock  => Material.NETHER_WART_BLOCK
      case _: Observer         => Material.OBSERVER
      case _: Obsidian         => Material.OBSIDIAN
      case _: Podzol           => Material.PODZOL
      case _: Potato           => Material.POTATO
      case _: Pumpkin          => Material.PUMPKIN
      case _: PurpurBlock      => Material.PURPUR_BLOCK
      case _: QuartzOre        => Material.NETHER_QUARTZ_ORE
      case _: Redstone         => Material.REDSTONE_BLOCK
      case _: RedstoneLamp     => Material.REDSTONE_LAMP
      case _: RedstoneOre      => Material.REDSTONE_ORE
      case _: RedstoneTorch    => Material.REDSTONE_TORCH
      case _: Repeater         => Material.REPEATER
      case _: Scaffolding      => Material.SCAFFOLDING
      case _: Seagrass         => Material.SEAGRASS
      case _: SeaLantern       => Material.SEA_LANTERN
      case _: SeaPickle        => Material.SEA_PICKLE
      case _: SlimeBlock       => Material.SLIME_BLOCK
      case _: SmithingTable    => Material.SMITHING_TABLE
      case _: Smoker           => Material.SMOKER
      case _: Snow             => Material.SNOW
      case _: SnowBlock        => Material.SNOW_BLOCK
      case _: Spawner          => Material.SPAWNER
      case _: Stonecutter      => Material.STONECUTTER
      case _: SugarCane        => Material.SUGAR_CANE
      case _: SweetBerries     => Material.SWEET_BERRIES
      case _: TNT              => Material.TNT
      case _: Torch            => Material.TORCH
      case _: TurtleEgg        => Material.TURTLE_EGG
      case _: Vine             => Material.VINE
      case _: Wheat            => Material.WHEAT
    }

    val spigotItem = new SpigotItemStack(material)

    // TODO map skull meta

    spigotItem
  }
}

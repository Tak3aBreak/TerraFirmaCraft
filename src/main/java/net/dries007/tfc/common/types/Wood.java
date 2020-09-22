/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.common.types;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.util.NonNullFunction;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.common.blocks.wood.ToolRackBlock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.trees.OverlayTreeConfig;
import net.dries007.tfc.world.feature.trees.RandomTreeConfig;
import net.dries007.tfc.world.feature.trees.TFCTree;

public class Wood
{
    /**
     * Default wood types used for block registration calls
     * Not extensible
     *
     * todo: re-evaluate if there is any data driven behavior that needs a json driven ore
     *
     * @see Wood instead and register via json
     */
    public enum Default
    {
        ACACIA(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("acacia", 35)),
            wood -> TFCFeatures.DOUBLE_RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("acacia_large", 6, 2, 2, 2))
        ),
        ASH(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("ash/base"), Helpers.identifier("ash/overlay"), 1, 3, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState())),
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("ash_large", 5))
        ),
        ASPEN(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("aspen", 16))
        ),
        BIRCH(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("birch", 16))
        ),
        BLACKWOOD(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("blackwood/base"), Helpers.identifier("blackwood/overlay"), 2, 2, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState()))
        ),
        CHESTNUT(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("chestnut/base"), Helpers.identifier("chestnut/overlay"), 1, 3, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState()))
        ),
        DOUGLAS_FIR(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("douglas_fir", 9)),
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("douglas_fir_large", 5))
        ),
        HICKORY(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("hickory", 9)),
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("hickory_large", 5))
        ),
        KAPOK(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("kapok", 7))
        ),
        MAPLE(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("maple/base"), Helpers.identifier("maple/overlay"), 1, 3, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState())),
            wood -> TFCFeatures.DOUBLE_RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("maple_large", 5))
        ),
        OAK(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("oak/base"), Helpers.identifier("oak/overlay"), 1, 3, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState()))
        ),
        PALM(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("palm", 7)) // todo: random height variation
        ),
        PINE(true,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("pine", 9)),
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("pine_large", 5))
        ),
        ROSEWOOD(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("rosewood/base"), Helpers.identifier("rosewood/overlay"), 1, 3, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState()))
        ),
        SEQUOIA(true,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("sequoia", 7))
            // todo: large conifer generator
        ),
        SPRUCE(true,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("spruce", 7))
            // todo: large conifer generator
        ),
        SYCAMORE(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("sycamore/base"), Helpers.identifier("sycamore/overlay"), 1, 3, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState())),
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("sycamore_large", 5))
        ),
        WHITE_CEDAR(false,
            wood -> TFCFeatures.OVERLAY_TREE.get().configured(new OverlayTreeConfig(Helpers.identifier("white_cedar/base"), Helpers.identifier("white_cedar/overlay"), 1, 3, TFCBlocks.WOODS.get(wood).get(BlockType.LOG).get().defaultBlockState()))
        ),
        WILLOW(false,
            wood -> TFCFeatures.RANDOM_TREE.get().configured(RandomTreeConfig.forVariants("willow", 7))
        );

        private final boolean conifer;
        private final TFCTree tree;
        private final int fallFoliageCoords;

        Default(boolean conifer, Function<Default, ConfiguredFeature<?, ?>> feature)
        {
            this.conifer = conifer;
            this.tree = new TFCTree(() -> feature.apply(this)); // Allow the possibility to use a "this" reference in the initializer
            this.fallFoliageCoords = 100 | (100 << 8); // todo: random locations for each non-conifer tree
        }

        Default(boolean conifer, Function<Default, ConfiguredFeature<?, ?>> feature, Function<Default, ConfiguredFeature<?, ?>> oldGrowthFeature)
        {
            this.conifer = conifer;
            this.tree = new TFCTree(() -> feature.apply(this), () -> oldGrowthFeature.apply(this)); // Allow the possibility to use a "this" reference in the initializer
            this.fallFoliageCoords = 100 | (100 << 8); // todo: random locations for each non-conifer tree
        }

        public boolean isConifer()
        {
            return conifer;
        }

        public TFCTree getTree()
        {
            return tree;
        }

        public int getFallFoliageCoords()
        {
            return fallFoliageCoords;
        }

        public MaterialColor getMaterialColor()
        {
            return MaterialColor.COLOR_ORANGE; // todo: in 1.16 there are two material colors, one for the top, one for bark. We need to figure out which materials match our logs.
        }
    }

    public enum BlockType
    {
        LOG(wood -> new LogBlock(MaterialColor.SAND, Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(0.5F).sound(SoundType.WOOD)), false),
        STRIPPED_LOG(wood -> new LogBlock(MaterialColor.WOOD, Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F).sound(SoundType.WOOD)), false),
        WOOD(wood -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F).sound(SoundType.WOOD)), false),
        STRIPPED_WOOD(wood -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F).sound(SoundType.WOOD)), false),
        LEAVES(wood -> TFCLeavesBlock.create(Block.Properties.of(Material.LEAVES, wood.getMaterialColor()).strength(0.5F).sound(SoundType.GRASS).randomTicks().noOcclusion(), 6), false),
        PLANKS(wood -> new Block(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false),
        SAPLING(wood -> new TFCSaplingBlock(wood.getTree(), Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)), false),
        BOOKSHELF(wood -> new Block(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)), true),
        DOOR(wood -> new DoorBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()) {}, true),
        TRAPDOOR(wood -> new TrapDoorBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()) {}, true),
        FENCE(wood -> new FenceBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)), true),
        LOG_FENCE(wood -> new FenceBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)), true),
        FENCE_GATE(wood -> new FenceGateBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)), true),
        BUTTON(wood -> new WoodButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)) {}, true),
        PRESSURE_PLATE(wood -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.of(Material.WOOD, wood.getMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)) {}, true),
        SLAB(wood -> new SlabBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)), true),
        STAIRS(wood -> new StairsBlock(() -> TFCBlocks.WOODS.get(wood).get(PLANKS).get().defaultBlockState(), Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)), true),
        TOOL_RACK(wood -> new ToolRackBlock(Block.Properties.of(Material.WOOD, wood.getMaterialColor()).strength(2.0F).sound(SoundType.WOOD).noOcclusion()) {}, true);

        public static final BlockType[] VALUES = values();

        public static BlockType valueOf(int i)
        {
            return i >= 0 && i < VALUES.length ? VALUES[i] : LOG;
        }

        private final NonNullFunction<Default, Block> blockFactory;
        private final boolean isPlanksVariant;

        BlockType(NonNullFunction<Default, Block> blockFactory, boolean isPlanksVariant)
        {
            this.blockFactory = blockFactory;
            this.isPlanksVariant = isPlanksVariant;
        }

        public Supplier<Block> create(Default wood)
        {
            return () -> blockFactory.apply(wood);
        }

        public String id(Default wood)
        {
            return (isPlanksVariant ? "wood/planks/" + wood.name() + "_" + name().toLowerCase() : "wood/" + name() + "/" + wood.name()).toLowerCase();
        }
    }
}
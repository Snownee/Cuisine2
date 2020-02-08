package snownee.cuisine.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.tile.SpiceRackTile;
import snownee.kiwi.block.ModBlock;

public class SpiceRackBlock extends HorizontalBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    // master flag, for testing
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public SpiceRackBlock(Properties builder) {
        super(builder);
        ModBlock.deduceSoundAndHardness(this);
        setDefaultState(stateContainer.getBaseState().with(LIT, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return BaseModule.SPICE_RACK_TILE.create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult ray) {
        //        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
        //            TileEntity tile = worldIn.getTileEntity(pos);
        //            if (tile instanceof SpiceRackTile) {
        //                tile.getCapability(CuisineCapabilities.MULTIBLOCK).ifPresent(m -> {
        //                    if (!m.isMaster()) {
        //                        BlockPos pos1 = m.getMaster().getTile().getPos();
        //                        LightningBoltEntity lightningboltentity = new LightningBoltEntity(worldIn, pos1.getX() + 0.5D, pos1.getY(), pos1.getZ() + 0.5D, true);
        //                        ((ServerWorld) worldIn).addLightningBolt(lightningboltentity);
        //                    }
        //                    System.out.println(m.getMaster().all.keySet());
        //                });
        //            }
        //        }
        //        return ActionResultType.PASS;
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof SpiceRackTile) {
                player.openContainer((SpiceRackTile) tile);
            }
        }
        return ActionResultType.SUCCESS;
    }
}

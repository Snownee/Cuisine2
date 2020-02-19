package snownee.cuisine.cookware.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import snownee.cuisine.base.block.CookstoveBlock;
import snownee.cuisine.cookware.tile.CookwareTile;
import snownee.cuisine.cookware.tile.HeatingCookwareTile;
import snownee.kiwi.block.ModBlock;

public class CookwareBlock extends HorizontalBlock {

    private TileEntityType<?> tileType;

    public CookwareBlock(Properties builder) {
        super(builder);
        ModBlock.deduceSoundAndHardness(this);
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    public void setTileType(TileEntityType<?> tileType) {
        this.tileType = tileType;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return tileType != null;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tileType != null ? tileType.create() : null;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof CookwareTile) {
                player.openContainer((CookwareTile) tile);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!pos.down().equals(fromPos)) {
            return;
        }
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof HeatingCookwareTile) {
            ((HeatingCookwareTile) tile).heating = blockIn instanceof CookstoveBlock;
        }
    }

}

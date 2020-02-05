package snownee.cuisine.cookware.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.cookware.CookwareModule;
import snownee.cuisine.cookware.tile.OvenTile;
import snownee.kiwi.block.ModBlock;

public class OvenBlock extends HorizontalBlock {

    public OvenBlock(Properties builder) {
        super(builder);
        ModBlock.deduceSoundAndHardness(this);
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return CookwareModule.OVEN_TILE.create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof OvenTile) {
            if (!worldIn.isRemote) {
                CuisineRecipe recipe = ((OvenTile) tile).cook(player);
                System.out.println(recipe);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

}

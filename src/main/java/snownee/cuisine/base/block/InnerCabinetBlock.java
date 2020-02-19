package snownee.cuisine.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import snownee.cuisine.base.BaseModule;
import snownee.kiwi.block.ModBlock;

public class InnerCabinetBlock extends HorizontalBlock {

    public InnerCabinetBlock(Properties builder) {
        super(builder);
        ModBlock.deduceSoundAndHardness(this);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return BaseModule.createConnector();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

}

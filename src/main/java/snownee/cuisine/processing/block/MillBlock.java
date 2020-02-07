package snownee.cuisine.processing.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import snownee.cuisine.processing.ProcessingModule;
import snownee.cuisine.processing.tile.MillTile;
import snownee.kiwi.block.ModBlock;

public class MillBlock extends HorizontalBlock {

    public MillBlock(Properties builder) {
        super(builder);
        ModBlock.deduceSoundAndHardness(this);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MillTile(ProcessingModule.MILL_TILE);
    }

}

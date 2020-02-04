//////////////////////////////////////////////
//from https://github.com/mallrat208/UnWIRED
//////////////////////////////////////////////

package snownee.cuisine.util.ingredient;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeFluidHelper
{
	public RecipeFluidHelper() {}
	
	public static int pack(FluidStack stack)
	{
		return Registry.FLUID.getId(stack.getFluid());
	}
	
	public static Fluid unpack(int packedFluid)
	{
		return packedFluid == 0 ? Fluids.EMPTY : Registry.FLUID.getByValue(packedFluid);
	}
}

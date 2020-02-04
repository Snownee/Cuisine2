package snownee.cuisine.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.fluids.FluidStack.EMPTY;

public class FluidHelper {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();

    public static FluidStack read(CompoundNBT compound) {
        try {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(compound.getString("id")));
            if (fluid == null) {
                throw new RuntimeException("could not found fliud.");
            }
            if (compound.contains("tag", 10)) {
                return new FluidStack(fluid,
                        compound.getInt("amount"), compound.getCompound("tag"));
            } else {
                return new FluidStack(fluid,
                        compound.getInt("amount"));
            }
        } catch (RuntimeException runtimeexception) {
            LOGGER.debug("Tried to load invalid fluid: {}", compound, runtimeexception);
            return EMPTY;
        }
    }

    public static FluidStack getItemStack(JsonObject json, boolean readNBT) {
        String itemName = JSONUtils.getString(json, "fluid");

        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(itemName));

        if (fluid == null)
            throw new JsonSyntaxException("Unknown fluid '" + itemName + "'");

        if (readNBT && json.has("nbt")) {
            try {
                JsonElement element = json.get("nbt");
                CompoundNBT nbt;
                if (element.isJsonObject())
                    nbt = JsonToNBT.getTagFromJson(GSON.toJson(element));
                else
                    nbt = JsonToNBT.getTagFromJson(JSONUtils.getString(element, "nbt"));

                CompoundNBT tmp = new CompoundNBT();

                tmp.put("tag", nbt);
                tmp.putString("id", itemName);
                tmp.putInt("amount", JSONUtils.getInt(json, "amount", 1));

                return read(tmp);
            } catch (CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
            }
        }

        return new FluidStack(fluid, JSONUtils.getInt(json, "amount", FluidAttributes.BUCKET_VOLUME));
    }
    public static FluidStack read(PacketBuffer buffer) {
        Fluid fluid = buffer.readRegistryIdUnsafe(ForgeRegistries.FLUIDS);
        int amount  = buffer.readInt();
        CompoundNBT nbt = buffer.readCompoundTag();
        return new FluidStack(fluid,amount,nbt);
    }

    public static void write(PacketBuffer buffer,FluidStack fluidStack) {
        buffer.writeRegistryIdUnsafe(ForgeRegistries.FLUIDS,fluidStack.getFluid());
        buffer.writeInt(fluidStack.getAmount());
        buffer.writeCompoundTag(fluidStack.getTag());
    }
}

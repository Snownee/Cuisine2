//////////////////////////////////////////////
//from https://github.com/mallrat208/UnWIRED
//////////////////////////////////////////////

package snownee.cuisine.util.ingredient;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FluidIngredient implements Predicate<FluidStack>
{
	private static final Set<FluidIngredient> INSTANCES = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Predicate<? super IFluidList> IS_EMPTY = fluidList -> {
		return !fluidList.getStacks().stream().allMatch(FluidStack::isEmpty);
	};
	
	public static final FluidIngredient EMPTY = new FluidIngredient(Stream.empty());
	private final IFluidList[] acceptedFluids;
	private FluidStack[] matchingFluids;
	private IntList matchingFluidsPacked;
	
	private final boolean isDefault = this.getClass() == FluidIngredient.class;
	
	protected FluidIngredient(Stream<? extends IFluidList> fluidLists)
	{
		this.acceptedFluids = fluidLists.filter(IS_EMPTY).toArray((index) ->
		{
			return new IFluidList[index];
		});
		
		
		
		INSTANCES.add(this);
	}
	
	public FluidStack[] getMatchingFluids()
	{
		this.determineMatchingFluids();
		return this.matchingFluids;
	}
	
	private void determineMatchingFluids()
	{
		if(this.matchingFluids == null)
		{
			this.matchingFluids = Arrays.stream(this.acceptedFluids).flatMap(iFluidList ->iFluidList.getStacks().stream()).distinct().toArray(FluidStack[]::new);
		}
	}
	
	public static void invalidateAll()
	{
		INSTANCES.stream().filter((e) -> {
			return e != null;
		}).forEach((i) -> {i.invalidate();});
	}
	
	protected void invalidate()
	{
		this.matchingFluids = null;
		this.matchingFluidsPacked = null;
	}
	
	@Override
	public boolean test(@Nullable FluidStack testStack)
	{
		if(testStack == null)
			return false;
		else if(this.acceptedFluids.length == 0)
			return testStack.isEmpty();
		else
		{
			this.determineMatchingFluids();
			FluidStack[] fluidStacks = this.matchingFluids;
			for(FluidStack fluidStack : fluidStacks)
			{
				if(fluidStack.getFluid()==testStack.getFluid() && fluidStack.getAmount() <= testStack.getAmount())
					return true;
			}
			
			return false;
		}
		
	}
	
	public IntList getMatchingFluidsPacked()
	{
		if(this.matchingFluidsPacked==null)
		{
			this.determineMatchingFluids();
			this.matchingFluidsPacked = new IntArrayList(this.matchingFluids.length);
			
			for(FluidStack fluidStack:this.matchingFluids)
				this.matchingFluidsPacked.add(RecipeFluidHelper.pack(fluidStack));
			
			
			this.matchingFluidsPacked.sort(IntComparators.NATURAL_COMPARATOR);
		}
		
		return this.matchingFluidsPacked;
	}
	
	public final void write(PacketBuffer buffer)
	{
		this.determineMatchingFluids();
		
		buffer.writeVarInt(this.matchingFluids.length);
		
		for(FluidStack stack:this.matchingFluids)
			buffer.writeCompoundTag(stack.writeToNBT(new CompoundNBT()));
	}
	
	public static FluidIngredient read(PacketBuffer buffer)
	{
		int i = buffer.readVarInt();
		return fromFluidListStream(Stream.generate(() ->new SingleFluidList(FluidStack.loadFluidStackFromNBT(buffer.readCompoundTag()))).limit(i));
	}
	
	public static FluidIngredient fromFluidListStream(Stream<? extends IFluidList> stream)
	{
		FluidIngredient ingredient = new FluidIngredient(stream);
		return ingredient.acceptedFluids.length == 0 ? EMPTY : ingredient;
	}
	
	public JsonElement serialize()
	{
		if(this.acceptedFluids.length == 1)
		{
			return this.acceptedFluids[0].serialize();
		}
		else
		{
			JsonArray jsonArray = new JsonArray();
			IFluidList[] fluids = this.acceptedFluids;
			
			for(IFluidList fluid : fluids)
			{
				jsonArray.add(fluid.serialize());
			}
			
			return jsonArray;
		}
	}
	
	public static FluidIngredient deserialize(@Nullable JsonElement json)
	{
		if(json!=null && !json.isJsonNull())
		{
			if(json.isJsonObject())
			{
				return fromFluidListStream(Stream.of(deserializeFluidList(json.getAsJsonObject())));
			}
			else if(json.isJsonArray())
			{
				JsonArray array = json.getAsJsonArray();
				if(array.size() == 0)
				{
					throw  new JsonSyntaxException("Fluid array cannot be empty, at least one fluid must by defined");
				}
				else
				{
					return fromFluidListStream(StreamSupport.stream(array.spliterator(), false).map(element -> {
						return deserializeFluidList(JSONUtils.getJsonObject(element, "fluid"));
					}));
				}
				
			}
			else
			{
				throw new JsonSyntaxException("Expected fluid to be object or array of objects");
			}
		}
		else
		{
			throw new JsonSyntaxException("Fluid cannot be null");
		}
	}
	
	public static IFluidList deserializeFluidList(JsonObject json)
	{
		if(json.has("fluid") && json.has("tag"))
		{
			throw new JsonParseException("A FluidIngredient entry should be a Tag or Fluid, not both");
		}
		else
		{
			ResourceLocation resourceLocation;
			int amount = 1000;
			
			if(json.has("amount"))
			{
				amount = JSONUtils.getInt(json,"amount");
			}
			
			if(json.has("fluid"))
			{
				resourceLocation = new ResourceLocation(JSONUtils.getString(json, "fluid"));
				Fluid fluid = Registry.FLUID.getValue(resourceLocation).orElseThrow(() ->{return new JsonSyntaxException("Unknown fluid '" + resourceLocation + "'");});
				return new SingleFluidList(new FluidStack(fluid, amount));
			}
			else if(json.has("tag"))
			{
				resourceLocation = new ResourceLocation(JSONUtils.getString(json, "tag"));
				Tag<Fluid> tag = FluidTags.getCollection().get(resourceLocation);
				
				if(tag==null)
				{
					throw new JsonSyntaxException("Unknown fluid tag '" + resourceLocation + "'");
				}
				else
				{
					return new TagList(tag, amount);
				}
			}
			else
			{
				throw new JsonParseException("A fluid ingredient requires either a fluid or a tag");
			}
		}
		
	}
	
	public static FluidIngredient merge(Collection<FluidIngredient> parts)
	{
		return fromFluidListStream(parts.stream().flatMap((i) ->Arrays.stream(i.acceptedFluids)));
	}
	
	public static FluidIngredient fromFluids(int amount, Fluid... fluidsIn)
	{
		return fromFluidListStream(Arrays.stream(fluidsIn).map(fluids ->new SingleFluidList(new FluidStack(fluids, amount))));
	}
	
	public static FluidIngredient fromTag(int amount, Tag<Fluid> tagIn)
	{
		return fromFluidListStream(Stream.of(new TagList(tagIn, amount)));
	}
	
	public static FluidIngredient fromFluidStacks(FluidStack... stacks)
	{
		return fromFluidListStream(Arrays.stream(stacks).map(SingleFluidList::new));
	}
	
	public static class TagList implements IFluidList
	{
		private final Tag<Fluid> tag;
		private final int amount;
		
		public TagList(Tag<Fluid> tagIn, int amountIn)
		{
			this.tag = tagIn;
			this.amount = amountIn;
		}
		
		@Override
		public Collection<FluidStack> getStacks()
		{
			List<FluidStack> list =Lists.newArrayList();
			Iterator itr = this.tag.getAllElements().iterator();
			
			while(itr.hasNext())
			{
				Fluid fluid = (Fluid)itr.next();
				list.add(new FluidStack(fluid, amount));
			}
			
			if(list.size() == 0 && !ForgeConfig.SERVER.treatEmptyTagsAsAir.get())
			{
				list.add(FluidStack.EMPTY);
			}
			
			return list;
		}
		
		@Override
		public JsonObject serialize()
		{
			JsonObject json = new JsonObject();
			json.addProperty("tag", this.tag.getId().toString());
			json.addProperty("amount", this.amount);
			
			return json;
		}
	}
	
	public static class SingleFluidList implements IFluidList
	{
		private final FluidStack stack;
		
		public SingleFluidList(FluidStack stackIn)
		{
			this.stack = stackIn;
		}
		
		@Override
		public Collection<FluidStack> getStacks()
		{
			return Collections.singleton(this.stack);
		}
		
		@Override
		public JsonObject serialize()
		{
			JsonObject json = new JsonObject();
			json.addProperty("fluid", Registry.FLUID.getKey(this.stack.getFluid()).toString());
			json.addProperty("amount", this.stack.getAmount());
			
			return json;
		}
	}
	
	public interface IFluidList
	{
		Collection<FluidStack> getStacks();
		
		JsonObject serialize();
	}
}

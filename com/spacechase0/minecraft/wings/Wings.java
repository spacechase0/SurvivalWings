package com.spacechase0.minecraft.wings;

import static net.minecraft.init.Blocks.obsidian;
import static net.minecraft.init.Items.feather;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.wings.item.Items;
import com.spacechase0.minecraft.wings.item.WingsItem;

// 1.2.7 - Hopefully fixed other mods not being able to use PlayerCapabilities.allowFlying
// 1.2.6 - Updated for SpaceCore 0.7.9.
// 1.2.5 - Updated for SpaceCore 0.7.4.
// 1.2.4 - Updated for SpaceCore 0.6.0.

// TODO: Make ticks per-stack, degrades faster with more people using it otherwise

@Mod( modid = "SC0_Wings", useMetadata = true, dependencies="required-after:SC0_SpaceCore" )
public class Wings extends BaseMod
{
	public Wings()
	{
		super( "wings" );
	}
	
	@Instance( "SC0_Wings" )
	public static Wings instance;
	
	@SidedProxy( clientSide = "com.spacechase0.minecraft.wings.client.ClientProxy",
			     serverSide = "com.spacechase0.minecraft.wings.CommonProxy" )
	public static CommonProxy proxy;
	
	@Override
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		super.init( event );
		proxy.init();
		addRecipes();
		addTickers();
	}
	
	public boolean degradeWingsByFlying()
	{
		return config.get( "misc", "useDegradingWings", true ).getBoolean( true );
	}
	
	public int getDurabilityFactor()
	{
		return config.get( Configuration.CATEGORY_GENERAL, "durabilityMult", DEFAULT_DURABILITY_MULT ).getInt();
	}
	
	private void addRecipes()
	{
		GameRegistry.addRecipe( new ItemStack( items.featherWings ),
		                        new Object[]
		                        {
			                    	"## ",
			                    	"###",
			                    	" ##",
			                    	'#', feather
		                        } );
		GameRegistry.addRecipe( new ItemStack( items.obsidianWings ),
                                new Object[]
                                {
                	            	"## ",
                	            	"###",
                	            	" ##",
                	            	'#', obsidian
                                } );
		GameRegistry.addShapelessRecipe( new ItemStack( items.sturdyWings ),
                                         new Object[]
                                         {
                	            	     	new ItemStack( items.featherWings ),
			                             	new ItemStack( items.obsidianWings ),
			                             	new ItemStack( net.minecraft.init.Items.nether_wart )
                                         } );
		GameRegistry.addRecipe( new FastWingsRecipe() );
	}
	
	private void addTickers()
	{
		FMLCommonHandler.instance().bus().register( new WingsUpdater() );
	}
	
	public static boolean isPairOfWings( Item item )
	{
		return ( item instanceof WingsItem );
	}

	public static float getWingSpeed( Item item )
	{
		float base = 0.05f;
		
		if ( item == items.obsidianWings )
		{
			base = 0.02f;
		}
		else if ( item == items.fastWings )
		{
			base = 0.08f;
		}
		
		return base;
	}
	
	public Configuration config;
	public static Items items;
	
	private final int DEFAULT_DURABILITY_MULT = 50;
}

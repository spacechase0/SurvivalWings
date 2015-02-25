package com.spacechase0.minecraft.wings.item;

import net.minecraftforge.common.config.Configuration;

import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.util.ModObject;
import com.spacechase0.minecraft.wings.Wings;

public class Items extends com.spacechase0.minecraft.spacecore.item.Items
{
	@Override
	public void register( BaseMod mod, Configuration config )
	{
		super.register( mod, config );
		configureMaxDurability();
	}
	
	@ModObject
	public WingsItem featherWings;
	public Object[] featherWingsParams = new Object[] { "feather", false };
	
	@ModObject
	public WingsItem obsidianWings;
	public Object[] obsidianWingsParams = new Object[] { "obsidian", false };
	
	@ModObject
	public WingsItem sturdyWings;
	public Object[] sturdyWingsParams = new Object[] { "sturdy", false };
	
	@ModObject
	public WingsItem fastWings;
	public Object[] fastWingsParams = new Object[] { "fast", true };
	
	private void configureMaxDurability()
	{
		if ( Wings.instance.degradeWingsByFlying() )
		{
			int factor = Wings.instance.getDurabilityFactor();
			featherWings .setMaxDamage( 25  * factor - 1 );
			obsidianWings.setMaxDamage( 100 * factor - 1 );
			sturdyWings  .setMaxDamage( 75  * factor - 1 );
			fastWings    .setMaxDamage( 50  * factor - 1 );
		}
		else
		{
			featherWings .setMaxDamage(  5 - 1 );
			obsidianWings.setMaxDamage( 50 - 1 );
			sturdyWings  .setMaxDamage( 40 - 1 );
			fastWings    .setMaxDamage( 30 - 1 );
		}
	}
}

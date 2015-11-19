package com.spacechase0.minecraft.wings;

import java.lang.reflect.Field;

import com.spacechase0.minecraft.wings.item.WingsItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

// TODO: Fix with other flight things

public class WingsUpdater
{
	@SubscribeEvent
	public void tick( TickEvent.PlayerTickEvent event )
	{
		//if ( !event.side.equals( Side.SERVER ) ) return;
		if ( !event.phase.equals( TickEvent.Phase.START ) ) return;
		
		// Maybe I should move the armor degradation here so it is all together?
		// Currently in WingsItem.onArmorTick
		
		EntityPlayer player = ( EntityPlayer ) event.player;
		if ( player.capabilities.isCreativeMode ) return;
		NBTTagCompound data = player.getEntityData();
		
		if ( player.inventory.armorInventory[ 2 ] != null )
		{
			Item item = player.inventory.armorInventory[ 2 ].getItem();
			if ( Wings.isPairOfWings( item ) )
			{
				if ( !data.hasKey( WingsItem.TICKS_TAG ) )
				{
					data.setByte( WingsItem.TICKS_TAG, ( byte ) 0 );
				}
				player.capabilities.allowFlying = true;
				float speed = Wings.getWingSpeed( item );
				if ( player.capabilities.getFlySpeed() != speed )
				{
					setFlySpeed( player.capabilities, speed );
				}
			}
			// If wings aren't equipped but the tag is still there (ie. it was equipped before)
			// Can't constantly set allowFlying to false because it conflicts with other mods
			else if ( data.hasKey( WingsItem.TICKS_TAG ) ) 
			{
				data.removeTag( WingsItem.TICKS_TAG );
				player.capabilities.allowFlying = false;
				player.capabilities.isFlying = false;
				if ( player.capabilities.getFlySpeed() != 0.05f )
				{
					setFlySpeed( player.capabilities, 0.05f );
				}
			}
		}
		else if ( data.hasKey( WingsItem.TICKS_TAG ) )
		{
			data.removeTag( WingsItem.TICKS_TAG );
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
			if ( player.capabilities.getFlySpeed() != 0.05f )
			{
				setFlySpeed( player.capabilities, 0.05f );
			}
		}
	}
	
	// Client-only setter? Weird.
	private void setFlySpeed( PlayerCapabilities pc, float speed )
	{
		ReflectionHelper.setPrivateValue( PlayerCapabilities.class, pc, speed, 5 );
	}
}

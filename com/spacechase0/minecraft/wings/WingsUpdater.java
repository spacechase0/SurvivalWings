package com.spacechase0.minecraft.wings;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

// TODO: Fix with other flight things

public class WingsUpdater
{
	@SubscribeEvent
	public void tick( TickEvent.PlayerTickEvent event )
	{
		if ( !event.phase.equals( TickEvent.Phase.START ) ) return;
		
		EntityPlayer player = ( EntityPlayer ) event.player;
		
		boolean wasFlying = player.capabilities.isFlying;
		
		if ( player.inventory.armorInventory[ 2 ] != null )
		{
			Item item = player.inventory.armorInventory[ 2 ].getItem();
			if ( Wings.isPairOfWings( item ) )
			{
				player.capabilities.allowFlying = true;
				float speed = Wings.getWingSpeed( item );
				if ( player.capabilities.getFlySpeed() != speed )
				{
					setFlySpeed( player.capabilities, speed );
				}
			}
			else
			{
				player.capabilities.allowFlying = false;
				player.capabilities.isFlying = false;
				if ( player.capabilities.getFlySpeed() != 0.05f )
				{
					setFlySpeed( player.capabilities, 0.05f );
				}
			}
		}
		else
		{
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
			if ( player.capabilities.getFlySpeed() != 0.05f )
			{
				setFlySpeed( player.capabilities, 0.05f );
			}
		}
		
		if ( player.capabilities.isCreativeMode )
		{
			player.capabilities.allowFlying = true;
			player.capabilities.isFlying = wasFlying;
		}
	}
	
	// Client-only setter? Weird.
	private void setFlySpeed( PlayerCapabilities pc, float speed )
	{
		try
		{
			Field field = PlayerCapabilities.class.getDeclaredFields()[ 5 ];
			field.setAccessible( true );
			field.set( pc, speed );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
}

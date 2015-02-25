package com.spacechase0.minecraft.wings.client;

import java.util.Random;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class WingsModel extends ModelBiped
{
	public WingsModel( EntityLivingBase entity )
	{
		player = ( EntityPlayer ) entity;
		
		leftWing = new ModelRenderer( this, 0, 0 );
		leftWing.addBox( 0.f, -1.f, 0.f, 0, 13, 12, 0.f );
		leftWing.setRotationPoint( 0.f, 0.f, 2.f );
		leftWing.rotateAngleY = MIN;
		
		rightWing = new ModelRenderer( this, 0, 0 );
		rightWing.addBox( 0.f, -1.f, 0.f, 0, 13, 12, 0.f );
		rightWing.setRotationPoint( 0.f, 0.f, 2.f );
		rightWing.rotateAngleY = MIN;
		rightWing.mirror = true;
	}
	
	@Override
    public void render( Entity entity, float par2, float par3, float par4, float par5, float par6, float par7 )
    {
    	if ( player.capabilities.isFlying )
		{
    		updateRotation();
		}
    	
        leftWing.render( par7 );
        rightWing.render( par7 );
    }
	
	private void updateRotation()
	{
		float totalMotion = ( float )( Math.abs( player.motionX ) + Math.abs( player.motionY ) + Math.abs( player.motionZ ) );
		float incr = ( ( outwards ) ? INCR : -INCR );
		float amt = incr * totalMotion * 5;
		rot += ( incr > 0 ) ? Math.max( incr, amt ) : Math.min( incr, amt );
		
    	if ( rot <= MIN )
    	{
    		outwards = true;
            player.worldObj.playSound( player.posX, player.posY, player.posZ, "mob.enderdragon.wings", 0.25f, 1.5f + soundRand.nextFloat() * 0.5f, true );
    	}
    	else if ( rot >= MAX )
    	{
    		outwards = false;
    	}
    	
    	leftWing.rotateAngleY = rot;
    	rightWing.rotateAngleY = -rot;
	}
	
	private final EntityPlayer player;
	private ModelRenderer leftWing;
	private ModelRenderer rightWing;
	private Random soundRand = new Random();

	private float rot = 0.f;
	private boolean outwards = true;
	private static final float INCR = 0.0175f * 3;
	private static final float MIN = 0.2f;
	private static final float MAX = 1.35f;
}

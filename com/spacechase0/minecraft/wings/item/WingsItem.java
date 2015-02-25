package com.spacechase0.minecraft.wings.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import com.spacechase0.minecraft.wings.Wings;
import com.spacechase0.minecraft.wings.client.WingsModel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WingsItem extends ItemArmor
{
	public WingsItem( String name, boolean theShine )
	{
		super( getMaterialFor( name ), 0, 1 );
		shine = theShine;
		
		setUnlocalizedName( name + "Wings" );
        maxStackSize = 1;
	}
	
	@Override
	public void registerIcons( IIconRegister ir )
	{
		itemIcon = ir.registerIcon( "wings:" + getUnlocalizedName().substring( 5 ) );
	}

    @Override
    public void onArmorTick( World world, EntityPlayer player, ItemStack stack )
    {
    	if ( !Wings.instance.degradeWingsByFlying() || world.isRemote )
    	{
    		return;
    	}
    	
    	if ( player.inventory.armorInventory[ 2 ] != stack || !player.capabilities.isFlying )
    	{
    		return;
    	}
    	
    	if ( ++ticks >= 20 )
    	{
    		ticks -= 20;
    		
        	int y = ( int ) player.posY;
        	int iy = y;
        	for ( ; iy >= 0; --iy )
        	{
        		Block block = world.getBlock( ( int ) player.posX, iy, ( int ) player.posZ );
        		if ( block.getMaterial().isSolid() )
        		{
        			break;
        		}
        	}
        	
        	int dmg = ( int ) Math.ceil( ( y - iy ) / 1.7f );
        	stack.damageItem( dmg, player );
        	
        	if ( player.inventory.armorInventory[ 2 ].stackSize == 0 )
            {
        		player.inventory.armorInventory[ 2 ] = null;
            }
    	}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect( ItemStack stack, int pass )
    {
        return shine || super.hasEffect( stack, pass );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity( ItemStack stack)
    {
    	if ( Wings.items.fastWings == this )
    	{
    		return EnumRarity.epic;
    	}
    	else if ( Wings.items.sturdyWings == this )
    	{
    		return EnumRarity.rare;
    	}
    	else if ( Wings.items.obsidianWings == this )
    	{
    		return EnumRarity.uncommon;
    	}
    	
    	return EnumRarity.common;
    }
    
    private static ArmorMaterial getMaterialFor( String name )
    {
    	if ( name.equals( "feather" ) )
    	{
    		return featherMaterial;
    	}
    	else if ( name.equals( "obsidian" ) )
    	{
    		return obsidianMaterial;
    	}
    	else if ( name.equals( "sturdy" ) )
    	{
    		return sturdyMaterial;
    	}
    	else if ( name.equals( "fast" ) )
    	{
    		return fastMaterial;
    	}
    	
    	return null;
    }
    
    @Override
    public String getArmorTexture( ItemStack stack, Entity entity, int slot, String type )
    {
		if ( this == Wings.items.obsidianWings )
		{
			return "wings:textures/models/armor/wings-obsidian.png";
		}
		
		return "wings:textures/models/armor/wings-normal.png";
    }
    
    @Override
    @SideOnly( Side.CLIENT )
    public ModelBiped getArmorModel( EntityLivingBase entity, ItemStack stack, int slot )
    {
    	// Why is it slot 1 here, but slot 2 above? I have no idea
    	if ( slot != 1 )
    	{
    		return null;
    	}

    	WingsModel model = wingModels.get( entity );
    	if ( model == null )
    	{
    		wingModels.put( entity, new WingsModel( entity ) );
    		return getArmorModel( entity, stack, slot );
    	}
    	
        return model;
    }
	
	private static final ArmorMaterial featherMaterial;
	private static final ArmorMaterial obsidianMaterial;
	private static final ArmorMaterial sturdyMaterial;
	private static final ArmorMaterial fastMaterial;
	private final boolean shine;
	private static int ticks;
	private static Map< EntityLivingBase, WingsModel > wingModels = new HashMap< EntityLivingBase, WingsModel >();
	
	static
	{
		featherMaterial = EnumHelper.addArmorMaterial( "wingsFeather", 1, new int[] { 1, 1, 1, 1 }, 5 );
		featherMaterial.customCraftingMaterial = feather;
		
		obsidianMaterial = EnumHelper.addArmorMaterial( "wingsObsidian", 1, new int[] { 3, 3, 3, 3 }, 15 );
		obsidianMaterial.customCraftingMaterial = Item.getItemFromBlock( obsidian );
		
		sturdyMaterial = EnumHelper.addArmorMaterial( "wingsSturdy", 1, new int[] { 2, 2, 2, 2 }, 13 );
		sturdyMaterial.customCraftingMaterial = net.minecraft.init.Items.nether_wart;
		
		fastMaterial = EnumHelper.addArmorMaterial( "wingsFast", 1, new int[] { 1, 1, 1, 1 }, 10 );
		fastMaterial.customCraftingMaterial = ender_eye;
	}
}

package com.spacechase0.minecraft.wings.item;

import static net.minecraft.init.Blocks.obsidian;
import static net.minecraft.init.Items.ender_eye;
import static net.minecraft.init.Items.feather;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spacechase0.minecraft.wings.Wings;
import com.spacechase0.minecraft.wings.client.WingsModel;

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
    	
    	NBTTagCompound data = player.getEntityData();
    	data.setByte( TICKS_TAG, ( byte )( data.getByte( TICKS_TAG ) + 1 ) );
    	if ( data.getByte( TICKS_TAG ) >= 20 )
    	{
    		data.setByte( TICKS_TAG, ( byte ) 0 );
    		
        	int y = ( int ) player.posY;
        	int iy = y;
        	for ( ; iy >= 0; --iy )
        	{
        		Block block = world.getBlockState( new BlockPos( ( int ) player.posX, iy, ( int ) player.posZ ) ).getBlock();
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
    public boolean hasEffect( ItemStack stack )
    {
        return shine || super.hasEffect( stack );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity( ItemStack stack )
    {
    	if ( Wings.items.fastWings == this )
    	{
    		return EnumRarity.RARE;
    	}
    	else if ( Wings.items.sturdyWings == this )
    	{
    		return EnumRarity.UNCOMMON;
    	}
    	else if ( Wings.items.obsidianWings == this )
    	{
    		return EnumRarity.COMMON;
    	}
    	
    	return EnumRarity.COMMON;
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
			return "sc0_wings:textures/models/armor/wings-obsidian.png";
		}
		
		return "sc0_wings:textures/models/armor/wings-normal.png";
    }
    
    @Override
    @SideOnly( Side.CLIENT )
    public ModelBiped getArmorModel( EntityLivingBase entity, ItemStack stack, int slot )
    {
    	if ( slot != 3 )
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
    
    public static final String TICKS_TAG = "Wings_Ticks";
	
	private static final ArmorMaterial featherMaterial;
	private static final ArmorMaterial obsidianMaterial;
	private static final ArmorMaterial sturdyMaterial;
	private static final ArmorMaterial fastMaterial;
	private final boolean shine;
	private static Map< EntityLivingBase, WingsModel > wingModels = new HashMap< EntityLivingBase, WingsModel >();
	
	static
	{
		featherMaterial = EnumHelper.addArmorMaterial( "wingsFeather", "", 1, new int[] { 1, 1, 1, 1 }, 5 );
		featherMaterial.customCraftingMaterial = feather;
		
		obsidianMaterial = EnumHelper.addArmorMaterial( "wingsObsidian", "", 1, new int[] { 3, 3, 3, 3 }, 15 );
		obsidianMaterial.customCraftingMaterial = Item.getItemFromBlock( obsidian );
		
		sturdyMaterial = EnumHelper.addArmorMaterial( "wingsSturdy", "", 1, new int[] { 2, 2, 2, 2 }, 13 );
		sturdyMaterial.customCraftingMaterial = net.minecraft.init.Items.nether_wart;
		
		fastMaterial = EnumHelper.addArmorMaterial( "wingsFast", "", 1, new int[] { 1, 1, 1, 1 }, 10 );
		fastMaterial.customCraftingMaterial = ender_eye;
	}
}

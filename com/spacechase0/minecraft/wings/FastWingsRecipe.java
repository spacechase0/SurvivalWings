package com.spacechase0.minecraft.wings;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class FastWingsRecipe implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches( InventoryCrafting inv, World world )
    {
    	if ( doUpgradeCheck( inv ) != null )
    	{
    		return true;
    	}
    	
    	if ( doDyeCheck( inv ) != null )
    	{
    		return false;//true;
    	}
    	
    	return false;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult( InventoryCrafting inv )
    {
    	ItemStack upgrade = doUpgradeCheck( inv );
    	ItemStack dye = doDyeCheck( inv );
    	if ( upgrade != null )
    	{
    		ItemStack result = new ItemStack( Wings.items.fastWings, 1, upgrade.getItemDamage() );
    		result.setTagCompound( ( NBTTagCompound ) upgrade.getTagCompound().copy() );
    		result.setItemDamage( 0 );
    		return result;
    	}
    	else if ( dye != null )
    	{
    		return null;
    	}

    	return null;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 5;
    }

    public ItemStack getRecipeOutput()
    {
        return null;
    }
    
    private ItemStack doUpgradeCheck( InventoryCrafting inv )
    {
        ItemStack wings = null;
        int enderEyeCount = 0;

        for ( int i = 0; i < inv.getSizeInventory(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );
            if ( stack == null )
            {
            	continue;
            }
            
            if ( stack.getItem() == Wings.items.sturdyWings )
            {
            	wings = stack;
            }
            else if ( stack.getItem() == Items.ender_eye )
            {
            	++enderEyeCount;
            }
            else
            {
            	return null;
            }
        }

        if ( enderEyeCount == 4 )
        {
        	return wings;
        }
        
        return null;
    }
    
    private ItemStack doDyeCheck( InventoryCrafting inv )
    {
        ItemStack wings = null;
        int dye = -1;

        for ( int i = 0; i < inv.getSizeInventory(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );
            if ( stack == null )
            {
            	continue;
            }
            
            if ( stack.getItem() == Wings.items.sturdyWings || stack.getItem() == Wings.items.fastWings )
            {
            	wings = stack;
            }
            else if ( stack.getItem() == Items.dye && dye == -1 )
            {
            	dye = stack.getItemDamage();
            }
            else
            {
            	return null;
            }
        }

        if ( dye >= 0 && dye < 16 )
        {
        	return wings;
        }
        
        return null;
    }
    
	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting craftInv)
	{
        ItemStack[] stacks = new ItemStack[craftInv.getSizeInventory()];

        for (int i = 0; i < stacks.length; ++i)
        {
            ItemStack itemstack = craftInv.getStackInSlot(i);
            stacks[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return stacks;
	}
}

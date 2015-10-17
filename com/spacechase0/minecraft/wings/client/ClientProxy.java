package com.spacechase0.minecraft.wings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;

import com.spacechase0.minecraft.wings.CommonProxy;
import com.spacechase0.minecraft.wings.Wings;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();
		ItemModelMesher imm = ri.getItemModelMesher();

		imm.register( Wings.items.featherWings,  0, new ModelResourceLocation( "sc0_wings:featherWings",  "inventory" ) );
		imm.register( Wings.items.obsidianWings, 0, new ModelResourceLocation( "sc0_wings:obsidianWings", "inventory" ) );
		imm.register( Wings.items.sturdyWings,   0, new ModelResourceLocation( "sc0_wings:sturdyWings",   "inventory" ) );
		imm.register( Wings.items.fastWings,     0, new ModelResourceLocation( "sc0_wings:fastWings",     "inventory" ) );
	}
}

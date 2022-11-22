package com.uberhelixx.flatlights.item;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ArmorModelImplementation extends ArmorBaseModel {

    public ArmorModelImplementation() {
        super(64, 64, new ResourceLocation("![](../../../../../resources/assets/flatlights/textures/models/armor/PrismaHelm.png)"));
    }

    @Override
    public void setupArmorParts() {
        //You need to cut out some parts from Blockbenchs exported model, and paste them here.
        //You can see 2 examples of how this may look, adding a cube to the head
        //and a rotated cube on the chest
        //Export the unedited armor template to see which parts you should **not** copy over
        armorHead.cubeList.add(new ModelRenderer.ModelBox(armorHead, 0, 18, -0.5F, -22.0F, -5.0F, 1, 22, 10, 0.0F, false));
    }
}
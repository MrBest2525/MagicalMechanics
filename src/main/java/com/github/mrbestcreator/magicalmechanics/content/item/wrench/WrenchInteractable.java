package com.github.mrbestcreator.magicalmechanics.content.item.wrench;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public interface WrenchInteractable {
    InteractionResult onWrenchUse(UseOnContext context);
}

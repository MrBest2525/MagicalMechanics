package com.github.mrbest2525.magicalmechanics.content.item.wrench;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public interface WrenchInteractable {
    InteractionResult onWrenchUse(UseOnContext context);
}

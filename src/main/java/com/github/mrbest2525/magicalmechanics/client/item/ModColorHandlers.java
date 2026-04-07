package com.github.mrbest2525.magicalmechanics.client.item;

import com.github.mrbest2525.magicalmechanics.content.item.ModItems;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items.EnergyCoreItem;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

//@EventBusSubscriber(modid = MagicalMechanics.MODID, value = Dist.CLIENT)
public class ModColorHandlers {

//    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // 対象のアイテムすべてに対して登録
        event.register((stack, tintIndex) -> {
                    // JSONで書いた "tintindex": 0 のエレメントだけ色を変える
                    if (tintIndex == 0 && stack.getItem() instanceof EnergyCoreItem coreItem) {
                        // Tier Enum から色を直接取得
                        int color = coreItem.getTier().getTextureColor();
                        return color;
                    }
                    // tintIndex が 0 以外（枠など）は元のテクスチャの色（白）のまま
                    return 0xFFFFFFFF;
                },
                ModItems.NORMAL_ENERGY_CORE.get(),
                ModItems.ADVANCED_ENERGY_CORE.get(),
                ModItems.UNLIMITED_ENERGY_CORE.get());
    }
}

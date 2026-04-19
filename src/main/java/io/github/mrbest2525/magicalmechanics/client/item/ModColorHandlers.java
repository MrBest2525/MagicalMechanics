package io.github.mrbest2525.magicalmechanics.client.item;

import io.github.mrbest2525.magicalmechanics.content.item.ModItems;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items.EnergyCoreItem;
import io.github.mrbest2525.magicalmechanics.content.item.wirelesslinker.mfwirelesslinker.MFLinkedStaffItem;
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
                        return coreItem.getTier().getTextureColor();
                    }
                    // tintIndex が 0 以外（枠など）は元のテクスチャの色（白）のまま
                    return 0xFFFFFFFF;
                },
                ModItems.NORMAL_ENERGY_CORE.get(),
                ModItems.ADVANCED_ENERGY_CORE.get(),
                ModItems.UNLIMITED_ENERGY_CORE.get());
        
        event.register((stack, tintIndex) -> {
                    // JSONで書いた "tintindex": 0 のエレメントだけ色を変える
                    if (tintIndex == 0 && stack.getItem() instanceof MFLinkedStaffItem mfLinkedStaffItem) {
                        // ItemStackから色を直接取得
                        return mfLinkedStaffItem.getColor(stack);
                    }
                    // tintIndex が 0 以外（枠など）は元のテクスチャの色（白）のまま
                    return 0xFFFFFFFF;
                },
                ModItems.MF_LINK_STAFF.get());
    }
}

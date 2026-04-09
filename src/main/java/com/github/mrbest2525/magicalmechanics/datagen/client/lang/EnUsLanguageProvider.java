package com.github.mrbest2525.magicalmechanics.datagen.client.lang;

import com.github.mrbest2525.magicalmechanics.content.item.ModItems;
import com.github.mrbest2525.magicalmechanics.util.MMLang;
import net.minecraft.data.PackOutput;

import java.util.Arrays;

public class EnUsLanguageProvider extends AbstractModLanguageProvider {
    public EnUsLanguageProvider(PackOutput output) {
        super(output, "en_us");
    }
    
    @Override
    protected void setupMaterialDictionary() {
    }
    
    @Override
    protected String joinNames(String... names) {
        return String.join(" ", Arrays.stream(names).map(EnUsLanguageProvider::capitalize).toArray(String[]::new)); // 英語はスペース結合
    }
    
    // 最初の文字を大文字にする
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    @Override
    protected void buildTranslations() {
        // MMLangの定数を使った登録
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_EMPTY, "The core is cold and silent.");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_FAINT, "A faint magical light flickers within.");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_STABLE, "The magic flows with a steady rhythm.");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_VIBRANT, "Powerful energy surges through the handle.");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_FULL, "The core is overflowing with magic!");
        
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.SHIFT_FOR_INFO, "Press SHIFT to view details");
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.CONTENTS, "Incorporated Parts");
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.CORE_SLOT, "Core: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.SIDE_SLOT, "Side: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.Wrench.MODE, "Mode: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.Wrench.CORE, "CORE");
        add(MMLang.Tooltip.Item.MagicalMechanics.Wrench.SIDE, "SIDE");
        add(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.LINKED, "Target block pos: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.BLOCK_POS, "[X=%s Y=%s Z=%s]");
        
        add(MMLang.Msg.Actionbar.Wrench.MODE_CHANGE, "Wrench Mode: %s");
        add(MMLang.Msg.Actionbar.Linker.BLOCK_POS, "[X=%s Y=%s Z=%s]");
        add(MMLang.Msg.Actionbar.Linker.LINK_SUCCESSFUL, "Linked to: %s");
        add(MMLang.Msg.Actionbar.Linker.SET_BLOCK_POS, "Target set: %s");
        add(MMLang.Msg.Actionbar.Linker.ACCESS_FAILED, "Connection Failed!");
        
        add("tag.item.magicalmechanics.frame_core_parts", "Core Parts");
        add("tag.item.magicalmechanics.frame_side_parts", "Side Parts");
        
        
        add(ModItems.MF_LINK_STAFF.get(), "MF Linked Staff");
        
        
        // Item, Blockを使った登録（もし ModItems に登録済みなら）
        // addMaterialItem(ModItems.IRON_MAYURANT, "Iron Mayurant");
        
        // クリエイティブタブなど
    }
}

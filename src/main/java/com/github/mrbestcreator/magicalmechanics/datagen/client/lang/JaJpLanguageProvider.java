package com.github.mrbestcreator.magicalmechanics.datagen.client.lang;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import com.github.mrbestcreator.magicalmechanics.util.MMLang;
import net.minecraft.data.PackOutput;

import java.util.Arrays;

public class JaJpLanguageProvider extends AbstractModLanguageProvider{
    public JaJpLanguageProvider(PackOutput output) {
        super(output, "ja_jp");
    }
    
    @Override
    protected void setupMaterialDictionary() {
        addMaterial("wooden", "木");
        addMaterial("stone", "石");
        addMaterial("iron", "鉄");
        addMaterial("golden", "金");
        addMaterial("diamond", "ダイヤモンド");
        addMaterial("netherite", "ネザライト");
        addMaterial("wrench", "レンチ");
        addMaterial("tool", "ツール");
        addMaterial("tools", "ツール");
        addMaterial("furnace", "かまど");
        addMaterial("core", "コア");
        addMaterial("side", "サイド");
        addMaterial("machine", "マシン");
        addMaterial("machines", "マシン");
        addMaterial("frame", "フレーム");
        addMaterial("frames", "フレーム");
        addMaterial("part", "パーツ");
        addMaterial("parts", "パーツ");
        addMaterial("mayurant", "マユラント");
    }
    
    @Override
    protected String joinNames(String... names) {
        return String.join("の", Arrays.stream(names).map(JaJpLanguageProvider::capitalize).toArray(String[]::new));
    }
    
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    @Override
    protected void buildTranslations() {
        add(MMLang.Tooltip.Item.Mayurant.MAGIC_POWER_EMPTY, "核は冷たく、静まり返っている。");
        add(MMLang.Tooltip.Item.Mayurant.MAGIC_POWER_FAINT, "かすかな魔力の光が、内部でゆらめいている。");
        add(MMLang.Tooltip.Item.Mayurant.MAGIC_POWER_STABLE, "魔力は一定のリズムで穏やかに流れている。");
        add(MMLang.Tooltip.Item.Mayurant.MAGIC_POWER_VIBRANT, "力強いエネルギーが、柄を通じて脈動している。");
        add(MMLang.Tooltip.Item.Mayurant.MAGIC_POWER_FULL, "核から、制御しきれぬ魔力が溢れ出している！");
        
        add(MMLang.Msg.Actionbar.Wrench.MODE_CHANGE, "レンチモード: %s");
        
        add(ModItems.FURNACE_CORE.get(), "ファーネスコア");
        add(ModBlocks.MACHINE_FRAME.get(), "マシーンフレーム");
        
        add(MMLang.ItemGroup.MACHINE_PARTS, "マシンパーツ");
        add(MMLang.ItemGroup.MACHINE_FRAMES, "マシンフレーム");
    }
}

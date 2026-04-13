package com.github.mrbest2525.magicalmechanics.datagen.client.lang;

import com.github.mrbest2525.magicalmechanics.content.block.ModBlockItems;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import com.github.mrbest2525.magicalmechanics.content.item.ModItems;
import com.github.mrbest2525.magicalmechanics.util.MMLang;
import net.minecraft.data.PackOutput;

import java.util.Arrays;

public class JaJpLanguageProvider extends AbstractModLanguageProvider{
    public JaJpLanguageProvider(PackOutput output) {
        super(output, "ja_jp");
    }
    
    @Override
    protected void setupMaterialDictionary() {
        addMaterial("wooden", "木");
        addMaterial("wood", "木");
        addMaterial("stone", "石");
        addMaterial("iron", "鉄");
        addMaterial("golden", "金");
        addMaterial("gold", "金");
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
        addMaterial("base", "基礎");
        addMaterial("machine_frame", "マシンフレーム");
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
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_EMPTY, "核は冷たく、静まり返っている。");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_FAINT, "かすかな魔力の光が、内部でゆらめいている。");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_STABLE, "魔力は一定のリズムで穏やかに流れている。");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_VIBRANT, "力強いエネルギーが、柄を通じて脈動している。");
        add(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_FULL, "核から、制御しきれぬ魔力が溢れ出している！");
        
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.SHIFT_FOR_INFO, "Shiftで詳細を表示");
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.CONTENTS, "組み込まれたパーツ");
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.CORE_SLOT, "コア: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.SIDE_SLOT, "サイド: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.Wrench.MODE, "Mode: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.Wrench.CORE, "CORE");
        add(MMLang.Tooltip.Item.MagicalMechanics.Wrench.SIDE, "SIDE");
        add(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.LINK, "リンクするブロック座標: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.BLOCK_POS, "[X=%s Y=%s Z=%s]");
        add(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.MODE, "モード: %s");
        add(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.SOURCE_MODE, "リンク元（Source）取得");
        add(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.TARGET_MODE, "リンク先（Target）設定");
        
        add(MMLang.Msg.Actionbar.Wrench.MODE_CHANGE, "レンチモード: %s");
        add(MMLang.Msg.Actionbar.Linker.BLOCK_POS, "[X=%s Y=%s Z=%s]");
        add(MMLang.Msg.Actionbar.Linker.LINK_SUCCESSFUL, "リンク完了: %s");
        add(MMLang.Msg.Actionbar.Linker.SET_BLOCK_POS, "座標を記録: %s");
        add(MMLang.Msg.Actionbar.Linker.ACCESS_FAILED, "接続に失敗しました！");
        add(MMLang.Msg.Actionbar.Linker.SET_SOURCE_MODE, "リンク元（Source）取得モードに変更");
        add(MMLang.Msg.Actionbar.Linker.SET_TARGET_MODE, "リンク先（Target）設定モードに変更");
        
        add(ModBlocks.MACHINE_FRAME.get(), "マシンフレーム");
        add(ModItems.FURNACE_CORE.get(), "ファーネスコア");
        add(ModItems.FURNACE_SIDE.get(), "ファーネスサイド");
        
        add(MMLang.ItemGroup.MagicalMechanics.MACHINE_PARTS, "マシンパーツ");
        add(MMLang.ItemGroup.MagicalMechanics.MACHINE_FRAMES, "マシンフレーム");
        add(MMLang.ItemGroup.MagicalMechanics.MACHINE_BLOCKS, "マシンブロック");
        
        add(MMLang.Tag.Item.MagicalMechanics.FRAME_CORE_PARTS, "コアパーツ");
        add(MMLang.Tag.Item.MagicalMechanics.FRAME_SIDE_PARTS, "サイドパーツ");
        
        add(ModBlockItems.BASE_FRAME_ITEM.get(), "基礎フレーム");
        add(ModBlockItems.FE_INPUT_ADAPTER_BLOCK_ITEM.get(), "エネルギー インプット アダプター");
        add(ModBlockItems.FE_OUTPUT_ADAPTER_BLOCK_ITEM.get(), "エネルギー アウトプット アダプター");
        
        add(ModItems.NORMAL_ENERGY_CORE.get(), "ノーマル エネルギー コア");
        add(ModItems.ADVANCED_ENERGY_CORE.get(), "アドバンスド エネルギー コア");
        add(ModItems.UNLIMITED_ENERGY_CORE.get(), "アンリミテッド エネルギー コア");
        add(ModItems.UNLIMITED_WIRELESS_ENERGY_INTERFACE.get(), "アンリミテッド ワイアレス エネルギー インターフェース");
        
        add(ModItems.MF_LINK_STAFF.get(), "MFリンクスタッフ");
    }
}

package com.github.mrbestcreator.magicalmechanics.datagen.client.lang;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import com.github.mrbestcreator.magicalmechanics.util.MMLang;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractModLanguageProvider extends LanguageProvider {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    // 登録されたキーを記録する（後で司令部が検証に使う）
    protected final Set<String> trackedKeys = new HashSet<>();
    // 素材と翻訳名の辞書を作成する
    protected final Map<String, String> materialDict = new HashMap<>();
    
    protected final Set<String> requiredKeys = new HashSet<>();
    
    public AbstractModLanguageProvider(PackOutput output, String locale) {
        super(output, MagicalMechanics.MODID, locale);
    }
    
    /**
     * DataGenの実行フローをオーバーライドして、生成完了後に検証を差し込む
     */
    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        // super.run() の中で registerTranslations() が呼ばれる
        return super.run(cache).thenAccept(ignored -> runValidation());
    }
    
    /**
     * LanguageProviderの抽象メソッド。
     * ここですべての準備、スキャン、追加を実行する。
     */
    @Override
    protected void addTranslations() {
        // 1. 下準備
        setupMaterialDictionary();
        scanForRequiredKeys();
        
        // 2. 実装クラス（子クラス）での具体的な翻訳追加
        buildTranslations();
        
        requiredKeys.removeAll(trackedKeys);
        for (String key :requiredKeys) {
            String[] oldNames = key.split("\\.");
            String oldName = oldNames[oldNames.length - 1];
            String[] names = oldName.split("_");
            for (int i = 0; i < names.length; i++) {
                // Mapにキーが存在すれば値を取得、なければ元の要素を残す
                names[i] = materialDict.getOrDefault(names[i].toLowerCase(), names[i]);
            }
            String name = joinNames(names);
            add(key, name);
        }
    }
    
    // 標準の add をオーバーライドして履歴を取る
    @Override
    public void add(@NotNull String key, @NotNull String value) {
        super.add(key, value);
        trackedKeys.add(key);
    }
    
    
    private void runValidation() {
        List<String> missing = new ArrayList<>();
        for (String req : requiredKeys) {
            if (!trackedKeys.contains(req)) {
                missing.add(req);
            }
        }
        
        if (!missing.isEmpty()) {
            LOGGER.error("===== [MISSING TRANSLATIONS: {}] =====", getName());
            missing.forEach(key -> LOGGER.error(" -> {}", key));
            LOGGER.error("Total missing: {} keys", missing.size());
            // 必要に応じてここにファイル出力ロジックを追加
        } else {
            LOGGER.info("Validation Passed for [{}]: No missing keys found.", getName());
        }
    }
    
    
    /**
     * 辞書をセットアップするメソッド（各言語クラスで実装）
     */
    protected abstract void setupMaterialDictionary();
    
    protected void addMaterial(@NotNull String key, @NotNull String name) {
        if (materialDict.containsKey(key.toLowerCase())) {
            LOGGER.warn("Key:\"{}\"を\"{}\"で置き換えました。\n{}", key, name, new Throwable());
        }
        materialDict.put(key.toLowerCase(), name);
    }
    
    /**
     * 結合ルール（言語クラスで実装）
     * 英語なら "Iron" + " " + "Mayurant"
     * 日本語なら "鉄" + "の" + "マユラント"
     */
    protected abstract String joinNames(String... names);
    
    /**
     * 各言語クラスで具体的な翻訳内容を書くメソッド
     */
    protected abstract void buildTranslations();
    
    /**
     * 指定されたクラスとその内部クラスから、すべての翻訳キーを再帰的に取得する
     */
    private void scanConstantsRecursively(Class<?> clazz) {
        // 1. そのクラス自身の public static final String フィールドを収集
        for (Field field : clazz.getFields()) {
            try {
                if (field.getType() == String.class) {
                    requiredKeys.add((String) field.get(null));
                }
            } catch (IllegalAccessException ignored) {}
        }
        
        // 2. 内部クラス（static class）があれば、それらも再帰的にスキャン
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            scanConstantsRecursively(innerClass);
        }
    }
    
    private void scanForRequiredKeys() {
        // A. レジストリから自動取得（アイテム・ブロック）
        ModItems.ITEMS.getEntries().forEach(entry -> requiredKeys.add(entry.get().getDescriptionId()));
        ModBlocks.BLOCKS.getEntries().forEach(entry -> requiredKeys.add(entry.get().getDescriptionId()));
        
        // B. MMLang クラスから手動定義分を全取得（再帰スキャン）
        scanConstantsRecursively(MMLang.class);
        
        // C. コメントによる明示的な指定（必要であれば）
        // scanSourceFiles(); // ソーススキャンも残しておけば「ついうっかり」の検知に役立ちます
    }
}

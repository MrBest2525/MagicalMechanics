package io.github.mrbest2525.magicalmechanics.core.gui;

import com.mojang.logging.LogUtils;
import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.Optional;
import java.util.stream.Collectors;

public class SoraUICoreLoader {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    
    /**
     * ResourceLocationを受け取り、そのパスを SoraUI/ フォルダ配下として解釈して読み込む
     * 入力: new ResourceLocation("magicalmechanics", "main")
     * 実際に見に行くパス: assets/magicalmechanics/SoraUI/main.yml
     */
    public static String loadSoraUI(ResourceLocation location) {
        // 渡された ResourceLocation のパスを "SoraUI/ <元のパス> .yml" に変換
        ResourceLocation fullPath = ResourceLocation.fromNamespaceAndPath(
                location.getNamespace(),
                String.format("%s/soraui/%s", MagicalMechanics.MODID, location.getPath())
        );
        
        return loadYamlToString(fullPath);
    }
    
    /**
     * ResourceLocationからYAMLの内容を文字列として取得する
     */
    public static String loadYamlToString(ResourceLocation location) {
        try {
            var resourceManager = Minecraft.getInstance().getResourceManager();
            Optional<Resource> resource = resourceManager.getResource(location);
            
            if (resource.isEmpty()) {
                // ファイルが見つからない場合のログ出しなど
                LOGGER.error("File not found!: \"{}\"", location);
                return "";
            }
            
            try (BufferedReader reader = resource.get().openAsReader()) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load GUI YAML from: \"{}\"", location, e);
            return "";
        }
    }
}

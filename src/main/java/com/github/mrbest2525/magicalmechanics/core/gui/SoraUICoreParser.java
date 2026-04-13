package com.github.mrbest2525.magicalmechanics.core.gui;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;

public class SoraUICoreParser {
    private final Yaml yaml;
    
    public SoraUICoreParser() {
        LoaderOptions loaderOptions = new LoaderOptions();
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        
        Constructor constructor = new Constructor(SoraUICoreYamlItems.UIRoot.class, loaderOptions);
        constructor.setPropertyUtils(propertyUtils);
        
        constructor.addTypeDescription(new TypeDescription(SoraUICoreYamlItems.UISetting.class, "setting"));
        constructor.addTypeDescription(new TypeDescription(SoraUICoreYamlItems.UIRender.class, "!RENDER"));
        constructor.addTypeDescription(new TypeDescription(SoraUICoreYamlItems.UIBox.class, "!BOX"));
        constructor.addTypeDescription(new TypeDescription(SoraUICoreYamlItems.UIView.class, "!VIEW"));
//        constructor.addTypeDescription(new TypeDescription(SoraUICoreYamlItems.UILive.class, "!LIVE"));
        // TODO 埒が明かないため一旦無効化
        
        this.yaml = new Yaml(constructor);
    }
    
    public SoraUICoreYamlItems.UIRoot parse(String yamlContent) {
        if (yamlContent == null || yamlContent.isEmpty()) return null;
        try {
            return yaml.load(yamlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String prepareYamlTags(String content) {
        // キー名をタグに変換。uiParts内の要素（- BOX:）と、直下の項目をカバー
//
        return content;
    }
}

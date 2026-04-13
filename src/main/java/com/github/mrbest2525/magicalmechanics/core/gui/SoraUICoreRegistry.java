package com.github.mrbest2525.magicalmechanics.core.gui;

import java.util.HashMap;
import java.util.Map;

public class SoraUICoreRegistry {
    // ID（名前）とレンダラー（魂）の保管庫
    private final Map<String, SoraUICoreRenderer> RENDERERS = new HashMap<>();
    private final Map<String, SoraUICoreLiveViewer> LIVE_VIEWERS = new HashMap<>();
    
    // プログラムの初期化時に「この名前の時はこう描く」と登録しておく
    public <T extends ISoraUICoreDrawingElement<T>> void registerRenderer(String name, SoraUICoreRenderer renderer) {
        RENDERERS.put(name, renderer);
    }
    
    public <T extends ISoraUICoreDrawingElement<T>> void registerLiveViewer(String name, SoraUICoreLiveViewer liveViewer) {
        LIVE_VIEWERS.put(name, liveViewer);
    }
    
    public SoraUICoreRenderer getRenderer(String name) {
        return RENDERERS.get(name);
    }
    
    public SoraUICoreLiveViewer getLiveViewer(String name) {
        return LIVE_VIEWERS.get(name);
    }
}

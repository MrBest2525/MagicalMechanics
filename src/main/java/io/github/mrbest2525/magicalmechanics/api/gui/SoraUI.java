package io.github.mrbest2525.magicalmechanics.api.gui;

import com.mojang.logging.LogUtils;
import io.github.mrbest2525.magicalmechanics.core.gui.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.slf4j.Logger;

import java.util.*;

public class SoraUI {
    private final Logger LOGGER = LogUtils.getLogger();
    
    private final List<ISoraUICoreDrawingElement<?>> renderCode = new ArrayList<>();
    private final SoraUICoreRegistry renderRegistry = new SoraUICoreRegistry();
    
    private final Map<String, SoraUICoreDrawingElements.MOVE> moveMap = new HashMap<>();
    private final Map<String, SoraUICoreDrawingElements.SCALE> scaleMap = new HashMap<>();
    
    private final Set<ResourceLocation> viewStack;
    private int windowX = 0, windowY = 0;
    
    public SoraUI() {
        viewStack = new HashSet<>();
    }
    
    private SoraUI(Set<ResourceLocation> viewStack) {
        this.viewStack = viewStack;
    }
    
    public SoraUI load(ResourceLocation resourceLocation) {
        viewStack.add(resourceLocation);
        Deque<SoraUICoreYamlItems.DefaultItem> queue = new ArrayDeque<>();
        renderCode.addAll(new SoraUICoreAssembler(new SoraUICoreParser().parse(SoraUICoreLoader.loadSoraUI(resourceLocation))).build(queue, viewStack));
        return this;
    }
    
    public <T extends ISoraUICoreDrawingElement<T>> SoraUI bind(String name, SoraUICoreRenderer renderer) {
        renderRegistry.registerRenderer(name, renderer);
        return this;
    }
    
    public <T extends ISoraUICoreDrawingElement<T>> SoraUI bind(String name, SoraUICoreLiveViewer liveViewer) {
        renderRegistry.registerLiveViewer(name, liveViewer);
        return this;
    }
    
    public <T extends ISoraUICoreDrawingElement<T>> SoraUI bindRenderers(Map<String, SoraUICoreRenderer> rendererMap) {
        rendererMap.forEach(renderRegistry::registerRenderer);
        return this;
    }
    
    public <T extends ISoraUICoreDrawingElement<T>> SoraUI bindLiveViewers(Map<String, SoraUICoreLiveViewer> rendererMap) {
        rendererMap.forEach(renderRegistry::registerLiveViewer);
        return this;
    }
    
    public SoraUI bindComplete() {
        renderCode.forEach(code -> {
            if (code instanceof SoraUICoreDrawingElements.MOVE move && move.getId() != null) {
                moveMap.put(move.getId(), move);
            }
            if (code instanceof SoraUICoreDrawingElements.SCALE scale && scale.getId() != null) {
                scaleMap.put(scale.getId(), scale);
            }
            if (code instanceof SoraUICoreDrawingElements.RENDER render) {
                render.setRenderer(renderRegistry.getRenderer(render.getId()));
            }
            if (code instanceof SoraUICoreDrawingElements.LIVE_VIEWER liveViewer) {
                liveViewer.setLiveViewer(renderRegistry.getLiveViewer(liveViewer.getId()));
            }
        });
        return this;
    }
    
    
    public SoraUI init(int windowX, int windowY) {
        this.windowX = windowX;
        this.windowY = windowY;
        renderCode.forEach(code -> {
            if (code instanceof SoraUICoreDrawingElements.MOVE move) {
                move.setWindowSize(windowX, windowY);
            } else if (code instanceof SoraUICoreDrawingElements.SCALE scale) {
                scale.setWindowSize(windowX, windowY);
            }
        });
        return this;
    }
    
    public SoraUI moveOffset(String id, double x, double y, double z) {
        SoraUICoreDrawingElements.MOVE move = moveMap.get(id);
        if (move != null) {
            move.offset(x, y, z);
        }
        return this;
    }
    
    public SoraUI scaleOffset(String id, float x, float y, float z) {
        SoraUICoreDrawingElements.SCALE scale = scaleMap.get(id);
        if (scale != null) {
            scale.offset(x, y, z);
        }
        return this;
    }
    
    /**
     * 描画時等に呼び出し処理を行う
     * 通常描画、マウス判定、クリック判定等を行う
     *
     * @param guiGraphics GuiGraphics
     * @param partialTick partialTick
     * @param mouseX      MouseのX座標
     * @param mouseY      MouseのY座標
     * @param window      Minecraft.getInstance().getWindow().getWindow()
     * @param isPressed   mouseClickedイベントが呼ばれたときTrue
     * @return SoraUIを返す
     */
    public SoraUI draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed) {
        renderCode.forEach(code -> {
            switch (code) {
                case ISoraUICoreDrawingElement.ISoraUIDrawingMouseEventElement<?> mouseEventCode:
                    if (guiGraphics != null) {
                        // 1. 現在の変換行列を取得
                        Matrix4f matrix = guiGraphics.pose().last().pose();
                        // 2. 座標 (Translation) の抽出
                        // (0,0,0)地点が現在の行列によってスクリーン上のどこに投影されるかを計算
                        Vector4f pos = new Vector4f(0, 0, 0, 1).mul(matrix);
                        float screenX = pos.x();
                        float screenY = pos.y();
                        // 3. スケール (Scale) の抽出
                        // 各軸のベクトル（列ベクトル）の長さを計算することで、現在の拡大率がわかる
                        // m00, m10, m20 は X軸の変形
                        float scaleX = new Vector3f(matrix.m00(), matrix.m10(), matrix.m20()).length();
                        // m01, m11, m21 は Y軸の変形
                        float scaleY = new Vector3f(matrix.m01(), matrix.m11(), matrix.m21()).length();
                        mouseEventCode.updateSnapShot(screenX, screenY, scaleX, scaleY);
                    }
                    mouseEventCode.draw(guiGraphics, partialTick, mouseX, mouseY, window, isPressed);
                    break;
                
                case ISoraUICoreDrawingElement.ISoraUIDrawingLiveViewerElement<?> liveViewerCode:
                    if (guiGraphics != null) {
                        Matrix4f matrix = guiGraphics.pose().last().pose();
                        // 2. 座標 (Translation) の抽出
                        // (0,0,0)地点が現在の行列によってスクリーン上のどこに投影されるかを計算
                        Vector4f pos = new Vector4f(0, 0, 0, 1).mul(matrix);
                        float screenX = pos.x();
                        float screenY = pos.y();
                        // 3. スケール (Scale) の抽出
                        // 各軸のベクトル（列ベクトル）の長さを計算することで、現在の拡大率がわかる
                        // m00, m10, m20 は X軸の変形
                        float scaleX = new Vector3f(matrix.m00(), matrix.m10(), matrix.m20()).length();
                        // m01, m11, m21 は Y軸の変形
                        float scaleY = new Vector3f(matrix.m01(), matrix.m11(), matrix.m21()).length();
                        liveViewerCode.updateSnapShot(screenX, screenY, scaleX, scaleY);
                    }
                    ResourceLocation srcRL = liveViewerCode.getSrcRL(guiGraphics);
                    if (srcRL == null) {
                        LOGGER.error("Live src not fund!; id:{}", liveViewerCode.getId());
                        break;
                    }
                    new SoraUI(viewStack).load(srcRL).bindRenderers(liveViewerCode.getRendererMap()).bindLiveViewers(liveViewerCode.getLiveViewerMap()).bindComplete().init(windowX, windowY).draw(guiGraphics, partialTick, mouseX, mouseY, window, isPressed);
                    break;
                
                default:
                    code.draw(guiGraphics, partialTick, mouseX, mouseY, window, isPressed);
                    break;
            }
        });
        return this;
    }
    
}

package io.github.mrbest2525.magicalmechanics.core.gui;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class ISoraUICoreDrawingElement<T extends ISoraUICoreDrawingElement<T>> {
    protected String id;
    
    public String getId() {
        return id;
    }
    
    @SuppressWarnings("unchecked")
    public T setId(String id) {
        this.id = id;
        return (T) this;
    }
    
    public abstract String getInstructionName();
    
    public abstract void draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed);
    
    public String toString() {
        return getInstructionName() + " id: " + id;
    }
    
    public static abstract class ISoraUIDrawingMouseEventElement<T extends ISoraUIDrawingMouseEventElement<T>> extends ISoraUICoreDrawingElement<T> {
        protected final SoraUICoreSnapShot snapShot = new SoraUICoreSnapShot();
        private final Logger LOGGER = LogUtils.getLogger();
        protected boolean mouse_event;
        protected boolean hide;
        protected SoraUICoreRenderer renderer;
        
        @SuppressWarnings("unchecked")
        public T setMouseEvent(boolean mouse_event) {
            this.mouse_event = mouse_event;
            return (T) this;
        }
        
        @SuppressWarnings("unchecked")
        public T setHide(boolean hide) {
            this.hide = hide;
            return (T) this;
        }
        
        @SuppressWarnings("unchecked")
        public T setRenderer(SoraUICoreRenderer renderer) {
            this.renderer = renderer;
            return (T) this;
        }
        
        @SuppressWarnings("unchecked")
        public T updateSnapShot(float x, float y, float scaleX, float scaleY) {
            snapShot.update(x, y, scaleX, scaleY);
            return (T) this;
        }
        
        /**
         * ホバー判定
         *
         * @param mouseX マウスのX座標
         * @param mouseY マウスのY座標
         * @param x1     X座標の左側
         * @param y1     Y座標の左側
         * @param x2     X座標の右側
         * @param y2     Y座標の右側
         * @return ホバー常態か
         */
        public boolean isHovered(double mouseX, double mouseY, float x1, float y1, float x2, float y2) {
            return snapShot.checkHover(mouseX, mouseY, x1, y1, x2, y2);
        }
        
        @Override
        public void draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed) {
            if (this.hide) return;
            if (this.renderer == null) {
                LOGGER.error("Renderer not found!; id:{}", this.id);
                return;
            }
            this.renderer.render(this, guiGraphics, partialTick, mouseX, mouseY, window, isPressed);
        }
        
        @Override
        public String toString() {
            return String.format("%s mouse_event:%s hide:%s", super.toString(), this.mouse_event, this.hide);
        }
    }
    
    public static abstract class ISoraUIDrawingLiveViewerElement<T extends ISoraUIDrawingLiveViewerElement<T>> extends ISoraUICoreDrawingElement<T> {
        
        private final Logger LOGGER = LogUtils.getLogger();
        protected final Map<String, SoraUICoreRenderer> rendererMap = new HashMap<>();
        final Map<String, SoraUICoreLiveViewer> liveViewerMap = new HashMap<>();
        private final SoraUICoreSnapShot snapShot = new SoraUICoreSnapShot();
        protected SoraUICoreLiveViewer liveViewer;
        
        
        @SuppressWarnings("unchecked")
        public T setLiveViewer(SoraUICoreLiveViewer renderer) {
            this.liveViewer = renderer;
            return (T) this;
        }
        
        @SuppressWarnings("unchecked")
        public T updateSnapShot(float x, float y, float scaleX, float scaleY) {
            snapShot.update(x, y, scaleX, scaleY);
            return (T) this;
        }
        
        @Override
        public void draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed) {
            if (this.liveViewer == null) {
                LOGGER.error("Renderer not found!; id:{}", this.id);
                return;
            }
            ResourceLocation src = this.liveViewer.liveViewer(this);
        }
        
        @SuppressWarnings("unchecked")
        public <U extends ISoraUICoreDrawingElement<U>> T bind(String name, SoraUICoreRenderer renderer) {
            rendererMap.put(name, renderer);
            return (T) this;
        }
        
        @SuppressWarnings("unchecked")
        public <U extends ISoraUICoreDrawingElement<U>> T bind(String name, SoraUICoreLiveViewer liveViewer) {
            liveViewerMap.put(name, liveViewer);
            return (T) this;
        }
        
        public ResourceLocation getSrcRL(GuiGraphics guiGraphics) {
            if (this.liveViewer == null) {
                LOGGER.error("Renderer not found!; id:{}", this.id);
                return null;
            }
            return this.liveViewer.liveViewer(this);
        }
        
        public Map<String, SoraUICoreRenderer> getRendererMap() {
            return rendererMap;
        }
        
        public Map<String, SoraUICoreLiveViewer> getLiveViewerMap() {
            return liveViewerMap;
        }
    }
}

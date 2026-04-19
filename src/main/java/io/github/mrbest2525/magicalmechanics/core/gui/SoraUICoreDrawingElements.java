package io.github.mrbest2525.magicalmechanics.core.gui;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class SoraUICoreDrawingElements {
    /**
     * @param x 0.0-1.0(座標)
     */
    private static int getPointX(double x, int screenX) {
        return (int) (screenX * x);
    }
    
    /**
     * @param y 0.0-1.0(座標)
     */
    private static int getPointY(double y, int screenY) {
        return (int) (screenY * y);
    }
    
    private static double resolve(String raw, double windowSize) {
        if (raw.endsWith("$x") || raw.endsWith("$y")) {
            return (Double.parseDouble(raw.replaceAll("[^0-9.-]", "")) / 100.0) * windowSize;
        } else {
            return Double.parseDouble(raw.replaceAll("[^0-9.-]", ""));
        }
    }
    
    /**
     * 短編基準で統一Scale取得
     *
     * @param raw 計算元Scale
     * @return 計算後Scale
     */
    public static float getScale(String raw, int windowX, int windowY) {
        float value = raw.isEmpty() ? 1.0f : Float.parseFloat(raw.replaceAll("[^0-9.-]", ""));
        
        if (raw.endsWith("$")) {
            
            if (value == 0.0) return 0.0f;
            
            final double BASE_WIDTH = 1920;
            final double BASE_HEIGHT = 1080;
            
            double scaleW = windowX / BASE_WIDTH;
            double scaleH = windowY / BASE_HEIGHT;
            
            // はみ出さないよう小さい方を採用
            double factor = Math.min(scaleW, scaleH);
            
            return (float) (value * factor);
        } else {
            return value;
        }
    }
    
    public static class PUSH extends ISoraUICoreDrawingElement<PUSH> {
        
        @Override
        public String getInstructionName() {
            return "PUSH";
        }
        
        @Override
        public void draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed) {
            if (guiGraphics == null) {
                return;
            }
            guiGraphics.pose().pushPose();
        }
    }
    
    public static class POP extends ISoraUICoreDrawingElement<POP> {
        
        @Override
        public String getInstructionName() {
            return "POP";
        }
        
        @Override
        public void draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed) {
            if (guiGraphics == null) {
                return;
            }
            guiGraphics.pose().popPose();
        }
    }
    
    public static class MOVE extends ISoraUICoreDrawingElement<MOVE> {
        
        public final String x;
        public final String y;
        public final String z;
        
        private double drawingX = 0, drawingY = 0, drawingZ = 0;
        private double offsetX = 0, offsetY = 0, offsetZ = 0;
        
        public MOVE(String x, String y, String z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        @Override
        public String getInstructionName() {
            return "MOVE";
        }
        
        public MOVE setWindowSize(int x, int y) {
            
            drawingX = resolve(this.x, x);
            drawingY = resolve(this.y, y);
            drawingZ = resolve(this.z, 1);
            
            return this;
        }
        
        public MOVE offset(double x, double y, double z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
            return this;
        }
        
        @Override
        public void draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed) {
            if (guiGraphics == null) {
                return;
            }
            double moveX = drawingX + offsetX, moveY = drawingY + offsetY, moveZ = drawingZ + offsetZ;
            offsetY = 0;
            offsetZ = 0;
            offsetX = 0;
            if (moveX != 0 || moveY != 0 || moveZ != 0) {
                guiGraphics.pose().translate(moveX, moveY, moveZ);
            }
        }
        
        @Override
        public String toString() {
            return String.format("%s x:%s y:%s z:%s", super.toString(), x, y, z);
        }
    }
    
    public static class SCALE extends ISoraUICoreDrawingElement<SCALE> {
        public final String scaleX;
        public final String scaleY;
        public final String scaleZ;
        
        private float drawingX = 0, drawingY = 0, drawingZ = 0;
        private float offsetX = 0, offsetY = 0, offsetZ = 0;
        
        public SCALE(String scaleX, String scaleY, String scaleZ) {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
            
        }
        
        @Override
        public String getInstructionName() {
            return "SCALE";
        }
        
        public SCALE setWindowSize(int x, int y) {
            drawingX = getScale(scaleX, x, y);
            drawingY = getScale(scaleY, x, y);
            drawingZ = getScale(scaleZ, x, y);
            
            return this;
        }
        
        public SCALE offset(float x, float y, float z) {
            offsetX = x;
            offsetY = y;
            offsetZ = z;
            
            return this;
        }
        
        @Override
        public void draw(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed) {
            if (guiGraphics == null) {
                return;
            }
            float moveX = drawingX + offsetX, moveY = drawingY + offsetY, moveZ = drawingZ + offsetZ;
            offsetX = 0;
            offsetY = 0;
            offsetZ = 0;
            if (moveX != 1 || moveY != 1 || moveZ != 1) {
                guiGraphics.pose().scale(drawingX + offsetX, drawingY + offsetY, drawingZ + offsetZ);
            }
        }
        
        @Override
        public String toString() {
            return String.format("%s x:%s y:%s z:%s", super.toString(), scaleX, scaleY, scaleZ);
        }
    }
    
    public static class RENDER extends ISoraUICoreDrawingElement.ISoraUIDrawingMouseEventElement<RENDER> {
        
        @Override
        public String getInstructionName() {
            return "RENDER";
        }
    }
    
    public static class LIVE_VIEWER extends ISoraUICoreDrawingElement.ISoraUIDrawingLiveViewerElement<LIVE_VIEWER> {
        
        private final List<ISoraUICoreDrawingElement<?>> path = new ArrayList<>();
        
        @Override
        public String getInstructionName() {
            return "LIVE_VIEWER";
        }
    }
}

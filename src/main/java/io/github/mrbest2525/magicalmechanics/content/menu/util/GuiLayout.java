package io.github.mrbest2525.magicalmechanics.content.menu.util;

public class GuiLayout {
    private int screenW, screenH;
    
    // Minecraftデフォルトコンテナサイズ
    private static final double BASE_WIDTH  = 1920;
    private static final double BASE_HEIGHT = 1080;
    
    public GuiLayout(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;
    }
    
    public void updateSize(int w, int h) {
        this.screenW = w;
        this.screenH = h;
    }
    
    /**
     * 短編基準で統一Scale取得
     * @param value 計算元Scale
     * @return 計算後Scale
     */
    public float getScale(double value) {
        if (value == 0.0) return 0.0f;
        
        double scaleW = screenW / BASE_WIDTH;
        double scaleH = screenH / BASE_HEIGHT;
        
        // はみ出さないよう小さい方を採用
        double factor = Math.min(scaleW, scaleH);
        
        return (float) (value * factor);
    }
    
    /**
     * @param x 0.0-1.0(座標)
     */
    public int getPointX(double x) {
        return (int) (screenW * x);
    }
    
    /**
     * @param y 0.0-1.0(座標)
     */
    public int getPointY(double y) {
        return (int) (screenH * y);
    }
    
    /**
     * @param anchorX 0.0-1.0 (中心位置)
     * @param anchorY 0.0-1.0 (中心位置)
     * @param offset  中心からの広がり（短い辺に対する比率）
     */
    public Box getBox(double anchorX, double anchorY, double offset) {
        int centerX = getPointX(anchorX);
        int centerY = getPointY(anchorY);
        
        // 短い方の辺を基準にしたサイズ（正方形を維持）
        int size = (int) (Math.min(screenW, screenH) * offset);
        
        return new Box(
                centerX - size, // Left
                centerY - size, // Top
                centerX + size, // Right
                centerY + size  // Bottom
        );
    }
    
    public record Point(int x, int y) {}
    
    public record Box(int left, int top, int right, int bottom) {
        public int width() { return right - left; }
        public int height() { return bottom - top; }
        
        // 当たり判定もこれで一発
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
        }
    }
}


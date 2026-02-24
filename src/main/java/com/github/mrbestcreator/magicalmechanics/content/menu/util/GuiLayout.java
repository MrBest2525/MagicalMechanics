package com.github.mrbestcreator.magicalmechanics.content.menu.util;

public class GuiLayout {
    private int screenW, screenH;
    
    public GuiLayout(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;
    }
    
    public void updateSize(int w, int h) {
        this.screenW = w;
        this.screenH = h;
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


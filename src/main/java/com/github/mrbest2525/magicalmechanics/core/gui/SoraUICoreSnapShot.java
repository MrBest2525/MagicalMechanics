package com.github.mrbest2525.magicalmechanics.core.gui;

public class SoraUICoreSnapShot {
    private float x;
    private float y;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    
    /**
     * 描画時に呼び出して現在の状態をスナップショットとして保存する
     */
    public void update(float x, float y, float scaleX, float scaleY) {
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    /**
     * マウス判定ヘルパー
     *
     * @param mouseX マウスX
     * @param mouseY マウスY
     * @param x1     左オフセット (未スケール時)
     * @param y1     上オフセット (未スケール時)
     * @param x2     右オフセット (未スケール時)
     * @param y2     下オフセット (未スケール時)
     */
    public boolean checkHover(double mouseX, double mouseY, float x1, float y1, float x2, float y2) {
        // スケールを適用して判定範囲を広げる/狭める
        float left = x - (x1 * scaleX);
        float right = x + (x2 * scaleX);
        float top = y - (y1 * scaleY);
        float bottom = y + (y2 * scaleY);
        
        return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
    }
    
    // Getters
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getScaleX() {
        return scaleX;
    }
    
    public float getScaleY() {
        return scaleY;
    }
}

package io.github.mrbest2525.magicalmechanics.util;


import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class MMTextures {
    private static final String MODID = MagicalMechanics.MODID;
    
    // テクスチャの定義とメタデータを保持するレコード
    public record TextureAsset(ResourceLocation location, int u, int v, int width, int height, int texWidth, int texHeight) {
        // ResourceLocation だけ指定する場合の簡易版
        public TextureAsset(ResourceLocation location, int width, int height) {
            this(location, 0, 0, width, height, width, height);
        }
        
        /**
         * 指定された中心座標 (centerX, centerY) に配置するための、左上の X 座標を計算する
         */
        public int getXForCenter(int centerX) {
            // 高解像度（10倍）を考慮する場合は、表示サイズで計算
            // ここでは 1:1 の場合で例示
            return centerX - (this.width / 2);
        }
        
        /**
         * 指定された中心座標 (centerX, centerY) に配置するための、左上の Y 座標を計算する
         */
        public int getYForCenter(int centerY) {
            return centerY - (this.height / 2);
        }
    }
    
    /**
     * 描画位置を渡せばテクスチャサイズに合わせて描画します
     */
    public static void blitAsset(GuiGraphics gui, TextureAsset asset, int x, int y) {
        gui.blit(asset.location(), x, y, asset.u(), asset.v(), asset.width(), asset.height(), asset.texWidth(), asset.texHeight());
    }
    
    /**
     * 渡された描画位置をテクスチャの中心とし描画します
     * @param gui GuiGraphics
     * @param asset 描画したいテクスチャ
     * @param centerX 中心のX座標
     * @param centerY 中心のY座標
     */
    public static void blitAssetCentered(GuiGraphics gui, TextureAsset asset, int centerX, int centerY) {
        int x = asset.getXForCenter(centerX);
        int y = asset.getYForCenter(centerY);
        
        gui.blit(asset.location(), x, y, asset.u(), asset.v(), asset.width(), asset.height(), asset.texWidth(), asset.texHeight());
    }
    
    private static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
    
    public static class GUI {
        private static final String BASE = "textures/gui/";
        
        public static final TextureAsset NORMAL_INVENTORY = new TextureAsset(loc(BASE + "inventory_slot/normal_inventory_slot.png"), 0, 0, 220, 166, 220, 166);
        
        public static final TextureAsset NORMAL_SLOT = new TextureAsset(loc(BASE + "inventory_slot/normal_slot1.png"), 0, 0, 20, 20, 20, 20);
        
        public static final TextureAsset UTIL_BAR_MEMORY = new TextureAsset(loc(BASE + "util/bar/bar_memory.png"), 0, 0, 128, 16, 128, 16);
        
        public static final TextureAsset UTIL_BAR_FRAME = new TextureAsset(loc(BASE + "util/bar/bar_frame.png"), 0, 0, 128, 16, 128, 16);
    }
}

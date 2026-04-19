package io.github.mrbest2525.magicalmechanics.core.gui;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public class SoraUICoreAssembler {
    
    private final Logger LOGGER = LogUtils.getLogger();
    
    private final SoraUICoreYamlItems.UIRoot uiRoot;
    
    public SoraUICoreAssembler(SoraUICoreYamlItems.UIRoot uiRoot) {
        this.uiRoot = uiRoot;
    }
    
    public List<ISoraUICoreDrawingElement<?>> build(Deque<SoraUICoreYamlItems.DefaultItem> queue, Set<ResourceLocation> viewStack) {
        List<ISoraUICoreDrawingElement<?>> drawingList = new ArrayList<>();
        
        if (uiRoot == null || uiRoot.ui_parts == null) {
            return drawingList; // 空のリストを返して平和に終わる
        }
        
        queue.addAll(uiRoot.ui_parts);
        
        while (!queue.isEmpty()) {
            // 先頭から一つ取り出す
            SoraUICoreYamlItems.DefaultItem item = queue.pollFirst();
            
            // 解析を実行
            // ここで SET が見つかったら、queue の「最後尾」に回すようにすればOK
            buildItem(drawingList, queue, item, viewStack);
        }
        StringBuilder sb = new StringBuilder().append("drawingList debug print;\n");
        drawingList.forEach(code -> sb.append(code.toString()).append("\n"));
        LOGGER.debug(sb.toString());
        return drawingList;
    }
    
    private void buildItem(List<ISoraUICoreDrawingElement<?>> drawingList, Deque<SoraUICoreYamlItems.DefaultItem> queue, SoraUICoreYamlItems.DefaultItem defaultItem, Set<ResourceLocation> viewStack) {
        switch (defaultItem) {
            case SoraUICoreYamlItems.UIBox box:
                if (!box.set_x.isEmpty() || !box.set_y.isEmpty() || !box.set_z.isEmpty()) {
                    // SETのパターン
                    if (box.firstFlag) {
                        box.firstFlag = false;
                        queue.add(box);
                    } else {
                        buildBoxItem(drawingList, queue, box, viewStack);
                    }
                } else {
                    // SETじゃないパターン
                    buildBoxItem(drawingList, queue, box, viewStack);
                }
                break;
            
            case SoraUICoreYamlItems.UIRender render:
                drawingList.add(new SoraUICoreDrawingElements.RENDER().setId(render.id).setMouseEvent(render.mouse_event).setHide(render.hide));
                break;
            
            case SoraUICoreYamlItems.UIView view:
                ResourceLocation viewLocation = ResourceLocation.tryParse(view.src);
                if (viewLocation == null) {
                    LOGGER.error("ResourceLocation cannot be recognized: {}", view.src);
                    break;
                }
                if (viewStack.contains(viewLocation)) {
                    LOGGER.error("You are stuck in an infinite recursive call loop: {}", viewLocation);
                    break;
                }
                viewStack.add(viewLocation);
                try {
                    drawingList.addAll(new SoraUICoreAssembler(new SoraUICoreParser().parse(SoraUICoreLoader.loadSoraUI(viewLocation))).build(queue, viewStack));
                } catch (Exception e) {
                    LOGGER.error(e.toString());
                } finally {
                    viewStack.remove(viewLocation);
                }
                break;
            
            case SoraUICoreYamlItems.UILive live:
                drawingList.add(new SoraUICoreDrawingElements.LIVE_VIEWER().setId(live.id));
                break;
            
            default:
                break;
        }
    }
    
    private void buildBoxItem(List<ISoraUICoreDrawingElement<?>> drawingList, Deque<SoraUICoreYamlItems.DefaultItem> queue, SoraUICoreYamlItems.UIBox box, Set<ResourceLocation> viewStack) {
        drawingList.add(new SoraUICoreDrawingElements.PUSH().setId(box.id));
        
        if (!box.firstFlag) {
            drawingList.add(new SoraUICoreDrawingElements.MOVE(box.set_x.isEmpty() ? "0px" : box.set_x, box.set_y.isEmpty() ? "0px" : box.set_y, box.set_z.isEmpty() ? "0px" : box.set_z).setId(box.id));
        }
        drawingList.add(new SoraUICoreDrawingElements.MOVE(box.x, box.y, box.z).setId(box.id));
        drawingList.add(new SoraUICoreDrawingElements.SCALE(box.scale_3d, box.scale_3d, box.scale_3d).setId(box.id));
        drawingList.add(new SoraUICoreDrawingElements.SCALE(box.scale_2d, box.scale_2d, "1.0").setId(box.id));
        drawingList.add(new SoraUICoreDrawingElements.SCALE(box.scale_x, box.scale_y, box.scale_z).setId(box.id));
        for (SoraUICoreYamlItems.DefaultItem item : box.children) {
            buildItem(drawingList, queue, item, viewStack);
        }
        
        drawingList.add(new SoraUICoreDrawingElements.POP().setId(box.id));
    }
}

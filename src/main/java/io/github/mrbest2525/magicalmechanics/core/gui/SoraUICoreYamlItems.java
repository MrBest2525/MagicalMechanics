package io.github.mrbest2525.magicalmechanics.core.gui;

import java.util.List;

public class SoraUICoreYamlItems {
    public static class DefaultItem {
        public String id;
    }
    
    public static class DefaultDrawingPartItem extends DefaultItem {
        public boolean mouse_event = false;
        public boolean hide = false;
    }
    
    public static class UIRoot {
        public UISetting setting;
        public List<DefaultItem> ui_parts;
    }
    
    public static class UISetting {
        public String version = "";
        public String extendsPath = "";
    }
    
    public static class UIBox extends DefaultItem {
        public String x = "0px";
        public String y = "0px";
        public String z = "0px";
        public String set_x = "";
        public String set_y = "";
        public String set_z = "";
        public String scale_x = "1.0";
        public String scale_y = "1.0";
        public String scale_z = "1.0";
        public String scale_2d = "1.0";
        public String scale_3d = "1.0";
        public List<DefaultItem> children;
        
        public boolean firstFlag = true;
    }
    
    public static class UIRender extends DefaultDrawingPartItem {
        public String type = "";
    }
    
    public static class UIView extends DefaultItem {
        public String src = "";
    }
    
    public static class UILive extends DefaultItem {
    
    }
    
}

package com.github.mrbestcreator.magicalmechanics.util;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;

public class MMLang {
    private static final String MODID = MagicalMechanics.MODID;
    
    /**
     * 文字列結合ヘルパーメゾッド("."結合)
     * @param strings 結合したい文字列 (A, B, C)
     * @return 結合後 "A.B.C"
     */
    private static String join(String... strings) {
        return String.join(".", strings);
    }
    
    public static class ItemGroup {
        private static final String BASE = join("itemGroup", MODID);
        
        public static final String MACHINE_PARTS = join(BASE, "machine_parts");
        public static final String MACHINE_FRAMES = join(BASE, "machine_frames");
        public static final String TOOLS = join(BASE, "tools");
    }
    
    public static class Msg {
        private static final String BASE = join("msg", MODID);
        
        public static class Actionbar {
            private static final String BASE = join(Msg.BASE, "actionbar");
            
            public static class Wrench {
                private static final String BASE = join(Actionbar.BASE, "wrench");
                
                public static final String MODE_CHANGE = join(BASE, "modeChange");
            }
        }
    }
    
    public static class Tooltip {
        private static final String BASE = "tooltip";
        
        public static class Item {
            private static final String BASE = join(Tooltip.BASE, "item", MODID);
            
            public static class Mayurant {
                private static final String BASE = join(Item.BASE, "mayurant");
                
                public static final String MAGIC_POWER_EMPTY = join(BASE, "magic_power_empty");
                public static final String MAGIC_POWER_FAINT = join(BASE, "magic_power_faint");
                public static final String MAGIC_POWER_STABLE = join(BASE, "magic_power_stable");
                public static final String MAGIC_POWER_VIBRANT = join(BASE, "magic_power_vibrant");
                public static final String MAGIC_POWER_FULL = join(BASE, "magic_power_full");
            }
            
            public static class MachineFrame {
                private static final String BASE = join(Item.BASE, "machine_frame");
                
                public static final String SHIFT_FOR_INFO = join(BASE, "shift_for_info");
                public static final String CONTENTS = join(BASE, "contents");
                
                public static final String CORE_SLOT = join(BASE, "core_slot_name");
                public static final String SIDE_SLOT = join(BASE, "side_slot_name");
                
            }
        }
    }
}

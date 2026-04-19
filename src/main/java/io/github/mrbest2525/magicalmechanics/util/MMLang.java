package io.github.mrbest2525.magicalmechanics.util;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;

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
        private static final String BASE = join("itemGroup");
        
        public static class MagicalMechanics {
            private static final String BASE = join(ItemGroup.BASE, MODID);
            
            public static final String MACHINE_PARTS = join(BASE, "machine_parts");
            public static final String MACHINE_FRAMES = join(BASE, "machine_frames");
            public static final String MACHINE_BLOCKS = join(BASE, "machine_blocks");
            public static final String TOOLS = join(BASE, "tools");
        }
    }
    
    public static class Msg {
        private static final String BASE = join("msg", MODID);
        
        public static class Actionbar {
            private static final String BASE = join(Msg.BASE, "actionbar");
            
            public static class Wrench {
                private static final String BASE = join(Actionbar.BASE, "wrench");
                
                public static final String MODE_CHANGE = join(BASE, "modeChange");
            }
            
            public static class Linker {
                private static final String BASE = join(Actionbar.BASE, "linker");
                
                public static final String SET_BLOCK_POS = join(BASE, "set_block_pos");
                public static final String LINK_SUCCESSFUL = join(BASE, "link_successful");
                public static final String BLOCK_POS = join(BASE, "block_pos");
                public static final String ACCESS_FAILED = join(BASE, "access_failed");
                
                public static final String SET_SOURCE_MODE = join(BASE, "set_source_mode");
                public static final String SET_TARGET_MODE = join(BASE, "set_target_mode");
            }
        }
    }
    
    public static class Tag {
        private static final String BASE = "tag";
        
        public static class Item {
            private static final String BASE = join(Tag.BASE, "item");
            
            public static class MagicalMechanics {
                private static final String BASE = join(Item.BASE, MODID);
                
                public static String FRAME_CORE_PARTS = join(BASE, "frame_core_parts");
                public static String FRAME_SIDE_PARTS = join(BASE, "frame_side_parts");
                public static String MAYURANT = join(BASE, "mayurant");
                public static String WRENCH = join(BASE, "wrench");
            }
        }
    }
    
    public static class Tooltip {
        private static final String BASE = "tooltip";
        
        public static class Item {
            private static final String BASE = join(Tooltip.BASE, "item");
            
            public static class MagicalMechanics {
                private static final String BASE = join(Item.BASE, MODID);
                
                public static class Mayurant {
                    private static final String BASE = join(MagicalMechanics.BASE, "mayurant");
                    
                    public static final String MAGIC_POWER_EMPTY = join(BASE, "magic_power_empty");
                    public static final String MAGIC_POWER_FAINT = join(BASE, "magic_power_faint");
                    public static final String MAGIC_POWER_STABLE = join(BASE, "magic_power_stable");
                    public static final String MAGIC_POWER_VIBRANT = join(BASE, "magic_power_vibrant");
                    public static final String MAGIC_POWER_FULL = join(BASE, "magic_power_full");
                }
                
                public static class Wrench {
                    private static final String BASE = join(MagicalMechanics.BASE, "wrench");
                    
                    public static final String MODE = join(BASE, "mode");
                    public static final String CORE = join(BASE, "core");
                    public static final String SIDE = join(BASE, "side");
                }
                
                public static class MFLinkStaff {
                    private static final String BASE = join(MagicalMechanics.BASE, "mf_link_staff");
                    
                    public static final String LINK = join(BASE, "link");
                    public static final String BLOCK_POS = join(BASE, "block_pos");
                    
                    public static final String MODE = join(BASE, "mode");
                    public static final String SOURCE_MODE = join(BASE, "source_mode");
                    public static final String TARGET_MODE = join(BASE, "target_mode");
                }
                
                public static class MachineFrame {
                    private static final String BASE = join(MagicalMechanics.BASE, "machine_frame");
                    
                    public static final String SHIFT_FOR_INFO = join(BASE, "shift_for_info");
                    public static final String CONTENTS = join(BASE, "contents");
                    
                    public static final String CORE_SLOT = join(BASE, "core_slot_name");
                    public static final String SIDE_SLOT = join(BASE, "side_slot_name");
                    
                }
            }
        }
    }
}

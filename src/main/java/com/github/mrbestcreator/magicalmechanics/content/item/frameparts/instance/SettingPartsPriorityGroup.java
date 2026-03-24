package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance;

public enum SettingPartsPriorityGroup {
    EARLIEST(0), // 最速
    BEFORE(1),   // 前
    STANDARD(2), // 標準
    AFTER(3),    // 後
    LATEST(4);   // 最遅
    
    private final int order;
    
    SettingPartsPriorityGroup(int order) {
        this.order = order;
    }
    
    public int getOrder() {
        return order;
    }
}

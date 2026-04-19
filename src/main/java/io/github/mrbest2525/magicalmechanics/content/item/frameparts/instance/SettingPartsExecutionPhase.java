package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance;

public enum SettingPartsExecutionPhase {
    /**
     * Core, Sideの処理より前に実行（エネルギー搬入、材料セットなど）
     */
    PRE,
    /**
     * Core, Sideの処理より後に実行（アイテム搬出、冷却、副産物処理など）
     */
    POST
}

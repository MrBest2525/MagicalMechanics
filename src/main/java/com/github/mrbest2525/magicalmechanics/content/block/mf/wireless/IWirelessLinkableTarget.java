package com.github.mrbest2525.magicalmechanics.content.block.mf.wireless;

import com.github.mrbest2525.magicalmechanics.api.SourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface IWirelessLinkableTarget {
    
    /**
     * 指定された座標からのリンクを受け入れ可能か判定する
     * @param sourcePos 接続元の座標
     * @param player 実行しているプレイヤー（権限チェック用などに）
     * @return 接続可能な場合は true
     */
    boolean canAcceptLinkTarget(SourceType type, BlockPos sourcePos, Player player);
    
    void setTargetLink(SourceType type, BlockPos pos);
}

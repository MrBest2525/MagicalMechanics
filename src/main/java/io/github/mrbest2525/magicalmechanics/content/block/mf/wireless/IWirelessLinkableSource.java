package io.github.mrbest2525.magicalmechanics.content.block.mf.wireless;

import io.github.mrbest2525.magicalmechanics.api.SourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface IWirelessLinkableSource {
    
    /**
     * リンク用の座標共有が可能かを管理する
     * @param player 実行しているプレイヤー（権限チェック用などに）
     * @return 接続可能な場合は true
     */
    boolean canAcceptLinkSource(SourceType type, Player player);
    
    BlockPos getSourcePos(SourceType type);
}

package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy;

import io.github.mrbest2525.magicalmechanics.util.math.MMLong;
import org.jetbrains.annotations.NotNull;

public interface IWirelessMFProvider {
    /**
     * 無線経由でエネルギーを引き出す
     * @param resultBuffer 実際に引き出せた量を書き込むためのバッファ
     * @param maxExtract 引き出したい最大量(MMLong)
     * @param simulate シミュレートかどうか
     */
    void extractWirelessEnergy(@NotNull MMLong resultBuffer, @NotNull MMLong maxExtract, boolean simulate);
    
    /**
     * 無線経由でエネルギーを受け入れる
     * @param resultBuffer 実際に受け入れた量を書き込むためのバッファ
     * @param maxInsert 受け入れたい最大量(MMLong)
     * @param simulate シミュレートかどうか
     */
    void insertWirelessEnergy(@NotNull MMLong resultBuffer, @NotNull MMLong maxInsert, boolean simulate);
    
    /**
     * 無線経由で得られるエネルギーの最大量
     * @param resultBuffer 値を取得するためのバッファー
     */
    void getAvailableWirelessEnergy(@NotNull MMLong resultBuffer);
    
    /**
     * 無線経由で得られる
     * @param resultBuffer 値を取得するためのバッファー
     */
    void getWirelessMaxEnergyCapacity(@NotNull MMLong resultBuffer);
}

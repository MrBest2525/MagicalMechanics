package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy;

import com.github.mrbest2525.magicalmechanics.util.math.MMLong;

public interface IWirelessMFProvider {
    /**
     * 無線経由でエネルギーを引き出す
     * @param resultBuffer 実際に引き出せた量を書き込むためのバッファ
     * @param maxExtract 引き出したい最大量(MMLong)
     * @param simulate シミュレートかどうか
     */
    void extractWirelessEnergy(MMLong resultBuffer, MMLong maxExtract, boolean simulate);
    
    /**
     * 無線経由でエネルギーを受け入れる
     * @param resultBuffer 実際に受け入れた量を書き込むためのバッファ
     * @param maxInsert 受け入れたい最大量(MMLong)
     * @param simulate シミュレートかどうか
     */
    void insertWirelessEnergy(MMLong resultBuffer, MMLong maxInsert, boolean simulate);
}

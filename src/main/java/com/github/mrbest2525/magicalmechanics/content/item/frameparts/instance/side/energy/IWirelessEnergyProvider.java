package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy;

import com.github.mrbest2525.magicalmechanics.util.math.MMLong;

public interface IWirelessEnergyProvider {
    /**
     * 無線経由でエネルギーを引き出す
     * @param maxExtract 引き出したい最大量(MMLong)
     * @param simulate シミュレートかどうか
     * @return 実際に引き出せた量
     */
    MMLong extractWirelessEnergy(MMLong maxExtract, boolean simulate);
    
    /**
     * 無線経由でエネルギーを受け入れる
     * @param maxInsert 受け入れたい最大量(MMLong)
     * @param simulate シミュレートかどうか
     * @return 実際に受け入れた量
     */
    MMLong insertWirelessEnergy(MMLong maxInsert, boolean simulate);
}

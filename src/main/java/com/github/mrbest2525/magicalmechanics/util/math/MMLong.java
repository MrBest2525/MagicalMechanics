package com.github.mrbest2525.magicalmechanics.util.math;

import net.minecraft.nbt.CompoundTag;

import java.math.BigInteger;

public class MMLong {
    private long smallValue = 0L;
    private BigInteger largeValue = null;
    private boolean isPromoted = false;
    
    // --- コンストラクタ ---
    public MMLong() {
    }
    
    public MMLong(long initial) {
        this.smallValue = initial;
    }
    
    public MMLong(BigInteger initial) {
        promote(initial);
    }
    
    public MMLong(MMLong initial) {
        this.smallValue = initial.smallValue;
        this.largeValue = initial.largeValue;
        this.isPromoted = initial.isPromoted;
    }
    
    // --- getter ---
    
    
    // --- 内部昇格用 ---
    private void promote(BigInteger value) {
        this.largeValue = value;
        this.isPromoted = true;
    }
    
    /**
     * 明示的に複製を作成するメソッド。
     * 使用例: MMLong snapshot = energy.copy();
     */
    public MMLong copy() {
        return new MMLong(this);
    }
    
    // ==========================================
    // ⚙️ 設定 (Setter)
    // ==========================================
    
    /**
     * long値で上書きする。
     * 昇格状態を解除してスリム化できるので、基本はこちらを使うのが効率的。
     */
    public MMLong set(long value) {
        this.smallValue = value;
        this.largeValue = null;
        this.isPromoted = false;
        return this;
    }
    
    /**
     * BigIntegerで上書きする。
     * もし long で収まる範囲なら、自動的に long モードへ降格させる。
     */
    public MMLong set(BigInteger value) {
        if (value == null) {
            return set(0L);
        }
        
        // longの範囲内かチェック (bitLength が 63 未満なら確実に long に収まる)
        if (value.bitLength() <= 63) {
            return set(value.longValue());
        } else {
            promote(value);
        }
        return this;
    }
    
    /**
     * 他の MMLong から値をコピーする。
     */
    public MMLong set(MMLong other) {
        if (!other.isPromoted) {
            return set(other.smallValue);
        } else {
            return set(other.largeValue);
        }
    }
    
    
    // ==========================================
    // ➕ 加算 (Addition)
    // ==========================================
    public MMLong add(long amount) {
        if (!isPromoted) {
            if (Long.MAX_VALUE - smallValue >= amount) {
                smallValue += amount;
            } else {
                promote(BigInteger.valueOf(smallValue).add(BigInteger.valueOf(amount)));
            }
        } else {
            largeValue = largeValue.add(BigInteger.valueOf(amount));
        }
        return this;
    }
    
    public MMLong add(BigInteger amount) {
        if (!isPromoted) promote(BigInteger.valueOf(smallValue));
        largeValue = largeValue.add(amount);
        return this;
    }
    
    public MMLong add(MMLong other) {
        return other.isPromoted ? this.add(other.largeValue) : this.add(other.smallValue);
    }
    
    // ==========================================
    // ➖ 減算 (Subtraction)
    // ==========================================
    public MMLong sub(long amount) {
        if (!isPromoted) {
            smallValue -= amount;
        } else {
            largeValue = largeValue.subtract(BigInteger.valueOf(amount));
        }
        return this;
    }
    
    public MMLong sub(BigInteger amount) {
        if (!isPromoted) promote(BigInteger.valueOf(smallValue));
        largeValue = largeValue.subtract(amount);
        return this;
    }
    
    public MMLong sub(MMLong other) {
        return other.isPromoted ? this.sub(other.largeValue) : this.sub(other.smallValue);
    }
    
    // ==========================================
    // ✖️ 乗算 (Multiplication)
    // ==========================================
    public MMLong mul(long factor) {
        if (!isPromoted) {
            try {
                smallValue = Math.multiplyExact(smallValue, factor);
            } catch (ArithmeticException e) {
                promote(BigInteger.valueOf(smallValue).multiply(BigInteger.valueOf(factor)));
            }
        } else {
            largeValue = largeValue.multiply(BigInteger.valueOf(factor));
        }
        return this;
    }
    
    public MMLong mul(BigInteger factor) {
        if (!isPromoted) promote(BigInteger.valueOf(smallValue));
        largeValue = largeValue.multiply(factor);
        return this;
    }
    
    public MMLong mul(MMLong other) {
        return other.isPromoted ? this.mul(other.largeValue) : this.mul(other.smallValue);
    }
    
    // ==========================================
    // ➗ 除算 (Division)
    // ==========================================
    public MMLong div(long divisor) {
        if (divisor == 0) throw new ArithmeticException("Division by zero");
        if (!isPromoted) {
            smallValue /= divisor;
        } else {
            largeValue = largeValue.divide(BigInteger.valueOf(divisor));
        }
        return this;
    }
    
    public MMLong div(BigInteger divisor) {
        if (divisor.equals(BigInteger.ZERO)) throw new ArithmeticException("Division by zero");
        if (!isPromoted) promote(BigInteger.valueOf(smallValue));
        largeValue = largeValue.divide(divisor);
        return this;
    }
    
    public MMLong div(MMLong other) {
        return other.isPromoted ? this.div(other.largeValue) : this.div(other.smallValue);
    }
    
    // ==========================================
    // 🔍 比較 (Comparison)
    // ==========================================
    public boolean atLeast(long amount) {
        if (!isPromoted) return smallValue >= amount;
        return largeValue.compareTo(BigInteger.valueOf(amount)) >= 0;
    }
    
    public boolean atLeast(BigInteger amount) {
        BigInteger thisVal = isPromoted ? largeValue : BigInteger.valueOf(smallValue);
        return thisVal.compareTo(amount) >= 0;
    }
    
    public boolean atLeast(MMLong other) {
        return other.isPromoted ? this.atLeast(other.largeValue) : this.atLeast(other.smallValue);
    }
    
    // ==========================================
    // ⚖️ 最小値 (Minimum)
    // ==========================================
    
    /**
     * staticメソッド: 2つの MMLong のうち小さい方を返す
     */
    public static MMLong min(MMLong a, MMLong b) {
        return a.atLeast(b) ? b : a;
    }
    
    /**
     * staticメソッド: 2つの MMLong のうち小さい方の「複製」を返す
     */
    public static MMLong minCopy(MMLong a, MMLong b) {
        return a.atLeast(b) ? b.copy() : a.copy();
    }
    
    // ==========================================
    // ⚖️ 最大値 (Maximum)
    // ==========================================
    
    /**
     * staticメソッド: 2つの MMLong のうち大きい方を返す
     */
    public static MMLong max(MMLong a, MMLong b) {
        return a.atLeast(b) ? a : b;
    }
    
    /**
     * staticメソッド: 2つの MMLong のうち大きい方の「複製」を返す
     */
    public static MMLong maxCopy(MMLong a, MMLong b) {
        return a.atLeast(b) ? a.copy() : b.copy();
    }
    
    // ==========================================
    // 💾 保存 (NBT Serialization)
    // ==========================================
    public CompoundTag save() {
        CompoundTag mmlongTag = new CompoundTag();
        mmlongTag.putBoolean("isPromoted", isPromoted);
        if (!isPromoted) {
            mmlongTag.putLong("value", smallValue);
        } else {
            // BigIntegerはbyte[]として保存するのが最も確実で効率的です
            mmlongTag.putByteArray("value", largeValue.toByteArray());
        }
        CompoundTag tag = new CompoundTag();
        tag.put("MMLong", mmlongTag);
        return tag;
    }
    
    public MMLong load(CompoundTag tag) {
        CompoundTag mmlongTag = tag.getCompound("MMLong");
        this.isPromoted = mmlongTag.getBoolean("isPromoted");
        if (!isPromoted) {
            this.smallValue = mmlongTag.getLong("value");
            this.largeValue = null;
        } else {
            byte[] bytes = mmlongTag.getByteArray("value");
            this.largeValue = new BigInteger(bytes);
            this.smallValue = 0L; // 昇格済みなので不要
        }
        
        return this;
    }
    
    // ==========================================
    // その他
    // ==========================================
    
    @Override
    public String toString() {
        return isPromoted ? largeValue.toString() : String.valueOf(smallValue);
    }
}

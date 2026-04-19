package io.github.mrbest2525.magicalmechanics.util.math;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

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
    public boolean isPromoted() {
        return isPromoted;
    }
    
    // --- 内部昇格用 ---
    private void promote(BigInteger value) {
        this.largeValue = value;
        this.isPromoted = true;
    }
    
    /**
     * 明示的に複製を作成するメソッド。
     * 使用例: MMLong snapshot = energy.copy();
     */
    @Deprecated
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
        if (this == other) return this; // 同一インスタンスなら何もしない
        
        this.isPromoted = other.isPromoted;
        if (!other.isPromoted) {
            this.smallValue = other.smallValue;
            this.largeValue = null;
        } else {
            // BigIntegerは不変なので参照コピーでZero-Allocationを実現
            this.largeValue = other.largeValue;
            this.smallValue = 0; // 念のためクリア
        }
        return this;
    }
    
    public MMLong setZero() {
        this.smallValue = 0;
        this.largeValue = null;
        this.isPromoted = false;
        return this;
    }
    
    
    // ==========================================
    // ➕ 加算 (Addition)
    // ==========================================
    public MMLong add(long amount) {
        if (amount == 0) return this;
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
        if (amount == 0) return this;
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
        if (factor == 1) return this;
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
        if (divisor == 1) return this;
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
        
        // 自分が昇格済み（巨大数）の場合のショートカット
        int sign = largeValue.signum();
        if (sign > 0 && amount <= 0) return true;  // 正の巨大数 >= 0以下のlong は確定
        if (sign < 0 && amount >= 0) return false; // 負の巨大数 >= 0以上のlong は確定
        
        return largeValue.compareTo(BigInteger.valueOf(amount)) >= 0;
    }
    
    public boolean atLeast(BigInteger amount) {
        BigInteger thisVal = isPromoted ? largeValue : BigInteger.valueOf(smallValue);
        return thisVal.compareTo(amount) >= 0;
    }
    
    public boolean atLeast(MMLong other) {
        // 1. 両方 long モードなら primitive 比較（爆速）
        if (!this.isPromoted && !other.isPromoted) {
            return this.smallValue >= other.smallValue;
        }
        // 2. どちらかが Promoted なら BigInteger で比較
        // ここで BigInteger.valueOf() を使うとキャッシュ外で new が走るため、
        // 内部的に「一時的な BigInteger」を作らない工夫が必要
        return this.toBigIntegerInternal().compareTo(other.toBigIntegerInternal()) >= 0;
    }
    
    // 内部用：new を最小限にする工夫
    private BigInteger toBigIntegerInternal() {
        return isPromoted ? largeValue : BigInteger.valueOf(smallValue);
    }
    
    public boolean isEqualTo(MMLong other) {
        if (other == null) return false; // null とは等しくない
        if (this.isPromoted != other.isPromoted) {
            // 片方だけ BigInteger なら、揃えて比較
            BigInteger t = this.isPromoted ? this.largeValue : BigInteger.valueOf(this.smallValue);
            BigInteger o = other.isPromoted ? other.largeValue : BigInteger.valueOf(other.smallValue);
            return t.equals(o);
        }
        return isPromoted ? largeValue.equals(other.largeValue) : smallValue == other.smallValue;
    }
    
    // ==========================================
    // 🔍 比較 (Comparison - Static Utilities)
    // ==========================================
    
    /** A > B (Greater Than) */
    public static boolean gt(MMLong a, MMLong b) {
        if (a == null || b == null) return false;
        if (a == b) return false; // 同じインスタンスなら「より大きい」は成立しない
        return a.atLeast(b) && !a.isEqualTo(b);
    }
    
    /** A < B (Less Than) */
    public static boolean lt(MMLong a, MMLong b) {
        if (a == null || b == null) return false;
        return !a.atLeast(b);
    }
    
    /** A >= B (Greater or Equal) */
    public static boolean ge(MMLong a, MMLong b) {
        if (a == null || b == null) return false;
        return a.atLeast(b);
    }
    
    /** A <= B (Less or Equal) */
    public static boolean le(MMLong a, MMLong b) {
        if (a == null || b == null) return false;
        // a <= b は、b >= a と同義
        return b.atLeast(a);
    }
    
    /** A == B (Equivalence) */
    public static boolean eq(MMLong a, MMLong b) {
        if (a == null || b == null) return false;
        return a.isEqualTo(b);
    }
    
    // ==========================================
    // ⚖️ 最小値 (Minimum)
    // ==========================================
    
    /**
     * staticメソッド: 2つの MMLong のうち小さい方を返す
     */
    @Deprecated
    public static MMLong min(@NotNull MMLong a, @NotNull MMLong b) {
        return a.atLeast(b) ? b : a;
    }
    
    /**
     * a と b を比較し、小さい方の「値」を dest に書き込む。
     * インスタンスの入れ替えが発生しないため、ポインタ汚染が起きない。
     */
    public static MMLong minTo(@NotNull MMLong a, @NotNull MMLong b, @NotNull MMLong dest) {
        if (a.atLeast(b)) {
            dest.set(b);
        } else {
            dest.set(a);
        }
        return dest;
    }
    
    // ==========================================
    // ⚖️ 最大値 (Maximum)
    // ==========================================
    
    /**
     * staticメソッド: 2つの MMLong のうち大きい方を返す
     */
    @Deprecated
    public static MMLong max(@NotNull MMLong a, @NotNull MMLong b) {
        return a.atLeast(b) ? a : b;
    }
    
    /**
     * a と b を比較し、大きい方の「値」を dest にコピーする。
     * インスタンスの入れ替えが発生しないため、定数を汚染するリスクがない。
     */
    public static MMLong maxTo(@NotNull MMLong a, @NotNull MMLong b, @NotNull MMLong dest) {
        if (a.atLeast(b)) {
            dest.set(a);
        } else {
            dest.set(b);
        }
        return dest;
    }
    
    // ==========================================
    // 🔍 状態判定 (Status Checks)
    // ==========================================
    
    /**
     * 値が 0 かどうかを判定する。
     */
    public boolean isZero() {
        if (!isPromoted) {
            return smallValue == 0L;
        }
        // signum() は 0 の時 0 を返す
        return largeValue.signum() == 0;
    }
    
    /**
     * 値が 0 より大きい（正の数）か判定する。
     */
    public boolean isPositive() {
        if (!isPromoted) {
            return smallValue > 0L;
        }
        return largeValue.signum() > 0;
    }
    
    /**
     * 値が 0 未満（負の数）か判定する。
     * 魔法エネルギーとして負の値を許可しない場合のガード句に。
     */
    public boolean isNegative() {
        if (!isPromoted) {
            return smallValue < 0L;
        }
        return largeValue.signum() < 0;
    }
    
    // ==========================================
    // 📥 外部連携用 (Extraction)
    // ==========================================
    
    /**
     * int型として値を取り出す。
     * もし int の最大値を超えていたら Integer.MAX_VALUE を返し、
     * 0以下なら 0 を返す。
     * 注意！Integer.MAX_VALUE以上では正確な値ではありません！
     */
    public int asInt() {
        if (!isPromoted) {
            if (smallValue > Integer.MAX_VALUE) return Integer.MAX_VALUE;
            if (smallValue < 0) return 0;
            return (int) smallValue;
        }
        // 昇格済み（BigInteger）なら、確実に int を超えている
        return largeValue.signum() <= 0 ? 0 : Integer.MAX_VALUE;
    }
    
    /**
     * long型として値を取り出す。
     * もし long の最大値を超えていたら Long.MAX_VALUE を返す。
     * 注意！Long.MAX_VALUE以上では正確な値ではありません！
     */
    public long asLong() {
        if (!isPromoted) {
            return Math.max(0, smallValue);
        }
        // 昇格済みなら、その値が long の範囲内か再確認して返す
        if (largeValue.bitLength() <= 63) {
            long val = largeValue.longValue();
            return Math.max(0, val);
        }
        return largeValue.signum() <= 0 ? 0 : Long.MAX_VALUE;
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

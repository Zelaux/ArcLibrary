package tests;

import arc.util.Log;

import java.util.Arrays;

public class BigNumber {
    public static final int SIZE = 32;

    public final int[] bits;

    BigNumber temp1, temp2, temp3, temp4;

    public BigNumber(int maxSize) {
        bits = new int[maxSize];
        temp1 = new BigNumber(maxSize);
        temp2 = new BigNumber(maxSize);
        temp3 = new BigNumber(maxSize);
        temp4 = new BigNumber(maxSize);
    }
    
    public BigNumber set(BigNumber other) {
        clear();
        System.arraycopy(other.bits, 0, bits, 0, other.bits.length);
        return this;
    }

    public BigNumber set(int val) {
        clear();
        for (int i = 0; i < SIZE; i++) {
            int b = (val >> i) & 0b1;
            at(i, b);
        }
        return this;
    }
    
    public BigNumber clear() {
        Arrays.fill(bits, 0b0);
        return this;
    }

    public BigNumber left() {
        for (int i = 0; i < bits.length * SIZE; i++) {
            if (i != 0) {
                at(i - 1, at(i));
            }
            at(i, 0b0);
        }

        return this;
    }

    public BigNumber right() {
        for (int i = bits.length * SIZE - 1; i >= 0; i--) {
            if (i != bits.length * SIZE - 1) {
                at(i + 1, at(i));
            }
            at(i, 0b0);
        }

        return this;
    }

    public BigNumber cpy() {
        BigNumber out = new BigNumber(bits.length);
        System.arraycopy(bits, 0, out.bits, 0, bits.length);
        return out;
    }

    public int compare(BigNumber other) {
        for (int i = 0; i < other.bits.length * SIZE; i++) {
            int a = at(i);
            int b = other.at(i);
            if (a > b) {
                return 1;
            } else if (b > a) {
                return -1;
            }
        }
        return 0;
    }

    int c;
    public BigNumber add(BigNumber other) {
        for (int i = 0; i < other.bits.length * SIZE - 1; i++) {
            int a = at(i);
            int b = other.at(i);
            c += a + b;
            at(i, c & 0b1);
            c >>= 1;
        }
        return this;
    }

    public BigNumber sub(BigNumber other) {
        for (int i = 0; i < other.bits.length * SIZE - 1; i++) {
            int a = at(i);
            int b = other.at(i);
            if (a < b) {
                per(i);
                a = 0b10;
            }
            int c = a - b;
            at(i, c);
        }
        return this;
    }

    void per(int from) {
        if (from == bits.length * SIZE) {
            Log.warn("Subtraction overflow!");
            return;
        }
        int b = at(from);
        if (b == 0b0) {
            per(from+1);
            at(from, 0b1);
        } else {
            at(from, 0b0);
        }
    }

    public BigNumber mul(BigNumber other) {
        temp1.set(this);
        temp2.clear();

        for (int i = 0; i < other.bits.length; i++) {
            int b = other.at(i);
            if (b == 0b1) {
                temp1.set(this);
                for (int j = 0; j < i; j++) {
                    temp1.right();
                }
                temp2.add(temp1);
            }
        }

        return set(temp2);
    }

    public BigNumber div(BigNumber other) {


        return this;
    }

    protected void at(int pos, int val) {
        if (val == 0b1) {
            bits[pos / SIZE] = bits[pos / SIZE] | (0b1 << pos % SIZE);
        } else {
            bits[pos / SIZE] = bits[pos / SIZE] & ~(0b1 << pos % SIZE);
        }
    }

    protected int at(int pos) {
        return (bits[pos / SIZE] >> (pos % SIZE)) & 0b1;
    }

    @Override
    public String toString() {
        String out = "0b";
        for (int i = bits.length * SIZE - 1; i >= 0; i--) {
            out += at(i) == 1 ? '1' : '0';
        }
        out += "  |  0b";
        for (int i = 0; i < bits.length * SIZE; i++) {
            out += at(i) == 1 ? '1' : '0';
        }
        return out;
    }
}

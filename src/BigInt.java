import java.beans.PropertyEditorSupport;

public class BigInt {
    public int limit = 1000;
    public int[] digits = new int[limit];
    public int size = 0;
    public int sign;

    public BigInt() {
        sign = 0;
    }

    public BigInt(String s) {
        digits = new int[limit];
        size = s.length();
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            BigInt c = new BigInt(s.substring(1));
            this.digits = c.digits;
            this.sign = c.sign;
            this.size = c.size;
            if (s.charAt(0) == '-') sign *= -1;

        } else {
            sign = 1;
            for (int i = s.length() - 1; i >= 0; i--) {
                digits[size - 1 - i] = (int) s.charAt(i) - 48;

            }
            clearLeadingZeros();
        }

    }

    public static boolean compare(int[] a, int sizeA, int[] b, int sizeB) {
        if (sizeA > sizeB) return true;
        if (sizeA < sizeB) return false;

        for (int i = 0; i < sizeA; i++) {
            if (a[i] > b[i]) return true;
            if (a[i] < b[i]) return false;
        }

        return true;
    }

    public static String toStringFromArray(int[] a, int size) {
        String result = "";
        for (int i = 0; i < size; i++) {
            result += (char) (a[i] + '0');
        }
        return result;
    }

    private void clearLeadingZeros() {
        while (this.size != 0 && digits[this.size - 1] == 0) {
            this.size--;
        }
        if (this.size == 0) this.sign = 0;
    }

    public BigInt plus(BigInt b) {
        if (this.sign == 0) return b;
        if (b.sign == 0) return this;
        if (this.sign == -1 && b.sign == -1) {
            return (this.changeSign().plus(b.changeSign())).changeSign();
        }
        if (this.sign == -1) {
            return b.subtract(this.changeSign());
        }
        if (b.sign == -1) {
            return this.subtract(b.changeSign());
        }
        BigInt c = new BigInt();
        c.sign = 1;
        c.size = 0;
        c.digits = new int[limit];
        int d = 0, r;
        for (int i = 0; i < this.size || i < b.size; i++) {
            if (i < this.size) d += this.digits[i];
            if (i < b.size) d += b.digits[i];
            c.digits[c.size++] = d % 10;
            d /= 10;
        }
        if (d > 0) c.digits[c.size++] = d;
        return c;
    }

    public BigInt subtract(BigInt b) {
        if (b.sign == 0) return this;
        if (this.sign == 0) return b.changeSign();
        if (b.sign == -1) return this.plus(b.changeSign());
        if (this.sign == -1) return (this.changeSign().plus(b)).changeSign();
        if (this.comp(b)) return (b.subtract(this)).changeSign();
        BigInt c = new BigInt();
        c.sign = 1;
        c.digits = new int[limit];
        c.size = 0;
        int x, q = 0;
        for (int i = 0; i < this.size; i++) {
            q += this.digits[i];
            if (i < b.size) q -= b.digits[i];
            if (q < 0) {
                x = q + 10;
                q = -1;
            } else {
                x = q;
                q = 0;
            }
            c.digits[c.size++] = x;
        }
        c.clearLeadingZeros();
        return c;
    }

    public BigInt mult(BigInt b) {
        if (this.sign == 0 || b.sign == 0) {
            BigInt c = new BigInt("0");
            return c;
        }
        BigInt c = new BigInt();
        c.sign = this.sign * b.sign;
        c.digits = new int[limit];
        c.size = this.size + b.size;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < b.size; j++) {
                c.digits[i + j] += this.digits[i] * b.digits[j];
            }
        }
        for (int i = 0; i < c.size; i++) {
            if (c.digits[i] > 9) {
                c.digits[i + 1] += c.digits[i] / 10;
                c.digits[i] %= 10;
            }
        }
        c.clearLeadingZeros();
        return c;
    }

    public BigInt division(BigInt b) {
        if (b.sign == 0) return new BigInt("0");
        if (this.sign == 0) return new BigInt("0");
        if (this.sign == -1 && b.sign == -1) return this.changeSign().division(b.changeSign());
        if (this.sign == -1) return (this.changeSign().division(b)).changeSign();
        if (b.sign == -1) return (this.division(b.changeSign())).changeSign();
        if (this.comp(b)) return new BigInt("0");
        BigInt c = new BigInt("0"), q = new BigInt("0");
        int r = this.digits.length - 1, k = 0;
        while (q.comp(b)) q = q.mult(new BigInt("10")).plus(new BigInt(this.digits[r--] + ""));
        while (!(q.comp(b))) {
            k++;
            q = q.subtract(b);
        }
        c = c.mult(new BigInt("10")).plus(new BigInt(k + ""));
        while (r >= 0) {
            q = q.mult(new BigInt("10")).plus(new BigInt(this.digits[r--] + ""));
            k = 0;
            while (!(q.comp(b))) {
                k++;
                q = q.subtract(b);
            }
            c = c.mult(new BigInt("10")).plus(new BigInt(k + ""));
        }
        return c;
    }


    public BigInt divideTwo(BigInt val) {
        if (this.comp(val)) {
            return new BigInt("0");
        }

        int[] r = new int[limit];
        int[] b = new int[limit];

        int sizeB = 0;
        for (int i = 0; i < val.size; i++) {
            b[i] = val.digits[val.size - i - 1];
        }
        sizeB = val.size;

        int sizeR = 0;

        int[] res = new int[limit];
        int sizeRes = 0;

        for (int i = 0; i < this.size; i++) {
            r[sizeR++] = this.digits[this.size - i - 1];

            //clear leading zeros
            int lz = 0;
            for (int k = 0; k < sizeR; k++) {
                if (r[k] == 0) {
                    lz++;
                } else {
                    break;
                }
            }
            for (int k = lz; k < sizeR; k++) {
                r[k - lz] = r[k];
            }
            sizeR -= lz;

            // debug start
//            System.out.println("R:");
//            for (int k = 0; k < sizeR; k++) {
//                System.out.print(r[k]);
//            }
//            System.out.println();
            // debug finish
            if (compare(r, sizeR, b, sizeB)) {
                BigInt bf = new BigInt(BigInt.toStringFromArray(r, sizeR));
                int counter = 0;
//                System.out.println("St");
                while (true) {
                    bf = bf.subtract(val);
                    //
//                    System.out.println("BF:");
//                    bf.print();

                    counter++;

                    if (bf.comp(val)) {
                        break;
                    } else {
//                        System.out.println("P");
                    }
                }
//                System.out.println("End");

                for (int ii = 0; ii < bf.size; ii++) {
                    r[ii] = bf.digits[bf.size - ii - 1];
                }
                sizeR = bf.size;

                res[sizeRes++] = counter;
            } else {
                sizeRes++;
//                res[sizeRes++] = 0;
            }
        }

        return new BigInt(BigInt.toStringFromArray(res, sizeRes));
    }


    public boolean comp(BigInt b) {
        if (this.sign != b.sign) return this.sign < b.sign;
        if (this.sign == 0) return false;
        if (this.size != b.size) return this.size * this.sign < b.size * b.sign;
        int i = this.size - 1;
        while (i >= 0 && this.digits[i] == b.digits[i]) i--;
        if (i == -1) return false;
        return this.digits[i] * this.sign < b.digits[i] * this.sign;
    }

    public BigInt changeSign() {
        BigInt res = this;
        res.sign *= -1;
        return res;
    }

    public void print() {
        if (sign == -1) System.out.print("-");
        if (sign == 0) System.out.println("0");
        for (int i = 0; i < size; i++) {
            System.out.print(digits[size - 1 - i]);
        }
        System.out.println();
    }

    public String toString() {
        String res = "";
        if (sign == -1) res += '-';
        if (sign == 0) res += '0';
        for (int i = size - 1; i >= 0; i--) {
            res += digits[i];
        }
        return res;
    }
}

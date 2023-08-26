public class BigIntWithBase {
    public int limit = 1000;
    public long[] digits = new long[limit];
    public int size = 0;
    public int sign;

    public static int base = 2;

    public BigIntWithBase() {
        sign = 0;
    }

    private void clearLeadingZeros() {
        while (this.size != 0 && digits[this.size - 1] == 0) {
            this.size--;
        }
        if (this.size == 0) this.sign = 0;
    }

    public BigIntWithBase(String s) {
        int number, cnt = 0;
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            BigIntWithBase c = new BigIntWithBase(s.substring(1));
            this.digits = c.digits;
            this.sign = c.sign;
            this.size = c.size;
            if (s.charAt(0) == '-') sign *= -1;
        } else {
            sign = 1;
            for (int i = s.length(); i > 0; i -= base, cnt++) {
                if (i - base >= 0) number = Integer.parseInt(s.substring(i - base, i));
                else number = Integer.parseInt(s.substring(0, i));
                digits[cnt] = number;
            }
            size = cnt;
            clearLeadingZeros();
        }
    }

    public boolean comp(BigIntWithBase b) {
        if (this.sign != b.sign) return this.sign < b.sign;
        if (this.sign == 0) return false;
        if (this.size != b.size) return this.size * this.sign < b.size * b.sign;
        int i = this.size - 1;
        while (i >= 0 && this.digits[i] == b.digits[i]) i--;
        if (i == -1) return false;
        return this.digits[i] * this.sign < b.digits[i] * this.sign;
    }

    public BigIntWithBase plus(BigIntWithBase b) {
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
        BigIntWithBase c = new BigIntWithBase();
        int d = 0, bs = (int) Math.pow(10, base);
        c.sign = 1;
        for (int i = 0; i < this.size || i < b.size; i++) {
            if (i < this.size) d += this.digits[i];
            if (i < b.size) d += b.digits[i];
            c.digits[c.size++] = d % bs;
            d /= bs;
        }
        if (d > 0) c.digits[c.size++] = d;
        return c;
    }

    public BigIntWithBase subtract(BigIntWithBase b) {
        if (b.sign == 0) return this;
        if (this.sign == 0) return b.changeSign();
        if (b.sign == -1) return this.plus(b.changeSign());
        if (this.sign == -1) return (this.changeSign().plus(b)).changeSign();
        if (this.comp(b)) return (b.subtract(this)).changeSign();
        BigIntWithBase c = new BigIntWithBase();
        c.sign = 1;
        c.size = 0;
        int x, q = 0;
        for (int i = 0; i < this.size; i++) {
            q += this.digits[i];
            if (i < b.size) q -= b.digits[i];
            if (q < 0) {
                x = q + (int) Math.pow(10, base);
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

    public BigIntWithBase multiply(BigIntWithBase b) {
        if (this.sign == 0 || b.sign == 0) {
            BigIntWithBase c = new BigIntWithBase("0");
            return c;
        }
        BigIntWithBase c = new BigIntWithBase();
        int bs = (int) Math.pow(10, base);
        c.sign = this.sign * b.sign;
        c.size = this.size + b.size;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < b.size; j++) {
                c.digits[i + j] += this.digits[i] * b.digits[j];
            }
        }
        for (int i = 0; i < c.size; i++) {
            if (c.digits[i] > 9) {
                c.digits[i + 1] += c.digits[i] / bs;
                c.digits[i] %= bs;
            }
        }
        c.clearLeadingZeros();
        return c;
    }

    public BigIntWithBase division(BigIntWithBase b) throws Exception {
        if (b.sign == 0) throw new Exception("Value Error");
        if (this.sign == 0) return new BigIntWithBase("0");
        if (this.sign == -1) return this.changeSign().division(b).changeSign();
        if (b.sign == -1) return this.division(b.changeSign()).changeSign();

        BigIntWithBase l = new BigIntWithBase("0");
        BigIntWithBase r = this, m;
        while (l.comp(r)) {
            m = l.plus(r).plus(new BigIntWithBase("1")).divideTwo();
            if (this.comp(m.multiply(b))) r = m.subtract(new BigIntWithBase("1"));
            else l = m;
        }
        return l;
    }

    private BigIntWithBase divideTwo() {
        int base10 = (int)Math.pow(10, base);
        long q = 0;
        BigIntWithBase res = new BigIntWithBase("0");
        for(int i = size - 1; i >= 0; i --){
            q = q * base10 + digits[i];
            res = res.multiply(new BigIntWithBase(base10 + "")).plus(new BigIntWithBase(q / 2 + ""));
            q = q % 2;
        }
        if(sign == -1) res.sign *= -1;
        res.clearLeadingZeros();
        return res;
    }


    private BigIntWithBase changeSign() {
        BigIntWithBase result = this;
        result.sign *= -1;
        return result;
    }

    public void print() {
        if (sign == -1) System.out.print("-");
        if (sign == 0) System.out.println("0");
        for (int i = size - 1; i >= 0; i--) {
            String digit = digits[i] + "";
            if (i != size - 1)
                while (digit.length() < base) digit = "0" + digit;
            System.out.print(digit);
        }

        System.out.println();
    }

    public String toString() {
        String res = "";
        if (sign == -1) res += '-';
        if (sign == 0) res += '0';
        for (int i = size - 1; i >= 0; i--) {
            String digit = digits[i] + "";
            if (i != size - 1)
                while (digit.length() < base) digit = "0" + digit;
            res += digit;
        }
        return res;
    }

}

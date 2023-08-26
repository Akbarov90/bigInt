import java.math.BigInteger;
import java.util.Scanner;

public class acmp {
    public static void main(String[] args) { // zaychik
        Scanner cin = new Scanner(System.in);
        int n = cin.nextInt();
        //int n = cin.nextInt();
//        BigInteger[] dp = new BigInteger[n + 1];
//        dp[0] = BigInteger.ONE;
//        for (int i = 1; i <= n; i++) {
//            dp[i] = BigInteger.ZERO;
//            for (int j = Math.max(i - k, 0); j < i; j++) dp[i] = dp[i].add(dp[j]);
//        }
//        System.out.println(dp[n].toString());
        BigInteger res = new BigInteger("2");
        for (int i = 2; i <= n; i++) {
            if (res.remainder(new BigInteger("2").pow(i)).compareTo(BigInteger.ZERO) == 0) {
                res = res.add(new BigInteger("2").multiply(new BigInteger("10").pow(i - 1)));
            } else {
                res = res.add(new BigInteger("10").pow(i - 1));
            }
        }
        System.out.println(res.toString());
    }
}

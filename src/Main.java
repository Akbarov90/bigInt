import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static String[] arrOfStr = new String[2];
    public static String[] numberOfStr = new String[2];
    public static BigIntWithBase a, b;

    public static String[] test = {
            "555034944/232=2392392",
            "2392392*232=555034944",
            "99999977799981232102220001/9999998889999=9999998889999",
            "9999998889999*9999998889999=99999977799981232102220001",
            "99999999980000000001*999999999=99999999880000000020999999999",
            "132999912399777999321312320001-99999977799981232102220001=132899912421978018089210100000",
            "999999777102220001+1329999123997779993210222=1330000123997557095430223",
            "1330000123997557095430223-999999777102220001=1329999123997779993210222",
            "12/2=6",
            "50000+111=50111",
            "50000-1=49999",
            "4*4=16",
            "1000000-1=999999",
            "11*11=121",
            "7776-7767=9"
    };


    public static int operation(String number) {
        if (number.indexOf('+') > -1) return 1;
        else if (number.indexOf('-') > -1) return 2;
        else if (number.indexOf('*') > -1) return 3;
        else if (number.indexOf('/') > -1) return 4;
        return -1;
    }

    static void runTests() throws Exception {
        for (int i = 0; i < test.length; i++) {
            arrOfStr = test[i].split("=");
            switch (operation(test[i])) {
                case 1:
                    numberOfStr = arrOfStr[0].split("\\+");
                    a = new BigIntWithBase(numberOfStr[0]);
                    b = new BigIntWithBase(numberOfStr[1]);
                    if (Objects.equals(arrOfStr[1], a.plus(b).toString())) {
                        System.out.println(i + 1 + " " + "Accepted");
                    } else System.out.println(i + 1 + " " + "Wrong Answer plus");
                    break;
                case 2:
                    numberOfStr = arrOfStr[0].split("-");
                    a = new BigIntWithBase(numberOfStr[0]);
                    b = new BigIntWithBase(numberOfStr[1]);
                    if (Objects.equals(arrOfStr[1], a.subtract(b).toString())) {
                        System.out.println(i + 1 + " " + "Accepted");
                    } else System.out.println(i + 1 + " " + "Wrong Answer subtract");
                    break;
                case 3:
                    numberOfStr = arrOfStr[0].split("\\*");
                    a = new BigIntWithBase(numberOfStr[0]);
                    b = new BigIntWithBase(numberOfStr[1]);
                    if (Objects.equals(arrOfStr[1], a.multiply(b).toString())) {
                        System.out.println(i + 1 + " " + "Accepted");
                    } else System.out.println(i + 1 + " " + "Wrong Answer multiply");
                    break;
                case 4:
                    numberOfStr = arrOfStr[0].split("/");
                    a = new BigIntWithBase(numberOfStr[0]);
                    b = new BigIntWithBase(numberOfStr[1]);
                    if (Objects.equals(arrOfStr[1], a.division(b).toString())) {
                        System.out.println(i + 1 + " " + "Accepted");
                    } else System.out.println(i + 1 + " " + "Wrong Answer division");
                    break;
                default:
                    System.out.println("Wrong Format ! Please enter valid format");
            }
        }
    }


    public static void main(String[] args) throws Exception {
        runTests();
        BigIntWithBase a = new BigIntWithBase("88");
        BigIntWithBase b = new BigIntWithBase("-2");
        //a.division(b).print();
    }
}
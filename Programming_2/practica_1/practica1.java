
import java.util.Arrays;
import acm.program.CommandLineProgram;

/**
 * Implementations of infinite precision naturals using arrays of ints
 * <p>
 * - Each position in the array corresponds to a digit (between 0 and 9)
 * - Least significative digit on pos 0
 * - No 0's allowed on the front (except for the case of number 0)
 *
 * @author jmgimeno
 */

public class BigNaturals extends CommandLineProgram {

    //Function that return an array of 0
    public int[] zero() {
        return new int[]{0};
    }

    //Function that return an array of 1
    public int[] one() {
        return new int[]{1};
    }

    public boolean equals(int[] number1, int[] number2) {
        int i=0;
        boolean equal=true;
        if(number1.length != number2.length){           //check arrays length
            equal=false;
        }else{
            while(i<number1.length && equal){           //check digit by digit
                if(number1[i]==number2[i]){
                    i++;
                }else{
                    equal=false;
                }
            }
        }
        return equal;
    }
    public int[] add(int[] num1, int[] num2) {
        //set position on "top" or "bot" depending on the array size
        int[] top = (num1.length >= num2.length) ? copy(num1) : copy(num2);
        int[] bot = (num1.length >= num2.length) ? copy(num2) : copy(num1);
        int j=0; //index for "bot" number
        int[] sum = new int[top.length+1]; //create result array
        for(int i=0;i<top.length;i++){
            if(top[i]+bot[j]+sum[i]>9){         //check if there's carry
                sum[i] += (top[i]+bot[j])-10;
                sum[i+1]+=1;
            }else{
                sum[i] += top[i]+bot[j];
            }
            if(j<(bot.length-1)){           //increment "bot" position
                j++;
            }else if(j==(bot.length-1)){    //if it reaches the end set 0 to the last "bot" digit to continue with the sum
                bot[j]=0;
            }
        }
        if(sum[top.length]==0){                         //if there is an extra 0 create a new array of the correct size
            int[] sum_final = new int[top.length];
            for(int i=0;i<top.length;i++){
                sum_final[i]=sum[i];
            }
            return sum_final;
        }else{
            return sum;
        }
    }

    public int[] shiftLeft(int[] number, int positions) {
        if (equals(number,zero())) {
            return zero();
        } else {
            int[] shifted = new int[number.length + positions];         //fill with 0
            for (int i = 0; i < positions; i++) {
                shifted[i] = 0;
            }
            for (int j = 0; j < number.length; j++) {                   //fill with number
                shifted[j + positions] = number[j];
            }
            return shifted;
        }
    }

    public int[] multiplyByDigit(int[] number, int digit) {
        int[] multiplied = new int[]{0};
        for(int i=0;i<digit;i++){                               //loop of add() function
            multiplied = add(multiplied,number);
        }
        return multiplied;
    }

    public int[] multiply(int[] number1, int[] number2) {
        int[] top = (number1.length >= number2.length) ? copy(number1) : copy(number2); //sort numbers
        int[] bot = (number1.length >= number2.length) ? copy(number2) : copy(number1);
        int[][] multiplied = new int[bot.length][];
        for(int i=0;i<bot.length;i++){                              //multiply digit by digit and shifting one position every time it changes
            multiplied[i] = multiplyByDigit(top, bot[i]);
            multiplied[i] = shiftLeft(multiplied[i],i);
        }
        int[] result = new int[]{0};                                //sum final matrix into result array
        for (int[] ints : multiplied) {
            result = add(ints, result);
        }
        return result;
    }

    public int[] factorial(int[] number) {
        int[] count = zero();
        int[] factorial = one();
        while(!equals(count, number)) {
            count = add(count, one());
            factorial = multiply(factorial, count);
        }
        return factorial;
    }

    public int[] fibonacci(int[] number) {
        int[] count = zero();
        int[] prev = zero();
        int[] post = one();
        int[] fibonacci = zero();
        while(!equals(count,number)){
            fibonacci = add(prev,post);
            post = copy(prev);
            prev = copy(fibonacci);
            count = add(count, one());
        }
        return fibonacci;
    }

    public int[] copy(int[] num){               //simply copy the given array into another one
        int[] copied = new int[num.length];
        for(int i=0;i<num.length;i++){
            copied[i]=num[i];
        }
        return copied;
    }

    public void run() {

        testFromString();
        testAsString();
        testZero();
        testOne();
        testEquals();
        testAdd();
        testShiftLeft();
        testMultiplyByDigit();
        testMultiply();
        testFactorial();
        testFibonacci();
    }

    public static void main(String[] args) {
        new BigNaturals().start(args);
    }

    // -----
    // TESTS
    // -----

    // Functions for simplifying vector tests.

    public int[] fromString(String number) {
        // "25"  -> {5, 2}
        // "1"   -> {1}
        int[] digits = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            String digit = number.substring(i, i + 1);
            digits[number.length() - i - 1] = Integer.parseInt(digit);
        }
        return digits;
    }

    private void testFromString() {
        printlnInfo("Inicio de las pruebas de fromString");
        if (!Arrays.equals(new int[]{5, 2}, fromString("25"))) {
            printlnError("Error en el caso \"25\"");
        }
        if (!Arrays.equals(new int[]{1}, fromString("1"))) {
            printlnError("Error en el caso \"1\"");
        }
        printlnInfo("Final de las pruebas de fromString");
    }

    public String asString(int[] ints) {
        // {1}    -> "1"
        // {5, 2} -> "25"
        char[] intToChar =
                new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] chars = new char[ints.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = intToChar[ints[ints.length - i - 1]];

        }
        return new String(chars);
    }

    private void testAsString() {
        printlnInfo("Inicio de las pruebas de fromString");
        if (!"25".equals(asString(new int[]{5, 2}))) {
            printlnError("Error en el caso int[] {5, 2}");
        }
        if (!"1".equals(asString(new int[]{1}))) {
            printlnError("Error en el caso int[] {1}");
        }
        printlnInfo("Final de las pruebas de fromString");
    }

    private void testZero() {
        printlnInfo("Inicio de las pruebas de zero");
        if (!"0".equals(asString(zero()))) {
            printlnError("Error en la función zero");
        }
        printlnInfo("Final de las pruebas de zero");
    }

    private void testOne() {
        printlnInfo("Inicio de las pruebas de one");
        if (!"1".equals(asString(one()))) {
            printlnError("Error en la función one");
        }
        printlnInfo("Final de las pruebas de one");
    }

    private void testEquals() {
        printlnInfo("Inicio de las pruebas de equals");
        if (equals(zero(), one())) {
            printlnError("Error en el caso 0 != 1");
        }
        if (!equals(one(), one())) {
            printlnError("Error en el caso 1 = 1");
        }
        if (!equals(fromString("12345"), fromString("12345"))) {
            printlnError("Error en el caso 12345 = 12345");
        }
        printlnInfo("Final de las pruebas de equals");
    }

    private boolean checkAdd(String number1, String number2, String result) {
        return Arrays.equals(add(fromString(number1), fromString(number2)),
                fromString(result));
    }

    private void testAdd() {
        printlnInfo("Inicio de las pruebas de add");
        if (!checkAdd("1", "1", "2")) {
            printlnError("Error en la suma 1 + 1 = 2");
        }
        if (!checkAdd("5", "5", "10")) {
            printlnError("Error en la suma 5 + 5 = 10");
        }
        if (!checkAdd("99", "999", "1098")) {
            printlnError("Error en la suma 99 + 999 = 1098");
        }
        if (!checkAdd("999", "99", "1098")) {
            printlnError("Error en la suma 999 + 99 = 1098");
        }
        if (!checkAdd("0", "5", "5")) {
            printlnError("Error en la suma 5 + 0 = 5");
        }
        printlnInfo("Final de las pruebas de add");
    }

    private boolean checkShiftLeft(String number, int exponent, String result) {
        return Arrays.equals(shiftLeft(fromString(number), exponent),
                fromString(result));
    }

    private void testShiftLeft() {
        printlnInfo("Inicio de las pruebas de shiftLeft");
        if (!checkShiftLeft("54", 1, "540")) {
            printlnError("Error en 54 1 posición a la izquierda = 540");
        }
        if (!checkShiftLeft("54", 3, "54000")) {
            printlnError("Error en 54 3 posiciones a la izquierda = 54000");
        }
        if (!checkShiftLeft("0", 3, "0")) {
            printlnError("Error en 0 3 posiciones a la izquierda = 0");
        }
        printlnInfo("Final de las pruebas de shiftLeft");
    }

    private boolean checkMultByDigit(String number, int digit, String result) {
        return Arrays.equals(multiplyByDigit(fromString(number), digit),
                fromString(result));
    }

    private void testMultiplyByDigit() {
        printlnInfo("Inicio de las pruebas de multiplyByDigit");
        if (!checkMultByDigit("24", 2, "48")) {
            printlnError("Error en 24 * 2 = 48");
        }
        if (!checkMultByDigit("54", 4, "216")) {
            printlnError("Error en 54 * 3 = 162");
        }
        if (!checkMultByDigit("0", 3, "0")) {
            printlnError("Error en 0 * 3 = 0");
        }
        if (!checkMultByDigit("24", 0, "0")) {
            printlnError("Error en 24 * 0 = 0");
        }
        printlnInfo("Final de las pruebas de multiplyByDigit");
    }

    // multiply

    private boolean checkMultiply(String number1, String number2, String result) {
        return Arrays.equals(multiply(fromString(number1), fromString(number2)),
                fromString(result));
    }

    private void testMultiply() {
        printlnInfo("Inicio de las pruebas de multiply");
        if (!checkMultiply("2", "3", "6")) {
            printlnError("Error en 2 * 3 = 6");
        }
        if (!checkMultiply("999", "888", "887112")) {
            printlnError("Error en 999 * 888 = 887112");
        }
        if (!checkMultiply("10", "5", "50")) {
            printlnError("Error en 10 * 50 = 50");
        }
        if (!checkMultiply("12", "555443535", "6665322420")) {
            printlnError("Error en 12 * 555443535 = 6665322420");
        }
        if (!checkMultiply("555443535", "12", "6665322420")) {
            printlnError("Error en 555443535 * 12 = 6665322420");
        }
        if (!checkMultiply("999", "10", "9990")) {
            printlnError("Error en 99 * 10 = 90");
        }
        if (!checkMultiply("100", "5", "500")) {
            printlnError("Error en 10 * 50 = 50");
        }
        if (!checkMultiply("5", "17", "85")) {
            printlnError("Error en 5 * 17 = 85");
        }
        if (!checkMultiply("24", "5", "120")) {
            printlnError("Error en 24 * 5 = 120");
        }
        if (!checkMultiply("0", "888", "0")) {
            printlnError("Error en 0 * 888 = 0");
        }
        if (!checkMultiply("20397882081197443358640281739902897356800000000",
                "40",
                "815915283247897734345611269596115894272000000000")) {
            printlnError("Error en último producto de 40!");
        }
        if (!checkMultiply("815915283247897734345611269596115894272000000000",
                "41",
                "33452526613163807108170062053440751665152000000000")) {
            printlnError("Error en último producto de 41!");
        }
        printlnInfo("Final de las pruebas de multiply");
    }


    // factorial

    private boolean checkFactorial(String number, String result) {
        return Arrays.equals(factorial(fromString(number)), fromString(result));
    }

    private void testFactorial() {
        printlnInfo("Inicio de las pruebas de factorial");
        if (!checkFactorial("0", "1")) {
            printlnError("Error en 0! = 1");
        }
        if (!checkFactorial("5", "120")) {
            printlnError("Error en 5! = 120");
        }
        if (!checkFactorial("6", "720")) {
            printlnError("Error en 6! = 720");
        }
        if (!checkFactorial("7", "5040")) {
            printlnError("Error en 7! = 5040");
        }
        if (!checkFactorial("8", "40320")) {
            printlnError("Error en 8! = 720");
        }
        if (!checkFactorial("9", "362880")) {
            printlnError("Error en 9! = 720");
        }
        if (!checkFactorial("11", "39916800")) {
            printlnError("Error en 11! = 720");
        }
        if (!checkFactorial("10", "3628800")) {
            printlnError("Error en 10! = 3628800");
        }
        if (!checkFactorial("15", "1307674368000")) {
            printlnError("Error en 15! = 1307674368000");
        }
        if (!checkFactorial("20", "2432902008176640000")) {
            printlnError("Error en 20! = 2432902008176640000");
        }
        if (!checkFactorial("40", "815915283247897734345611269596115894272000000000")) {
            printlnError("40! ---------> " + asString(factorial(fromString("40"))));
            printlnError("Error en 40! = 815915283247897734345611269596115894272000000000");
        }
        if (!checkFactorial("41", "33452526613163807108170062053440751665152000000000")) {
            printlnError("41! ---------> " + asString(factorial(fromString("41"))));
            printlnError("Error en 41! = 33452526613163807108170062053440751665152000000000");
        }
        if (!checkFactorial("42", "1405006117752879898543142606244511569936384000000000")) {
            printlnError("42! ---------> " + asString(factorial(fromString("42"))));
            printlnError("Error en 42! = 1405006117752879898543142606244511569936384000000000");
        }
        if (!checkFactorial("100", "93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000")) {
            printlnError("100! ---------> " + asString(factorial(fromString("100"))));
            printlnError("Error en 100! = 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000 ");
        }
        if (!checkFactorial("200", "788657867364790503552363213932185062295135977687173263294742533244359449963403342920304284011984623904177212138919638830257642790242637105061926624952829931113462857270763317237396988943922445621451664240254033291864131227428294853277524242407573903240321257405579568660226031904170324062351700858796178922222789623703897374720000000000000000000000000000000000000000000000000")) {
            printlnError("200! ---------> " + asString(factorial(fromString("200"))));
            printlnError("Error en 200! = 788657867364790503552363213932185062295135977687173263294742533244359449963403342920304284011984623904177212138919638830257642790242637105061926624952829931113462857270763317237396988943922445621451664240254033291864131227428294853277524242407573903240321257405579568660226031904170324062351700858796178922222789623703897374720000000000000000000000000000000000000000000000000 ");
        }
        printlnInfo("Final de las pruebas de factorial");
    }

    private boolean checkFibonacci(String number, String result) {
        return Arrays.equals(fibonacci(fromString(number)), fromString(result));
    }

    private void testFibonacci() {
        printlnInfo("Inicio de las pruebas del fibonacci");
        if (!checkFibonacci("0", "0")) {
            printlnError("Error en fibonacci(0) != 0");
        }
        if (!checkFibonacci("1", "1")) {
            printlnError("Error en fibonacci(1) != 1");
        }
        if (!checkFibonacci("2", "1")) {
            printlnError("Error en fibonacci(2) != 1");
        }
        if (!checkFibonacci("3", "2")) {
            printlnError("Error en fibonacci(3) != 2");
        }
        if (!checkFibonacci("4", "3")) {
            printlnError("Error en fibonacci(4) != 3");
        }
        if (!checkFibonacci("10", "55")) {
            printlnError("Error en fibonacci(10) != 55");
        }
        if (!checkFibonacci("20", "6765")) {
            printlnError("Error en fibonacci(20) != 6765");
        }
        if (!checkFibonacci("30", "832040")) {
            printlnError("Error en fibonacci(30) != 832040");
        }
        if (!checkFibonacci("40", "102334155")) {
            printlnError("Error en fibonacci(40) != 102334155");
        }
        if (!checkFibonacci("50", "12586269025")) {
            printlnError("Error en fibonacci(60) != 12586269025");
        }
        if (!checkFibonacci("100", "354224848179261915075")) {
            printlnError("Error en fibonacci(100) != 354224848179261915075");
        }
        if (!checkFibonacci("200", "280571172992510140037611932413038677189525")) {
            printlnError("Error en fibonacci(200) != 280571172992510140037611932413038677189525");
        }
        if (!checkFibonacci("900", "54877108839480000051413673948383714443800519309123592724494953427039811201064341234954387521525390615504949092187441218246679104731442473022013980160407007017175697317900483275246652938800")) {
            printlnError("Error en fibonacci(900) != 54877108839480000051413673948383714443800519309123592724494953427039811201064341234954387521525390615504949092187441218246679104731442473022013980160407007017175697317900483275246652938800");
        }
        printlnInfo("Final de las pruebas del fibonacci");
    }

    // Colorize output for CommandLineProgram

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public void printlnInfo(String message) {
        if (acm.program.CommandLineProgram.class.isInstance(this))
            println(ANSI_BLUE + message + ANSI_RESET);
        else
            println(message);
    }

    public void printlnOk(String message) {
        if (acm.program.CommandLineProgram.class.isInstance(this))
            println(ANSI_GREEN + "OK: " + message + ANSI_RESET);
        else
            println("OK: " + message);
    }

    public void printlnError(String message) {
        if (acm.program.CommandLineProgram.class.isInstance(this))
            println(ANSI_RED + "ERROR: " + message + ANSI_RESET);
        else
            println("ERROR: " + message);
    }

    public void printBar() {
        println("--------------------------------------------------");
    }
}

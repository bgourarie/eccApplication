/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECCToolBox;

import ECGroups.ECGroup;
import ECGroups.ECPoint;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author benbenben
 */
public class Utilities {

    /**
     * given a maximum index, it returns an array of integers containing
     * [0-maxIndex] but in random order
     *
     * @param length
     */
    public static int[] getRandomizedArrayIndices(int maxIndex) {
        ArrayList<Integer> temp;
        temp = new ArrayList();
        for (int i = 0; i <maxIndex; i++) {
            temp.add(new Integer(i));
        }
        java.util.Collections.shuffle(temp);
        int[] toast = new int[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            toast[i]=temp.get(i);
        }
        return toast;
    }

    /**
     *
     * @param toast
     */
    public static void print(String toast) {
        if (!isBlackListed(toast)) {
            System.out.println(toast);
        }
    }

    public static String[] ArrayToString(ECPoint[] ecp) {
        String[] toast = new String[ecp.length];
        for (int i = 0; i < toast.length; i++) {
            toast[i] = ecp[i].toString();
        }
        return toast;
    }

//public static sortECViewPointsByOrder()
    
    public static String[] convertArrayListToArrayString(ArrayList<String> answers) {
        String[] ben = new String[answers.size()];
        for (int i = 0; i < ben.length; i++) {
            ben[i] = answers.get(i);
        }
        return ben;
    }

    /**
     * Not yet working! not sure how to calculate a square root.. there must be
     * a trick. involving bits?
     *
     * @param a
     * @return sqrt(a)
     */
    public static BigInteger sqrtBigInt(BigInteger a) {
        return a.divide(Globals.bigInt(2));
    }

    static boolean isBlackListed(String toast) {
        return toast.contains("v u x2 x1") || toast.contains("TextField: ") || toast.contains("slider: ") || toast.contains("returning ptatinf:") || toast.contains("q, v, u=") || toast.contains("r,v,q,u=") || toast.contains("sY/sX= ") || toast.contains("sY= ") || toast.contains("x, x2, q, x1");
    }

    public static int inversionModP(int a, int p) {
        BigInteger m = new BigInteger("" + a);
        return m.modInverse(new BigInteger("" + p)).intValue();
    }

    /**
     * http://stackoverflow.com/a/4916464 originally python...
     *
     * @param x
     * @param y
     * @return
     */
//    private static int[] euclid(int x, int y) {
////    """Given x < y, find a and b such that a * x + b * y = g where, g is the
////    gcd of x and y.  Returns (a,b,g)."""
//        assert x < y;
//        assert x >= 0;
//        assert y > 0;
//        if (x == 0) {
////        gcd(0,y) = y
//            int[] abn = {0, 1, y};
//            return abn;
//        } else {
////        # Write y as y = dx + r
//            int d = y / x;
//            int r = y - d * x;
////        # Compute for the simpler problem.
//            int[] abd = euclid(r, x);
//// (a,b,g)=euclid(r,x);
////        # Then ar + bx = g     -->
////        #      a(y-dx) + bx = g    -->
////        #      ay - adx + bx = g    -->
////        #      (b-ad)x + ay = g
//            int[] abn = new int[3];
//            abn[0] = abd[1] - (abd[0] * d);
//            abn[1] = abd[0];
//            abn[2] = abd[2];
//            return abn;
//        }
//    }

    /**
     * also originally python http://stackoverflow.com/a/4916464
     *
     * @param x
     * @param n
     * @return
     */
//    private static int modInverse(int x, int n) {
//        int[] abg = euclid(x % n, n); //a,b,g = euclid...
//        assert abg[2] == 1; //    # a * x + b * n = 1 therefore
//        //    # a * x = 1 (mod n)
//        return abg[0] % n;
//    }

//    /**
//     * private helper class for the babyStepGiantStep algorthm to store its
//     * results.
//     */
//    private static class BabyStepItem {
//
//        private int j;
//        private ECPoint alphaJ;
//
//        BabyStepItem(int je, ECPoint alphaJe) {
//            j = je;
//            alphaJ = alphaJe;
//        }
//
//        int getJ() {
//            return j;
//        }
//
//        ECPoint getECPoint() {
//            return alphaJ;
//        }
//    }
//
//    /**
//     * Seems to work just fine...
//     *
//     * @param ecg
//     * @param P
//     * @param Q
//     * @return
//     */
//    public static int babyStepGiantStep(ECGroup ecg, ECPoint P, ECPoint Q) {
//        int k = 0;
//        long startTime = System.currentTimeMillis();
//        int n = getPointOrder(ecg, P); //n = order of P
//        int m = (int) Math.ceil(Math.sqrt(n)) + 1; // math.ceil is said to return an integer value so we aren't losing precision
//        BabyStepItem[] lookupTable = new BabyStepItem[m]; // we'll store the m multiples of alpha in the table
//        ECPoint mP = ecg.multiple(m, P);
//        for (int i = 0; i < m; i++) {
//            lookupTable[i] = new BabyStepItem(i, ecg.multiple(i, P));
//        }
//        int prog = 0;
//        boolean running = true;
//        while (running) {
//            for (int j = 0; j < m; j++) { //for all pairs, if i is the right one, tehn we are done.. if we reach the end of the loop, then we change gamma.
////                print("going through every element in the lookuptable now, comparing to " + ecg.add(Q, ecg.multiple((-j), mP)));
//
//                for (BabyStepItem bs : lookupTable) {
////                    if(prog%10==0){
////                  print("babyStepGiantStep Progress = "+prog/10);
////              }
//                    if (bs.getECPoint().compareTo(ecg.add(Q, ecg.multiple((-j), mP))) == 0) {
//                        k = bs.getJ() + j * m;
//                        print("BabyStep GiantStep took " + (System.currentTimeMillis() - startTime) + " ms to complete");
//                        return k % n;
//                    }
//                    prog += (m / 10);
//                }
//
//            }
//            break;
//        }
//        print("BabyStep GiantStep took " + (System.currentTimeMillis() - startTime) + " ms to complete");
//        print("reached end of i loop in babyStepGiantStep algorithm without returning a value...");
//        return -1;
//    }
//
//    public static int pollardRho(ECGroup ecg, ECPoint P, ECPoint Q) {
//        PollardRhoClass ret = new PollardRhoClass(ecg, P, Q);
//        return ret.getResult();
//    }
/*
    private static class PollardRhoClass {

        private ECPoint[] R;
        private ECGroup ecg;
        private ECPoint P, Q;
        private int L, n;
        private int[] a, b;
        private long startTime;

        PollardRhoClass(ECGroup e, ECPoint P, ECPoint Q) {
            ecg = e;
            this.P = P;
            this.Q = Q;
            n = Utilities.getPointOrder(ecg, P);
            startTime = -1;
            getRandomIntegers(n);
            getRandomPoints();
        }

        private void reset() {
            getRandomIntegers(n);
            getRandomPoints();
        }

        private void getRandomPoints() {
            R = new ECPoint[L];
            for (int i = 0; i < R.length; i++) {
                R[i] = ecg.add(ecg.multiple(a[i], P), ecg.multiple(b[i], Q)); //R[i]=a[i]P+b[i]Q
//                print("R[" + i + "] = " + R[i]);
            }
        }

        private void getRandomIntegers(int size) {
            L = n / 60;
            if (L < 4) {
                L = 4;
            }
            a = new int[L];
            b = new int[L];
            for (int i = 0; i < a.length; i++) {
                a[i] = (int) (Math.random() * n) % n;
                b[i] = (int) (Math.random() * n) % n;
            }
            /*
             * the following four lines are test data for the example 4.4 in Meneze's
             */
//            int[] a1 = {79, 206, 87, 219};
//            int[] b1 = {163, 19, 109, 68};
//            a = a1;
//            b = b1;
//        }
//
//        /**
//         * we say that each point is x%L for the partitions...
//         *
//         * @param X
//         * @return
//         */
//        private int H(ECPoint X) {
//            if (X.isConstant()) {
//                return n % L;
//            }
//            /**
//             * X.compareTo( (0,0) ) is essentially quantifying the point X =
//             * (x,y) as x^2+y^2 or something...
//             */
//            return X.compareTo(new ECPoint(ecg.getField().getElement(0), ecg.getField().getElement(0))) % L;
//        }
//
//        private int getResult() {
//            if (startTime == -1) {
//                startTime = System.currentTimeMillis();;
//            }
//            //starting from step 4 of algorithm 4.3 on p 159 of "guide to elliptic curve cryptography", A.Menezes et al.
//            //4) c',d' \in [0,n-1]
//            int cPrime = (int) (Math.random() * (1000 * n)) % n;//ecg.getSize())) % ecg.getSize();
//            int dPrime = (int) (Math.random() * (1000 * n)) % n;//ecg.getSize())) % ecg.getSize();
//            ECPoint xPrime = ecg.add(ecg.multiple(cPrime, P), ecg.multiple(dPrime, Q));
//            ECPoint xDoublePrime = xPrime;
//            int cDoublePrime = 0 + cPrime, dDoublePrime = 0 + dPrime;
//            int j = 0;
//            int progress = 0;
//            //step 6
//            do {
////                if(progress%10==0){
////                    print("PollardRho progress = "+progress/10 +"%");
////                }
//                j = H(xPrime);
//                xPrime = ecg.add(xPrime, R[j]); //x' = x'+R_j
//                cPrime = (cPrime + a[j]) % n; //c'= c'+a_j mod n
//                dPrime = (dPrime + b[j]) % n; // d'= d'+b_j mod n
//
//                for (int i = 0; i < 2; i++) {
//                    j = H(xDoublePrime);
//                    xDoublePrime = ecg.add(xDoublePrime, R[j]);
//                    cDoublePrime = (cDoublePrime + a[j]) % n;
//                    dDoublePrime = (dDoublePrime + b[j]) % n;
//                }
//                progress++;
//                /*
//                 * Now we add in a fail-fast catch...
//                 */
//                if (progress % 100 > 2000) {
//                    print("PollardRho took too long, starting again.");
//                    reset();
//                    return getResult();
//                }
//            } while (xDoublePrime.compareTo(xPrime) != 0);
//            //step 7...
//            if (dPrime == dDoublePrime) {
//                reset(); //just gets new random values...
//                return getResult(); // if dPrime == dDoublePrime then we haven't actually found it and need to start over. 
//
//            } else {
////                print("c= " + cPrime + " c'= " + cDoublePrime + " d = " + dPrime + " d'=" + dDoublePrime + " n =" + n);
//                int x;
//                try {
//                    x = (cPrime - cDoublePrime) * inversionModP((dDoublePrime - dPrime), n);
//                } //return l= (c-c')(d'-d)^-1 mod n
//                catch (ArithmeticException ae) {
//                    print("error doing modular inversion (" + "c= " + cPrime + " c'= " + cDoublePrime + " d = " + dPrime + " d'=" + dDoublePrime + " n =" + n + ").  starting over");
//                    reset();
//                    return getResult();
//                }
//                long endTime = System.currentTimeMillis();
//                print("PollardRho took " + (endTime - startTime) + " ms to complete ");
//                if (x < 0) { // this seems to be caused by something wierd with how java handles %  so it maintains sign, but we want it to be in [0-n]
//                    return (x % n) + n;
//                } else {
//                    return x % n;
//                }
//            }
//
//        }
//    }*/

    /**
     * brute force calculates the order of a point by adding it to itself until
     * it can take no more.
     *
     * @param ecg
     * @param P
     * @return
     */
    public static int getPointOrder(ECGroup ecg, ECPoint P) {
        ECPoint currPt = P;
        int mult = 0;
        do { //if currPt==p then we have run around, and currPt = mult*P = P 
            mult++;
            currPt = ecg.add(currPt, P);
        } while (!currPt.toString().equalsIgnoreCase(Globals.INF_STRING));
        //now currPt = original point, and mult = order of the point
        return mult+1;

    }    
}

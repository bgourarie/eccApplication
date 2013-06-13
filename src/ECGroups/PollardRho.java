/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Observable;

/**
 *This class is different then the version for JDK1.7
 * there seems ot be an issue with arrayList methods from the jdk library; so i've altered the behavior on reset.
 * @author benbenben
 */
public class PollardRho extends DLPSolver {

    private ECPoint[] R;
    private ECGroup ecg;
    private ECPoint P, Q;
    private int storedK, L, n;
    private int[] a, b;
    private long startTime;
    private ArrayList<String> pathTaken;

    /**
     *
     * @param e
     * @param P
     * @param Q
     */
    public PollardRho(ECGroup e, ECPoint P, ECPoint Q) {
        super(e,P,Q);
        ecg = e;
        this.P = P;
        this.Q = Q;
        n = Utilities.getPointOrder(ecg, P);
        startTime = -1;
        storedK = -99;
        newPath();
        getRandomIntegers(n);
        getRandomPoints();
        if (randomlyGotPtAtInf()) {
            reset();
        }
    }

    private boolean randomlyGotPtAtInf() {
        for (ECPoint ecp : R) {
            if (ecp.isConstant()) {
//                System.err.println("Randomly got PTATINF");
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public int solveDLP() {
        storedK = getResult();
        return storedK;
    }

  

    /**This method is altered from the JDK 1.7 version
     * 
     */
    private void reset() {
        getRandomIntegers(n);
        getRandomPoints();
        if (randomlyGotPtAtInf()) {
            reset();
        }
        //debug code:
//        int testRetainWorked=pathTaken.size();
       
        /*
         * The commented out lines below are changed from the JDK 1.7 version;
         *  instead of counting restarts, we simply clear and start again.
         * 
         */
        //we'll clear out the path, except we'll keep the times that we had to reset. 
//        ArrayList<String> temp = new ArrayList();
//        temp.add(Globals.RESET_POLLARD);
//        temp.add("Modular Inversion error, resetting");
//        pathTaken.retainAll(temp);
        pathTaken.clear();
//      then we add record of this instance of resetting...
//        addToPath(Globals.RESET_POLLARD);
    }

    /**
     *
     */
    public void resetPath() {
        newPath();
    }

    private void newPath() {
        pathTaken = new ArrayList<String>();
    }

    private void addToPath(String s) {
        pathTaken.add(pathTaken.size(), s);
    }

    private void getRandomPoints() {
        R = new ECPoint[L];
        for (int i = 0; i < R.length; i++) {
            R[i] = ecg.add(ecg.multiple(a[i], P), ecg.multiple(b[i], Q)); //R[i]=a[i]P+b[i]Q
//                print("R[" + i + "] = " + R[i]);
        }
    }

    private void getRandomIntegers(int size) {
        L = n > 64 ? 16 : n > 32 ? 8 : 4;
        //if >64, we'll do 16, else we'll check if it's greater then 32 and use 8, else we'll use 4...
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
//        
    }

    /**
     * we say that each point is x%L for the partitions...
     *
     * @param X
     * @return
     */
    private int H(ECPoint X) {
//      The following 3 lines are for use in example 4.4 of the menezes book. 
//        if(1==1){
//         return X.getX().getVal().mod(new BigInteger("4")).intValue();
//        }
        if (X.isConstant()) {
            return n % L;
        }
        /**
         * X.compareTo( (0,0) ) is essentially quantifying the point X = (x,y)
         * as x^2+y^2 or something...
         */
        // the size is a complicated assignment, however:
        // it's really just manually assigning the number of bits based on the value of L, all in one line.
        boolean[] lastNBits = new boolean[L == 32 ? 5 : L == 16 ? 4 : L == 8 ? 3 : 2];

        // get the last five bits from the X value....
        for (int i = 0; i < lastNBits.length; i++) {
            lastNBits[i] = X.getX().getVal().testBit(i);
        }
        // convert the five bits to an integer...
        int decimal = 0;
        for (int i = 0; i < lastNBits.length; i++) {
            if (lastNBits[i]) {
                decimal += 2 ^ (i);
            }
        }
        // then we return that integer within the range from 0<=decimal<L
        return decimal % L;
    }

    private void shareInfoOnPoint(ECPoint p) {
        addToPath(p.toString());
    }

    private int getResult() {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();;
        }
        //starting from step 4 of algorithm 4.3 on p 159 of "guide to elliptic curve cryptography", A.Menezes et al.
        //4) c',d' \in [0,n-1]
        int cPrime = (int) (Math.random() * n + 1) % n;//ecg.getSize())) % ecg.getSize();
        int dPrime = (int) (Math.random() * n + 1) % n;//ecg.getSize())) % ecg.getSize();
        ECPoint xPrime = ecg.add(ecg.multiple(cPrime, P), ecg.multiple(dPrime, Q));
        if (xPrime.isConstant()) {
            System.err.println("xPrime is infinity... gotta get new randoms!");
            return getResult();
        }
        int cDoublePrime = 0 + cPrime, dDoublePrime = 0 + dPrime;
        ECPoint xDoublePrime = xPrime.clone();
        addToPath("a="+cPrime);
        addToPath("b="+dPrime);    
        addToPath("c=" + cPrime);
        addToPath("d=" + dPrime);
        addToPath("X=" + xPrime);
        addToPath("a="+cDoublePrime);
        addToPath("b="+dDoublePrime);
        addToPath("c'=" + cDoublePrime);
        addToPath("d'=" + dDoublePrime);
        addToPath("X'=" + xDoublePrime);
        int j = 0;
        int progress = 0;
        //step 6
        do {
            if (progress % 10000 == 0) {
                Utilities.print("PollardRho Still going. Time elapsed = " + (System.currentTimeMillis() - startTime));
            }
            j = H(xPrime);
            xPrime = ecg.add(xPrime, R[j]); //x' = x'+R_j
            cPrime = (cPrime + a[j]) % n; //c'= c'+a_j mod n
            dPrime = (dPrime + b[j]) % n; // d'= d'+b_j mod n
            addToPath("a=" + a[j]);
            addToPath("b=" + b[j]);
            addToPath("c=" + cPrime);
            addToPath("d=" + dPrime);
            addToPath("X=" + xPrime);

            //            System.err.println("XPrime= " + xPrime.toString() + " c', d' = " + cPrime + " , " + dPrime + " and j =" + j);
            for (int i = 0; i < 2; i++) {
                j = H(xDoublePrime);
                xDoublePrime = ecg.add(xDoublePrime, R[j]);
                cDoublePrime = (cDoublePrime + a[j]) % n;
                dDoublePrime = (dDoublePrime + b[j]) % n;
//                System.err.println("XDoublePrime= " + xDoublePrime.toString() + " c'', d' = " + cDoublePrime + " , " + dDoublePrime + " and j =" + j);
            }
            
            addToPath("a=" + a[j]);
            addToPath("b=" + b[j]);
            addToPath("c'=" + cDoublePrime);
            addToPath("d'=" + dDoublePrime);
            addToPath("X'=" + xDoublePrime);
            progress++;
            /*
             * Now we add in a fail-fast catch...
             * Not sure this is a good idea or not?
             */
            if (progress / 100 > 2000) {
                System.out.println("TIMEOUT-PollardRho took too long, resetting (which will clear logs).");
//                return -1;
                reset();
                // this should work okay, because it will remove whatever steps made it take so long.
                return getResult();
            }
        } while (xDoublePrime.compareTo(xPrime) != 0);
        //step 7...
        if (dPrime == dDoublePrime) {
            // this adds a "reset algorithm" step to the pathTaken
            reset(); //just gets new random values...

            return getResult(); // if dPrime == dDoublePrime then we haven't actually found it and need to start over. 

        } else {
//                print("c= " + cPrime + " c'= " + cDoublePrime + " d = " + dPrime + " d'=" + dDoublePrime + " n =" + n);
            int x;
            try {
                x = (cPrime - cDoublePrime) * Utilities.inversionModP((dDoublePrime - dPrime), n);
            } //return l= (c-c')(d'-d)^-1 mod n
            catch (ArithmeticException ae) {
//                System.err.println("error doing modular inversion (" + "c= " + cPrime + " c'= " + cDoublePrime + " d = " + dPrime + " d'=" + dDoublePrime + " n =" + n + ").  starting over");
                this.addToPath("Modular Inversion error, resetting");
                // reset will actually remove that line from the path anyways....
                reset();
                return getResult();
            }
            long endTime = System.currentTimeMillis();
            this.addToPath("PollardRho took " + (endTime - startTime) + " ms to complete ");
            if (x < 0) { // this seems to be caused by something wierd with how java handles %  so it maintains sign, but we want it to be in [0-n]
                return (x % n) + n;
            } else {
                return x % n;
            }
        }

    }



    @Override
    int runAlgorithm() {
        return getResult();
    }

    @Override
    ArrayList<String> getLogs() {
        return pathTaken;
    }
    
}

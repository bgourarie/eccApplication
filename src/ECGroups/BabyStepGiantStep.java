/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import ECCToolBox.Utilities;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author benbenbenultra
 */
public class BabyStepGiantStep extends DLPSolver {

    private ECGroup ecg;
    private ECPoint p, q, active;
    private int storedK;
    private static int memAccess;
    private double timeTaken;

    /**
     *
     * @param ecg
     * @param p
     * @param q
     */
    public BabyStepGiantStep(ECGroup ecg, ECPoint p, ECPoint q) {
        super(ecg, p, q);
        this.ecg = ecg;
        this.p = p;
        this.q = q;
        storedK = -1;
//        this.storageSpace=0;
    }

    private int solve() {
        memAccess = 0;
        storedK = babyStepGiantStep(ecg, p, q);
        return storedK;
    }

    @Override
    int runAlgorithm() {
        return solve();
    }

    @Override
    ArrayList<String> getLogs() {
        return this.getInfoPrivate();
    }

    /**
     * private helper class for the babyStepGiantStep algorthm to store its
     * results.
     */
    private static class BabyStepItem {

        private int j;
        private ECPoint alphaJ;

        BabyStepItem(int je, ECPoint alphaJe) {
            j = je;
            alphaJ = alphaJe;
            memAccess++;
        }

        int getJ() {
            return j;
        }

        ECPoint getECPoint() {
            memAccess++;
            return alphaJ;

        }
    }

    /**
     * Seems to work just fine...
     *
     * @param ecg
     * @param P
     * @param Q
     * @return
     */
    public int babyStepGiantStep(ECGroup ecg, ECPoint P, ECPoint Q) {
        int k = 0;
        long startTime = System.currentTimeMillis();
        int n = Utilities.getPointOrder(ecg, P); //n = order of P
        int m = (int) Math.ceil(Math.sqrt(n)); // math.ceil is said to return an integer value so we aren't losing precision
        BabyStepItem[] lookupTable = new BabyStepItem[m]; // we'll store the m multiples of alpha in the table
        ECPoint mP = ecg.multiple(m, P); // this is for
        for (int i = 0; i < m; i++) {
            lookupTable[i] = new BabyStepItem(i, ecg.multiple(i, P));
        }
        int prog = 0;
        boolean running = true;
        while (running) {
            for (int j = 0; j < m; j++) {
//                for all pairs, if i is the right one, tehn we are done.. 
//                if we reach the end of the loop, then we change gamma.
//                print("going through every element in the lookuptable now,
                // comparing to " + ecg.add(Q, ecg.multiple((-j), mP)));
                for (BabyStepItem bs : lookupTable) {
                    // why is it -j??
                    // because we are looking for a pair *j and *2j; which we have shifted to be -j and +j (which still differs by j)
                    if (bs.getECPoint().compareTo(ecg.add(Q, ecg.multiple((-j), mP))) == 0) {
                        k = bs.getJ() + j * m;
                        timeTaken = (System.currentTimeMillis() - startTime);
                        System.err.println("k = " + k + " from bsgs... that needs to be modulo " + n);
                        return k % n;
                    }
                    prog += (m / 10);
                }

            }
            break;
        }
        System.err.println("BSGS failed, values were: m=" + m + " = sqrt(" + n + ") (sqrt(n))..." + "lookupTable had " + lookupTable.length);

        return -1;
    }

    /**
     *
     * @return
     */
    private ArrayList<String> getInfoPrivate() {
        ArrayList<String> toast = new ArrayList<String>();
        if (this.getStoredK() > 0) {
            toast.add("Baby-Step Giant-Step method accessed memory " + memAccess + " times and took " + timeTaken + " ms to complete.");
        } else {
            toast.add("Baby-Step Giant-Step failed to complete.");
        }
        return toast;
    }
}

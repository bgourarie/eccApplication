/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.awt.Dimension;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JFrame;

/**
 * defines an object containing all the points within a given elliptic curve
 * over a given finite field
 *
 * @author benbenbenBEASTMODE
 */
public class ECGroup implements Serializable {

//    private ECPoint[] elements;
    private FieldElementFactory field;
    private EllipticCurve ec;
    private BigInteger groupSize;
    private ArrayList<ECPoint> elementList;
    private static int indexCount = 0;

    /**
     *
     * @return
     */
    public FieldElementFactory getField() {
        return field;
    }

    /**
     *
     * @param ec
     * @param field
     */
    public ECGroup(EllipticCurve ec, FieldElementFactory field) {
        this.field = field;
        this.ec = ec;
        //if (!this.checkIndex()) {
        this.computeElements();
        //  this.saveECG();
        // }

    }

    /**
     * this method is very slow.. but let's do it anyway. it will brute force
     * it, so the field must be small, as it will take at worst, O(n^2+n)
     *
     * @return
     */
    private void computeElements() {
        int count = 0;
        elementList = new ArrayList<ECPoint>();
        int percentFinished, size = field.getSize(), hundredth = (size > 100) ? size / 100 : size;
        long time = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            //this line can change to an update command..
            if (i % hundredth == 0) {
                percentFinished = (100 * i) / size;
                Utilities.print("Calculating ECG a"+this.ec.getA().toString()+"b"+this.ec.getB().toString()+":" + percentFinished+" ms");
            }
            for (int j = 0; j < field.getSize(); j++) {

                //ECPoint temp = new ECPoint(Globals.bigInt(i),Globals.bigInt(j));
                if (this.isIn(new ECPoint(field.getElement(i), field.getElement(j)))) {
                    elementList.add(new ECPoint(field.getElement(i), field.getElement(j)));
                    count++;
//                    Utilities.print(elementList.get(count-1).toString());
                }
            }
        }
        java.util.Collections.sort(elementList);
        time = System.currentTimeMillis() - time;
        Utilities.print("Time taken: " + time);
        /*        elements = new ECPoint[count];
         for (int i = 0; i < count; i++) {
         //          elements[i] = elementStack.pop().clone();
         }
         */ this.groupSize = new BigInteger("" + count);
        // return toast;
    }

    /**
     * not yet working
     *
     * @return
     */
//    private BigInteger hasseTheorem() {
//        }
    /**
     * Is this safe? elements[i] will not be a pointer, right? actually, better
     * question- is it useful? this would require me to sort it. is it even
     * necessary?
     *
     * @param i
     * @return
     */
    public ECPoint getElementAt(int i) {
        return elementList.get(i);
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean isIn(ECPoint p) {
        return this.ec.contains(p) && field.contains(p.getX()) && field.contains(p.getY());
    }

    /**
     * knowing the EC and FF, it checks an index file for the existence of the
     * group, and if it exists, it will construct this object from that one.
     *
     * @return
     */
//    private boolean checkIndex() {
//        try {
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ECGIndex"));
//            for (int i = 0; i < this.indexCount; i++) {
//                if (ois.readChar() == Globals.OBJ_WRITE_SEP) {
//                    if (((EllipticCurve) ois.readObject()).equals(this.ec)) {
//                        if (ois.readChar() == Globals.OBJ_WRITE_SEP) {
//                            if (((FiniteField) ois.readObject()).equals(this.field)) {
//                                if (ois.readChar() == Globals.OBJ_WRITE_SEP) {
//                                    this.elementStack = getElementsFromIndex(ois.readInt());
//                                    ois.close();
//                                    return true;
//                                }
//                            }
//                        }
//                    }
//                }
//            }//if we go through all of the indices and can't find shit... then we musta fucked up.
//            ois.close();
//            Globals.print("did not find an equivalent group in the index file");
//            return false;
//        } catch (FileNotFoundException fnf) {
//            Globals.print(fnf.getMessage() + " fnf checkIndex");
//            return false;
//        } catch (IOException ioe) {
//            Globals.print(ioe.getMessage() + " ioe checkIndex");
//            return false;
//        } catch (ClassNotFoundException cnfe) {
//            Globals.print(cnfe.getMessage() + " cnfe checkIndex");
//            return false;
//        }
//
//    }
//    public Stack<ECPoint> getElementStack() {
//        return elementStack;
//    }
//    private Stack getElementsFromIndex(int index) {
//        try {
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ECGroups"));
//            for (int i = 0; i < indexCount; i++) {
//                if (ois.readChar() == Globals.ECG_SEP) {
//                    if (ois.readInt() == index) {
//                        if (ois.readChar() == Globals.ECG_SEP) {
//                            ECGroup ecg = (ECGroup) ois.readObject();
//                            ois.close();
//                            return ecg.getElements();
//                        }
//                    }
//                }
//            }
//            ois.close();
//        } catch (FileNotFoundException ex) {
//            Globals.print(ex.getMessage() + "fnf get elementsfrom index");
//        } catch (IOException ioe) {
//            Globals.print(ioe.getMessage() + " ioe get elementsfrom index");
//        } catch (ClassNotFoundException cnfe) {
//            Globals.print(cnfe.getMessage() + " cnfe get elementsfrom index");
//        }
//
//        Globals.print("error will robinsons");
//        return new Stack<>();
//
//    }
//
//    /**
//     * writes the objects. Index file will go Char EC Char FF char Int groups
//     * file will go Char Int Char ECGroup
//     */
//    private void saveECG() {
//        //we will write two files, one for index, one for the actual ECG
//        try {
//            // open files
//            ObjectOutputStream fosIndex = new ObjectOutputStream(new FileOutputStream("ECGIndex", true));
//            ObjectOutputStream fosGroups = new ObjectOutputStream(new FileOutputStream("ECGroups", true));
//            //write to files           
//            fosIndex.writeChar(Globals.OBJ_WRITE_SEP);
//            fosIndex.writeObject(this.ec);
//            fosIndex.writeChar(Globals.OBJ_WRITE_SEP);
//            fosIndex.writeObject(this.field);
//            fosIndex.writeChar(Globals.OBJ_WRITE_SEP);
//            fosIndex.writeInt(this.indexCount); //we are writing the value of indexCount at this time
//            fosGroups.writeChar(Globals.ECG_SEP);
//            fosGroups.writeInt(indexCount);
//            fosGroups.writeChar(Globals.ECG_SEP);
//            fosGroups.writeObject(this);
//            indexCount++;
//
//            fosIndex.flush();
//            fosGroups.flush();
//            fosIndex.close();
//            fosGroups.close();
//            Globals.print("finished saving!");
//        } catch (java.io.FileNotFoundException e) {
//            Globals.print(e.getMessage() + " fnfe saveIndex");
//        } catch (java.io.IOException e) {
//            Globals.print(e.getCause() + " ioe saveIndex");
//        }
//    }
//
//    /**
//     * returns #a#b#fieldType#p#
//     *
//     * @return
//     */
//    private String indexString() {
//        return (char) Globals.OBJ_WRITE_SEP + "" + this.ec.a + (char) Globals.OBJ_WRITE_SEP + "" + this.ec.b + (char) Globals.OBJ_WRITE_SEP
//                + "" + this.field.getFactory() + (char) Globals.OBJ_WRITE_SEP + "" + this.field.getMOrP() + (char) Globals.OBJ_WRITE_SEP + "";
//    }
    private ArrayList getElements() {
        return this.elementList;
    }

    @Override
    public String toString() {
        return "Group defined by the EC= " + ec.toString() + " over the FiniteField = " + field.toString() + " has " + groupSize + " points.";
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return elementList.size();
    }

    /**
     *
     * @return
     */
    public EllipticCurve getCurve() {
        return ec;
    }

    /**
     * WHAT IS THE PROPER WAY TO FORMAT THIS? calculates the double of a point P
     * (P+P=2P)
     *
     * comes from certicom ecc tutorial..
     *
     * @param p
     * @return
     */
    public ECPoint doubleP(ECPoint p) {
        if (p.isConstant() || p.getY().getVal().intValue() == 0) {
            return Globals.ECPT_AT_INF;
        }
        FieldElement sOne = ((p.getX().multiply(p.getX())).multiply(field.getElement(3))).add(this.ec.getA());
        FieldElement sTwo = p.getY().multiply(field.getElement(2));
        FieldElement s = sOne.divide(sTwo);
        FieldElement newX = s.multiply(s).subtract(p.getX().multiply(field.getElement(2)));
        return new ECPoint(newX, p.getY().negate().add(s.multiply(p.getX().subtract(newX))));
        /*if (field.fieldType == Globals.PRIME_P) {
         * if (p.getY().equals(BigInteger.ZERO)) {
         * return Globals.PT_AT_INF;
         * }
         * BigInteger sOne = (((p.getX().pow(2)).multiply(new BigInteger("3"))).add(this.ec.getA())).mod(field.getMOrP()); // 3*x^2+a mod p
         * //   Globals.print("sOne= "+sOne);
         * BigInteger sTwo = p.getY().multiply(new BigInteger("2")).mod(field.getMOrP()); //2 *y mod p
         * // Globals.print("sTwo= "+sTwo);
         * // now we need to have sOne * sTwo^-1 but in FFPrime we have a special definition for inverse. so p whole thing should probably be defined under FFPrime. but we'll soldier on...
         * BigInteger s = (sOne.multiply(inversionModP(sTwo, field.getMOrP()))).mod(field.getMOrP());
         * //      Globals.print(s);
         * BigInteger newX = (s.pow(2).subtract(p.getX().multiply(new BigInteger("2")))).mod(field.getMOrP());
         * BigInteger newY = (p.getY().negate().add(s.multiply(p.getX().subtract(newX)))).mod(field.getMOrP());
         * return new ECPoint(newX, newY);
         * } // will need to add a case for binary fields.*/
    }

    /**
     * calculates P+Q=R in the given FF this works... but only for prime
     * fields.. so is it in the right place?
     *
     * @param p
     * @param q
     * @return R
     */
    public ECPoint add(ECPoint p, ECPoint q) {
        if (p.equals(q)) {
            return doubleP(p);
        }
        if (p.equals(Globals.ECPT_AT_INF)) {
            return q;
        }
        if (q.equals(Globals.ECPT_AT_INF)) {
            return p;
        }
        FieldElement sY = p.getY().subtract(q.getY());
        FieldElement sX = p.getX().subtract(q.getX());
        Utilities.print("sX= " + sX + " sY= " + sY);
        if (sX.compareTo(field.getElement(0)) == 0) {
//            Globals.print("0 reached");
            Utilities.print("returning ptatinf: " + Globals.ECPT_AT_INF);
            return Globals.ECPT_AT_INF;
        }
        FieldElement s = sY.divide(sX);
        Utilities.print("sY/sX= " + s);
        FieldElement newX = (s.multiply(s)).subtract(p.getX()).subtract(q.getX());
        return new ECPoint(newX, (p.getY().negate().add(s.multiply(p.getX().subtract(newX)))));

        /*if (field.fieldType.equalsIgnoreCase(Globals.PRIME_P)) {
         //compute the slope, as per http://www.certicom.com/index.php/32-arithmetic-in-an-elliptic-curve-group-over-fp
         BigInteger sY = p.getY().subtract(q.getY()).mod(field.getMOrP());
         BigInteger sX = p.getX().subtract(q.getX()).mod(field.getMOrP());
         if (sX.equals(BigInteger.ZERO)) {// if x slope is 0 then we have a vertical line, so the result is the point at infinity
         return Globals.PT_AT_INF;
         }

         //   Globals.print("sy, sx "+sY+" "+sX);
         BigInteger s = (sY.mod(field.getMOrP()).multiply(inversionModP(sX.mod(field.getMOrP()), field.getMOrP())));
         //    Globals.print("s= "+s);
         BigInteger newX = (s.pow(2)).subtract(p.getX()).subtract(q.getX()).mod(field.getMOrP()); // x= s^2-px -qx mod p
         BigInteger newY = (p.getY().negate().add(s.multiply((p.getX().subtract(newX))))).mod(field.getMOrP());
         return new ECPoint(newX, newY);
         } // will need to add a case for binary fields.
         else {
         throw (new UnsupportedOperationException());
         }*/

    }

    /**
     * computes and returns the ECPoint Q = k*P Does not look at generator-ness
     * or anything liek that
     *
     * @param k integer which will be multiplying P by
     * @param p point which we'll find a multiple of
     * @return
     */
    public ECPoint multiple(int k, ECPoint p) {
        if (p.isConstant()) {
            return Globals.ECPT_AT_INF;
        }
        if (k < 0) { //if k is negative, then we will do -P * |k|
            ECPoint d = new ECPoint(p.getX(), p.getY().negate());
            return multiple((-1) * k, d);
        }
        if (k == 0) {
            return Globals.ECPT_AT_INF;
        }
        if (k == 2) {
            return doubleP(p);
        }
        if (k == 1) {
            return p;
        }
        if (k % 2 == 0) { //we keep p the same always...
            return this.add(this.doubleP(p), multiple(k - 2, p));
        } else {//k%2==1
            return this.add(p, multiple(k - 1, p));
        }
//        ECPoint Q = Globals.ECPT_AT_INF;
//        while (k > 0) {
//            Q = add(p, Q);
//            k--;
//        }
//        return Q;
    }

    /**
     * returns a string of each point in the group, separated by new lines
     *
     * @return
     */
    public String printPoints() {
        String toast = "";
        for (ECPoint pt : elementList) {
            toast += pt.toString() + "\n";
            // Globals.print(pt.toString());
        }
        return toast;
    }

    /**
     *
     * @return
     */
    public ArrayList<ECPoint> getElementList() {
        return elementList;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        FieldElementFactory fef = new FieldPFactory(Globals.bigInt(229));
        EllipticCurve testEC = new EllipticCurve(167, 135, fef);
        ECGroup testGroup = new ECGroup(testEC, fef);
        ECPoint alpha = testGroup.getElementAt(99);//new ECPoint(fef.getElement(5), fef.getElement(116));
        Utilities.print("alpha = " + alpha);
//        int n = 99;
        ECPoint beta = testGroup.multiple(1627, alpha);// new ECPoint(fef.getElement(155), fef.getElement(166));
        Utilities.print("264*alpha= " + beta);
        Utilities.print("alpha is of order "+Utilities.getPointOrder(testGroup,alpha));
        Utilities.print("solving DLP for alpha and beta");
        System.err.println("---------------");
//        Utilities.print("babyStep Giant Step returned with: " + Utilities.babyStepGiantStep(testGroup, alpha, beta));
        System.err.println("---------------");
        DLPSolver dlp= new PollardRho(testGroup,alpha,beta);
        Utilities.print("pollard Rho has returned with: " +dlp.solveDLP());




        //        Quiz.AdditionQuiz addTest= new Quiz.AdditionQuiz(3,testGroup);
//        Quiz.QuizView testQuiz= new QuizView(addTest,new Dimension(600,600));
//        JFrame test= new JFrame();
//        test.setVisible(true);
//        test.add(testQuiz);
//        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        for (int i = 0; i < 3; i++) {
//            System.out.println(addTest.getTheQuiz().getQuestions()[i].getQuestion());
//            System.out.println(addTest.getTheQuiz().getQuestions()[i].getAnswerAt(0));
//            System.out.println(addTest.getTheQuiz().getQuestions()[i].getCorrectAnswer());
//        }

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import GUIAttempt2.*;
import ECCToolBox.Globals;
import ECGroups.*;
import ECCToolBox.Utilities;
import Quiz.AdditionQuiz;
import Quiz.ECGQuiz;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ben
 */
public class ECGModel extends Observable implements Observer {

    /**
     * purely for testing, this creates an ECCFrame with a model.
     *
     * @param args
     */
    public static void main(String[] args) {
        ECCFrame ben = new ECCFrame();
    }
    private int a, b, p;
    double scale;
    /**
     *
     */
    public static final int GRAPH_PT_OFFSET = 4;
    private static final String NO_SELECTED_PTS = "Selected Points: \n";
    private ECGroup ecg;
//    private Dimension frameSize;
    private ArrayList<ECViewPoint> selectedPoints, points;
    private ECViewPoint result;
    private int zoom;
    private int minZoom;
    private int maxZoom;
    private FieldElementFactory field;
    //private ArrayList points;
    private ArrayList<String> selectedPointsText;
    private ECViewPoint currentMouseLoc;
    private int pSliderMax = 150;
    private int pSliderMin = 0;
    private int aBMax = 100;
    private int aBMin = -100;
    private ECGQuiz additionQuiz;
    private ECViewPoint currentHighlightPt;
    private boolean ECGHasChanged;
    private DLPSolver solver;
    private boolean dlpUsedPollard;
    private boolean solverIsUnseen;

    /**
     * creates an ECGModel given a dimension, which should be the dimension of
     * the frame which we'll be running in. starts with default values: Field =
     * f_29, EC= x^3+15x+4, zoom = -3
     *
     */
    public ECGModel() {
        super();
//        this.ecg=ecg;
        selectedPoints = new ArrayList<ECViewPoint>();
        selectedPointsText = new ArrayList<String>();
        selectedPointsText.add(NO_SELECTED_PTS);
        p = 9;
        a = 15;
        b = 4;
        points = new ArrayList();
        currentMouseLoc = new ECViewPoint(Globals.INF_STRING, p);
        this.setECG();
        this.setQuizzes();
    }
    
    public ECGModel(ECGroup ecg){
        super();
        this.ecg=ecg;
        selectedPoints = new ArrayList<ECViewPoint>();
        selectedPointsText = new ArrayList<String>();
        selectedPointsText.add(NO_SELECTED_PTS);
        p = 9;
        a = 15;
        b = 4;
        points = new ArrayList();
        currentMouseLoc = new ECViewPoint(Globals.INF_STRING, p);
        this.setQuizzes();
    }
    void setECG(ECGroup ecg){
        this.ecg= ecg;
    }
    ECGroup getECG(){
        return ecg;
    }

    //<editor-fold defaultstate="collapsed" desc="methods called by ControlPanel()">
    /* m
     * 
     */
    //</editor-fold>
    /**
     * sets the field a and notifies observer
     *
     * @param a new value of a
     */
    public void setA(int a) {
        this.a = a;
        this.setChanged();
        notifyObservers();
    }

    /**
     *
     * @param p
     * @param q
     * @return
     */
 /*   public int solveDLP(ECViewPoint p, ECViewPoint q) {
//        try {
//            p = this.getPointFromString("(5,116)");
//            q = this.getPointFromString("(155,166)");
//        } catch (PointNotFoundException pe) {
//            //do nothing, use original p,q
//        }
        DLPSolver solver = new PollardRho(this.ecg, convertECViewPointToECPoint(p), convertECViewPointToECPoint(q));
        int x = solver.solveDLP();
        ArrayList<String> ben = solver.getInfo();
        int resetCount = 0, tracker = 0;
        for (String s : ben) {
//            try {
//                if (tracker % 2 == 0) {
//                    getPointFromString(s).highlight(true);
//                    tracker++;
//                } else {
//                    getPointFromString(s).highlightYellow(true);
//                    tracker++;
//                }
//            } catch (PointNotFoundException pnfe) {
//                if (s.equalsIgnoreCase(Globals.RESET_POLLARD)) {
//                    resetCount++;
//                } else {
//                    System.err.println("error at point " + s + pnfe.getMessage());
//                }
//            }
//// ANIMATION DOES NOT WORK IN MODEL. IT SHOULD BE IN VIEW (?)
            //            //  at this point i can put in the wait() call, and then undo what i've done, and move on...
//            this.setChanged(); 
//            this.notifyObservers();
//            int sleeper=0;
//            while(sleeper<987654321){
//                sleeper++;
//            }
//            // for now, we'll reset all points to get back to it, but we could be more precise and only reset the points we changed
//            this.clearSelectedPoints();
//            System.err.println("reset points, did it animate?");
        }

        //TEMP DEBUG CODE:
//        System.out.println("DLP solution is: " + x + " and pollard reset " + resetCount + " times!");
        this.setChanged();
        this.notifyObservers();
        return x;
    }
*/
    /**
     * set's the field b and notifies observers
     *
     * @param b new value of b
     */
    public void setB(int b) {
        this.b = b;
        this.setChanged();
        notifyObservers();
    }

    /**
     * sets the p field to be a prime, which is found at the index of the given
     * parameter, and then takes steps to update everything else (ie changes the
     * zoomBounds...)
     *
     * probably shouldn't change that.
     *
     * @param i the index to pass to Globals.getPrimeAt();
     */
    public void setP(int i) {
        this.p = Globals.getPrimeAt(i);
        this.setChanged();
        notifyObservers();
    }

    /**allows direct access to the p field
     * 
     * @param prime MUST BE A PRIME NUMBER
     */
    public void setPFromPrime(int prime){
        this.p=prime;
        this.setChanged();
        this.notifyObservers();
    }
    /**
     * updates the ecg based on the given a, b, p if a value is 0 it doesn't
     * change it. does NOT update the ECG afterwards ,and does not notify any
     * observers of the change you must call updateECG() after this method.
     *
     * @param a for some reason, this cannot be 0
     * @param b for some reason, this cannot be 0
     * @param p for some reason, this cannot be 0. although that's a good thing.
     */
    public void updateNoChange(int a, int b, int p) {
//        if (a != 0) {
        this.a = a;
//        }
//        if (b != 0) {
        this.b = b;
//        }
        if (p != 0) {
            this.p = p;

        }
    }

    /**
     * updates the ECGroup based on the currently held values of a,b and p by
     * calling setECG(), and then takes steps to notify observers
     *
     */
    public void updateECGAndNotify() {
        this.setECG();
        this.ECGHasChanged = true;
//        this.setZoomBounds();
        this.setQuizzes();
        this.setChanged();
        this.notifyObservers();
        this.ECGHasChanged = false;
    }

    /**
     * gets a in the field P
     *
     * @return value held at a mod P
     */
    public int getA() {
        return a % getP_prime();// - this.getP_prime();
    }

    /**
     * returns the minimum intended value for the range of a,b- the coefficients
     * of our elliptic curve
     *
     * @return aBMin
     */
    public int getABMin() {
        int x = getABMax();
        return -1 * x;
    }

    /**
     * returns the maximum intended value for the range of a,b- the coefficients
     * of our elliptic curve
     *
     * @return aBMax
     */
    int getABMax() {
//        System.err.println("returning "+(this.getP_prime()-1)/2 );
        return (this.getP_prime());//-1)/2 +1;
    }

    /**
     * returns the intended minimum value for p. should be 0... (although its
     * not)
     *
     * @return pSliderMin
     */
    int getPSliderMin() {
        return pSliderMin;
    }

    /**
     * returns the intended maximum range for p. should be bound by valid
     * indices of primes
     *
     * @return pSliderMax
     */
    int getPSliderMax() {
        return pSliderMax;
    }

    /**
     * does what it says on the tin, modulo p
     *
     * @return current value of b within the field of P
     */
    public int getB() {
        return b % getP_prime();// -getP_prime();
    }

    /**
     * sets the ecg field to be a new ECGroup over a new finite field based on
     * the currently known values of a,b, and the prime at index p
     *
     * this needs to handle the ECG not being valid!
     *
     */
    public void setECG() {
        this.field = new FieldPFactory(Globals.bigInt(this.getP_prime()));
        this.ecg = new ECGroup(new EllipticCurve(field.getElement(a), field.getElement(b)), field);
        this.points = new ArrayList();
        int i = 0;
        //Globals.print(""+"returning a new list of points with p= " + p);
        for (ECPoint p : ecg.getElementList()) {
            points.add(convertECPointToECViewPoint(p));

        }
        points.add(new ECViewPoint(Globals.INF_STRING, this.getP_prime()));
        java.util.Collections.sort(points);
//        this.ECGHasChanged = true;
//        this.setChanged();
//        this.ECGHasChanged=false;
//        points.size();

    }

    /**
     * returns SpecCheck.getParamExplanation()
     *
     * @return SpecCheck.getParamExplanation(a,b,getP_Prime())
     */
    public AttributedString getECEquation() {
        return SpecCheck.getParamExplanation(a, b, getP_prime(), this.getGroupSize());
    }

    /**
     * I hope this works.
     *
     * @param P point to double
     * @return returns this.addPQ(P,P)
     */
    public ECViewPoint doubleP(ECViewPoint P) {
        return this.convertECPointToECViewPoint(ecg.doubleP(convertECViewPointToECPoint(P)));
    }

    /**
     * preferred, known working method to add two Points.
     *
     * @param P Point object to add
     * @param Q
     * @return the converted ECPoint returned from ECGroup.add(ECPoint p,q)
     */
    public ECViewPoint addPQ(ECViewPoint P, ECViewPoint Q) {
        ECPoint add = this.ecg.add(convertECViewPointToECPoint(P), convertECViewPointToECPoint(Q));
        return convertECPointToECViewPoint(add);
    }

    /**
     *
     * @return
     */
    public ECViewPoint getHighlightedPoint() {
        return currentHighlightPt;
    }

    /**
     *
     * @param p1
     */
    public void setHighlightedPoint(ECViewPoint p1) {

        try {
            currentHighlightPt.highlight(false);
        } catch (NullPointerException npe) {
        }
        try {
//        p1.highlight(true);
            currentHighlightPt = this.getPointFromString(p1.toString());
            currentHighlightPt.highlight(true);
        } //ensures that we can accept a copy/new point
        catch (PointNotFoundException pnfe) {
//        Utilities.print("we failed to find the point "+p1+" in the list of points in the current model.");
        }
    }

    ECViewPoint getPointFromString(String p1) throws PointNotFoundException {
        if (p1.equalsIgnoreCase(Globals.INF_STRING)) {
//        Utilities.print("we are returning the inf point (we think) = "+points.get(points.size()-1));
            return points.get(points.size() - 1);
        }
        for (ECViewPoint p : points) {
            if (p.toString().equalsIgnoreCase(p1)) {
                return p;
            }
        }
        //we shouldnt reach this point. what is wrong?
        throw new PointNotFoundException();
    }

    /**
     * used to add a point to itself a number of times (scalar multiplication)
     *
     * @param P a Point which will be converted to an ECPoint using
     * convertECPointToPoint(P)
     * @param k the scalar which we'll multiply it by
     * @return the point given by converting the ECPoint returned by
     * ECGroup.multiple(k,P)
     */
    public ECViewPoint scalarMultiply(ECViewPoint P, int k) {
        return convertECPointToECViewPoint(ecg.multiple(k, convertECViewPointToECPoint(P)));
    }

    /**
     * given an ECPoint it returns a Point
     *
     * @param ecp the ECPoint to convert
     * @return a Point with x,y given by ecp.getX().getVal().intValue(), same
     * for y (getVal() returns a Big Int)
     */
    ECViewPoint convertECPointToECViewPoint(ECPoint ecp) {
        if (ecp.isConstant()) {
            try {
                return this.getPointFromString(Globals.INF_STRING);
            } catch (PointNotFoundException ex) {
                Logger.getLogger(ECGModel.class.getName()).log(Level.SEVERE, null, ex);
                return new ECViewPoint(Globals.INF_STRING, getP_prime());
            }
        }
        try {
            return this.getPointFromString(new ECViewPoint(ecp.getX().getVal().intValue(), ecp.getY().getVal().intValue()).toString());
        } catch (PointNotFoundException ex) {
//            System.err.println("failed to find the converted ECPoint in our grup...");
            return new ECViewPoint(ecp.getX().getVal().intValue(), ecp.getY().getVal().intValue());
        }
    }

    /**
     * Converts a point to an ECPoint
     *
     * @param p Point object
     * @return ECPoint with x,y set from p.getX() and p.getY() as fieldElements
     * within the current field
     */
    ECPoint convertECViewPointToECPoint(ECViewPoint p) {
        if (p.isInfinity()) {
            return Globals.ECPT_AT_INF;
        }
        return new ECPoint(
                field.getElement(p.getX()),
                field.getElement(p.getY()));
    }

    /**
     * resets all the points to being unselected
     *
     */
    public void clearSelectedPoints() {
        for (ECViewPoint p : points) {
            p.reset();
        }
        selectedPoints.clear();
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * should probably be called addPointToSelectedPoints() or something, since
     * thats what it does.
     *
     * @param p a Point to add to the list of selected points
     */
    public void addSelectedPoint(ECViewPoint p) {
        selectedPoints.add(p);
    }
    //<editor-fold defaultstate="collapsed" desc="methods called by ECGraphPanel">
    /*
     *
     */
    //</editor-fold>

    /**
     * returns the field points, which is an arrayList
     *
     * @return List object
     */
    public List<ECViewPoint> getOrderedPoints() {
        return points;
    }

    /**
     * really just a wrapper for the Globals method Globals.getPrimeAt()
     *
     * @param p index of the prime we want
     * @return integer value of the p-th prime
     */
    public int getPrimeAt(int p) {
        return Globals.getPrimeAt(p);
    }

    /**
     * p, integer intended to be the index of the prime we want. ie p=3 implies
     * that we want to use the 3rd prime = 5; p=9 ==> 9th prime = 23
     *
     * @return index of the prime we want to use for our ECG
     */
    public int getP() {
        return p;
    }

    //</editor-fold>
    /**
     * given a button and an index for a point (from the sorted list of points)
     * it calls either leftClick() or rightClick() for the point at the index
     * given
     *
     * @param i potential issue- is arrayList(i) limited to [0,(length-1)]?
     * @param button 1 or 3, from the MouseEvent static fields.
     */
    public void clickedPt(int i, int button) {
        if (button == MouseEvent.BUTTON1) {//assuming mousebutton1 = left click
            points.get(i).leftClick(); //left click the point
//            this.setHighlightedPoint(points.get(i));

        }
        if (button == MouseEvent.BUTTON3) {//assuming mousebutton3 = right click
            points.get(i).rightClick();
        }
        if (button == MouseEvent.BUTTON2) {
            points.get(i).highlight(!points.get(i).isHighlighted());
        }
        //if its not selected, we select it.
        if (!points.get(i).isSelected()) {
            points.get(i).setSelected(true);
            selectedPoints.add(points.get(i));
        }
        // if the coefficient is 0 then we can deselect it. and since it's been clicked, we can unhighlight it ??
        if (points.get(i).getK() == 0) {
            points.get(i).setSelected(false);
            points.get(i).highlight(false);
            selectedPoints.remove(points.get(i));
        }
        // now, we'll update the sum, and highlight the result.
        this.setHighlightedPoint(this.getPointSum());
        this.setChanged();
        this.notifyObservers();
    }

    private ECViewPoint getPointSum() {
        if (selectedPoints.isEmpty()) {
            return new ECViewPoint(Globals.INF_STRING, this.getP_prime());
        }
        ECViewPoint sum = convertECPointToECViewPoint(Globals.ECPT_AT_INF);
        for (ECViewPoint p : points) {
            if (p.isSelected()) {
                sum = this.addPQ(scalarMultiply(p, p.getK()), sum);
            }
        }
        return sum;
    }

    /**
     * SHOULD RETURN A LIST OF POINTS, AND/OR A SUM OF THE LIST checks the
     * selectedPoints list and adds them up uses
     * {@link #addPQ(GUIAttempt2.Point, GUIAttempt2.Point) addPQ(p,sum)} with p
     * =
     * {@link #scalarMultiply(GUIAttempt2.Point, int) scalarMultiply(p, p.getK())}
     * and
     * {@link #convertECPointToECViewPoint(ECGroups.ECPoint) convertECPtToPt()}
     *
     * @return string representation of the Point which is the sum of the
     * elements of selectedPoints
     */
    String addPointsFromList() {
        if (selectedPoints.isEmpty()) {
            points.get(points.size() - 1).highlight(false);
            return "=(   0 ,   0 )";
        }
        ECViewPoint sum = convertECPointToECViewPoint(Globals.ECPT_AT_INF);
        for (ECViewPoint p : points) {
            if (p.isSelected()) {
                sum = this.addPQ(scalarMultiply(p, p.getK()), sum);
            }  
        }
        return "= " + sum.toString();
    }

    /**
     * estimates the number of columns that should be used when listing points
     *
     * @return 5 times the length of the string "x*(xxx,yyy)+ "
     */
    int getSelectedPointColumns() {
        return "x*(xxx,yyy)+ ".length() * 5;
    }

    /**
     * returns a constant value. should be changed to a static field.
     *
     * @return 10 (currently)
     */
    int getSelectedPointRows() {
        return 10;
    }

    /**
     * should use static variables instead of as written.
     *
     * @return string representation of the "selected points"
     */
    String getSelectedPointsText() {
        if (selectedPoints.isEmpty()) {
            // Globals.print("i dont see aynthing selected...");
            return "Click on points to find their sum.";
        }
        String ptsAsList = "";//Selected points are: \n";
        /// we know that selectedPoints is a list containing all the points we want to return..
        // so we'll iterate through and add the points...
        int i=0;
        for (ECViewPoint p : points) {
            if (p.getK() != 0) {
                ptsAsList += " " + p.getK() + "*" + this.convertECViewPointToECPoint(p).toString() + "+ ";
            }
        }
        return ptsAsList;
    }

    /**
     * simply calls {@link #getPrimeAt(int) getPrimeAt()} with the current value
     * of the p field
     *
     * @return see {@link #getPrimeAt(int) getPrimeAt()}
     */
    int getP_prime() {
        return this.getPrimeAt(p);
    }

    String[] getArrayOfPointStrings() {
        String[] toast = new String[points.size()];
        for (int i = 0; i < toast.length; i++) {
            toast[i] = points.get(i).toString();
        }
        return toast;
    }
    /**
     * This should be a Point[]
     */
    private String[] multiplesOfSelectedPoints = {"Select a point above to begin"};

    /**
     * This should set up a field for multiples of a point... or it doesnt even
     * need to be a field.
     *
     * @param i the index of the point in the list of points
     */
    void calculateMultiplesOfPointAtIndex(int i) {
        ECViewPoint p = points.get(i);
        ECViewPoint currPt;
        try {
            currPt = this.getPointFromString(Globals.INF_STRING);
        } catch (PointNotFoundException ex) {
            System.err.println("Failed to find ptatInf in the list of points..");
            currPt = new ECViewPoint(Globals.INF_STRING, getP_prime());
        }
        int mult = 1;
        ArrayList<String> pointMultipleStrings = new ArrayList();
        while (!currPt.toString().equalsIgnoreCase(p.toString())) { //if currPt==p then we have run around, and currPt = mult*P = P 
            mult++;
            currPt = this.scalarMultiply(p, mult);
            pointMultipleStrings.add(currPt.toString());
//            Globals.print(currPt.toString());
        }
        //now currPt = original point, and mult = order of the point
        multiplesOfSelectedPoints = new String[mult - 1];
        for (int j = 0; j < mult - 1; j++) {
            multiplesOfSelectedPoints[j] = (String) pointMultipleStrings.get(j).toString();
//            Utilities.print(multiplesOfSelectedPoints[j]);
        }
//        Globals.print(multiplesOfSelectedPoints);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * calculates the multiples of the point at the given index and returns an
     * array containing all of it's multiples
     *
     * @param i index of the point P in the list of sorted points
     * @return an array that begins with the point P and ends with (0,0)
     */
    ECViewPoint[] getGroupGeneratedBy(int i) {
        int order = this.getOrderOf(points.get(i));
        Utilities.print(points.get(i).toString()+" is of order "+order);
        ECViewPoint[] multiplesOfP = new ECViewPoint[order];
        for (int j = 0; j < order; j++) {
            multiplesOfP[j] = this.scalarMultiply(points.get(i), j + 1);
            System.out.println(multiplesOfP[j]);
        }
        return multiplesOfP;
//        ECViewPoint p = points.get(i);
//        ECViewPoint currPt = new ECViewPoint(Globals.INF_STRING, this.getP_prime());
//        int mult = 1;
//        ArrayList<ECViewPoint> multiplesOfP = new ArrayList();
//        multiplesOfP.add(p);
//        while (!currPt.toString().equalsIgnoreCase(p.toString())) { //if currPt==p then we have run around, and currPt = mult*P = P 
//            currPt.highlight(true);
//            mult++;
//            currPt = this.scalarMultiply(p, mult);
//            multiplesOfP.add(currPt);
////            Utilities.print(currPt.toString());
//        }
//        multiplesOfP.remove(multiplesOfP.size() - 1);
//        //now currPt = original point, and mult = order of the point
//        ECViewPoint[] multiplePoints = new ECViewPoint[multiplesOfP.size()];
//        for (int j = 0; j < multiplesOfP.size(); j++) {
//            multiplePoints[j] = multiplesOfP.get(j);
////            Globals.print(multiplesOfSelectedPoints[j]);
//        }
//        return multiplePoints;
    }

    /**this is used in the OrderOfPointView (and therefore not called in the final product)
     *
     * @return
     */
    String getMultiplesOfPointAsString() {
        String[] ben = this.multiplesOfSelectedPoints;
        if (ben.length == 1 && ben[0].equalsIgnoreCase("select a point above to begin")) {
            return ben[0];
        }
        String toast = "";
        for (int i = 0; i < ben.length; i++) {
            toast += (i + 2) + " " + ben[i];
            toast += " \n";
        }
        toast += " Thus the order of the point " + ben[ben.length - 1] + " is " + (ben.length) + "\n";
//        Globals.print("pts are: " + toast);
        return toast;
    }

    ECViewPoint getPointAt(int y) {
        return points.get(y);
    }

    ECGQuiz getQuiz(int type) {
        if (type == ECGQuiz.ADDITION_QUIZ) {
            return additionQuiz;
        } else {
            return null;
        }
    }

    private void setQuizzes() {
        additionQuiz = new AdditionQuiz(5, ecg);       
        Utilities.print("set additionQuiz to a new quiz..");
//        System.err.println("Quiz P = " + additionQuiz.getP()+" "+additionQuiz.getQuestionAt(0).getQuestionText());
    }
//    private Point currentHighlightedPoint;
//    private algorit

    @Override
    public void update(Observable o, Object arg) {
//            currentHighlightedPoint=algorithm.getActivePoint();
    }

    int getGroupSize() {
        return ecg.getSize() + 1; // +1 for the ptatInf.. which i should have included in the ecg
    }

    boolean ECGHasChanged() {
        return ECGHasChanged;
    }

    int getOrderOf(ECViewPoint point) {
        return Utilities.getPointOrder(ecg, convertECViewPointToECPoint(point));
    }

    ECViewPoint[] getGroupGeneratedBy(ECViewPoint p) {
        int i = points.indexOf(p);
        return this.getGroupGeneratedBy(i);
    }

    void getDLPSolver(ECViewPoint p, ECViewPoint q, int type) {

        if (type == Globals.POLLARD) {
            solver = new PollardRho(this.ecg, convertECViewPointToECPoint(p), convertECViewPointToECPoint(q));
            dlpUsedPollard = true;
        } else {// if(type==Globals.BSGS){
            solver = new BabyStepGiantStep(ecg, convertECViewPointToECPoint(p), convertECViewPointToECPoint(q));
            dlpUsedPollard = false;
        }
        solver.solveDLP();
//        System.err.println("DLPSolver has finished running with result "+solver.getStoredK());
        //        ArrayList<String> ben = solver.getInfo();
        //TEMP DEBUG CODE:
//        System.out.println("DLP solution is: "+x + " and pollard reset "+resetCount+" times!");
        this.solverIsUnseen = true;
        this.setChanged();
        this.notifyObservers();
//        return x;

    }

    /**
     * could return null..
     *
     * @return possibly null
     */
    DLPSolver getSolver() {
        return solver;
    }

    boolean DLPUsedPollard() {
        return dlpUsedPollard;
    }

    ECViewPoint[] getDLPPoints() {
        if (dlpUsedPollard) {
            //now i need to look at the solverlogs to determine which points were tocuhed and transmit them in order...
            ArrayList<ECViewPoint> logPts = new ArrayList<ECViewPoint>();
            for (String s : solver.getInfo()) {
                if (s.startsWith("X=") || s.startsWith("X'=")) {
                    try {
                        //then we have a point...
//                        System.out.println(s);
                        logPts.add(
                                // tries to find the point described by extracting the substring between ( and )
                                this.getPointFromString(s.substring(s.indexOf('=') + 1))//s.contains("infinity")?Globals.INF_STRING:s.substring(s.indexOf('('),s.indexOf(')')))
                                );
                    } catch (PointNotFoundException ex) {
                        System.err.println("UNable to animate points. failed to find the point " + s + " with substring " + s.substring(s.indexOf('=') + 1));
                    }
                }
            }
            return logPts.toArray(new ECViewPoint[logPts.size()]);
        }
        ECViewPoint[] temp = new ECViewPoint[5];
        return temp;
    }

    void setDLPLogSeen(boolean b) {
        this.solverIsUnseen = b;
        if (!solverIsUnseen) {
            dlpUsedPollard = false;
            // this should reset solver info....
        }
    }

  
    PollardModel getPollardModel() {
        return new PollardModel(this,(PollardRho)solver);
    }
}

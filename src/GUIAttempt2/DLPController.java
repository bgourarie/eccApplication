/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import ECGroups.DLPSolver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author benbenbenultra
 */
class DLPController implements MouseListener, Observer, ActionListener, ChangeListener {

    private ECGModel mod;
    private DLPView view;
    private ArrayList<ECViewPoint> points;
    private ECViewPoint p, q;
    private int currentDLPSolution;
    private boolean qChosen;
    private String[] dlpLog;
    private boolean dlpIsSolved;
    private boolean pIsChosen;
    private boolean fetchSolver;
    private boolean dlpUsedPollard;
    private int numberOfPoints;

    public DLPController(ECGModel mod, DLPView view) {
        this.mod = mod;
        mod.addObserver(this);
        this.view = view;
        mod.addObserver(this);
        populatePoints();
    }

    private void populatePoints() {
        int groupSize = mod.getGroupSize();
        ArrayList<ECViewPoint> temp = new ArrayList<ECViewPoint>();
        numberOfPoints = groupSize > 50 ? 10 : groupSize / 3;
        int[] indices = Utilities.getRandomizedArrayIndices(mod.getGroupSize());
//        int[] orders = new int[indices.length];
        int i = 0;
        while (temp.size() < numberOfPoints) {
            if (!temp.contains(mod.getPointAt(indices[i]))) {
                // if we haven't already found this point, we add it.
                temp.add(mod.getPointAt(indices[i]));
            }
            i++;
        }
        // now i need a way to sort them by size.... since it will be at most ten, i think I can do this a long way.. but maybe not.. 
        //for now.. i will write a quick mergesort private method, although it could be elsewhere probably.
//        String comp1 = "temp points are: ", comp2 = "sorted points are: ";
//        for (ECViewPoint p : temp) {
//            comp1 += " " + p.toString();
//        }
        points = mergeSortByOrder(temp);
//        for (ECViewPoint p : points) {
//            comp2 += " " + p.toString();
//        }
//        System.err.println(comp1);
//        System.err.println(comp2);
    }

    private ArrayList<ECViewPoint> mergeSortByOrder(ArrayList<ECViewPoint> pts) {
        if (pts.size() <= 1) {
            return pts;
        } else {
            ArrayList<ECViewPoint> l = new ArrayList<ECViewPoint>(), r = new ArrayList<ECViewPoint>();
            int mid = pts.size() / 2;
            for (int i = 0; i < mid; i++) {
                l.add(pts.get(i));
                r.add(pts.get(i + mid)); //don't think that will throw any errors...
            }
            if (pts.size() % 2 == 1) {//if pts was odd, then we skipped the last element...
                // because 7/2 = 3.5 = 3; 3+3 = 6 which is final element but we never get up there...
                // we can't go all the way there because 8/2 = 4, so i+mid reaches 7, which is final element.
                r.add(pts.get(mid + mid));
            }
            l = mergeSortByOrder(l);
            r = mergeSortByOrder(r);
            return mergeByOrder(l, r);
        }
    }

    private ArrayList<ECViewPoint> mergeByOrder(ArrayList<ECViewPoint> l, ArrayList<ECViewPoint> r) {
//        if (l.size() == 1 || r.size() == 1) {
//            String e = "Sorting l/r=1 with: ";
//            e += l.size() == 1 ? l.get(0).toString() : " no l ";
//            e += r.size() == 1 ? r.get(0).toString() : " no r ";
//            System.err.println(e);
//        }
        ArrayList<ECViewPoint> merged = new ArrayList<ECViewPoint>();
        while (l.size() > 0 || r.size() > 0) {
            if (l.size() > 0 && r.size() > 0) {
                if (mod.getOrderOf(l.get(0)) < mod.getOrderOf(r.get(0))) {
                    merged.add(l.remove(0));
                } else {
                    merged.add(r.remove(0));
                }
            } else {
                if (l.size() > 0) {
                    merged.add(l.remove(0));
                } else { // if (r.size() > 0) {
                    merged.add(r.remove(0));
                }
            }
        }
        return merged;
    }

    int getNumberOfPoints() {
        return this.points.size();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.dlpIsSolved = false;//dlpIsSolved=false;
        this.qChosen = false;
        view.mouseClickAt(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (mod.ECGHasChanged()) {
//            System.err.println("ecg changed??");
            this.dlpIsSolved = false;
            this.dlpUsedPollard = false;
            this.fetchSolver = false;
            this.pIsChosen = false;
            this.populatePoints();
            view.updateComponents();
        }
        if (this.fetchSolver) {
//            System.err.println("");
            DLPSolver solver = mod.getSolver();
            if (this.dlpUsedPollard) {
                PollardViewFrame b = new PollardViewFrame(mod.getPollardModel());
                this.fetchSolver = false;
            } else {
                this.dlpIsSolved = true;
                this.currentDLPSolution = solver.getStoredK();
                dlpLog = new String[solver.getInfo().size()];
                int i = 0;
                for (String s : solver.getInfo()) {
                    dlpLog[i] = "" + s;
                    i++;
//                    System.err.println(s);
                }
            }
            view.DLPSolved();
        }

    }

    ECViewPoint getPoint(int i) {
        return points.get(i);

    }

    int getDLPSolution() {
        return currentDLPSolution;
    }

    ECViewPoint getRandomPointGeneratedBy(ECViewPoint p) {
//     this is nicer but has too much overhead on larger groups...
//     ECViewPoint[] gen = mod.getGroupGeneratedBy(p);
//        ArrayList<ECViewPoint> temp = new ArrayList();
//        temp.addAll(Arrays.asList(gen));
//        java.util.Collections.shuffle(temp);
//        System.out.println("returning "+temp.get(0));

        ECViewPoint q = mod.scalarMultiply(p, (int) (Math.random() * mod.getGroupSize()));
        System.out.println("p = " + p + " q = " + q);
        if (q.compareTo(p) == 0) { //don't want to return p... or the pt at infinity.. but we have to allow ptAtInf..
            return getRandomPointGeneratedBy(p);
        } else {
            return q;
        }
//        return temp.get(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (this.pIsChosen) {
            if (e.getActionCommand().equalsIgnoreCase("q")) {
                this.setQRandom();
                view.updateComponents();
//                mod.setHighlightedPoint(p);
            } else if (e.getActionCommand().equalsIgnoreCase("" + Globals.POLLARD)
                    || e.getActionCommand().equalsIgnoreCase("" + Globals.BSGS)) {
//                System.err.println("attempting dlp solve using " + e.getActionCommand());
                if (!this.qChosen) {
                    this.setQRandom();
                }

                this.dlpUsedPollard = e.getActionCommand().equalsIgnoreCase("" + Globals.POLLARD);
                this.fetchSolver = true;
                mod.getDLPSolver(p, q, Integer.parseInt((e.getActionCommand())));
            }
        }

    }

    String getP() {
        return p.toString();
    }

    int getPOrder() {
        return mod.getOrderOf(p);
    }

    boolean isQChosen() {
        return qChosen;
    }

    String getQ() {
        if (!qChosen) {
            return "";
        }
        return q.toString();
    }

    String[] getDLPLog() {
        return dlpLog;
    }

    boolean isDLPSolved() {
        return dlpIsSolved;
    }

    void setP(ECViewPoint p) {
        this.p = p;
        if(qChosen){
            qChosen=false;
            q.highlightYellow(false);
            q.highlight(false);
        }
        if (view.isVisible()) {
            p.highlightPurple(true);
        }
        view.getRootPane().invalidate();
        view.getRootPane().repaint();
//        mod.setHighlightedPoint(p);
        this.pIsChosen = true;
    }

    private void setQRandom() {
        if (qChosen) { //if it's already chosen...
            q.highlightYellow(false);
            q.highlight(false);
        }
        if (pIsChosen) {
            this.qChosen = true;
            q = this.getRandomPointGeneratedBy(p);
            if (view.isVisible()) {
                q.highlightYellow(true);
                q.highlight(true);
            }
            view.getRootPane().invalidate();
            view.getRootPane().repaint();
//            mod.setHighlightedPoint(q);
            dlpIsSolved = false;
        }
    }

    boolean isPChosen() {
        return this.pIsChosen;
    }

    int[] getPossibleOrders() {
        int[] ords = new int[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            ords[i] = mod.getOrderOf(points.get(i));
        }
        return ords;
    }

    boolean dlpUsedPollard() {
        return dlpUsedPollard;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider pts = (JSlider) e.getSource();
        if (!pts.getValueIsAdjusting()) {
            this.dlpIsSolved = false;//dlpIsSolved=false;
            if (qChosen) {
                q.highlightYellow(false);
                q.highlight(false);
//                System.err.println("");
            }
            if (this.pIsChosen) {
                p.highlightPurple(false);
            }
            pIsChosen = false; // think this should be done, although its not really necessary since momentarily it will be reset.
            this.qChosen = false;
            view.handlePointSelection(points.get(pts.getValue()));
//            System.err.println("handled point selection. rather nicely in fact. ");
        }
    }
}

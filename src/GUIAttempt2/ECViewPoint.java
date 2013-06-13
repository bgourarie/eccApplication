/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

//import GUIAttempt1.*;
import ECCToolBox.Globals;
import java.awt.Color;

/**
 *
 * @author ben
 */
public class ECViewPoint implements Comparable {

    private int x, y;
    private boolean selected;
    private int k;
    /**
     *
     */
//    public static ECViewPoint NOT_A_POINT = new ECViewPoint(-124, -24);
    private boolean isInfinity = false;
    private boolean highlighted;
    private boolean highlightYellow;
    private boolean highlightPurple;

    /**
     *
     * @param x
     * @param y
     */
    public ECViewPoint(int x, int y) {
        this.x = x;
        this.y = y;
        selected = false;
        k = 0;
        highlighted = false;
        highlightYellow = false;
    }

    ECViewPoint(double x, double y) {
        this((int) x, (int) y);
    }

    ECViewPoint(String t, int p) {
        if (t.equalsIgnoreCase(Globals.INF_STRING)||t.equalsIgnoreCase("PtAtInf")) {
            isInfinity = true;
            x = p;
            y = p;
        } else {
            int comma = t.indexOf(",");
            x = Integer.parseInt(t.substring(
                    t.indexOf("(") + 1,
                    comma ));
            y=Integer.parseInt(t.substring(comma+1,t.length()-1));
            selected = false;
        k = 0;
        highlighted = false;
        highlightYellow = false;
        }
    }

    /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     *
     * @return
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param t
     */
    public void highlight(boolean t) {
        highlighted = t;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return
     */
    public double norm() {
        return (x + y) * (x - y);
    }

    /**
     *
     * @param b
     */
    public void setSelected(boolean b) {
        selected = b;
    }

    /**
     *
     */
    public void cycleSelected() {
        selected = !selected;
    }

    /**
     * in response to a left click, a point will change colors through a list of
     * colors, although it will only work 10x
     *
     */
    public void leftClick() {
        k++;
    }

    /**
     *
     */
    public void rightClick() {
        k--;

    }

    @Override
    public int compareTo(Object o) {

        ECViewPoint b = (ECViewPoint) o;
        if (this.isInfinity()) {
            return 2;
        }
        if (b.isInfinity) {
            return -2;
        }
        double c = this.norm() - b.norm();
        if (c > 0) {// this is bigger than b
            return 1;
        } else if (c < 0) { // this is smaller than
            return -1;
        } else {//c==0
            return 0;
        }
    }

    @Override
    public String toString() {
//        if (this.equals(NOT_A_POINT)) {
//            return "No point";
//        }
        if (this.isInfinity()) {
            return "PtAtInf";
        }
        return "(" + this.x + "," + this.y + ")";
    }

    Color getColor() {
        if(highlightYellow){
            return Color.ORANGE;
        }
        if(highlightPurple){
            return Color.MAGENTA;
        }
        else if (highlighted) {
            return Globals.getHighlightColor();
        }
        if (k == 0) {
            return Color.BLACK;
        }
        if (k < 0) {
            return Color.RED;
        } else {
            return Color.BLUE;
        }
    }

    /**
     * resets color to 0
     *
     */
    void reset() {
        this.k = 0;
        this.highlighted = false;
        this.selected = false;
        this.highlightPurple=false;
        this.highlightYellow = false;
    }

    int getK() {
        return this.k;
    }

    void highlightPurple(boolean b){
        highlightPurple=b;this.highlighted=b;
        
    }
    boolean highlightPurple(){
        return highlightPurple;
    }
    boolean isInfinity() {
        return isInfinity;
    }

    void highlightYellow(boolean b) {
        this.highlightYellow = b;
    }

    boolean isCrossed() {
        return highlightYellow;
    }
}

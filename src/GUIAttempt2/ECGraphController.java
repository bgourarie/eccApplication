/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ben
 */
public class ECGraphController implements Observer, ChangeListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener{//, KeyListener {

    private ECViewPoint currentMouseLoc;
    private double scale, zoom;
    private ECGModel mod;
    private ECGraphView view;
    private int maxZoom;
    private int minZoom;
    private boolean scrollToZoom;

    /**
     *
     * @param mod
     * @param view
     */
    public ECGraphController(ECGModel mod, ECGraphView view) {
        this.mod = mod;
        mod.addObserver(this);
        this.view = view;
    }

    /**
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        view.requestFocusInWindow();
        //if the click was in a range near the point, then let's lookat the point.
        int xClick = e.getX(), yClick = e.getY();
//        Utilities.print(""+xClick+" , "+yClick);
        for (int i = 0; i < view.getClickAreas().size(); i++) {
            Shape r = view.getClickAreas().get(i);
            if (r.contains(xClick, yClick)) {
//                Utilities.print(""+e.getButton()+" mouse button was clicked at "+i);
                mod.clickedPt(i, e.getButton());//(i).leftClick();
                view.repaint();
                i = view.getClickAreas().size() + 1;
            }
        }
    }
//  private double startX, startY, endX, endY;
//    private boolean inDrag;
//    private int curX;
//    private int curY;

    /**
     * Called when the mouse has been pressed.
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {
//    java.awt.Point p = e.getPoint();
//  //  System.err.println("mousePressed at " + p);
//    startX = p.x;
//    startY = p.y;
//    inDrag = true;
    }

    /**
     * Called when the mouse has been released.
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
//          java.awt.Point p = e.getPoint();
//    curX = p.x;
//    curY = p.y;
//    if (inDrag) {
//       // System.err.println("moving?");
//      //this.getVisibleRect().setLocation(curX, curY);
//      this.setLocation(curX,curY);
//    }
//      inDrag = false;
//    
        // System.err.println("SELECTION IS " + startX + "," + startY + " to "+ curX + "," + curY);
    }

    // And two methods from MouseMotionListener:
    public void mouseDragged(MouseEvent e) {
//    java.awt.Point p = e.getPoint();
//    // System.err.println("mouse drag to " + p);
//    Globals.print("mouse Dragged to " + p);
//    curX = p.x;
//    curY = p.y;
//    if (inDrag) {
//       // System.err.println("moving?");
//      //this.getVisibleRect().setLocation(curX, curY);
//      this.setLocation(curX,curY);
//    }
    }

    private boolean mouseInGraph;
    @Override
    public void mouseEntered(MouseEvent e) {
        mouseInGraph= true;
        //   this.setToolTipText("mouse at +_x="+e.getX()+" or onScreen: "+e.getXOnScreen());
        // this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
        mouseInGraph=false;
    }

    @Override
    public void mouseMoved(MouseEvent me) {
//        this.mouseOverPoint(convertAWTPointToPoint(me.getPoint()));
        ECViewPoint p = view.convertAWTPointToPoint(me);

        int xLoc = me.getX(), yLoc = me.getY();
//        Utilities.print(""+xClick+" , "+yClick);
        for (int i = 0; i < view.getClickAreas().size(); i++) {
            Shape r = view.getClickAreas().get(i);
            if (r.contains(xLoc, yLoc)) {
//                if i can figure out who owns the cursor right now...
                view.changeCursor(true);
                view.setCurrentMouseOverPoint(true,r.getBounds().getLocation());
                break;
//                System.err.println("mouseOver point!");
            } else {
                view.changeCursor(false);
                
            }
        }
        view.setMouseOver(p);
//        Globals.print("mouse over "+me);
    }

    @Override
    public void update(Observable o, Object arg) {
        // lets use this to animate the rho method
//        if(mod.DLPUsedPollard()){
//            ECViewPoint[] touchedPoints = mod.getDLPPoints();
////            System.err.println("graphCtrl sees the log as: ");
//            for(int i= 0; i<touchedPoints.length; i+=2){
////                System.err.print(" "+touchedPoints[i].toString());
//                view.circlePoint(touchedPoints[i]);
//                view.squarePoint(touchedPoints[i+1]);
//                view.drawCircleAndSquare(true);
//                
//                Thread t = new Thread();
//                try {
//                    t.sleep(1500);
//                } catch (InterruptedException ex) {
//                    System.err.println("how did i get interrupted!? on the "+ i+"th iteration out of "+ touchedPoints.length);
//                }
//                
//            }
//            //let mod know we're done with the dlpsolver...
//            mod.setDLPLogSeen(false);
////             need to add in a timer, and then at the end i can uncomment the following:
//            view.drawCircleAndSquare(false);
//            
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("clearPoints")) {
            mod.clearSelectedPoints();
        } else if (e.getActionCommand().equalsIgnoreCase("toggleLabels")) {
            view.toggleLabels();
//            System.err.println("got an action event, assuming it's label toggle time.");
        } else if (e.getActionCommand().equalsIgnoreCase("fullScreen")) {
            view.setGraphFullScreen();
        } else if (e.getActionCommand().equalsIgnoreCase("useGridLines")) {
            view.toggleGridLines();
        } else if (e.getActionCommand().equalsIgnoreCase("labelLines")) {
            view.toggleLineLabels();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        view.updateZoom();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(scrollToZoom){
            view.zoomFromScroll(-1*e.getUnitsToScroll(),e.getPoint());
            if(mouseInGraph){
                
            }
        }
        
    }


    /**
     *
     * @param b
     */
    public void setScrollToZoom(boolean b) {
        this.scrollToZoom=b;
    }
}

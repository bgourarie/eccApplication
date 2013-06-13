/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Utilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

/**
 *
 * @author benbenbenultra
 */
    public class ECGraphPanel extends JPanel implements Observer {
//            MouseListener, MouseMotionListener {

        private ECGModel mod;
        private List<Shape> clickAreas;
        private Line2D[] verticalLines, horizontalLines;
        private double scale;
        private float shift, buffer;
        private int graphPtSize;
        private int minZoom;
        private int maxZoom;
        private double zoom;
        private boolean useLabels, zoomChanged, lineLabels;
        private boolean drawGridLines;
        private Dimension scrollSize;
        private Rectangle2D squarePt;
        private Ellipse2D circlePt;
        private boolean drawCircleAndSquare;

        /**
         *
         * @param mod n
         */
        public ECGraphPanel(ECGModel mod) {
            this(mod, new Dimension(600, 600));
            Utilities.print("created default graphview with 600x600");
        }

        public ECGraphPanel(ECGModel mod, Dimension d) {
            this.mod = mod;
            scrollSize = d;
            useLabels = true;
            lineLabels = false;//true;
            drawGridLines = true;
            this.setSize(d);
            mod.addObserver(this);//??? 
            clickAreas = new ArrayList<Shape>();
//            zoom = 100;
            zoomChanged = false;
            maxZoom = 200;//mod.getP_prime();
            minZoom = 50;
            scrollSize = d;

            shift = (float) (10);
            buffer = 25;// this value will be used solely to ensure that the rightmost edge of the graphpanel extends far enough for the labels to be visible...
            zoomFullScreen();
            this.setScale(); //sets scale relative to zoom...
            this.doUpdate();
            this.setVisible(true);
        }

        /**
         * will attempt to read in the arg as a list of points, will catch any
         * errors and try to print them requires that list is pre-sorted!
         *
         * @param o
         * @param arg
         */
        @Override
        public void update(Observable o, Object arg) {
            doUpdate();
        }
        private static final int CLICK_ELLIPSE = 10, POINT_ELLIPSE = 11, HIGHLIGHT_ELLIPSE = 12, LABEL_ELLIPSE = 13, CROSS_ELLIPSE = 14;

        private Ellipse2D getEllipseOfType(int ellipseType, ECViewPoint p1) {

            double r = 0;
            Ellipse2D toast;
            if (ellipseType == LABEL_ELLIPSE) {
                r = scale / 2;
                if (p1.isInfinity()) {
                    r = 9 * scale / 16;
                }
            } else if (ellipseType == CLICK_ELLIPSE) {
                // if it's negative the point wil lbe small,
                // and we want to keep the same (original) size area to click...
                if (p1.getK() < 0) {
                    r = (scale / 4);
                } else {
                    // but if it's not negative, we just use the size
                    // as drawn.
                    return getEllipseOfType(POINT_ELLIPSE, p1);
                }
            } else if (ellipseType == POINT_ELLIPSE) {
                r = (scale / 4) + (p1.getK() >= 0 ? p1.getK() : (p1.getK() / 2));
                if (r < 4) {
                    r = 4;
                }
            } else if (ellipseType == HIGHLIGHT_ELLIPSE) {
                r = (scale / 4) + p1.getK() / 2;
                r =r<4?4: r*2;
            }
            double xCoord, yCoord;
            if (p1.isInfinity()) {
                xCoord = verticalLines[verticalLines.length - 1].getX1() + scale - r;
                yCoord = horizontalLines[horizontalLines.length - 1].getY1() - scale - r;
            } else {
                xCoord = verticalLines[p1.getX()].getX1() - r;
                yCoord = horizontalLines[p1.getY()].getY1() - r; // removed  from inside [ horizontal... ]:  - (p1.getY() + 1)
            }
            Ellipse2D e = new Ellipse2D.Double(
                    xCoord,
                    yCoord,
                    2 * r + 1,
                    2 * r + 1);
            return e;
        }

        private Ellipse2D getClickAreaEllipse(ECViewPoint p1) {
            return getEllipseOfType(CLICK_ELLIPSE, p1);
        }

        private Ellipse2D getPointEllipse(ECViewPoint p1) {
            return getEllipseOfType(POINT_ELLIPSE, p1);
        }

         Ellipse2D getHighlightEllipse(ECViewPoint p1) {
            return getEllipseOfType(HIGHLIGHT_ELLIPSE, p1);
        }

        private Line2D[] getHighlightCross(ECViewPoint p1) {
            Line2D[] cross = new Line2D[2];// a cross has  a vertical and horizontal line
            Ellipse2D temp = getHighlightEllipse(p1);
            // we get x,y coordinates such that x1,y1 is at the same y but 
            //x is moved over the width of the circle, and x2y2 is similar but for y
            double c1x = temp.getCenterX() - temp.getWidth(),
                    c1y = temp.getCenterY(),//- temp.getWidth()/2,
                    c2x = temp.getCenterX(),//+temp.getWidth()/2,
                    c2y = temp.getCenterY() + temp.getHeight();
            cross[0] = new Line2D.Double(c1x, c1y, c1x + (2 * temp.getWidth()), c1y);
            cross[1] = new Line2D.Double(c2x, c2y, c2x, c2y - (2 * temp.getHeight()));
            return cross;
        }

        /**
         *
         *
         * @param pts
         */
        private void setClickablePoints() {
            clickAreas = new ArrayList<Shape>();
            int i = 0;
            for (ECViewPoint p : mod.getOrderedPoints()) {
                clickAreas.add(i, getClickAreaEllipse(p));
                i++;
            }
        }

        List<Shape> getClickAreas() {
            return clickAreas;
        }

        private double scaleValue(int x) {
            return x * scale;
        }

        /**
         * creates the lines and stores them in pairs, horizontal+vertical. is
         * not efficient!
         */
        private void setGridLines() {
            verticalLines = new Line2D.Float[mod.getP_prime()];
            horizontalLines = new Line2D.Float[mod.getP_prime()];
            int p = verticalLines.length;
            for (int i = 0; i < p; i++) {
                verticalLines[i] = new Line2D.Float(
                        (float) scale * i + getShift(),
                        (float) flipY(getShift()), //this flips it so that a point at 0+getShift() will be at Height-getShift()-0...
                        (float) scale * i + getShift(),
                        (float) flipY((p - 1) * scale + getShift()));//mod.getGraphHeight()+getShift());
                horizontalLines[i] = new Line2D.Float(
                        (float) 0 + getShift(),
                        (float) flipY(scale * i + getShift()),
                        (float) ((p - 1) * scale + getShift()),
                        (float) flipY(scale * i + getShift()));//(int) mod.getScale() * i+getShift());
            }
        }

        public Line2D[] getxLines() {
            return verticalLines;
        }

        public void setxLines(Line2D[] xLines) {
            this.verticalLines = xLines;
        }

        public Line2D[] getyLines() {
            return horizontalLines;
        }

        public void setyLines(Line2D[] yLines) {
            this.horizontalLines = yLines;
        }

        public double getScale() {
            return scale;
        }

        public float getShift() {
            return shift;
        }

        public void setShift(float shift) {
            this.shift = shift;
        }

        @Override
        public void setPreferredSize(Dimension d) {
            // we want to always be a square and we will go with the smaller of the 
            double w = d.getHeight() < d.getWidth() ? d.getHeight() : d.getWidth();
            d.setSize(w, w);
            super.setPreferredSize(d);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D d = (Graphics2D) g;
            d.setColor(Color.LIGHT_GRAY);

            for (int i = 0; i < verticalLines.length; i++) {
                if (drawGridLines) {
                    d.draw(verticalLines[i]);
                    d.draw(horizontalLines[i]);
                }
                if (lineLabels) {
                    d.setColor(Color.red);
                    d.drawString("" + i, (float) verticalLines[i].getX1(), (float) (verticalLines[i].getY1() + 15));
                    d.drawString("" + i, (float) (horizontalLines[i].getX1() - 10), (float) horizontalLines[i].getY1());
                    d.setColor(Color.LIGHT_GRAY);
                }
            }
//            DEBUG CODE:
//            d.setColor(Color.GREEN);
//            for (Shape s : clickAreas) {
//                d.draw(s);
//            }
            
            // we go through once to draw each point
            d.setColor(Color.black);
            for(ECViewPoint p:mod.getOrderedPoints()){
                  d.setColor(p.getColor());
                d.fill(getPointEllipse(p));
            }
            
            // now we go again and draw specific aspects, so that they don't get covered up by the other points
            
            for (ECViewPoint p : mod.getOrderedPoints()) {
                d.setColor(p.getColor());
                if(p.getK()!=0){
                    d.fill(getPointEllipse(p));
                }
                if (p.getK() <= -2) {
                    d.draw(getHighlightEllipse(p));
                }
                if (p.isHighlighted()) {
                    d.draw(getHighlightEllipse(p));
                }
                
                // draw a label for each point...
                if (useLabels) {
                    d.setColor(Color.GRAY);
                    if (!p.isInfinity()) {
                        d.drawString(
                                p.toString(),
                                (float) getEllipseOfType(LABEL_ELLIPSE, p).getMaxX(),
                                (float) getEllipseOfType(LABEL_ELLIPSE, p).getMaxY());
                    } else {
                        d.drawString(
                                p.toString(),
                                (float) getEllipseOfType(LABEL_ELLIPSE, p).getMinX(),
                                (float) getEllipseOfType(LABEL_ELLIPSE, p).getMaxY()+5);
                    }
                }
            }
//            if (drawCircleAndSquare) {
//                d.setColor(Color.GREEN);
//                d.fill(squarePt);
//                d.fill(circlePt);
//                d.setColor(Color.black);
//                d.draw(squarePt);
//                d.draw(circlePt);
//            }
        }

        public ECViewPoint getClosestGridIntersection(MouseEvent me) {
            double tempV, tempH, vDist = scale + 100, hDist = scale + 100, pX = me.getX(), pY = me.getY();
            double y = 0, x = 0;
            for (int i = 0; i < verticalLines.length; i++) {
                tempV = verticalLines[i].ptSegDist(pX, pY);
                tempH = horizontalLines[i].ptSegDist(pX, pY);
                // if the current line is closer to the point then the previous checked points,
                if (tempV < vDist) {
                    vDist = tempV + 0;
                    x = i;
                }
                if (tempH < hDist) {
                    hDist = tempH + 0;
                    y = i;
                }
            }
            return new ECViewPoint(x, y);
        }

        public void toggleLabels() {
            useLabels = !useLabels;
            this.repaint();
        }

        /**
         *
         * based on the zoom, it sets the scale of the graph.
         *
         * @return the scale is intended to be the distance between points on
         * the graph using zoom*{@link #getGraphHeight() getGraphHeight()}
         * divided by getP_prime(); (if zoom is negative, then it multiplies it
         * by negative 1) if zoom is 0: returns graphHeight/prime P
         */
        private double setScale() {
            scale = (this.getPreferredSize().getHeight() - getShift() - buffer) / mod.getP_prime();
//            System.err.println("scale = " + scale);
            return scale;
        }

        /**
         *
         * @return the current zoom value
         */
        //<editor-fold defaultstate="collapsed" desc="getZoom and min/maxZoom methods">
        public double getZoom() {
            return zoom;
        }

        /**
         *
         * @return the minimum zoom value
         */
        public int getMinZoom() {
            return minZoom;
        }

        /**
         *
         * @return the maximum zoom value
         */
        public int getMaxZoom() {
            return maxZoom;
        }

        /**
         * manually sets the minimum zoom
         *
         * @param a any integer (no constraints... might be dangerous)
         */
        public void setMinZoom(int a) {
            minZoom = a;
        }

        /**
         *
         * @param b (also no constraints)
         */
        public void setMaxZoom(int b) {
            maxZoom = b;
        }
        //</editor-fold>

        /**
         * does not check that value is within zoom limits...
         *
         * @param value zoom will be set to this value without checking
         */
        void setZoom(int value) {
            zoom = value * 0.01;
//            System.err.println(zoom + "````");
            zoomChanged = true;
            doUpdate();
        }

        private Line2D getLineFrom(ECViewPoint p1, ECViewPoint p2) {
            Ellipse2D e1 = getPointEllipse(p1), e2 = getPointEllipse(p2);
            return new Line2D.Double(e1.getCenterX(), e1.getCenterY(), e2.getCenterX(), e2.getCenterY());
        }

        /**
         * GIven a double for Y it should give the correct locaiton on screen? s
         *
         * @param d
         * @return
         */
        private double flipY(double d) {
            // if d was 0, it would be at the top edge.. we want it to go to the bottom edge...
            return Math.abs(this.getPreferredSize().getHeight() - d) - getShift(); //not sure why it is abs....
            // the shift is necessary to get a buffer on the bottom of the screen...
        }

        private void doUpdate() {

            if (zoomChanged) {
                // first, zoom changes the preferred size
//                System.err.println("zoom is " + zoom);
                Dimension d1 = new Dimension();
                d1.setSize(scrollSize.getWidth() * zoom + getShift() + buffer, scrollSize.getHeight() * zoom + getShift() + buffer);
                this.setPreferredSize(d1);
                this.setSize(d1);
                this.invalidate();
                zoomChanged = false;
                /// then we set the scale based on the preferred size... 

            }
            this.setScale();
//                || mod.getP_prime() + 1 != xLines.length
//                    }) {
            // if we end up here, then p  or zoom has changed...
            // so we change the size.
//                zoomChanged = false;
//                Dimension d = new Dimension();
//                d.setSize((mod.getP_prime() + 1) * scale + 2 * getShift() + buffer,
//                        (mod.getP_prime() + 1) * scale + 2 * getShift());
//                this.setSize(d);
//                this.setPreferredSize(d);
//            }
            this.setGridLines();
            this.setClickablePoints();
            this.invalidate();
            this.repaint();
        }

        public void toggleGridLines() {
            this.drawGridLines = !this.drawGridLines;
            this.repaint();
        }

        public void zoomFullScreen() {
            // for some reason this is the best size... not sure why...
            zoom = 0.93;
            zoomChanged = true;
            this.doUpdate();
        }

        public void toggleLineLabels() {
            this.lineLabels = !lineLabels;
            this.repaint();
        }

        private Point getAWTPoint(Point mouseLoc) {
            // this will find the nearest point..
            double tempV, tempH, vDist = scale + 100, hDist = scale + 100, pX = mouseLoc.getX(), pY = mouseLoc.getY();
            int y = 0, x = 0;
            for (int i = 0; i < verticalLines.length; i++) {
                tempV = verticalLines[i].ptSegDist(pX, pY);
                tempH = horizontalLines[i].ptSegDist(pX, pY);
                // if the current line is closer to the point then the previous checked points,
                if (tempV < vDist) {
                    vDist = tempV + 0;
//                    we want to know which direction as well...
                    x = (int) verticalLines[i].getX1();
                    if (x > pX) {//if x is further to the right then pX
                        x -= vDist; // we go back the distance it is from pX
                    } else {
                        x += vDist;
                    }
                }
                if (tempH < hDist) {
                    hDist = tempH + 0;
                    y = (int) horizontalLines[i].getY1();
                    if (y > pY) {
                        y -= hDist;
                    } else {
                        y += hDist;
                    }
                }
            }
            return new Point(x, y);
        }

        public void circlePoint(ECViewPoint e) {
            if (circlePt != null) {
                Graphics2D d = ((Graphics2D) this.getGraphics());
                d.setColor(Color.red);
                d.fill(circlePt);
                d.setColor(Color.BLACK);
            }
            circlePt = this.getHighlightEllipse(e);
//            this.invalidate();this.repaint();.
        }

        public void drawCircleAndSquare(boolean b) {
            this.drawCircleAndSquare = b;
            if (drawCircleAndSquare) {
                Graphics2D d = (Graphics2D) this.getGraphics();
                d.setColor(Color.GREEN);
                d.fill(squarePt);
                d.setColor(Color.MAGENTA);
                d.fill(circlePt);
//                System.err.println("We are drawign circle and square :" + circlePt + " and " + squarePt);
            }
            else{
//                this.invalidate();
//                this.repaint();
            }
        }

        public void squarePoint(ECViewPoint e) {
            if (squarePt != null) {
                Graphics2D d = ((Graphics2D) this.getGraphics());
                d.setColor(Color.red);
                d.fill(squarePt);
                d.setColor(Color.BLACK);
            }
            Ellipse2D ellipse = getHighlightEllipse(e);
            squarePt = new Rectangle2D.Double(ellipse.getX(), ellipse.getY(), ellipse.getWidth(), ellipse.getWidth());
        }
    }

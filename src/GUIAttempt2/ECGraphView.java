/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ben
 */
public class ECGraphView extends JPanel implements Observer {

    private ECGraphController control;
    private ECGraphPanel graph;
    private GraphControllerPanel ctrlPane;
    private ECGModel mod;
    private JScrollPane scroll;
    private boolean mouseIsOverPoint;
    private Point currentMousePoint;

    /**
     *
     * @param theMod
     * @param d
     */
    public ECGraphView(ECGModel theMod, Dimension d) {
//        if (d.getHeight() == 0) {
//            Utilities.print("d hieght is 0 " + d);
//        }
        mod = theMod;
        mod.addObserver(this);
        Dimension zoomDim = new Dimension(), scrollDim = new Dimension();
        double w = d.getWidth(), h = d.getHeight();
        scrollDim.setSize(w - 40, h - 40);
//        zoomDim.setSize(w + 100, 0.1 * h);

//        container.add(zoom);
        scroll = new JScrollPane();

        //b.add(graphPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(d);
        scroll.setSize(d);
        scroll.setVisible(true);
        graph = new ECGraphPanel(mod, scroll.getSize());
        control = new ECGraphController(mod, this);
        ctrlPane = new GraphControllerPanel(graph, control);
        ctrlPane.setPreferredSize(zoomDim);
        scroll.setViewportView(graph);
        graph.addMouseMotionListener(control);
        graph.addMouseListener(control);
        this.setGraphFullScreen();
        this.add(scroll);
//        container.add(ctrlPane);
//        this.addKeyListener(control);
//        ctrlPane.addKeyListener(control);
//        graph.addKeyListener(control);
        scroll.addMouseWheelListener(control);
//        this.add(container);
    }

    List<Shape> getClickAreas() {
        return this.graph.getClickAreas();
    }

    ECViewPoint convertAWTPointToPoint(MouseEvent me) {
        return graph.getClosestGridIntersection(me);//graph.convertAWTPointToPoint(me);
    }

    /**
     *
     * @return
     */
    public ECGraphPanel getGraphPanel() {
        return graph;
    }

    /**
     *
     * @return
     */
    public ECGraphController getGraphController() {
        return control;
    }

    /**
     *
     * @return
     */
    public GraphControllerPanel getGraphControllerPanel() {
        return ctrlPane;
    }

    /**
     *
     * @param ctrl
     */
    public void setGraphControllerPanel(GraphControllerPanel ctrl) {
        ctrlPane = ctrl;

    }

    void setMouseOver(ECViewPoint p) {
        ctrlPane.setCurrentMouseOverPoint(p);
    }

    void toggleLabels() {
        graph.toggleLabels();
    }

    void toggleGridLines() {
        graph.toggleGridLines();

    }

    void setGraphFullScreen() {
        graph.zoomFullScreen();
        ctrlPane.getZoomSlider().setValue((int) (graph.getZoom() * 100));
    }

//    void mouseOverPoint(MouseEvent me) {
//        System.out.println("mouse has gone over a point at " + graph.convertAWTPointToPoint(me));
//    }
    @Override
    public void update(Observable o, Object arg) {
        graph.update(o, arg);
//        zoom.update(o,arg)
    }

    void updateZoom() {
        if (!ctrlPane.zoom.getValueIsAdjusting()) {
            graph.setZoom(ctrlPane.getZoomLevel());
        }
    }

    void changeCursor(boolean b) {
        if (b) {
            this.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            this.mouseIsOverPoint = false;
            this.getRootPane().setCursor(Cursor.getDefaultCursor());
        }

    }

    private JScrollPane getScrollPane() {
        return scroll;
    }

    void toggleLineLabels() {
        graph.toggleLineLabels();
    }

    void zoomFromScroll(int wheelMoved, Point mouseLoc) {
        // p is where the viewport was at first
        Point p = scroll.getViewport().getViewPosition();
        ctrlPane.getZoomSlider().setValue(ctrlPane.getZoomSlider().getValue() + wheelMoved);
        updateZoom();
        double x, y;
        // x', y' are just where the mouse was on the graph (the addition of the location on the scroll pane plus where the viewport was.
        x = p.getX() + mouseLoc.getX();
        y = p.getY() + mouseLoc.getY();
    }

    void setCurrentMouseOverPoint(boolean b, Point location) {
        mouseIsOverPoint = b;
        currentMousePoint = location;
    }

    void circlePoint(ECViewPoint e) {
        graph.circlePoint(e);
    }

    void drawCircleAndSquare(boolean b) {
        graph.drawCircleAndSquare(b);
//        graph.invalidate();
//        graph.repaint();
        /// should I now reverse the boolean... ?
    }

    void squarePoint(ECViewPoint e) {
        graph.squarePoint(e);
    }


    private class GraphControllerPanel extends JPanel {

        private JTextField currPt;
        private JSlider zoom;
        private ECGraphPanel view;
        private ECGraphController control;
        private final JLabel currPtLabel;
        private final JLabel minusLabel;
        private final JLabel plusLabel;

        public JSlider getZoomSlider() {
            return zoom;
        }

        public int getZoomLevel() {
            return zoom.getValue();
        }
        private int TextBoxSize;
        private JButton clearPoints, fullScreen, zoomPlus, zoomMinus;
        private JCheckBox labels, useGridLines, labelLines;

        public GraphControllerPanel(ECGraphPanel view, ECGraphController control) {
            currPt = new JTextField();
            currPt.setEditable(false);
            TextBoxSize = "(  ,  )".length();
            currPt.setColumns(TextBoxSize);
            currPt.setHorizontalAlignment(JTextField.CENTER);
            currPtLabel= new JLabel("Current Mouse Location:");
            plusLabel = new JLabel("<html><h2>+</h2></html>");
            minusLabel = new JLabel("<html><h2>-</h2></html>");
            zoom = new JSlider(view.getMinZoom(), view.getMaxZoom(), 118);
            zoom.setMajorTickSpacing(25);
//            zoom.setPaintTicks(true);
            zoom.setOrientation(JSlider.HORIZONTAL);
            zoom.addChangeListener(control);
            zoom.setSize(50, this.getHeight());
            labels = new JCheckBox("Draw Labels");
            labels.setEnabled(true);
            labels.setActionCommand("toggleLabels");
            labels.addActionListener(control);
            labels.setSelected(true);
            clearPoints = new JButton("Clear Points");
            clearPoints.setEnabled(true);
            clearPoints.setActionCommand("clearPoints");
            clearPoints.addActionListener(control);
            useGridLines = new JCheckBox("Draw Gridlines");
            useGridLines.setEnabled(true);
            useGridLines.setSelected(true);
            useGridLines.setActionCommand("useGridLines");
            useGridLines.addActionListener(control);
//            zoomMinus = new JButton(new Icon());
            labelLines = new JCheckBox("Line #'s");
            labelLines.setEnabled(true);
            labelLines.setSelected(true);
            labelLines.setActionCommand("labelLines");
            labelLines.addActionListener(control);
            fullScreen = new JButton("View All Points");
            fullScreen.setEnabled(true);
            fullScreen.setActionCommand("fullScreen");
            fullScreen.addActionListener(control);
            Box b = Box.createVerticalBox();
//            b.setPreferredSize(this.getPreferredSize());
//            b.add(zoom);
            
//            b.add(currPt);
            Box k = Box.createHorizontalBox(), h = Box.createHorizontalBox(), i = Box.createHorizontalBox(), j = Box.createHorizontalBox();
            k.add(minusLabel);
            k.add(zoom);
            k.add(plusLabel);
            j.add(currPtLabel);
            j.add(currPt);
            h.add(labels);
            h.add(useGridLines);
//            h.add(labelLines);
            b.add(k);
            b.add(j);
            b.add(h);
            
            i.add(clearPoints);
            i.add(fullScreen);
            b.add(i);
            this.add(b);
        }

        public void setCurrentMouseOverPoint(ECViewPoint p) {
            // ensures the string is goign to take up the full size of the textbox, so that it's always in the same spot.
            int diff = p.toString().length() - TextBoxSize;
            String toast = "";
            if (diff < 0) {
                for (int i = 0; i > diff; i--) {
                    toast += " ";
                }
            }
            toast += p.toString();
            currPt.setText(toast);
            currPt.setVisible(true);
            this.invalidate();
        }
    }
}

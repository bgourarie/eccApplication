/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import GUIAttempt2.ECGraphPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * @author benbenbenultra
 */
public class PollardViewFrame extends JFrame {// implements Observer{

    private PollardController ctrl;
    private PollardModel mod;
    private PollardResultsPanel results;
    private PollardGraphPanel graph;

    public PollardViewFrame(PollardModel mod) {

        this.mod = mod;
        this.setTitle("Pollard's Rho Method");
        this.setSize(mod.getGroupSize()>50?750:600,mod.getGroupSize()>50?700: 600);
        ctrl = new PollardController(mod, this);
        Dimension graphPane = new Dimension(200, 200),
                resultPane = new Dimension(400, 200);
        this.graph = new PollardGraphPanel(mod, graphPane);
        this.results = new PollardResultsPanel(ctrl, resultPane);
        JSplitPane b = new JSplitPane();
        b.setOrientation(JSplitPane.VERTICAL_SPLIT);
        b.setTopComponent(graph);
        b.setRightComponent(results);
        b.setDividerLocation(mod.getGroupSize()>50?450:372);
        b.setDividerSize(10);
        b.setResizeWeight(1);
        b.setVisible(true);
        this.getContentPane().add(b);
        b.invalidate();
        b.repaint();
        this.setVisible(true);
        this.invalidate();
        this.repaint();
    }

    void updateComponents() {
        results.updateComponents();
        graph.updateComponents();
    }

    void enableNextButton(boolean b) {
        results.iPlus.setEnabled(b);
    }

    void enablePrevButton(boolean b) {
        results.iMinus.setEnabled(b);
    }

    private class PollardResultsPanel extends JPanel {

        private JButton iPlus;
        private JButton iMinus;
        private PollardController control;
        private JLabel solutionEquation;
        private String pString;
        private String qString;
        private JLabel fx1, fx2;
        private JTextField iEquals;

        PollardResultsPanel(PollardController ctrl, Dimension d) {
            //do something with the d.
//            this.setFocusable(false);
//            this.setFocusTraversalPolicyProvider(false);
            this.control = ctrl;
            pString = mod.getPointP().toString();
            qString = mod.getPointQ().toString();
            this.initComponents();
            this.addComponents();
            this.setVisible(true);
        }

        void updateComponents() {
            String fx1String =
                    "<html><h3><font color=\"red\">X<sub>" + control.getI() + "</sub></font> ="
                    + " f(x<sub>" + ((control.getI()==0?0:(control.getI() - 1)))
                    + "</sub>) = <font color=\"red\">"+ control.getFxi()
                    + "</font> = <font color=\"purple\">" + control.getC1()
                    + "</font>*" + pString + " + <font color=\"purple\"><i>" + control.getD1()
                    + "</i></font>*" + qString + " = <font color=\"red\"><i>" 
                    + control.getX(control.getI() - 1)
                    + "</i></font> + <font color=\"purple\"><i>" + control.getAi1() + "</i></font>*"
                    + pString + " + <font color=\"purple\"><i>" + control.getBi1() + "</i></font>*"
                    + qString + "</h3></html>";
            String fx2String =
                    "<html><h3><font color=\"blue\">X<sub>" + (2 * control.getI()) + "</sub></font> = "
                    + " f(x<sub>" + (control.getI()==0?0:((2 * control.getI()) - 1)) 
                    + "</sub>) = <font color=\"blue\">"+ control.getFx2i()
                    + "</font> = <font color=\"purple\">" + control.getC2()
                    + "</font>*" + pString + " + <font color=\"purple\"><i>" + control.getD2()
                    + "</i></font>*" + qString + " = <font color=\"blue\"><i>" 
                    + control.getX((2 * control.getI()) - 1)
                    + "</i></font> + <font color=\"purple\"><i>" + control.getAi2() + "</i></font>*"
                    + pString + " + <font color=\"purple\"><i>" + control.getBi2() + "</i></font>*"
                    + qString + "</h3></html>";
//            System.err.println(fx1String);
//            System.err.println(fx2String);
            fx1.setText(fx1String);
//            fx1.setForeground(Color.RED);
//            fx1.se
            fx2.setText(fx2String);
//            iEquals.setText("i = "+control.getI());
//            iEquals.setValue(control.getI());
//            fx2.setForeground(Color.BLUE);
            String space = "&nbsp;";
            String s = "<html><h3><font color=\"green\">i= " + control.getI()
                    +"</font>"+space+space+space+space+space+space
                    + " <i>(c-c')*(d'-d)<sup>-1</sup> mod n = "
                    + control.getC1() + " - " + control.getC2()
                    + " * (" + control.getD2() + " - " + control.getD1() + ")<sup>-1</sup> mod "
                    + mod.getGroupSize() + " = " + control.getSoln() + "</i></h3></html>";
            solutionEquation.setText(s);
            this.invalidate();
            this.repaint();
        }

        private void addComponents() {
            Box buttons = Box.createHorizontalBox();
            Box holdAll = Box.createVerticalBox();
//            buttons.add(Box.createHorizontalStrut(40));
            buttons.add(iPlus);
            buttons.add(Box.createHorizontalStrut(30));
//            buttons.add(iEquals);
//            buttons.add(Box.createHorizontalStrut(10));
            buttons.add(iMinus);
//            buttons.add(Box.createHorizontalStrut(40));
//            c-c' * (d'-d)^-1 mod n
            Box soln = Box.createHorizontalBox();
            soln.add(solutionEquation);
            Box b = Box.createHorizontalBox();
            b.add(fx1);
            Box c = Box.createHorizontalBox();
            c.add(fx2);

            holdAll.add(b);
            holdAll.add(Box.createVerticalStrut(5));
            holdAll.add(c);
            holdAll.add(Box.createVerticalStrut(10));
            holdAll.add(buttons);
            holdAll.add(soln);
            this.add(holdAll);
        }

        private void initComponents() {
            solutionEquation = new JLabel(" ben ben ben");
//            solutionEquation.setEditable(false);
            fx1 = new JLabel("");
//            fx1.setRows(1);
            fx2 = new JLabel("");
//            fx2.setRows(1);
            fx2.setBackground(Color.WHITE);
            iPlus = new JButton("i++");
            iPlus.addActionListener(control);
            iPlus.setActionCommand("i++");
//            iEquals = new JTextField();
//            iEquals.setColumns("i = 999".length());
//            iEquals.setHorizontalAlignment(JTextField.CENTER);
//            iEquals.setSize(30, 10);
////            iEquals.set
////            iEquals.setPreferredSize(new Dimension(30,30));
//            iEquals.setEditable(false);
            
//            iEquals.setFormatterFactory);
            iMinus = new JButton("i--");
            iMinus.addActionListener(control);
            iMinus.setActionCommand("i--");
            iMinus.setEnabled(false);
            // just a solid block of labels.
            updateComponents();
        }
    }

    /**
     * this should be sufficient, it will use the PollardModel to draw the
     * points and such so its all good..
     */
    private class PollardGraphPanel extends ECGraphPanel {

        private String x2;
        private String x1;
        private Ellipse2D ellipse1, ellipse2;
        private boolean drawHighlight;

        PollardGraphPanel(PollardModel mod, Dimension d) {
            super(mod, d);
            //            super.setGraphFullScreen();
//            super.zoomFromScroll(120, new Point(0,0)); //  zooms in a smidge.
            super.toggleGridLines();
            super.setZoom(150);
            if(mod.getGroupSize()>40){
                super.toggleLabels();
                super.setZoom(200);
            }
            
            x1 = ctrl.getFxi();
            x2 = ctrl.getFx2i();
            recordPointsFromStrings(true);
//            drawHighlight=true;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (drawHighlight) {
                Graphics2D d = (Graphics2D) g;
                d.setColor(Color.red);
                d.fill(ellipse1);
                d.drawString("X1", (int) ellipse1.getMaxX(), (int) ellipse1.getMaxY() - 15);
                d.setColor(Color.blue);
                d.fill(ellipse2);
                d.drawString("X2", (int) ellipse2.getMaxX(), (int) ellipse2.getMaxY() + 15);
                d.setColor(Color.black);
            }
        }

        void recordPointsFromStrings(boolean b) {
//            try {
                drawHighlight = b;
                ellipse1 = super.getHighlightEllipse(new ECViewPoint(x1,mod.getGroupSize()));
                ellipse2 = super.getHighlightEllipse(new ECViewPoint(x2, mod.getGroupSize()));
//            } catch (PointNotFoundException ex) {
//                System.err.println("point not found...");
//                drawHighlight=false;
//            }
        }

        void updateComponents() {
            recordPointsFromStrings(false);
            x2 = ctrl.getFx2i();
            x1 = ctrl.getFxi();
            recordPointsFromStrings(true);
            this.invalidate();
            this.repaint();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Globals;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author benbenbenultra
 */
public class DLPView extends JPanel implements Observer {

    private DLPOrderChooserPanel orderVis;
    private DLPCtrlPane ctrlPane;
    private DLPResultsPane results;
    private ECGModel mod;
    private DLPController ctrl;

    public DLPView(ECGModel mod, Dimension d) {
        this.mod = mod;
//        mod.addObserver(this);
        this.setPreferredSize(new Dimension(298, 562));
        //create controller;
        ctrl = new DLPController(mod, this);
        initComponents();
        addComponents();
//        Dimension cpDim = new Dimension();
//        cpDim.setSize(298, 562 / 3);
//        orderVis.setPreferredSize(cpDim);
//        ctrlPane.setPreferredSize(cpDim);
//        results.setPreferredSize(cpDim);
        this.setVisible(true);

    }

    private void addComponents() {
        Box holdAll = Box.createVerticalBox();
        holdAll.add(orderVis);
        holdAll.add(ctrlPane);
        holdAll.add(results);
        holdAll.setPreferredSize(this.getPreferredSize());
        this.add(holdAll);

    }

    private void initComponents() {
        orderVis = new DLPOrderChooserPanel(ctrl, new Dimension(298, (int) (562.0 / 6.0d)));
        Dimension d = new Dimension();
        d.setSize(298, 562 * (5d / 6d)); //set each of them to be 1/2 of 5/6ths of the height...
        ctrlPane = new DLPCtrlPane(ctrl, d);
        results = new DLPResultsPane(ctrl, d);
    }

    public void updateComponents() {
        ctrlPane.updateComponents();
        results.updateComponents();
        if (mod.ECGHasChanged()) {
            orderVis.updateComponents();
        }
        ctrlPane.invalidate();
        results.invalidate();
        orderVis.invalidate();
        orderVis.repaint();
        results.repaint();
        ctrlPane.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
//        ctrlPane.updateComponents();
//        results.updateComponents();
        if (mod.ECGHasChanged()) {
            orderVis.updateComponents();
        }
//        ctrlPane.invalidate();
//        results.invalidate();
//        orderVis.invalidate();
//        orderVis.repaint();
//        results.repaint();
//        ctrlPane.repaint();
    }

    void handlePointSelection(ECViewPoint p) {
        // to handle:
        // controller needs to select P.. ctrlPanel needs to display p and enable buttons
        ctrl.setP(p);
//        orderVis.updateComponents();
        results.updateComponents();
        ctrlPane.updateComponents();
    }

    void mouseClickAt(int x, int y) {
//      
    }

    void DLPSolved() {
        if (ctrl.dlpUsedPollard()) {
//            System.err.println("used pollard");
            // still doesnt display results.. but thats okay for now i think...
//            results = new PollardResultsPane(ctrl,ctrlPane.getPreferredSize());
//            this.removeAll();
//            this.add(orderVis);
//            this.add(ctrlPane);
//            this.add(results);
        } else {
            results.updateComponents();
        }
        orderVis.updateComponents();
        ctrlPane.updateComponents();
    }

    void pointSelected(int i) {
        this.handlePointSelection(ctrl.getPoint(i));
    }

    /**
     * Currently just has a JLabel to get an integer (DLP solution) from
     * controller, and it displays it.
     *
     */
    private class DLPResultsPane extends JPanel {

        private DLPController ctrl;
        private JLabel result;
        private JTextArea log;
        private Box PollardResultsBox;

        DLPResultsPane(DLPController ctrl, Dimension d) {
            this.ctrl = ctrl;
            this.setPreferredSize(d);
            result = new JLabel();
//            this.add(result);
            log = new JTextArea();
            log.setLineWrap(true);
            log.setWrapStyleWord(true);
            log.setEditable(false);
            log.setRows(10);

//            log.setMaximumSize(new Dimension(this.getPreferredSize().width,this.getPreferredSize().height -40));
            Box b = Box.createVerticalBox();
            b.setPreferredSize(new Dimension(this.getPreferredSize().width - 32, 150));
            b.add(new JScrollPane(log));
            b.add(result);
            this.add(b);
            this.setVisible(true);
        }

        void updateComponents() {
            if (ctrl.isDLPSolved()) {
                int k = ctrl.getDLPSolution();
                result.setText("DLP Solution = " + k);
                String logs = "";
                int resetCount = 0;
                for (String s : ctrl.getDLPLog()) {
                    if (s.equalsIgnoreCase(Globals.RESET_POLLARD)) {
                        resetCount++; // this will be included at end
                    } else if (s.startsWith("c")) {
                        logs += s + " "; //we just separate c, d and x (or c',d',x') with a space
                    } else {
                        logs += s + "\n"; // we separate x and x' with a new line.
                    }
                }
                logs += ctrl.getDLPLog().length > 1 // if it's length is 1, its BSGS (which has just one sentence log)
                        ? "\n" + "Pollard Rho reset " + resetCount + " times." : "";
                log.setText(logs);
                this.invalidate();
            } else {
                result.setText("  ");
                log.setText("  ");
            }
        }
    }

    private class DLPCtrlPane extends JPanel {

//        private JTextField selection;
        private JLabel instructions, selectedPt, selection, ptOrder, dlpSolvers, qInfo;
        private JButton pollard, bsgs, randomQ;
        private DLPController control;

        DLPCtrlPane(DLPController ctrl, Dimension d) {
            control = ctrl;
            this.setPreferredSize(d);
            initComponents();
            addComponents();

            this.setVisible(true);
        }

        private void addComponents() {
            Box holdAll = Box.createVerticalBox();
//Box text = Box.createHorizontalBox();
            holdAll.add(instructions);
//            text.add(Box.createGlue());
//            text.add(selection);
//            text.add(Box.createGlue());
            holdAll.add(selection);
            Box ptInfo = Box.createHorizontalBox();
//            ptInfo.add(Box.createGlue());
            ptInfo.add(selectedPt);
            ptInfo.add(Box.createHorizontalStrut(10));
            ptInfo.add(ptOrder);
//            ptInfo.add(Box.createGlue());
            holdAll.add(ptInfo);
            holdAll.add(dlpSolvers);

//            Box btns = Box.createHorizontalBox();
//            btns.add(pollard);
//            btns.add(bsgs);
            holdAll.add(randomQ);
            holdAll.add(pollard);
            holdAll.add(bsgs);

            holdAll.add(qInfo);
//            holdAll.setPreferredSize(this.getPreferredSize());
            holdAll.setVisible(true);
            this.add(holdAll);
        }

        private void initComponents() {
            instructions = new JLabel("Adjust the slider to choose a point.");
//            instructions.setEditable(/false);

            selection = new JLabel("You have selected:");
//            selection.setEditable(false);

            selectedPt = new JLabel("No Pt Selected");

            ptOrder = new JLabel("       ");

            dlpSolvers = new JLabel("Choose a DLP-Solving Algorithm: ");
// on update, this should happen:            randomPt= new JLabel("k*"+selectedPt.getText()+"="+control.getQ());
            //but for now:
            qInfo = new JLabel("     ");
            pollard = new JButton("Pollard's Rho Method");
            bsgs = new JButton("Baby-Step Giant-Step");
            randomQ = new JButton("Pick a new random pt");
            randomQ.setActionCommand("q");
            randomQ.addActionListener(control);
            randomQ.setEnabled(true);
            pollard.setActionCommand("" + Globals.POLLARD);
            pollard.addActionListener(control);
            pollard.setEnabled(true);

            bsgs.setActionCommand("" + Globals.BSGS);
            bsgs.addActionListener(control);
        }

        void updateComponents() {
            if (control.isPChosen()) {
                selectedPt.setText("<html><h4><font color=\"red\"> P = " + control.getP()
                        + "</font></h4></html>");
                ptOrder.setText("<html><h4><u>order of P </u>= <i>n</i> = " + control.getPOrder() + "</h4></html>");
                qInfo.setText(control.isQChosen() ? "k*" + control.getP() + " = " + control.getQ() : "   ");
                if (control.isQChosen()) {
                    String space = "&#160;&#160;&#160;";
                    qInfo.setText("<html><font color=\"red\">P =" + control.getP() + "</font>"
                            + space + space + space
                            + "<font color=\"blue\"> Q =" + control.getQ() + "</font></html>");
                }
            } else {
                selectedPt.setText("No Pt Selected");
                ptOrder.setText("       ");
                qInfo.setText("    ");
            }
        }
    }

    private class DLPOrderChooserPanel extends JPanel {

        private DLPController ctrl;
        private Rectangle2D[] orders; //these should be as a percent of the size of the whole...
        private Color[] colors; //these will be the colors of the rectangles...
        private JSlider orderSlider;

        DLPOrderChooserPanel(DLPController control, Dimension d) {
            this.ctrl = control;
            this.addMouseListener(ctrl);
            this.setPreferredSize(d);
//            test = 0;
            this.setSize(d);
            this.initComponents();
//            this.getInfoFromControl();
            this.setVisible(true);
            this.invalidate();
            this.repaint();
        }

        void initComponents() {
            orderSlider = new JSlider(0, ctrl.getNumberOfPoints() - 1, 0); // just goes from 0 to N;
            orderSlider.addChangeListener(ctrl);
            orderSlider.setMajorTickSpacing(1);
            orderSlider.setLabelTable(orderSlider.createStandardLabels(1));
            orderSlider.setSnapToTicks(true);
//            orderSlider.setPaintLabels(true);
            orderSlider.setPaintTicks(true);
            this.add(orderSlider);
        }

        private void getInfoFromControl() {
            if (!ctrl.isPChosen()) {
                orderSlider.setMaximum(ctrl.getNumberOfPoints() - 1);
//                System.err.println("order slider set to " + ctrl.getNumberOfPoints());
                orderSlider.setMinimum(0);
                orderSlider.setValue(0);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
//            Graphics2D d = (Graphics2D) g;
//            for (int i = 0; i < orders.length; i++) {
//                d.setColor(colors[i]);
//                d.fill(orders[i]);
//                d.setColor(Color.black);
//                d.draw(orders[i]); //gives it a black border...
//            }
        }

        private Rectangle2D[] getOrders() {
            return orders;
        }
//        private int test;

        private void updateComponents() {
            this.getInfoFromControl();
//            test++;a
            this.invalidate();
            this.repaint();
        }
    }
}

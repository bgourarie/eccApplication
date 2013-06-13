/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;


import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.AttributedString;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ben
 */
public class ECGControllerView extends JPanel implements  Observer {
    private Box buttons;

  
    private class ECGEquationView extends JPanel implements Observer {

        private ECGModel mod;
        private AttributedString equation;
        /**
         *
         * @param o
         */
        ECGEquationView(ECGModel o, Dimension d) {
            this(o,d,o.getECEquation());
        }

        ECGEquationView(ECGModel m, Dimension d,AttributedString as){
            mod= m;
            this.makeObserve();
            this.equation=as;
            
        }
        private void makeObserve() {
            mod.addObserver(this);
        }

        @Override
        public void update(Observable o, Object arg) {
            equation = mod.getECEquation();
            setEquation();
            this.repaint();
            //           this.revalidate();
        }
        private void setEquation(){
            // need a way to set something like html or something as a label...
        }
        @Override
        public void paintComponent(Graphics g){
            g.drawString(equation.getIterator(),5, 15);
        }
//        @Override
//        public void paintComponent(Graphics g) {
////            super.paint(g);
//            g.drawString(equation.getIterator(), 5, 15);       // super.paint(g);
//
//        }
    }
    private ECGModel mod;
    private Box specs, labelAndTextAreas, sliders;
    private ECGEquationPanel text;
    private JSlider sliderA, sliderB, sliderP, zoomSlider;
    private JFormattedTextField textA, textB, textP;
    private JButton buttonA, buttonB, buttonP;
    private JLabel labelA, labelB, labelP;
    private ECGControllerController control;

    /**
     *
     * @param theModel
     */
    public ECGControllerView(ECGModel theModel) {
        this(theModel, new Dimension(200, 50));
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        JFrame test = new JFrame();
        test.setSize(600, 200);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.add(new ECGControllerView(new ECGModel()));
        test.setVisible(true);
    }

    /**
     *
     * @param theModel
     * @param d
     */
    public ECGControllerView(ECGModel theModel, Dimension d) {
        this.setPreferredSize(d);
        this.setSize(d);
        mod = theModel;
        mod.addObserver(this);
        control = new ECGControllerController(mod, this);
        initComponents();
        addComponents();
//        mainBox.add(Box.createVerticalGlue());

        this.setVisible(true);
    }

    private void addComponents() {
        // first we fill the components. specs is largest and will be last. 
        
        //sliders contains the three sliders.
        sliders.add(sliderA);
        sliders.add(sliderB);
        sliders.add(sliderP);
        
        // texts will contain three horizontal boxes each containg a label "A=" and a jTextField...
        Box aEquals = Box.createHorizontalBox(), bEquals =Box.createHorizontalBox(), pEquals =Box.createHorizontalBox();
        aEquals.add(labelA);
        aEquals.add(textA);
        bEquals.add(labelB);
        bEquals.add(textB);
        pEquals.add(labelP);
        pEquals.add(textP);
        labelAndTextAreas.add(aEquals);
        labelAndTextAreas.add(bEquals);
        labelAndTextAreas.add(pEquals);
        
        //buttons will contain just the one update button...
        
        buttons.add(buttonA);
        
        // specs contains all those boxen!
        specs.add(labelAndTextAreas);
        specs.add(sliders);
        specs.add(buttons);
        Box holdAll= Box.createVerticalBox();
        holdAll.add(specs);
        holdAll.add(text);
        this.add(holdAll);
//        specs.setVisible(true);
//        texts.setVisible(true);
//        sliders.setVisible(true);
//        text.setVisible(true);
//        this.add(Box.createVerticalGlue());
////        mainBox.add(Box.createVerticalGlue());
//        this.add(specs);
//        this.add(labelAndTextAreas);
//        this.add(sliders);   
//        this.add(text);

//        this.add(Box.createVerticalGlue());
    }

    private void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        specs = Box.createHorizontalBox();
        labelAndTextAreas = Box.createVerticalBox();
        sliders = Box.createVerticalBox();
        buttons = Box.createVerticalBox();
        text = new ECGEquationPanel(mod);
        text.setPreferredSize(this.getECGEquationPanelSize());
//       
//        a.setPreferredSize(this.getControlBoxSize());
//        b.setPreferredSize(this.getControlBoxSize());
//        p.setPreferredSize(this.getControlBoxSize());
        /*a.add(Box.createHorizontalGlue());
         b.add(Box.createHorizontalGlue());
         *///add slider, textbox, and button

        ///sliders:
        sliderP = new JSlider(mod.getPSliderMin(), mod.getPSliderMax(), mod.getP());
        sliderA = new JSlider((int)mod.getABMin(), mod.getABMax(), mod.getA());
        sliderB = new JSlider((int)mod.getABMin(), mod.getABMax(), mod.getB());        
        sliderP.addChangeListener(control);
        sliderA.addChangeListener(control);
        sliderB.addChangeListener(control);
        
        
        sliderP.setMajorTickSpacing(1000);
        sliderP.setMinorTickSpacing(250);
        sliderP.setPaintTicks(true);
        sliderP.setName("sliderP");
sliderA.setMajorTickSpacing(50);
        sliderA.setName("sliderA");
        sliderA.setMinorTickSpacing(10);
        sliderA.setPaintTicks(true);
        sliderB.setMajorTickSpacing(50);
        sliderB.setMinorTickSpacing(10);
        sliderB.setPaintTicks(true);
        sliderB.setName("sliderB");

        
        sliderA.setPreferredSize(this.getSliderSize());
        sliderB.setPreferredSize(this.getSliderSize());
        sliderP.setPreferredSize(this.getSliderSize());

        /* 
         */ //labels:
        labelA = new JLabel("a= ");
        labelB = new JLabel("b= ");
        labelP = new JLabel("p= ");
//        a.add(labelA);
//        b.add(labelB);
//        p.add(labelP);
        /*
         */ //text boxes:
        textA = new JFormattedTextField(new Integer(sliderA.getValue()));
        textB = new JFormattedTextField(new Integer(sliderB.getValue()));
        textP = new JFormattedTextField(new Integer(mod.getPrimeAt(sliderP.getValue())));
        textA.setName("textA");
        textB.setName("textB");
        textP.setName("textP");
        textA.setPreferredSize(this.getBigIntTextFieldSize());
        textB.setPreferredSize(this.getBigIntTextFieldSize());
        textP.setPreferredSize(this.getBigIntTextFieldSize());
        textA.setMaximumSize(this.getBigIntTextFieldSize());
        textA.setMinimumSize(this.getBigIntTextFieldSize());
        textB.setMaximumSize(this.getBigIntTextFieldSize());
        textB.setMinimumSize(this.getBigIntTextFieldSize());
        textP.setMaximumSize(this.getBigIntTextFieldSize());
        textP.setMinimumSize(this.getBigIntTextFieldSize());


        textP.setEditable(false);

//        a.add(textA);
//        b.add(textB);
//        p.add(textP);
//        a.add(sliderA);
//        b.add(sliderB);
//        p.add(sliderP);
//        a.add(Box.createHorizontalStrut(5));
//        b.add(Box.createHorizontalStrut(5));
//        p.add(Box.createHorizontalStrut(5));
        //buttons
        buttonA = new JButton("Update");

        buttonA.setName("buttonA");
        buttonB = new JButton("Clear points");
        buttonB.setName("buttonB");
        buttonP = new JButton("Add or Double");

        buttonP.setName("buttonP");
//        a.add(Box.createHorizontalStrut(5));
//        b.add(Box.createHorizontalStrut(5));
//        p.add(Box.createHorizontalStrut(5));
////        buttonA.setPreferredSize(this.getFirstButtonWidth());
//        buttonB.setPreferredSize(this.getFirstButtonWidth());
//        buttonP.setPreferredSize(this.getFirstButtonWidth());
//        a.add(buttonA);
//        b.add(buttonB);
//        p.add(buttonP);
//        b.add(buttonB);
//         p.add(buttonP);
         //         */
//        a.add(Box.createHorizontalGlue());
//        b.add(Box.createHorizontalGlue());
//        p.add(Box.createHorizontalGlue());

        textA.addActionListener(control);
        textB.addActionListener(control);
        textP.addActionListener(control);
        buttonA.addActionListener(control);
        buttonB.addActionListener(control);
        buttonP.addActionListener(control);

    }

    @Override
    public void update(Observable o, Object arg) {

//        try {
            sliderA.setValue(mod.getA());
            sliderB.setValue(mod.getB());
            sliderP.setValue(mod.getP());
            
//        } catch (NullPointerException npe) {
//            Utilities.print("control panel got a null value from mod.getA/B/P()");
//        }

    }

    

    

    private Dimension getControlBoxSize() {
        Dimension d = new Dimension();
        d.setSize(this.getWidth(), this.getHeight() / 5);
        return d;
    }

    private Dimension getECGEquationPanelSize() {
        Dimension d = new Dimension();
        d.setSize(this.getWidth(), this.getHeight() / 5);
        return d;
    }

    private Dimension getFirstButtonWidth() {
        Dimension d = new Dimension();
        d.setSize(this.getWidth() / 5, getControlBoxSize().getHeight());
//        Utilities.print(d.toString());
        return d;
    }

    private Dimension getBigIntTextFieldSize() {
        Dimension d = new Dimension();
        d.setSize(this.getWidth() / 3, getControlBoxSize().getHeight());
        Utilities.print("TextField: " + d.toString());
        return d;
    }

    private Dimension getSliderSize() {
        Dimension d = new Dimension();
        d.setSize(this.getWidth() / 12, getControlBoxSize().getHeight());
        Utilities.print("slider: " + d.toString());
        return d;
    }
      void setECGText(AttributedString as) {
//       text.removeAll();
//       text.add(new ECGEquationView(mod,getECGEquationPanelSize(), as));
////       text.revalidate();
    }


    /**
     *
     * @return
     */
    public ECGModel getMod() {
        return mod;
    }

    /**
     *
     * @return
     */
    public Box getA() {
        return specs;
    }

    /**
     *
     * @return
     */
    public Box getB() {
        return labelAndTextAreas;
    }

    /**
     *
     * @return
     */
    public Box getP() {
        return sliders;
    }

//    public Box getText() {
//        return text;
//    }

    /**
     *
     * @return
     */
    public JFormattedTextField getTextA() {
        return textA;
    }

    /**
     *
     * @return
     */
    public JFormattedTextField getTextB() {
        return textB;
    }

    /**
     *
     * @return
     */
    public JFormattedTextField getTextP() {
        return textP;
    }

    /**
     *
     * @return
     */
    public JButton getButtonA() {
        return buttonA;
    }

    /**
     *
     * @return
     */
    public JButton getButtonB() {
        return buttonB;
    }

    /**
     *
     * @return
     */
    public JButton getButtonP() {
        return buttonP;
    }

    /**
     *
     * @return
     */
    public JLabel getLabelA() {
        return labelA;
    }

    /**
     *
     * @return
     */
    public JLabel getLabelB() {
        return labelB;
    }

    /**
     *
     * @return
     */
    public JLabel getLabelP() {
        return labelP;
    }

    /**
     *
     * @return
     */
    public JSlider getSliderA() {
        return sliderA;
    }

    /**
     *
     * @return
     */
    public JSlider getSliderB() {
        return sliderB;
    }

    /**
     *
     * @return
     */
    public JSlider getSliderP() {
        return sliderP;
    }
}

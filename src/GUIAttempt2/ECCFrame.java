/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

//import GUIAttempt1.*;
//import Quiz.QuizView;
import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import Quiz.AdditionQuiz;
import Quiz.ECGQuiz;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author benbenbenBEASTMODE
 */
public class ECCFrame extends JFrame implements KeyListener, Observer{
//private ECGModel mod;

    private ECGModel mod;
    private JTabbedPane tab;
//    private ZoomSliderView zoom;
    private PointAdditionView pav;
    private ECGraphView graph;
    private ECGControllerView ctrl;
    private Dimension frameSize;
    private DLPView dlp;
    private QuizController aq;
    private QuizView quizPanel;

    /**
     *
     */
    public ECCFrame() {
        super();
        this.setSize(750, 750);
        this.setTitle("Elliptic Curve Exploratory Application");
        mod = new ECGModel();
        initComponents();
        frameSize = this.getSize();
        this.addKeyListener(this);
        graph.addKeyListener(this);
        dlp.addKeyListener(this);
        this.setSize(newDimension(frameSize.width+160+22,frameSize.height+32));
        this.setResizable(false);
        try {        
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());//getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ECCFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ECCFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ECCFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ECCFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        frameSize.setSize(d);
    }

    /**
     * returns the preferred size of the controlPanel() which holds various
     * sliders and buttons in boxes. like the other get___Size() methods, it is
     * relative to the frameSize field, which is initialised at construction and
     * not modified.
     *
     * @return dimension which is the full width of the frame, and a quarter the
     * height
     */
    public Dimension getControlSize() {
        Dimension d = new Dimension(0, 0);
        d.setSize(frameSize.getWidth(), (frameSize.getHeight() * 0.25));
        //   Globals.print(""+"control size= " + d.getHeight() + " by " + d.getWidth());

        return d;
    }

    /**
     * returns the preferred size of a textField meant to display a bigInteger.
     * like the other get___Size() methods, it is relative to the frameSize
     * field, which is initialised at construction and not modified.
     *
     * @return dimension which is 1/15th the width of the full frame, and the
     * height of getControlBoxSize()
     */
    public Dimension getBigIntTextFieldSize() {
        Dimension d = new Dimension(getControlBoxSize());
        d.setSize(frameSize.getWidth() / 15, d.height);
        return d;
    }

    /* 
     * 
     * 
     * @return 3/4*frameSize.getHeight
     */
    private double getGraphHeight() {
        return this.frameSize.getHeight() * 3 / 4;// (int) (p * this.getScale());//
    }

    /**
     * THIS GOES TO ECGraphPanel! THIS IS THE MISSING LINK
     *
     * @return dimension with width = prime at P *(scale +2*graphShift), height
     * // * the same //
     */
//    public Dimension getGraphSize() {
//        Dimension d = new Dimension();
//        d.setSize((getP_prime()) * Math.abs(this.getScale()) + 2 * this.getGraphShift(), (getP_prime()) * Math.abs(this.getScale()) + 2 * this.getGraphShift());
//        return d;
//    }
    /**
     *
     * @return dimension with width = 3/4* frameSize.width(), 3/4
     * *framesize.height()
     */
    public Dimension getScrollPaneSize() {
        Dimension d = new Dimension(((int) frameSize.getWidth() * 3 / 4), ((int) (frameSize.getHeight() * 3 / 4)));
        //Globals.print("" + "graph size= " + d.getHeight() + " by " + d.getWidth());
        return d;
    }

    /**
     * see return
     *
     * @return 1/4 frameSize width and 3/4 framesize Height
     */
    Dimension getTabbedPaneSize() {
        Dimension d = new Dimension();
        d.setSize(frameSize.getWidth()  , frameSize.getHeight());// * 3 / 4);
        return d;
    }

    /**
     * currently returns true
     *
     * @return based on a keyboardlistener which knows if ctrl is currently
     * pushed
     */
    boolean isScrollToZoomEnabled() {
        return true;
    }

    /**
     * used to get the preferred size for a horizontal slider (intended for the
     * controlPanel()). like the other get___Size() methods, it is relative to
     * the frameSize field, which is initialised at construction and not
     * modified.
     *
     * @return dimension with width 1/6th the full frame, and height of the
     * controlBox
     */
    public Dimension getSliderSize() {
        Dimension d = new Dimension(getControlBoxSize());
        d.setSize(frameSize.getWidth() / 6, d.height);
        return d;
    }

    /**
     * this returns the preferred size. like the other get___Size() methods, it
     * is relative to the frameSize field, which is initialised at construction
     * and not modified.
     *
     * @return dimension with width = 1/6th of the full frame, and full height.
     */
    public Dimension getZoomSliderSize() {
        Dimension d = new Dimension();
        d.setSize(frameSize.getWidth() / 6, this.getECGEquationPanelSize().getHeight());
        return d;
    }

    /**
     * used to set the preferred width for buttons like the other get___Size()
     * methods, it is relative to the frameSize field, which is initialised at
     * construction and not modified.
     *
     * @return a dimension, width as 1/5th of the full frame width, height = the
     * return of getControlBoxSize()
     */
    public Dimension getFirstButtonWidth() {
        Dimension d = new Dimension();
        d.setSize(frameSize.getWidth() / 5, this.getControlBoxSize().getHeight());
        return d;
    }

    /**
     * returns the preferred size for the boxes in the controlPanel like the
     * other get___Size() methods, it is relative to the frameSize field, which
     * is initialised at construction and not modified.
     *
     * @return a dimension which is full frame width and 1/4 of 1/5th of the
     * full height (intended height would be 1/5th of the controlPanel
     */
    public Dimension getControlBoxSize() {
        Dimension d = new Dimension();
        d.setSize(frameSize.getWidth(), (frameSize.getHeight() / 4) / 5);
        Utilities.print("" + "controlbox size= " + d.getHeight() + " by " + d.getWidth());

        return d;
    }

    /**
     * returns the preferred size for the ECGEquationPanel
     *
     * @return dimension with full width of the frame, and the height/4/5
     * (intended height should be 1/5th of a panel which is 1/4th of the full
     * frame)
     */
    public Dimension getECGEquationPanelSize() {
        Dimension d = new Dimension(0, 0);
        d.setSize(frameSize.getWidth(), frameSize.getHeight() / 4 / 5);
        return d;

    }

    private Dimension newDimension(double w, double h) {
        Dimension d = new Dimension();
        d.setSize(w, h);
        return d;
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        System.err.println("creating the main Mod!");
//        mod = new ECGModel();
//        Box split = Box.createVerticalBox();
        JSplitPane split = new JSplitPane();
        Box top = Box.createHorizontalBox();
        this.graph = new ECGraphView(mod, newDimension(0.79* this.getWidth(), 0.79 * this.getHeight()));
        this.pav = new PointAdditionView(mod, newDimension(this.getWidth() * 0.2, this.getHeight() * 0.8));
        this.dlp = new DLPView(mod,newDimension(this.getWidth()*0.3, this.getHeight()*0.8));
        this.aq= new QuizController(mod, Globals.ADDITION_QUIZ);
        top.add(graph);
        tab = new JTabbedPane();
        tab.add("Adding Points", pav);
        tab.add("Discrete Logarithms", dlp);
        quizPanel = aq.getNewQuizPanel();
        tab.add("Quiz",quizPanel);
        top.add(tab);//new PointAdditionPanel(mod));
        split.setOrientation(JSplitPane.VERTICAL_SPLIT);
        Box controls = Box.createHorizontalBox();
        ECGControllerView ctrl= new ECGControllerView(mod, newDimension(.2*this.getWidth(),0.2*this.getHeight())); 
        controls.add(Box.createHorizontalStrut(3));
        controls.add(graph.getGraphControllerPanel());     
//        controls.add(Box.createGlue());
        controls.add(ctrl);

        split.setBottomComponent(controls);
        split.setLeftComponent(top);

        split.setDividerLocation(598);
        split.setDividerSize(0);
        split.setVisible(true);
        this.getContentPane().add(split);
        

        this.setVisible(true);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
//         try {
//            // Set cross-platform Java L&F (also called "Metal")
//        UIManager.setLookAndFeel(
//            UIManager.getSystemLookAndFeelClassName());
//    } 
//    catch (UnsupportedLookAndFeelException e) {
//       // handle exception
//    }
//    catch (ClassNotFoundException e) {
//       // handle exception
//    }
//    catch (InstantiationException e) {
//       // handle exception
//    }
//    catch (IllegalAccessException e) {
//       // handle exception
//    }
        ECCFrame ben = new ECCFrame();

    }

 
    @Override
    public void keyTyped(KeyEvent e) {
        //let's ignore it for now.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==17){
            graph.getGraphController().setScrollToZoom(true);
//            System.err.println("you pressed control. let it go!");
        }
        else if(e.getKeyCode()==81&&e.getModifiers()==2){
            System.err.println("exit?!");
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==17){
            graph.getGraphController().setScrollToZoom(false);
//            System.err.println("you let go of control.. ooo that was nice.");
        }
    }

    
    @Override
    public void update(Observable o, Object arg) {
//        if(mod.ECGHasChanged()){
//            System.err.println("changing quiz");
////            this.tab.remove(tab.getComponentAt(2));
//            quizPanel = aq.getNewQuizPanel();
//            tab.add("Quiz",quizPanel);
//            
//        }
    }
}

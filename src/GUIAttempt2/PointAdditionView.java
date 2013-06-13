/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author ben
 */
public class PointAdditionView extends JPanel implements  Observer {

    private ECGModel mod;
    private JEditorPane p;
    private JTextArea explanation;
    private JLabel result;
    private JButton add;
    private JButton dbl;

    /**
     *
     * @param theModel
     */
    public PointAdditionView(ECGModel theModel) {
        mod = theModel;
        mod.addObserver(this);
//        initCombos();
        initButtons();
        initTexts();
        addElements();
        this.setVisible(true);
    }
    /**
     *
     * @param mod
     * @param d
     */
    public PointAdditionView(ECGModel mod, Dimension d){
        this(mod);
//        this.setPreferredSize(d);
    }
      private void initTexts() {
        p = new JEditorPane();
//        Dimension d=new Dimension();
//        d.setSize(add.getWidth(),this.getHeight());
//       p.setPreferredSize(d);
//        p.setRows(mod.getSelectedPointRows());
        p.setEditable(false);
        p.setContentType("text/html");
//        p.setWrapStyleWord(true);
//        p.setLineWrap(true);
        
        p.setText(mod.getSelectedPointsText());
        p.setOpaque(false);
        explanation = new JTextArea();
        explanation.setEditable(false);
        explanation.setLineWrap(true);
        explanation.setWrapStyleWord(true);
        explanation.setText("This panel will assist you in calculating point addition on the elliptic curve group from below. "
                + "You may calculate mutliples of points by clicking on them. Clicking \"Add Selected\" will output the sum denoted below:");

        result = new JLabel();
//        result.setEditable(false);
//            result.setOpaque(false);
        result.setText(mod.addPointsFromList());


    }

    private void initButtons() {
//        add = new JButton("Add Selected Points");
//        //      dbl = new JButton("Double P");
//        add.setName("add");
//        //    dbl.setName("dbl");
//        add.addActionListener(this);
        //  dbl.addActionListener(this);
    }

    private void addElements() {
//        this.add(p);
//        this.add(q);
//        this.add(explanation);
//        this.add(add);
        Box b=Box.createVerticalBox();
        b.add(p);
        b.add(result);
        b.setVisible(true);
        this.add(b);
//        this.add(dbl);

    }

    private void updateText() {
        String pts="<html> <h3>";
        String cut =mod.getSelectedPointsText();
        String[] temp= cut.split("\\+");
        int i=0;
        for(String s: temp){
            if(s.indexOf('*')>=0){
                pts+= s +" + ";
//                System.err.println(s);
                i++;
                if(i%4==0){
                    pts+="<br>";
                }
            }
        }
        
        pts+="</h3></html>";
        p.setText(pts);
       result.setText("<html><h2><font color=\"green\">"+mod.addPointsFromList() + "</font></h2></html>");
    }
//    @Override
//    public void paintComponent(Graphics g){
//        g.fillRect(0,0,this.getWidth()-10,this.getHeight()-10);//, WIDTH, WIDTH, WIDTH);
//        //        this.setForeground(Color.red);
//    }

    @Override
    public void update(Observable o, Object arg) {
        updateText();
        p.revalidate();
        result.revalidate();
    }
}

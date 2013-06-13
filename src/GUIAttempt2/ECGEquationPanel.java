/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;


import java.awt.Dimension;
import java.awt.Graphics;
import java.text.AttributedString;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

/**
 *
 * @author ben
 */
public class ECGEquationPanel extends JPanel implements Observer{
   private ECGModel myModel;
   private AttributedString equation;
    /**
     *
     * @param o
     */
    public ECGEquationPanel(ECGModel o){
        myModel=o;
        //o.addObserver(this);
        this.makeObserve();
        this.equation=o.getECEquation();
//        this.setPreferredSize(myModel.getECGEquationPanelSize());
        
        //this.setPreferredSize(myModel.getECGEquationPanelSize());
    } 
    private void makeObserve(){
        myModel.addObserver(this);
    }
    @Override
    public void update(Observable o, Object arg) {
      //  System.out.println("before call eqtn= "+equation.toString());
        equation=myModel.getECEquation();
       // ..System.out.println(equation.toString());
        this.paint(this.getGraphics());
    }
    

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawString(equation.getIterator(), 5, 15);       // super.paint(g);

    }
    
    
   
}

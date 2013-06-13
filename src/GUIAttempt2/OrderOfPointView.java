/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author ben
 */
public class OrderOfPointView extends JPanel implements Observer {

    private ECGModel mod;
    private JComboBox list;
    private OrderOfPointController control;
    private JTextArea pointMultiples;
    private Box ben;

    /**
     *
     * @param theMod
     * @param d
     */
    public OrderOfPointView(ECGModel theMod, Dimension d) {
        mod = theMod;
        mod.addObserver(this);
        control = new OrderOfPointController(mod, this);
        this.setSize(d);
        this.initComponents();
        this.add(ben);
        this.setVisible(true);
    }
    
    private class PointOrderPanel extends JPanel{
        private double width,height;
        private OrderOfPointController ctrl;
        private double[] orders;
        private Rectangle2D[] markers;
        PointOrderPanel(OrderOfPointController ctrl, double w, double h){
            width=w;
            height= h;
            this.ctrl=ctrl;
            getOrders();
            createMarkers();
        }

        private void getOrders() {
           
            orders= new double[ctrl.getRandomPoints().length];
            for (int i = 0; i < orders.length; i++) {
                orders[i]=ctrl.getOrderPercent(i);
            }
        }
        // need to create lines...

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g1= (Graphics2D) g;
            g1.drawRect(0, 0, (int)width, (int)height);
            for (Rectangle2D r : markers) {
                g1.draw(r);
            }
        }

        private void createMarkers() {
            markers= new Rectangle2D[orders.length];
            for (int i = 0; i < orders.length; i++) {
                double dec= orders[i]/100;
                double xPos=width*dec-5;
                markers[i]= new Rectangle2D.Double(xPos, 0, 10, height);
            }
        }
        
        
        
    }

    @Override
    public void update(Observable o, Object arg) {
        list = new JComboBox(getPointList());
        pointMultiples.setText(mod.getMultiplesOfPointAsString());
        list.revalidate();
        pointMultiples.revalidate();
//        this.revalidate();
//        this.repaint();
    }

    private String[] getPointList() {
        return mod.getArrayOfPointStrings();
    }

    private void initComponents() {
        list = new JComboBox(getPointList());
        list.setMaximumRowCount(5);
//        list.setEditable(false);
        list.addActionListener(control);
        list.addItemListener(control);
        list.setName("pointList");
        pointMultiples = new JTextArea();
//        pointMultiples.setEditable(false);
        pointMultiples.setColumns("(   ,   ) * 3 = 4025204 ".length());
//        pointMultiples.(mod.getOrderedPoints().size());
        pointMultiples.setText(mod.getMultiplesOfPointAsString());
//        pointMultiples.setAutoscrolls(true);
        ben= Box.createVerticalBox();
        ben.add(list);
        ben.add(pointMultiples);
        ben.setVisible(true);
    }

    /**Given an array of points, let's create a string and display it 
     * 
     * @param multiplesOfPointAt 
     */
    void explainThesePoints(ECViewPoint[] p) {
        String b="The multiples of "+ p[0]+" are: \n";
        int i=1;
        for(i=1; i<p.length;i++){
            if(i%10==1&&i!=11){b+= "the "+i+"st ";}
            else if(i%10==2&&i!=12){b+="the "+i+"nd ";}
            else if(i%10==3&i!=13){b+="the "+i+"rd ";}
            else{ b+=" the "+i+"th ";}
            b+="multiple of "+p[0]+" is "+p[i]+"\n";
        }
        Utilities.print(b);
        b+=" Since "+p[0]+" + "+p[p.length-1]+" = "+ p[0]
                +"\n We say that the order of "+p[0]+" is "+i;
        this.pointMultiples.setText(b);
        this.revalidate();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComboBox;

/**
 *
 * @author ben
 */
class OrderOfPointController implements ActionListener, ItemListener, Observer{
    private OrderOfPointView view;
    private ECGModel mod;
    private ECViewPoint[] randomPoints;

    public OrderOfPointController(ECGModel mod, OrderOfPointView theView) {
        this.mod= mod;
        mod.addObserver(this);
        view= theView;
        createNewRandomPoints();
    }

    private void createNewRandomPoints(){
        randomPoints = new ECViewPoint[10];
        int[] temp= Utilities.getRandomizedArrayIndices(mod.getGroupSize());
        for (int i = 0; i < randomPoints.length; i++) {
            randomPoints[i]= mod.getPointAt(temp[i]);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox src= (JComboBox) e.getSource();
//        Globals.print("Action performed by OrderOfPointController!;");
        if(src.getName().equalsIgnoreCase("pointList")){
            int selectedPT= src.getSelectedIndex();
            //we want to have the mod calculate the multiples of the selectedPt;
            view.explainThesePoints(mod.getGroupGeneratedBy(selectedPT));
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Utilities.print("item changed!");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(mod.ECGHasChanged()){
            createNewRandomPoints();
        }
    }

    
    ECViewPoint[] getRandomPoints() {
        return randomPoints;
    }

    double getOrderPercent(int i) {
        return mod.getOrderOf(randomPoints[i])/mod.getGroupSize() *100;
    }
    
}

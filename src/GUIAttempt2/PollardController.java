/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author benbenbenultra
 */
class PollardController implements Observer, ActionListener {

    private PollardViewFrame view;
    private PollardModel mod;
    private String prevX2, prevX, ai1, ai2, bi1, bi2, c1, c2, d1, d2, fxi, fx2i;
    private int i;
    private String soln;
    private ArrayList<String> x;
    //<editor-fold defaultstate="collapsed" desc="getter Methods for fields ">
    public String getAi1() {
        return ai1;
    }

    public String getAi2() {
        return ai2;
    }

    public String getBi1() {
        return bi1;
    }

    public String getBi2() {
        return bi2;
    }

    public String getC1() {
        return c1;
    }

    public String getC2() {
        return c2;
    }

    public String getD1() {
        return d1;
    }

    public String getD2() {
        return d2;
    }

    public String getFxi() {
        return fxi;
    }

    public String getFx2i() {
        return fx2i;
    }//</editor-fold>

    PollardController(PollardModel mod, PollardViewFrame view) {
        this.mod = mod;
        i = 0;
        mod.addObserver(this);
        x= new ArrayList<String>();
        this.view = view;
        this.fxi = "0";
        this.fx2i = "0";
        this.soln = "0";
        this.setFields(mod.getLogsAt(i));
    }

    int getI() {
        return i;
    }

    void setFields(String[] logs) {
//output of pollardRho Log:
//        log[0]={a=2,b=5,c=2,d=5,X=(18,25),a=2,b=5,c'=2,d'=5,X'=(18,25),}
        //so...
        
        this.prevX = fxi;
        this.prevX2 = fx2i;
        ai1 = logs[0].substring(logs[0].indexOf("=") + 1);
        bi1 = logs[1].substring(logs[1].indexOf("=") + 1);
        c1 = logs[2].substring(logs[2].indexOf("=") + 1);
        d1 = logs[3].substring(logs[3].indexOf("=") + 1);
        fxi = logs[4].substring(logs[4].indexOf("=") + 1);
        ai2 = logs[5].substring(logs[5].indexOf("=") + 1);
        bi2 = logs[6].substring(logs[6].indexOf("=") + 1);
        c2 = logs[7].substring(logs[7].indexOf("=") + 1);
        d2 = logs[8].substring(logs[8].indexOf("=") + 1);
        fx2i = logs[9].substring(logs[9].indexOf("=") + 1);
        try {
            int x = (Integer.parseInt(c1) - Integer.parseInt(c2))
                    * Utilities.inversionModP(
                    (Integer.parseInt(d2) - Integer.parseInt(d1)), mod.getGroupSize());
            // if x > 0 return x mod n, otherwise make x>0and return x mod n.
            x = x % mod.getGroupSize();
            if (x < 0) {
                x += mod.getGroupSize();
            }
            soln = "" + x;
        } catch (ArithmeticException ae) {
            soln = "Not Invertible mod " + mod.getGroupSize();
        }
        // lastly we add the x values to the positions in the arraylist...
        x.add(i,fxi+"");
        x.add(2*i, fx2i+"");
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    String getPrevX2() {
        return prevX2;
    }

    String getPrevX() {
        return prevX;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.out.println("action listened!");
        if (e.getActionCommand().equalsIgnoreCase("i++")) {
                i++;
                view.enablePrevButton(true);
                setFields(mod.getLogsAt(i));
           if (i >= mod.getLogsLength()-1) {
                view.enableNextButton(false);
            }
        }
        if (e.getActionCommand().equalsIgnoreCase("i--")) {
            
                i--;
                view.enableNextButton(true);
                setFields(mod.getLogsAt(i));
            if(i==0){
                view.enablePrevButton(false);
            }
        }
        else{
//            for(int i=0; i<)
        }
        view.updateComponents();
    }

    String getSoln() {
        return soln;
    }

    String getX(int i) {
        if(i<0||i>x.size()-1){
            return "0";
        }
        return x.get(i);
    }
}

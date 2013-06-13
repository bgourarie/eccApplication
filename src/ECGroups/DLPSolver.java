/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import java.util.ArrayList;

/**
 *
 * @author benbenbenultra
 */
public abstract class DLPSolver {
    private int k;
    private ECPoint p,q;
    private ECGroup ecg;
    
    public DLPSolver(ECGroup e, ECPoint p, ECPoint q){
        this.p=p;
        this.q=q;
        this.ecg=e;
    }
    /**
     *
     * @return
     */
    public int solveDLP(){
        k = runAlgorithm();
        return k;
    }
    
    abstract int runAlgorithm();
    /**
     *
     * @return
     */
//    abstract ECPoint getActivePoint();
    /**
     *
     * @return
     */
    public int getStoredK(){
        return k;
    }
    /**
     *
     * @return
     */
    public ArrayList<String> getInfo(){
        return getLogs();
    }
    
    abstract ArrayList<String> getLogs();
    
    public ECPoint getPointP(){
        return p;
    }
   public  ECPoint getPointQ(){
        return q;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.math.BigInteger;

/**
 *
 * @author benbenbenBEASTMODE
 */
public class FieldPFactory implements FieldElementFactory {
    private BigInteger p;
    
    /**
     *
     * @param p
     */
    public FieldPFactory(BigInteger p){
        this.p=p;
    }
    /**
     *
     * @param i
     * @return
     */
    @Override
    public FieldElement getElement(int i){
        return  new FieldPElement(new BigInteger(""+i),this);
        //return el.mod(p);
    }

    /**
     *
     * @return
     */
    public BigInteger getP() {
        return p;
    } 
    /**
     *
     * @return
     */
    @Override
    public int getSize() {
        return p.intValue();
    } 
    /**
     *
     * @param mod
     * @return
     */
    @Override
    public FieldElement getElement(BigInteger mod) {
        return new FieldPElement(mod,this);
    }

    /**
     *
     * @param x
     * @return
     */
    @Override
    public boolean contains(FieldElement x) {
        return x.isFrom(this);
    }
    //   //<editor-fold defaultstate="collapsed" desc="main">
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
                FieldElementFactory fef=new FieldPFactory(new BigInteger(""+11));
                FieldElement[] arr=new FieldElement[fef.getSize()];
                for(int i=0; i < fef.getSize();i++){
                   // int ran=(int)(Math.random()*1000);
                    //Globals.print(""+"current randomer = "+ran);
                    arr[i]=fef.getElement(i);                   
                }
                for(FieldElement i:arr){
                    Utilities.print(""+i);
                }
                Utilities.print(""+"------------");
                Utilities.print(""+arr[2].negate()+" = negate "+arr[2].toString());//(arr[0]));
                Utilities.print(""+arr[1].subtract(arr[6]));
                Utilities.print(""+arr[3].inversionModP());
                Utilities.print(""+fef.contains(fef.getElement(2).add(fef.getElement(9)).multiply(fef.getElement(16))));
                 Utilities.print(""+fef.contains(new FieldPElement(Globals.bigInt(3),new FieldPFactory(Globals.bigInt(17)))));
                Utilities.print(""+arr[3].isFrom(new FieldPFactory(new BigInteger(""+11))));
//                Globals.print(""+arr[19].add(arr[19]));
            }
        //</editor-fold>

   

 
    
    
}

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
public class FieldPElement implements FieldElement {

    private BigInteger val, mod;
    private FieldElementFactory fef;

    /**
     *
     * @param b
     * @param mfpf
     */
    public FieldPElement(BigInteger b, FieldPFactory mfpf) {
        val = b.mod(mfpf.getP());
        mod = mfpf.getP();
        fef = mfpf;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    /**
     *
     * @param p
     * @return
     */
    @Override
    public FieldElement mod(FieldElement p) {
        return fef.getElement(val.mod(p.getVal()).mod(mod).intValue());
    }

    /**
     *
     * @param p
     * @return
     */
    @Override
    public FieldElement multiply(FieldElement p) {
        return fef.getElement(val.multiply(p.getVal()).mod(mod).intValue());

    }

    /**
     *
     * @param p
     * @return
     */
    @Override
    public FieldElement add(FieldElement p) {
        return fef.getElement(val.add(p.getVal()).mod(mod).intValue());
    }

    /**
     * inverts this FFelement modulo its modulus.
     *
     * @return
     */
    @Override
    public FieldElement inversionModP() {
        BigInteger u =val;
        //     Globals.print(""+"p = "+p+"u= "+u);
        BigInteger v = mod;
        BigInteger r, x, x1, x2;//Globals.print(""+"p = "+p+"v= "+v);
        //q=new BigInteger("1");
        x1 = new BigInteger("1");
        x2 = new BigInteger("0");
        while (!u.equals(Globals.ONE)) {
            BigInteger q = v.divide(u);
                       Utilities.print(""+"q, v, u= "+q+" "+v+" "+u);
            r = v.subtract(q.multiply(u));
                       Utilities.print(""+"r,v,q,u= "+r+" "+v+" "+q+" "+u);
            x = x2.subtract(q.multiply(x1));
                     Utilities.print(""+"x, x2, q, x1 = "+x+ " "+x2+" "+q+" "+x1);
            v = new BigInteger(u.toString());
            u = new BigInteger(r.toString());//r.abs();
            x2 = new BigInteger(x1.toString());
            x1 = new BigInteger(x.toString());
            Utilities.print(""+"v u x2 x1= "+v+" "+ u+" "+x2+ " "+x1);
        }
        // Globals.print(""+"inversion mod p = "+x1.mod(p));
        return fef.getElement(x1.mod(mod).intValue());
    }

    /**
     *
     * @return
     */
    @Override
    public BigInteger getVal() {
        return val;
    }

    /**
     *
     * @param RHS
     * @return
     */
    @Override
    public int compareTo(FieldElement RHS) {
        if(RHS.isNullVal()){
            return val.intValue();
        }
        return val.compareTo(RHS.getVal());
    }

    /**
     *
     * @param p
     * @return
     */
    @Override
    public FieldElement subtract(FieldElement p) {
        return this.add(p.negate());
    }

    /**
     *
     * @return
     */
    @Override
    public FieldElement negate() {
        return fef.getElement(this.val.negate().mod(mod));
    }

    /**
     * Divides this element by the given value
     *
     * @param p 
     * @return
     */
    @Override
    public FieldElement divide(FieldElement p) {
        //in modular arithmetic, division is just multiplying by the inversion of the denominator... so we multiply by the inversion of p
        return this.multiply(p.inversionModP());
    }
 
    /**
     *
     * @param fef
     * @return
     */
    @Override
    public boolean isFrom(FieldElementFactory fef) {
        return this.fef.getSize()== fef.getSize();//&& fef.getElement(val).equals(this);
        }
    
    /**
     *
     * @return
     */
    @Override
    public boolean isNullVal(){
        try{ 
            if(val.doubleValue()>0){
                         } return false;
        }catch(NullPointerException npe){
            return true;
        }
    }
}

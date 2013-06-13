/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import java.math.BigInteger;

//import org.jlinalg.polynomial.Polynomial;
//import ECGroups.Globals;

/**
 * defines a general elliptic curve from two constants a and b the EC will be
 * defined by the equation y^2=x^3+ax+b
 *
 *
 * @author benbenbenBEASTMODE
 */
public class EllipticCurve implements java.io.Serializable {

    FieldElement a, b;
//    Polynomial ec;

    /**
     *
     * @param a
     * @param b
     * @param ff
     */
    public EllipticCurve(int a, int b, FieldElementFactory ff) {
        this.a = ff.getElement(a);
        this.b = ff.getElement(b);
        //this.ec= 
    }

    /**
     *
     * @param a
     * @param b
     * @param ff
     */
    public EllipticCurve(BigInteger a, BigInteger b, FieldElementFactory ff) {
        this.a = ff.getElement(a.intValue());
        this.b = ff.getElement(b.intValue());
        //this.ec= 
    }

    /**
     *
     * @param a
     * @param b
     */
    public EllipticCurve(FieldElement a, FieldElement b) {
        this.a = a;
        this.b = b;
    }

    /**
     *
     * @return
     */
    public FieldElement getA() {
        return a;
    }

    /**
     *
     * @param a
     */
    public void setA(FieldElement a) {
        this.a = a;
    }

    /**
     *
     * @return
     */
    public FieldElement getB() {
        return b;
    }

    /**
     *
     * @param b
     */
    public void setB(FieldElement b) {
        this.b = b;
    }

    /**
     *
     * @return
     */
/*    public Polynomial getEc() {
        return ec;
    }
*/
    /**
     *
     * @param ec
     */
  /*  public void setEc(Polynomial ec) {
        this.ec = ec;
    }
*/
    /**
     * computes if a given pt (x,y, coordinates) will satisfy the equation for
     * this elliptic curve
     *
     * @param pt
     * @return true if pt is in the EC, false otherwise
     */
    public boolean contains(ECPoint pt) {
        //need to see if the pt will satisfy the equation... so we do y^2:
        FieldElement LHS = (pt.getY().multiply(pt.getY()));
        // then we do x^3 
        FieldElement RHS = ((pt.getX().multiply(pt.getX().multiply(pt.getX())))//x^3
                .add(b.add(pt.getX().multiply(a))));// +ax+b
        return LHS.compareTo(RHS) == 0;
    }

    @Override
    public String toString() {
        return "y^2=x^3+" + a + "x+" + b;
    }
}
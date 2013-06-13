/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import java.math.BigInteger;

/**
 *
 * @author benbenbenBEASTMODE
 */
public interface FieldElement {

    /**
     *
     * @param p
     * @return
     */
    FieldElement add(FieldElement p);

    /**inverts this FFelement modulo its modulus.
     *
     * @return
     */
    FieldElement inversionModP();

    /**
     *
     * @param p
     * @return
     */
    FieldElement mod(FieldElement p);

    /**
     *
     * @param p
     * @return
     */
    FieldElement multiply(FieldElement p);
    /**
     *
     * @param p
     * @return
     */
    FieldElement subtract(FieldElement p);
    /**
     *
     * @return
     */
    FieldElement negate();
    /**
     *
     * @param p
     * @return
     */
    FieldElement divide(FieldElement p);
    /**
     *
     * @return
     */
    BigInteger getVal();
    /**
     *
     * @return
     */
    @Override
    public String toString();
    /**
     *
     * @param fef
     * @return
     */
    boolean isFrom(FieldElementFactory fef);

    /**
     *
     * @param RHS
     * @return
     */
    public int compareTo(FieldElement RHS);

    /**
     *
     * @return
     */
    public boolean isNullVal();
}

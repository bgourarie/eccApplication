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
public interface FieldElementFactory {

    /**
     *
     * @param i
     * @return
     */
    FieldElement  getElement(int i);
    /**
     *
     * @return
     */
    int getSize();

    /**
     *
     * @param mod
     * @return
     */
    FieldElement getElement(BigInteger mod);

    /**
     *
     * @param x
     * @return
     */
    public boolean contains(FieldElement x);
}

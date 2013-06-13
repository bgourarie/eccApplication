/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import ECCToolBox.Globals;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.math.BigInteger;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * can verify that given specs are valid all static methods
 *
 * @author benbenbenBEASTMODE
 */
public class SpecCheck {
    
    /**
     * just checks if the given parameters over the given prime field will cause a repeated root.
     *
     * @param a
     * @param b
     * @param p
     * @return
     */
    public static boolean ECRepeatedRootsPrime(BigInteger a, BigInteger b, BigInteger p) {
        return (a.pow(3).multiply(Globals.bigInt(4)).add(b.pow(2).multiply(Globals.bigInt(27)))).mod(p) == BigInteger.ZERO;
    }
  
    /**checks that the input is valid and the parameters will not cause any repeated roots. 
     *
     * @param a
     * @param b
     * @param p
     * @throws ParameterError if input is invalid... 
     */
    private static void checkUserInput(int a, int b, int p) throws ParameterError {
        BigInteger bigA = new BigInteger("" + a);
        BigInteger bigB = new BigInteger("" + b);
        BigInteger bigP = new BigInteger("" + p);
        if (!ECRepeatedRootsPrime(bigA, bigB, bigP)) {
            //EC is valid
            //    return true;
        } else {
            throw new ParameterError(("Parameters "+a+" and "+b+" cause a repeated root"), a, b, "" + p);
        }
    }
     /**wrapper method that simply doesn't include a groupsize.
     *
     * @param a
     * @param b
     * @param p
     * @return
     */
    public static AttributedString getParamExplanation(int a, int b, int p){
     return getParamExplanation(a,b,p,0);
     }
    /**Gives an equation and field sentence, or explains why the EC does not make a group with given parameters.
     * 
     * @param a
     * @param b
     * @param p
     * @param groupSize 
     * @return an equation explaining the group; or the text of the parameter error if the specs cause one.
     */
    public static AttributedString getParamExplanation(int a, int b, int p, int groupSize) {
        try {
            checkUserInput(a, b, p);
            AttributedString ec = getECEquationAndField(a, b, p, groupSize);
            
            //AttributedString fp = getField(p);
            return ec;
            
        } catch (ParameterError pe) {
            return new AttributedString(pe.getMessage());
        }
    }
    /*
     public static AttributedString getField(int p) {
        
     AttributedString Fp = new AttributedString(f);
     Fp.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, 1, f.length());
     return Fp;
     }*/
    /** gives an attributed string "THe equation at y... over fintie field p...." etc
     * 
     * @param a
     * @param b
     * @param p
     * @param groupSize 
     * @return 
     */
    public static AttributedString getECEquationAndField(int a, int b, int p, int groupSize) {
        String ecIntro = "The elliptic curve y",
                exp1 = "2",
                eqSign = "=x",
                exp2 = "3",
                aXPlusB = "+" + a + "x+" + b,
                f = " over the field F",
                prime = "" + p,
                contains = " contains "+groupSize+" points.";
               //The Elliptic curve y3=x3+ax+b over the field Fp will contain approximately x points
        int LHS = ecIntro.length() + exp1.length(),
                preExp2 = ecIntro.length() + exp1.length() + eqSign.length(),
                postExp2 = ecIntro.length() + exp1.length() + eqSign.length() + exp2.length(),
                ECE = postExp2 + aXPlusB.length(),
                subP = ECE + f.length(),
                postSubP = subP + prime.length();        
        //Globals.print(""+a+" a and b "+b+" and p "+p);
      //  Font font1 = new Font("Arial", Font.ITALIC, 16); 
        AttributedString ECGSentence = new AttributedString(ecIntro + exp1 + eqSign + exp2 + aXPlusB+f+prime+contains);//+hasseThrm+points);
        ECGSentence.addAttribute(TextAttribute.SIZE, 14);
        ECGSentence.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, ecIntro.length(), LHS);
        ECGSentence.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, preExp2, postExp2);
        ECGSentence.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, subP, postSubP);
     
        return ECGSentence;
    }
  
    /**given a point, it returns an attributedString representation of the point
     *
     * @param p
     * @return
     */
//    public static AttributedString ListPoint(ECPoint p){
//        if(p.equals(Globals.NOT_A_POINT)){
//            return new AttributedString("No point");
//        }
//        return new AttributedString(p.toString());
//    }
}

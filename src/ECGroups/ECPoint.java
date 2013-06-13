/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;
import ECCToolBox.Globals;
//import java.math.BigInteger;

/**defines a point on an elliptic curve, knowing the values of a,b to define the curve, and the value of x,y that exist at the point
 *does not check to ensure that it really is on the curve...
 * @author benbenbenBEASTMODE
 */
public class ECPoint implements Comparable{
    private FieldElement x,y;
    private String toString;
    private int constant;
    
    /**
     *
     * @param x
     * @param y
     */
    public ECPoint(FieldElement x, FieldElement y) {
        this.x = x;
        this.y = y;
      //  this.ec = ec;
        constant=Globals.FALSE;
        toString= "("+x.toString()+","+y.toString()+")";
        /* if(!this.ec.contains(this)) {
            throw new ParameterError(Globals.ECPT_ERR,this,ec,"ECPoint");
        }*/ 
    }
    /**
     * creates an ECPoint with no parameters, need to set them to values using set methods
     */
    public ECPoint(){
        
    }
   
    /**
     * 
     * @param inf "infinity" for Point at Infinity, otherwise causes error
     */
    public ECPoint(String inf){
        if(inf.equalsIgnoreCase(Globals.INF_STRING)) {
            this.toString=inf;
            this.x=null; this.y=null;
            constant=Globals.TRUE;
        }
        else{
            //exception?
        }
            
    }

    /**
     *
     * @return
     */
    public FieldElement getX() {
        return x;
    }

//    @Override
//    public boolean equals(Object o){
//        try{
//            ECPoint p = (ECPoint) o;
//            if(this.isConstant()||p.isConstant()){
//                return this.isConstant()&&p.isConstant();
//            }
//            return (p.getX().compareTo(this.getX())==0)&&(p.getY().compareTo(this.getY())==0);
//        }catch(ClassCastException cce){
//            System.err.println("You tried to compare an ECPoint to something else?!");
//            return false;
//        }
//    }
    /**
     *
     * @return
     */
    public FieldElement getY() {
        return y;
    }


    /**
     *
     * @return
     */
    public int getConstant() {
        return constant;
    }
    /**
     *
     * @return
     */
    public boolean isConstant(){
      return constant!=0;
  }

    @Override
    public String toString() {
        if(constant==Globals.TRUE){
            return toString;           
        }
        else{
            return "("+this.getX().toString()+","+this.getY().toString()+")";
        } // (x,y)
    }
    @Override
    public ECPoint clone(){
        return this.isConstant()? new ECPoint(Globals.INF_STRING) : new ECPoint(this.x,this.y);
    }
    
   /*
    public static void main(String[] args) {
        FiniteField ben = new FFPrime(new BigInteger("23"));
        EllipticCurve ec = new EllipticCurve(11,13,ben);
        ECPoint P = new ECPoint(new BigInteger("6"),new BigInteger("11"),ec);
        ECPoint Q = new ECPoint(new BigInteger("15"),new BigInteger("17"),ec);
     ECPoint dblP=P.doubleP();
        Globals.print("P= "+P+"\nQ="+Q);
        Globals.print("Double P = "+dblP);
      //  Globals.print("P +Q = "+P.add(Q));
       
        
    }*/

    @Override
    public int compareTo(Object t) {
        ECPoint p = (ECPoint) t;
//        if (p.isConstant()){
//            if(this.isConstant()){
//                return 0;
//            }
//            else{
//                return this.x.getVal().intValue();
//            }
//        }
        try{
            FieldElement px= p.getX();

            FieldElement py= p.getY();
            if(px.compareTo(this.x)==0){
                return this.y.compareTo(py);
            }else{
                return this.x.compareTo(px);
            }
        }catch (NullPointerException npe){
            if( this.isConstant()){
                return -1;
            }
            else{
                return +1;
            }
        }
    }
}

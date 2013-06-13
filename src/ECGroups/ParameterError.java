/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ECGroups;

import ECCToolBox.Globals;

/**
 *
 * @author benbenbenBEASTMODE
 */
public class ParameterError extends java.lang.Error{
    private String errType;
    /**
     *
     * @param error
     */
    public ParameterError(String error){
        super(error);
        errType=Globals.GEN_ERR;
    }
    /**
     *
     * @param ERR_TYPE
     * @param o
     * @param o2
     * @param source
     */
    public ParameterError(String ERR_TYPE, Object o, Object o2, String source){
        super("Error: "+ERR_TYPE+ " "+o+" "+o2+" from "+source);
        errType=ERR_TYPE;
        
    }
    /**returns the type of the error (as referenced from Globals)
     * 
     * @return 
     */
    public String getType(){
        return errType;
    }
}

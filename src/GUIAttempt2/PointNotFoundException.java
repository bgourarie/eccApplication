/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

/**
 *
 * @author benbenbenultra
 */
class PointNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Supplied point was not found. This message is useless.";
    }

    
    public PointNotFoundException() {
        
    }
    
}

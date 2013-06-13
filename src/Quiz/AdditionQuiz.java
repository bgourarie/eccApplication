/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Quiz;

import ECCToolBox.Globals;
import ECGroups.*;
import ECCToolBox.Utilities;
/**
 *
 * @author ben
 */
public class AdditionQuiz extends ECGQuiz {

    /**
     *
     * @param n
     * @param ecg
     */
    public AdditionQuiz(int n, ECGroup ecg){
        super(n,ecg);
    }
    @Override
    void createQuestions(int n) {
        chooseRandomPoints();
        // now we have two random points PQ. we will create a few question objects based on that.
        Question[] questions = new Question[n];
        
        if(n>=3){ // i have some ideas for 3 addition questions...
            //q1: P+Q
            questions[0]= new Question(
                    "For questions 1-3, let P = "+getP()+" and Q= "+getQ()+
                    "\n What is P+Q?"
                    ,Utilities.ArrayToString(randomAnswers(4,getECG().add(getP(),getQ())))
                    ,1);
            // q2: what is 2P ?
            questions[1]= new Question(
                    "What is 2*P (where P= "+getP().toString()+") ?"
                    ,Utilities.ArrayToString(randomAnswers(4,getECG().doubleP(getP())))
                    ,2);
            questions[2]= new Question(
                    "What is 3*P+Q? "
                    ,Utilities.ArrayToString(randomAnswers(4,getECG().add(getECG().add(getP(), getQ()), getECG().doubleP(getP()))))
                    ,3);
            for(int i= 3;i<n;i++){ //if i have a way to get random points, then i can add unlimited questions.
                this.chooseRandomPoints();
                questions[i]= new Question(                        
                    "Now let P = "+getP()+" and Q= "+getQ()+
                    " What is P+Q?"
                    ,Utilities.ArrayToString(randomAnswers(4,getECG().add(getP(),getQ())))
                    ,i+1);
            }
        getTheQuiz().setQuestions(questions);    
        }
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
//      ECGQuiz test= new AdditionQuiz(4, Globals);
        
    }
    
}

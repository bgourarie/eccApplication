/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Quiz;

import ECGroups.*;
import ECCToolBox.Utilities;
import Quiz.Question;
import Quiz.Quiz;
import java.util.ArrayList;

/**abstract class for creating quizzes relative to elliptic curve groups 
 * 
 * @author ben
 */
public abstract class ECGQuiz {
    /**
     *
     */
    public static int ADDITION_QUIZ=234;
    private Quiz theQuiz;
    private ECPoint P, Q;
    private final ECGroup ecg;
    private int[] indices;
    private static int randInd;
    /**
     *
     * @return
     */
    public ECPoint getP() {
        return P;
    }
    
    /**
     *
     * @param P
     */
    public void setP(ECPoint P) {
        this.P = P;
    }
    
    /**
     *
     * @return
     */
    public ECPoint getQ() {
        return Q;
    }
    
    /**
     *
     * @param Q
     */
    public void setQ(ECPoint Q) {
        this.Q = Q;
    }
    
    /**
     *
     * @return
     */
    public Quiz getTheQuiz() {
        return theQuiz;
    }

    /**
     *
     * @return
     */
    public ECGroup getECG() {
        return ecg;
    }

//    private String[] questions;

    /**
     *
     * @param ecg
     */
    public ECGQuiz(ECGroup ecg) {
        this(4, ecg);
    }

    /**
     *
     * @return
     */
    public ECPoint getRandomPoint() {
        int n = (int) (Math.random() * 100 % ecg.getElementList().size());
        return ecg.getElementList().get(n);
    }

    void chooseRandomPoints() {
        this.indices = Utilities.getRandomizedArrayIndices(ecg.getSize());
        randInd=0;
        setP(ecg.getElementAt(indices[randInd++]));
        setQ(ecg.getElementAt(indices[randInd++]));
    }

    /**
     * returns n random answers
     *
     * @param n number of answers to return
     * @param ans the correct answer
     * @return an array of n strings, with index 0 being the correct answer
     */
    ECPoint[] randomAnswers(int n, ECPoint ans) {
        ECPoint[] answers = new ECPoint[n];
        answers[0] = ans;
        for (int i = 1; i < answers.length&& i<(ecg.getSize()-2); i++) {
            //sometimes this goes out of bounds...
            
            answers[i]= ecg.getElementAt(indices[randInd++]);
//            Utilities.print("Incremented RandInd to get "+randInd);
            if(answers[i].toString().equalsIgnoreCase(answers[0].toString())){
                i--;
            }
        }
        return answers;
    }
    
    /**
     *
     * @param n
     * @param ecg
     */
    public ECGQuiz(int n, ECGroup ecg) {
        theQuiz = Quiz.getBlankQuiz(n);
        this.ecg = ecg;
        this.createQuestions(n);
        
    }

    /**
     * this will be used to create Question objects, storing the Questions into
     * the Quiz object.
     *
     * @param n
     */
    abstract void createQuestions(int n);
    
    /**
     *
     */
    public void resetQuiz(){
        theQuiz.reset();
//        Utilities.print("Reset quiz, score is: "+theQuiz.getScore());
    }
    /**
     *
     * @return
     */
    public int getNumberOfQuestions() {
        return theQuiz.getQuestions().length;
    }
    /**
     *
     * @return
     */
    public int getScore(){
        return theQuiz.getScore();
    }
    /**
     *
     * @param i
     * @return
     */
    public Question getQuestionAt(int i) {
        return theQuiz.getQuestionAt(i);
    }
//    public void answerQuestionAt(int i, String ans){
//        getQuestionAt(i).setCurrentAnswer(ans);
//    }

    /**
     *
     * @param answers
     * @return
     */
    public int mark(String[] answers) {
      return theQuiz.checkAnswersGetScore();
    }

    /**
     *
     * @return
     */
    public int markQuiz() {
//        String[] ans = new String[theQuiz.getQuestions().length];
//        for (int i = 0; i < ans.length; i++) {
//            ans[i] = theQuiz.getQuestionAt(i).getCurrentAnswer();            
//        }
//        theQuiz.checkAnswers(ans);
        return theQuiz.checkAnswersGetScore();
    }

}

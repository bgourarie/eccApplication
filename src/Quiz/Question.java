/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Quiz;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ben
 */
public class Question {

    private String question, correctAnswer;
    private String[] answers;
    private boolean isAnswered, isCorrect;
    private int id;
    private String currentAnswer;

    /**
     *
     * @return
     */
    public int getTitle() {
        return id;
    }

    /**
     *
     * @param title
     */
    public void setTitle(int title) {
        this.id = title;
    }

    /**Question 
     *
     * @param question String representation of the question
     * @param answers array of possible answers. It assumes the first answer is correct
     * @param id The id of the question, e.g. intended to be used so that "Question"+ id prints as "Question 2"
     */
    public Question(String question, String[] answers, int id) {
        this(question, answers);
        this.id = id;
    }

    /**
     * Should only be used to reset a quiz, it requires a shuffled set of
     * answers, and a known correct answer.
     *
     * @param question
     * @param answers
     * @param title
     * @param correctAnswer
     */
    Question(String question, ArrayList<String> answers, int title, String correctAnswer) {
        this(question, Utilities.convertArrayListToArrayString(answers), title);
        this.correctAnswer = correctAnswer;
    }

    /**
     *does not set the id# for the quiz, using -1 to indicate it is unset.
     * @param question String rep of the question
     * @param answers array of string representation of answers, with the first (answers[0]) being the correct one
     */
    public Question(String question, String[] answers) {
        this.question = question;
        correctAnswer = answers[0];
        this.shuffleAnswers(answers);
        this.isAnswered = false;
        this.isCorrect = false;
        id = -1;
        currentAnswer = "No answer selected";
    }

    /**
     * re-orders the answers.
     * not currently used
     */
    public void reshuffle() {
        this.shuffleAnswers(this.answers);
    }

    /**
     *
     * @param i index of answer to return
     * @return likely to be unpredictable from constructed question due to the reshuffle() method
     */
    public String getAnswerAt(int i) {
        return answers[i];
    }

    /**
     *
     * @return
     */
    public String getQuestionText() {
        return this.question;
    }

    /**
     *self explanatory.
     * @return
     */
    public int getNumberOfAnswers() {
        return answers.length;
    }

    /**
     * currently not valid, but should be used to know if the question has been
     * answered validly.
     *
     * @return
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    /**
     *
     * @return true if an answer has been set for this question
     */
    public boolean isAnswered() {
        return isAnswered;
    }

    /**
     * 
     * @param t
     */
    public void setAnswered(boolean t) {
        isAnswered = t;
    }

   
    /**
     *
     * @param ans
     * @return true if the supplied answer matches the correct answer
     */
    public boolean isAnswerCorrect(String ans) {
        return ans.equalsIgnoreCase(correctAnswer);
    }

    /**
     *
     * @return the correct answer stored by this question
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    private void shuffleAnswers(String[] newAnswers) {
        // shuffle the answers up and store them in "answers"
        ArrayList<String> ans = new ArrayList();
        for (String s : newAnswers) {
            ans.add(s);
        }

        Collections.shuffle(ans);

        this.answers = new String[ans.size()];
        for (int i = 0; i < this.answers.length; i++) {
            this.answers[i] = ans.get(i);
        }
    }

    /**
     * sets this to be in the answered state 
     * @param ans answer to store as current answer
     */
    public void setCurrentAnswer(String ans) {
        this.currentAnswer = ans;
        if(this.isAnswerCorrect(ans)){
            isCorrect=true;
        }else{
            isCorrect=false;
        }
    }

    /**
     *
     * @return "" if the question is unanswered, otherwise the current selected answer
     */
    public String getCurrentAnswer() {
        return currentAnswer;
    }
}

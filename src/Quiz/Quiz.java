/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Quiz;

import ECCToolBox.Globals;
import ECCToolBox.Utilities;
import java.util.ArrayList;

/** General quiz object
 *
 * @author ben
 */
public class Quiz {
   private int score;
   private boolean isCompleted;
   private Question[] questions;

    /**Creates a new quiz with an array of Question objects and starts the current score at 0
     *
     * @param questions
     */
    public Quiz(Question[] questions) {
        this.questions = questions;
        score=0;
    }
    /**returns a new blankquiz. just for convenience
     *
     * @param numOfQuestions number of questions
     * @return a Quiz object which must have each question manually set using {@link setQuestion()}
     */
    public static Quiz getBlankQuiz(int numOfQuestions){
        return new Quiz(new Question[numOfQuestions]);
    }

    /**set's the question stored at n to the question q
     * 
     * @param n must be less than the number of questions
     * @param q  a Question object
     */
    public void setQuestion(int n, Question q){
        questions[n]=q;
    }
    /**
     *replaces this Quiz's question array with the given array.
     * @param q
     */
    public void setQuestions(Question[] q){
        questions=q;
    }
    /**
     *
     * @return the quizzes questions
     */
    public Question[] getQuestions(){
        return questions;
    }
    /**
     * for each question, it checks if it has been answered
     * @return the number of question which ahve been answered and have the correct answer.
     */
    public int checkAnswersGetScore(){
        score= 0;
        for (Question q : questions) {
            Utilities.print("Question "+q.getTitle()+" current ans: "+q.getCurrentAnswer()+" correct answer: "+q.getCorrectAnswer());
            if(q.isAnswered()&&q.isCorrect()){
                score++;
            }
        }
        return score;
    }
 
    /**
     * 
     * @return  current score
     */
    public int getScore(){
        return score;
    }
    /** returns the question stored at that index
     * 
     * @param i
     * @return 
     */
    public Question getQuestionAt(int i) {
        return questions[i];
    }

    /**resets the questions without changing their answers, and shuffles the order of their answers
     * 
     */
    public void reset() {
         int j=0;
        for (Question q:this.getQuestions()){
            String quest= q.getQuestionText();// save the question
            ArrayList<String> answers= new ArrayList<String>();
            for (int i=0;i<q.getNumberOfAnswers();i++) {
                answers.add(q.getAnswerAt(i));
            }//save the answers
            String corrAns= q.getCorrectAnswer();
            int id= q.getTitle();
            java.util.Collections.shuffle(answers);
            q=new Question(quest, answers,id,corrAns);
            this.setQuestion(j, q);
            j++;
        }
        this.score=0;
        this.isCompleted=false;
       
    }
   
   
   
    
}

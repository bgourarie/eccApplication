/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

//import Quiz.QuizView;
import ECCToolBox.Globals;
import Quiz.ECGQuiz;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author ben
 */
class QuizController implements ActionListener, Observer {

    private QuizView view;
    private ECGModel mod;
    private ECGQuiz ourQuiz;
    private int quizType;
    private boolean quizChanged;

    public QuizController(ECGModel theModel, int quizType) {
        mod = theModel;
        mod.addObserver(this);
        this.quizType = quizType;
        getNewQuiz();
//        getNewQuizPanel();
    }

    public QuizView getNewQuizPanel() {
        view = new QuizView(ourQuiz, this);
        return view;
    }

    public QuizView getCurrentQuizView() {
        return view;
    }

    void updateView() {
        view.updateQuiz(ourQuiz);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.err.println(e.getActionCommand());
        if (e.getActionCommand().equalsIgnoreCase(Globals.SUBMIT_QUESTION)) {
            ourQuiz.markQuiz();
        }
        if (e.getActionCommand().equalsIgnoreCase(Globals.RESET_QUIZ)) {
            ourQuiz.resetQuiz();
        }

        System.err.println("view dimension="+view.getPreferredSize()+" view width="+view.getWidth()+" view height= "+view.getHeight());
        view.updateQuiz(ourQuiz);

    }

    @Override
    public void update(Observable o, Object arg) {
        this.getNewQuiz();
        view.updateQuiz(ourQuiz);
    }
    public boolean getQuizChanged(){
        return quizChanged;
    }

    private void getNewQuiz() {
//        try{
//        quizChanged = ourQuiz.getQuestionAt(0).getQuestionText().equalsIgnoreCase(mod.getQuiz(quizType).getQuestionAt(0).getQuestionText());
        
//        System.err.println("quiz has changed in quizcontroller");
        ourQuiz = mod.getQuiz(quizType);
    }
}

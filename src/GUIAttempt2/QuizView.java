/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECCToolBox.Globals;
import Quiz.ECGQuiz;
import Quiz.Question;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

/**
 *
 * @author benbenben
 */
public class QuizView extends JPanel {

    private Box holdAll, controls;
    private JButton submit, reset;
    private JLabel marks;
    private QuestionView[] questions;
    private QuizController control;
    private String[] answers;
    private int score;
    private boolean isCompleted;

    private void initSwingComponents() {
        holdAll = Box.createVerticalBox();
        controls = Box.createHorizontalBox();
        marks = new JLabel("Marks: " + score);
        submit = new JButton("Submit");
        submit.setActionCommand(Globals.SUBMIT_QUESTION);
        submit.addActionListener(control);
        reset = new JButton("Reset");
        reset.setActionCommand(Globals.RESET_QUIZ);
        reset.addActionListener(control);
    }

    private void addToContainers() {
        for (QuestionView q : questions) {
            holdAll.add(q);
        }
        controls.add(submit);
        controls.add(reset);
        controls.add(marks);
        holdAll.add(controls);
        this.add(holdAll);
    }

    private class QuestionView extends JPanel implements ActionListener {

        Box holdAll, choiceBox, titleBar;
        ButtonGroup choices;
        JRadioButton[] answerButtons;
        String title, questionText, selectedAnswer, status;
        String[] answers;
        Question q;
        JTextArea question;
        JLabel statusLabel, titleLabel;

        QuestionView(Question q) {
            this.q = q;
            this.removeAll();
            initComponents(q); //create swing containers
            initQuestionDetails(q); // store the important bits of the question to display
            updateSwingContainers(); // add the stored info to the GUI
            
        }

        //this needs to accept a quiz and use that to draw everything. EVERYTHING.
        private void initComponents(Question que) {
            holdAll = Box.createVerticalBox();
            choiceBox = Box.createHorizontalBox();
            titleBar= Box.createHorizontalBox();
            answerButtons = new JRadioButton[que.getNumberOfAnswers()];
            initButtons();
            question = new JTextArea();
            question.setEditable(false);
            question.setOpaque(false);
            statusLabel = new JLabel();
            titleLabel= new JLabel();
        }

        private void initButtons() {
            choices = new ButtonGroup();
            for (JRadioButton j : answerButtons) {
                j = new JRadioButton();
            }
        }

        private void initQuestionDetails(Question que) {
            title = "Question " + que.getTitle();
            questionText = que.getQuestionText();
            status = que.isAnswered() ? "Your answer: " + (que.isCorrect()? "Correct":"Wrong") : "Unanswered";
            if(status.contains("rong")){
                statusLabel.setForeground(Color.red);
            }
            answers= new String[que.getNumberOfAnswers()];
            for (int i = 0; i < answers.length; i++) {
                answers[i]= que.getAnswerAt(i);                
            }

        }

        private void updateSwingContainers() {
            question.setText(questionText);
            titleLabel.setText(title);
            titleBar.add(titleLabel);
            titleBar.add(Box.createVerticalStrut(15));
            statusLabel.setText(status);
            titleBar.add(statusLabel);
            holdAll.add(titleBar);            
            question.setText(questionText);
            holdAll.add(question);
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i]= new JRadioButton(answers[i]);
                answerButtons[i].setActionCommand(answers[i]);
                choiceBox.add(answerButtons[i]);
                answerButtons[i].addActionListener(this);
                choices.add(answerButtons[i]);
                choices.setSelected(answerButtons[i].getModel(), answers[i].equalsIgnoreCase(q.getCurrentAnswer()));                
            }            
            holdAll.add(choiceBox);            
            this.add(holdAll);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            q.setAnswered(true);
            q.setCurrentAnswer(e.getActionCommand());
        }
    }

    /**
     *
     * @param quiz
     * @param ctrl
     */
    public QuizView(ECGQuiz quiz, QuizController ctrl) {
        control = ctrl;
        initSwingComponents();
        updateQuiz(quiz);
        addToContainers();
        this.setVisible(true);
    }

    /**
     *
     * @param quiz
     */
    public void updateQuiz(ECGQuiz quiz) {
        this.score = quiz.getScore();  
        holdAll.removeAll();
        this.removeAll();
        questions= new QuestionView[quiz.getNumberOfQuestions()];
        for (int i = 0; i < questions.length; i++) {
            questions[i] = new QuestionView(quiz.getQuestionAt(i));
        }
        this.initSwingComponents();     
        addToContainers();
        this.revalidate();
        this.repaint();
    }
}

package com.example.controller;

import com.example.AnswerTemp;
import com.example.domain.*;
import com.example.repository.QuizRepository;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-03-21.
 */
@Controller
public class QuizController {
    ArrayList<User> users;
    ArrayList<Question> questions;
    ArrayList<Quiz> quizzes;
    ArrayList<Answer> answers;
    ArrayList<Player> playerList = new ArrayList<>();
    int currentCorrectAnswer = 1;
    int playersAnswered = 0;
    int i1 = 0;

    @Autowired
    private SimpMessagingTemplate temp;

    @Autowired
    DataSource dataSource;

    @Autowired
    private QuizRepository repository;

    @PostConstruct
    public void RefreshUsers(){
        users = (ArrayList<User>) repository.getUsers();
    }
    @PostConstruct
    public void RefreshQuiz(){
        questions = (ArrayList<Question>) repository.getQuestions();
        quizzes = (ArrayList<Quiz>) repository.getQuizes();
        answers = (ArrayList<Answer>) repository.getAnswers();
    }
/*    @GetMapping("")
    public ModelAndView startPage(){
        return new ModelAndView("quiz/index");
    }*/
    @GetMapping("/2")
    public ModelAndView page2(){
        return new ModelAndView("quiz/index2");
    }


/*
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String messageSender(String message) throws Exception {
        return (message.toString());
    }
*/
    @MessageMapping("/alias")
    @SendTo("/topic/aliases")
    public ArrayList<Player> addingAlias(String alias) {
        playerList.add(new Player(alias, 0));
        return playerList;
    }

    public void nextQuestion() {

        repository.setCurrentQuestion(repository.getCurrentQuestion()+1);

            this.temp.convertAndSend("/topic/quiz", quizChannel());
            playersAnswered = 0;
            i1++;

    }
    public void showResults(Content content) {
        System.out.println("showResults");
        this.temp.convertAndSend("/topic/results", content);
    }

    @MessageMapping("/connect")
    @SendTo("/topic/quiz")
    public Content quizChannel(){
        if(questions.size() != repository.getCurrentQuestion()) {
        System.out.println("Currentquestion: " + repository.getCurrentQuestion());
        String s = "";

        s = "{\"question\":\""+questions.get(repository.getCurrentQuestion()).getText()+"\"";
        int questionCount = 1;
        for(int i=0; i<answers.size(); i++){
            if(questions.get(repository.getCurrentQuestion()).getQuestionID() == answers.get(i).getQuestion_ID()) {
                s += ", \"text" + questionCount + "\": \"" + answers.get(i).getText() + "\"";
                if (answers.get(i).isCorrect()) {
                    currentCorrectAnswer = questionCount;
                }
                questionCount++;
            }
        }
        s += ", \"img_URL\":\""+questions.get(repository.getCurrentQuestion()).getImg_URL()+"\"";
        s += "}";


        return new Content(s);
    }
    showResults(new Content(""));
    return new Content("");
    }

    @MessageMapping("/answer")
    @SendTo("/topic/answers")
    public Content playerAnswered(String message){
        playersAnswered++;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            AnswerTemp answerTemp = objectMapper.readValue(message, AnswerTemp.class);
            System.out.println(answerTemp);
            if(answerTemp.getOptionSelected() == currentCorrectAnswer){
                System.out.println(answerTemp.getPlayerAlias()+" has answered correctly");
                for(int i=0; i<playerList.size();i++){
                    if(playerList.get(i).getAlias().equalsIgnoreCase(answerTemp.getPlayerAlias())){
                        playerList.get(i).setScore(playerList.get(i).getScore()+1);
                        System.out.println(playerList.get(i).getScore());
                        break;
                    }
                }
            }

            if(playersAnswered == repository.getNumberOfConnections()) {
                nextQuestion();
            }
            return new Content(answerTemp.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Content("");
    }
}

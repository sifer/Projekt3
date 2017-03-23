package com.example.controller;

import com.example.AnswerTemp;
import com.example.domain.Content;
import com.example.domain.Answer;
import com.example.domain.Question;
import com.example.domain.Quiz;
import com.example.domain.User;
import com.example.repository.QuizRepository;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    ArrayList<String> aliasList = new ArrayList<>();

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
    public ArrayList<String> addingAlias(String alias) {
        aliasList.add(alias);
        return aliasList;
    }

    @MessageMapping("/connect")
    @SendTo("/topic/quiz")
    public Content quizChannel(){
        String s = "";
        int index = 0;
        s = "{\"question\":\""+questions.get(index).getText()+"\"";
        int questionCount = 1;
        for(int i=0; i<answers.size(); i++){

            if(questions.get(index).getQuestionID() == answers.get(i).getQuestion_ID()){
                s += ", \"text"+questionCount+"\": \""+answers.get(i).getText()+"\"";
                questionCount++;
                s += ", \"isCorrect\": \"   "+answers.get(i).isCorrect()+"\"";
            }
        }
        s += ", \"img_URL\":\""+questions.get(index).getImg_URL()+"\"";
        s += "}";
        System.out.println(s);
        return new Content(s);
    }

    @MessageMapping("/answer")
    @SendTo("/topic/answers")
    public Content playerAnswered(String message){

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            AnswerTemp answerTemp = objectMapper.readValue(message, AnswerTemp.class);

            System.out.println(answerTemp);
            return new Content(answerTemp.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Content("");
    }
}

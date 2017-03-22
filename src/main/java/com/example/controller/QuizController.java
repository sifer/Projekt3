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
    @GetMapping("")
    public ModelAndView startPage(){
        return new ModelAndView("quiz/index");
    }
    @GetMapping("/2")
    public ModelAndView page2(){
        return new ModelAndView("quiz/index2");
    }


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String messageSender(String message) throws Exception {
        return (message.toString());
    }

    @MessageMapping("/connect")
    @SendTo("/topic/quiz")
    public Content quizChannel(){
        String s = "";
        s = "{\"question\":\""+questions.get(1).getText()+"\"";
        int questionCount = 1;
        for(int i=0; i<answers.size(); i++){

            if(questions.get(1).getQuestionID() == answers.get(i).getQuestion_ID()){
                s += ", \"text"+questionCount+"\": \""+answers.get(i).getText()+"\"";
                questionCount++;
            }
        }
        s += "}";
        return new Content(s);
    }

    @MessageMapping("/answer")
    @SendTo("/topic/answers")
    public Content playerAnswered(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            AnswerTemp answerTemp = objectMapper.readValue(message, AnswerTemp.class);
            System.out.println(answerTemp.toString());

            return new Content(answerTemp.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Content("");
    }
}

package com.example;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Created by Administrator on 2017-03-21.
 */
@Controller
public class QuizController {

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

    @MessageMapping("/answer")
    @SendTo("/topic/answers")
    public Content playerAnswered(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            Answer answer = objectMapper.readValue(message, Answer.class);
            System.out.println(answer.toString());

            return new Content(answer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Content("");
    }
}

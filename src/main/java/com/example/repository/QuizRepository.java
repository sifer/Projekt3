package com.example.repository;

import com.example.domain.Answer;
import com.example.domain.Question;
import com.example.domain.Quiz;
import com.example.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017-03-22.
 */
@Component
public interface QuizRepository {
    List<Answer> getAnswers();
    List<Question> getQuestions();
    List<Quiz> getQuizes();
    List<User> getUsers();
}

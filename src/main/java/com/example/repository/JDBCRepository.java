package com.example.repository;

import com.example.domain.Answer;
import com.example.domain.Question;
import com.example.domain.Quiz;
import com.example.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-03-22.
 */
@Component
public class JDBCRepository implements QuizRepository{

    int numberOfConnections = 0;
    int currentQuestion = 0;
    int numberOfAnswers = 0;

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Answer> getAnswers() {
        try(Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT AnswerID, Text, IsCorrect, Question_ID FROM Answer")){
            ArrayList<Answer> answers = new ArrayList<>();
            while(resultSet.next()){
                answers.add(resultSetAnswer(resultSet));
            }
            return answers;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Question> getQuestions() {
        try(Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT QuestionID, Text, Img_URL, Quiz_ID FROM Question WHERE Quiz_ID = 15")){
            ArrayList<Question> questions = new ArrayList<>();
            while(resultSet.next()){
                questions.add(resultSetQuestion(resultSet));
            }
            return questions;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Quiz> getQuizes() {
        try(Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT QuizID, Name, User_ID FROM Quiz")){
            ArrayList<Quiz> quizes = new ArrayList<>();
            while(resultSet.next()){
                quizes.add(resultSetQuiz(resultSet));
            }
            return quizes;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        try(Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT QuizID, Name, User_ID FROM Quiz")){
            ArrayList<User> users = new ArrayList<>();
            while(resultSet.next()){
                users.add(resultSetUser(resultSet));
            }
            return users;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int setNumberOfConnections(int input) {
        numberOfConnections += input;
        return (numberOfConnections);
    }

    @Override
    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    @Override
    public int getCurrentQuestion() {
        return currentQuestion;
    }   

    @Override
    public void setCurrentQuestion(int input) {
        currentQuestion = input;
    }

    @Override
    public void setNumberOfAnswers(int input) {
        numberOfAnswers = input;
    }

    @Override
    public int getNumberOfAnswers() {
        return numberOfAnswers;
    }

    private Quiz resultSetQuiz(ResultSet resultSet) throws SQLException{
        return new Quiz(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3));
    }
    private Question resultSetQuestion(ResultSet resultSet) throws SQLException{
        return new Question(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getInt(4));
    }
    private Answer resultSetAnswer(ResultSet resultSet) throws SQLException{
        return new Answer(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getInt(4));
    }
    private User resultSetUser(ResultSet resultSet) throws SQLException{
        return new User(resultSet.getInt(1), resultSet.getString(2),resultSet.getString(3));
    }
}

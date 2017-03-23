package com.example;

import com.example.controller.QuizController;
import com.example.repository.QuizRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 * Created by Administrator on 2017-03-23.
 */
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

    @Autowired
    private QuizRepository repository;

    private final Log logger = LogFactory.getLog(PresenceChannelInterceptor.class);

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

        // ignore non-STOMP messages like heartbeat messages
        if(sha.getCommand() == null) {
            return;
        }

        String sessionId = sha.getSessionId();

        switch(sha.getCommand()) {
            case CONNECT:
                System.out.println("connecting");
                repository.setNumberOfConnections(1);
                logger.debug("STOMP Connect [sessionId: " + sessionId + "]");
                break;
            case CONNECTED:
                System.out.println("Connected");

                logger.debug("STOMP Connected [sessionId: " + sessionId + "]");
                break;
            case DISCONNECT:
                System.out.println("disconnected" + sessionId);
                repository.setNumberOfConnections(-1);

                logger.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
                break;
            default:
                break;

        }
    }
}

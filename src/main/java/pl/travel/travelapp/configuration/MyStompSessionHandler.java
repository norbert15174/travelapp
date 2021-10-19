package pl.travel.travelapp.configuration;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class MyStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession stompSession , StompHeaders stompHeaders) {
        //stompSession.subscribe("/topic/1", this);
    }

    @Override
    public void handleException(StompSession stompSession , StompCommand stompCommand , StompHeaders stompHeaders , byte[] bytes , Throwable throwable) {

    }

    @Override
    public void handleTransportError(StompSession stompSession , Throwable throwable) {
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders , Object o) {
    }
}

package pl.travel.travelapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import pl.travel.travelapp.entites.ChatMessage;

import java.util.concurrent.ExecutionException;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketClientConfig {

    private final static String url = "/app/chat/";

    public WebSocketClient webSocketClient(Long id, ChatMessage message) throws ExecutionException, InterruptedException {
        final WebSocketClient client = new StandardWebSocketClient();

        final WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final StompSessionHandler sessionHandler = new MyStompSessionHandler();
        String sendUrl = url + id;
        stompClient.connect("ws://localhost:8020/chat" , sessionHandler).get().send(sendUrl , message);

        return client;
    }
}

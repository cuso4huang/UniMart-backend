package org.jnu.unimart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单内存消息代理，并设置前缀为/topic和/queue
        config.enableSimpleBroker("/topic", "/queue");
        // 应用目的地前缀
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点，并启用SockJS备份选项
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*") // 根据需要限制跨域
                .withSockJS();
    }
}

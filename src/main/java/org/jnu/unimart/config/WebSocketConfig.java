package org.jnu.unimart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.messaging.simp.config.ChannelRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // 允许所有来源访问
                .withSockJS();  // 启用SockJS回退选项
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单的消息代理
        config.enableSimpleBroker("/topic", "/queue");
        // 设置应用的消息前缀
        config.setApplicationDestinationPrefixes("/app");
        // 设置用户目的地前缀
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration
                .setMessageSizeLimit(128 * 1024)     // 消息大小限制为128KB
                .setSendBufferSizeLimit(512 * 1024)   // 发送缓冲区大小限制为512KB
                .setSendTimeLimit(20000);             // 发送超时时间为20秒
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()
                .corePoolSize(4)     // 核心线程数
                .maxPoolSize(8)      // 最大线程数
                .keepAliveSeconds(60);// 线程空闲时间
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()
                .corePoolSize(4)     // 核心线程数
                .maxPoolSize(8)      // 最大线程数
                .keepAliveSeconds(60);// 线程空闲时间
    }
}
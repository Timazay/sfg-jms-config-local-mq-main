package guru.springframework.sfgjms.config;

import com.typesafe.config.Config;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * Created by jt on 2019-07-17.
 */
@Configuration
public class JmsConfig {
    public final static String CHILD_QUEUE = "child-queue-";

    @Bean
    public ArtemisConfigurationCustomizer customizer() {
        return new ArtemisConfigurationCustomizer() {
            @Override
            public void customize(org.apache.activemq.artemis.core.config.Configuration configuration) {
                try {
                    configuration.addAcceptorConfiguration("netty",
                            "tcp://localhost:8161?minLargeMessageSize=2500");
                } catch (Exception e) {
                    throw new RuntimeException("Failed to add netty transport acceptor to artemis instance", e);
                }
            }
        };
    }

    @Bean
    public MessageConverter messageConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


}

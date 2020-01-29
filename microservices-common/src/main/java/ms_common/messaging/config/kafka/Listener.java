package ms_common.messaging.config.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class Listener {

    private final String topic;

    @KafkaListener(topics = "#{__listener.topic}",
            groupId = "#{__listener.topic}.group")
    public void listen() {

    }

    public String getTopic() {
        return this.topic;
    }

}

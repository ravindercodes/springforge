package com.ravindercodes.producer;

import com.ravindercodes.constant.KafkaConstants;
import com.ravindercodes.dto.model.EmailVerificationTokenModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EmailVerificationProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVerificationToken(EmailVerificationTokenModel emailRequest) {
        kafkaTemplate.send(KafkaConstants.EMAIL_VERIFICATION_TOPIC, emailRequest);
    }
}


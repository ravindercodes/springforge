package com.ravindercodes.producer;

import com.ravindercodes.constant.KafkaConstants;
import com.ravindercodes.dto.model.EmailVerificationTokenModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendVerificationToken(EmailVerificationTokenModel emailRequest) {
        kafkaTemplate.send(KafkaConstants.EMAIL_VERIFICATION_TOPIC, emailRequest);
    }
}


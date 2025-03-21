package com.ravindercodes.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravindercodes.constant.KafkaConstants;
import com.ravindercodes.dto.model.EmailVerificationTokenModel;
import com.ravindercodes.util.EmailUtility;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationConsumer {

    private final EmailUtility emailUtility;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmailVerificationConsumer(EmailUtility emailUtility) {
        this.emailUtility = emailUtility;
    }

    @KafkaListener(topics = KafkaConstants.EMAIL_VERIFICATION_TOPIC, groupId = KafkaConstants.EMAIL_VERIFICATION_GROUP_ID)
    public void listenEmailTopic(@Payload String emailVerificationTokenModel) {
        try {
            EmailVerificationTokenModel emailVerificationToken = objectMapper.readValue(emailVerificationTokenModel, EmailVerificationTokenModel.class);
            emailUtility.sendEmailVerificationToken(emailVerificationToken);
        } catch (Exception e) {
            System.err.println("Failed to convert JSON to EmailVerificationTokenModel: " + e.getMessage());
        }
    }
}

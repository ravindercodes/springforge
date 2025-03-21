package com.ravindercodes.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravindercodes.constant.KafkaConstants;
import com.ravindercodes.dto.model.ResetPasswordEmailModel;
import com.ravindercodes.util.EmailUtility;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordConsumer {

    private final EmailUtility emailUtility;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResetPasswordConsumer(EmailUtility emailUtility) {
        this.emailUtility = emailUtility;
    }

    @KafkaListener(topics = KafkaConstants.RESET_PASSWORD_EMAIL_TOPIC, groupId = KafkaConstants.RESET_PASSWORD_EMAIL_GROUP_ID)
    public void listenEmailTopic(@Payload String resetPasswordModel) {
        try {
            ResetPasswordEmailModel resetPassword = objectMapper.readValue(resetPasswordModel, ResetPasswordEmailModel.class);
            emailUtility.resetPassword(resetPassword);
        } catch (Exception e) {
            System.err.println("Failed to convert JSON to EmailVerificationTokenModel: " + e.getMessage());
        }
    }

}

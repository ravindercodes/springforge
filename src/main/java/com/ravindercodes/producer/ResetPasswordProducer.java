package com.ravindercodes.producer;

import com.ravindercodes.constant.KafkaConstants;
import com.ravindercodes.dto.model.ResetPasswordEmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void resetPassword(ResetPasswordEmailModel resetPasswordEmailModel) {
        kafkaTemplate.send(KafkaConstants.RESET_PASSWORD_EMAIL_TOPIC, resetPasswordEmailModel);
    }

}

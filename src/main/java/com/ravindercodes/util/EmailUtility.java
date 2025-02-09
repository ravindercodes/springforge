package com.ravindercodes.util;

import com.ravindercodes.constant.TemplateConstant;
import com.ravindercodes.dto.model.EmailVerificationTokenModel;
import com.ravindercodes.exception.custom.EmailSendFailedEx;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailUtility {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.from}")
    private String fromEmail;

    public boolean sendEmailVerificationToken(EmailVerificationTokenModel emailVerificationTokenModel) {

        boolean isEmailSent = false;

        try {
            MimeMessageHelper helper = createEmailHelper();

            helper.setFrom(fromEmail);
            helper.setTo(emailVerificationTokenModel.getToEmail());

            if (emailVerificationTokenModel.getCcEmails() != null && emailVerificationTokenModel.getCcEmails().length > 0) {
                helper.setCc(emailVerificationTokenModel.getCcEmails());
            }

            if (emailVerificationTokenModel.getBccEmails() != null && emailVerificationTokenModel.getBccEmails().length > 0) {
                helper.setBcc(emailVerificationTokenModel.getBccEmails());
            }

            helper.setSubject(emailVerificationTokenModel.getSubject());

            Context context = new Context();
            context.setVariable("username", emailVerificationTokenModel.getUsername());
            context.setVariable("verificationToken", UrlUtility.getVerificationUrl(emailVerificationTokenModel.getVerificationToken()));
            String htmlContent = templateEngine.process(TemplateConstant.EMAIL_VERIFICATION, context);

            helper.setText(htmlContent, true);
            mailSender.send(helper.getMimeMessage());
            isEmailSent = true;
        } catch (MessagingException e) {
            isEmailSent = false;
            throw new EmailSendFailedEx("Failed to send OTP email to " + emailVerificationTokenModel.getToEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return isEmailSent;
    }

    private MimeMessageHelper createEmailHelper() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        return new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }
}

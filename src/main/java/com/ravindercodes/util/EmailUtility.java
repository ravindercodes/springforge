package com.ravindercodes.util;

import com.ravindercodes.constant.TemplateConstant;
import com.ravindercodes.dto.model.EmailVerificationTokenModel;
import com.ravindercodes.dto.model.ResetPasswordEmailModel;
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

    public void sendEmailVerificationToken(EmailVerificationTokenModel emailVerificationTokenModel) {
        try {
            MimeMessageHelper helper = createEmailHelper();

            helper.setFrom(fromEmail);
            helper.setTo(emailVerificationTokenModel.getToEmail());

            helper.setSubject(emailVerificationTokenModel.getSubject());

            Context context = new Context();
            context.setVariable("username", emailVerificationTokenModel.getUsername());
            context.setVariable("verificationToken", UrlUtility.getVerificationUrl(emailVerificationTokenModel.getVerificationToken()));
            String htmlContent = templateEngine.process(TemplateConstant.EMAIL_VERIFICATION, context);

            helper.setText(htmlContent, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            throw new EmailSendFailedEx("Failed to send OTP email to " + emailVerificationTokenModel.getToEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void resetPassword(ResetPasswordEmailModel resetPasswordEmailModel) {
        try {
            MimeMessageHelper helper = createEmailHelper();

            helper.setFrom(fromEmail);
            helper.setTo(resetPasswordEmailModel.getToEmail());

            helper.setSubject(resetPasswordEmailModel.getSubject());

            Context context = new Context();
            context.setVariable("username", resetPasswordEmailModel.getUsername());
            context.setVariable("resetPasswordLink", UrlUtility.getResetPasswordUrl(resetPasswordEmailModel.getVerificationToken()));
            String htmlContent = templateEngine.process(TemplateConstant.RESET_PASSWORD, context);

            helper.setText(htmlContent, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            throw new EmailSendFailedEx("Failed to send password reset email to " + resetPasswordEmailModel.getToEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MimeMessageHelper createEmailHelper() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        return new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }
}

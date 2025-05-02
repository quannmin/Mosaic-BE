package com.mosaic.service.impl;

import com.mosaic.exception.custom.MessagingException;
import com.mosaic.service.spec.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetOtp(String to, String otp) {
        String subject = "Mã đặt lại mật khẩu của bạn";
        String body = String.format(
                "Kính gửi người dùng,\n\n" +
                        "Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng sử dụng mã dưới đây để tiếp tục:\n\n" +
                        "Mã: %s\n\n" +
                        "Mã này sẽ hết hạn sau 5 phút.\n\n" +
                        "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này hoặc liên hệ hỗ trợ.\n\n" +
                        "Trân trọng,\n" +
                        "Đội ngũ hỗ trợ",
                otp);
        sendEmail(to, subject, body);
    }

    @Override
    public void sendAccountActivationOtp(String to, String otp) {
        String subject = "Kích hoạt tài khoản của bạn";
        String body = String.format(
                "Kính gửi người dùng,\n\n" +
                        "Cảm ơn bạn đã đăng ký tài khoản. Vui lòng sử dụng mã dưới đây để kích hoạt tài khoản:\n\n" +
                        "Mã: %s\n\n" +
                        "Mã này sẽ hết hạn sau 24 giờ.\n\n" +
                        "Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này.\n\n" +
                        "Trân trọng,\n" +
                        "Đội ngũ hỗ trợ",
                otp);
        sendEmail(to, subject, body);
    }
}

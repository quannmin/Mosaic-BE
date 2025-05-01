package com.mosaic.service.spec;

import java.util.Map;

public interface EmailService {
    public void sendAccountActivationOtp(String to, String otp);
    public void sendPasswordResetOtp(String to, String otp);
//    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables);
    public void sendEmail(String to, String subject, String body);
    }

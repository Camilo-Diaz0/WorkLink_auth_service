package com.example.service_auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String frontendUrl;
    private final String remitente;
        
    public EmailService(JavaMailSender mailSender,
                    @Value("${app.frontend-url}") String frontendUrl,
                    @Value("${spring.mail.username}") String remitente) {
                        
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
        this.remitente = remitente;
    }

    public void enviarVerificacion(String destino, String token) {
        String enlace = frontendUrl + "/verificar?token=" + token;
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);          // <-- la misma cuenta de Gmail
        mensaje.setTo(destino);
        mensaje.setSubject("Verifica tu cuenta en WorkLink");
        mensaje.setText(
            "Bienvenido a WorkLink.\n\n" +
            "Para activar tu cuenta haz clic en el siguiente enlace:\n" +
            enlace + "\n\n" +
            "El enlace vence en 24 horas. Si tú no creaste esta cuenta, ignora este mensaje."
        );
        mailSender.send(mensaje);
    }

    public void enviarRecuperacion(String destino, String token) {
        String enlace = frontendUrl + "/nueva-password?token=" + token;
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destino);
        mensaje.setSubject("Recuperación de contraseña en WorkLink");
        mensaje.setText(
            "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
            "Para elegir una nueva contraseña haz clic en el siguiente enlace:\n" +
            enlace + "\n\n" +
            "El enlace vence en 1 hora. Si tú no solicitaste el cambio, ignora este mensaje."
        );
        mailSender.send(mensaje);
    }
}
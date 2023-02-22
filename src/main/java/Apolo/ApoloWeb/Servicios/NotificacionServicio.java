package Apolo.ApoloWeb.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class NotificacionServicio {
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String mailFrom;
    
    
    @Async
    public void enviarMail(String cuerpo, String asunto, String mailTo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(mailTo);
        mensaje.setFrom(mailFrom);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        
        mailSender.send(mensaje);
    }
    
    

}

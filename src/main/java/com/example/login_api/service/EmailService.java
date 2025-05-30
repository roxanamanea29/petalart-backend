package com.example.login_api.service;

import com.example.login_api.entity.Order;
import com.example.login_api.entity.UserEntity;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final SendGrid sendGridClient;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendOrderConfirmation(UserEntity user, Order order) {
        Email form = new Email("no-reply@petalart.com", "PetalArt");
        Email to = new Email(user.getEmail(), user.getFirstName() + " " + user.getLastName());
        String subject = "Confirmación de tu pedido de - PetalArt #" + order.getId();
        String content = String.format(
                "Hola %s,\n\n" +
                "Gracias por tu pedido en PetalArt. Tu número de pedido es #%d.\n\n" +
                        "Tu pedido #%d ha sido recibido. Total: %.2f €\n" +
                        "Detalles: %s/orders/%d\n\n" +
                "¡Gracias por confiar en nosotros!\n\n" +
                "Saludos,\n" +
                "El equipo de PetalArt",
                user.getFirstName(),
                order.getId(),
                order.getId(),
                order.getTotal(),
                frontendUrl,
                order.getId()
        );
        Mail mail = new Mail(form, subject, to,  new Content("text/plain", content));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGridClient.api(request);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de confirmación de pedido", e);
        }

    }
}

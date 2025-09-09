package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/* La clase EmailUtil encapsula la logica para enviar correos de texto plano
 * mediante SMTP (usando Gmail como proveedor) esta utilidad es utilizada, por
 * ejemplo, para notificar eventos como la creacion, actualizacion o eliminacion
 * de un cliente en el sistema.*/

public class EmailUtil {

    private static final String REMITENTE = "salvatoredeleone.10@gmail.com";
    private static final String CONTRASENA = "iscy nayo kswy mbob"; 
    private static final String DESTINATARIO = "raphinha11.2005@gmail.com"; 

    public static void enviarCorreo(String asunto, String cuerpo) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        //Crea una sesion autenticada con el servidor SMTP (Protocolo de transferencia Simple) usando el correo y la contraseña
        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(REMITENTE, CONTRASENA);
                }
            });

        try {
        	//Creacion y envio del mensaje
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(DESTINATARIO));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);

            System.out.println("✅ Correo enviado correctamente.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("❌ Error al enviar el correo.");
        }
    }
}

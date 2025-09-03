package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.http.Part;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.*;

import java.io.*;
import java.util.Properties;

@WebServlet("/CorreoServlet")
@MultipartConfig // <-- Habilita subida de archivos
public class CorreoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String correoDestino = request.getParameter("correo");

        try (PrintWriter out = response.getWriter()) {
            if (correoDestino == null || correoDestino.trim().isEmpty()) {
                out.println("<h3>Error: Debes proporcionar un correo válido.</h3>");
                out.println("<a href='formulario.jsp'>Volver</a>");
                return;
            }

            // Obtener el archivo PDF del formulario
            Part archivo = request.getPart("archivo");

            if (archivo == null || archivo.getSize() == 0) {
                out.println("<h3>Error: Debes seleccionar un archivo PDF.</h3>");
                out.println("<a href='formulario.jsp'>Volver</a>");
                return;
            }

            // Leer el contenido del archivo como byte[]
            byte[] contenidoArchivo = archivo.getInputStream().readAllBytes();

            // Enviar el correo
            boolean enviado = enviarCorreoConAdjunto(
                correoDestino,
                "Reporte PDF adjunto",
                "Se adjunta el archivo PDF enviado desde el formulario.",
                contenidoArchivo,
                archivo.getSubmittedFileName()
            );

            if (enviado) {
                out.println("<h3>Correo enviado correctamente a " + correoDestino + "</h3>");
            } else {
                out.println("<h3>Error al enviar el correo.</h3>");
            }

            out.println("<a href='formulario.jsp'>Volver al formulario</a>");
        }
    }

    private boolean enviarCorreoConAdjunto(String destinatario, String asunto, String cuerpo, byte[] archivoAdjunto, String nombreArchivo) {
        final String remitente = "salvatoredeleone.10@gmail.com"; // Tu correo
        final String password = "iscy nayo kswy mbob";         // Tu app password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // ✅ Solución clave

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        session.setDebug(true); // Muestra logs de envío SMTP

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Parte de texto
            MimeBodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);

            // Parte del adjunto
            MimeBodyPart adjunto = new MimeBodyPart();
            ByteArrayDataSource source = new ByteArrayDataSource(archivoAdjunto, "application/pdf");
            adjunto.setDataHandler(new DataHandler(source));
            adjunto.setFileName(nombreArchivo); // Usa el nombre original

            // Juntar
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            message.setContent(multipart);

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}

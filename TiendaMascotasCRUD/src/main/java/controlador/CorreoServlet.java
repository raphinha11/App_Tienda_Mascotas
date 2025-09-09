package controlador;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import jakarta.activation.DataHandler;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/*Este servlet permite a un usuario enviar un correo electronico con un archivo
 * PDF adjunto.
 * El archivo es recibido a traves de un formulario, validado (Tipo MIME y tamaño)
 * y luego se envia usando la API de jakarta Mail.
 * Se utiliza las credenciales SMTP de una cuenta Gmail para el envio.*/

@WebServlet("/CorreoServlet")
@MultipartConfig
public class CorreoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String correoDestino = request.getParameter("correo");

        try (PrintWriter out = response.getWriter()) {

        	// Validación: correo obligatorio
            if (correoDestino == null || correoDestino.trim().isEmpty()) {
                out.println("<h3>Error: Debes proporcionar un correo válido.</h3>");
                return;
            }

            Part archivo = request.getPart("archivo");

            // Validación: archivo obligatorio
            if (archivo == null || archivo.getSize() == 0) {
                out.println("<h3>Error: Debes seleccionar un archivo PDF.</h3>");
                out.println("<a href='formulario.jsp'>Volver</a>");
                return;
            }

            // Validar tipo MIME
            String tipo = archivo.getContentType();
            if (!"application/pdf".equals(tipo)) {
                out.println("<h3>Error: Solo se permiten archivos PDF.</h3>");
                out.println("<a href='formulario.jsp'>Volver</a>");
                return;
            }

            // Debug en consola 
            System.out.println("Archivo recibido: " + archivo.getSubmittedFileName());
            System.out.println("Tamaño del archivo: " + archivo.getSize());
            System.out.println("Tipo MIME: " + tipo);

            // Leer el archivo como arreglo de bytes
            byte[] contenidoArchivo = archivo.getInputStream().readAllBytes();

            // Enviar correo
            boolean enviado = enviarCorreoConAdjunto(
                correoDestino,
                "Reporte PDF adjunto",
                "Se adjunta el archivo PDF enviado desde el sistema de clientes.",
                contenidoArchivo,
                archivo.getSubmittedFileName()
            );

            // Mostrar resultado al usuario
            if (enviado) {
                out.println("<h3 style='color:green;'>Correo enviado correctamente a " + correoDestino + "</h3>");
            } else {
                out.println("<h3 style='color:red;'>Error al enviar el correo.</h3>");
            }

            out.println("<a href='formulario.jsp'>Volver al formulario</a>");
        }
    }

    // Método privado que realiza el envío real del correo con el archivo adjunto
    private boolean enviarCorreoConAdjunto(String destinatario, String asunto, String cuerpo, byte[] archivoAdjunto, String nombreArchivo) {
        final String remitente = "salvatoredeleone.10@gmail.com";
        final String password = "iscy nayo kswy mbob";  

        // Configuración del servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        session.setDebug(true); // Muestra información de depuración en consola

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Parte de texto
            MimeBodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);

            // Parte del archivo adjunto
            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new ByteArrayDataSource(archivoAdjunto, "application/pdf")));
            adjunto.setFileName(nombreArchivo);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            // Establecer contenido al mensaje
            message.setContent(multipart);

             // Enviar el correo
            Transport.send(message);
            System.out.println("Correo enviado con archivo adjunto: " + nombreArchivo);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}

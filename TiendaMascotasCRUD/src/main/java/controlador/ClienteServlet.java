package controlador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import modelo.Cliente;
import modelo.ClienteDAO;
import util.EmailUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    ClienteDAO dao = new ClienteDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        switch (accion) {
            case "Guardar":
                Cliente c = new Cliente();
                c.setNumeroIdentidad(Integer.parseInt(request.getParameter("numero_identidad")));
                c.setNombres(request.getParameter("nombres"));
                c.setApellidos(request.getParameter("apellidos"));
                c.setDireccion(request.getParameter("direccion"));
                c.setTelefono(request.getParameter("telefono"));
                dao.agregar(c);

               //Notificacion por correo
                EmailUtil.enviarCorreo(
                    "Nuevo cliente registrado",
                    "Se registró un nuevo cliente:\n" +
                    "Nombre: " + c.getNombres() + " " + c.getApellidos() +
                    "\nTeléfono: " + c.getTelefono()
                );
                break;

            case "Actualizar":
                Cliente c2 = new Cliente();
                c2.setIdCliente(Integer.parseInt(request.getParameter("id_cliente")));
                c2.setNumeroIdentidad(Integer.parseInt(request.getParameter("numero_identidad")));
                c2.setNombres(request.getParameter("nombres"));
                c2.setApellidos(request.getParameter("apellidos"));
                c2.setDireccion(request.getParameter("direccion"));
                c2.setTelefono(request.getParameter("telefono"));
                dao.actualizar(c2);

              //Notificacion por correo
                EmailUtil.enviarCorreo(
                    "Cliente actualizado",
                    "Se actualizó el cliente:\n" +
                    "ID: " + c2.getIdCliente() +
                    "\nNombre: " + c2.getNombres() + " " + c2.getApellidos()
                );
                break;

            case "Eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id_cliente"));
                Cliente eliminado = dao.buscar(idEliminar); //Buscar antes de eliminar
                dao.eliminar(idEliminar);

              //Notificacion por correo
                if (eliminado != null) {
                    EmailUtil.enviarCorreo(
                        "Cliente eliminado",
                        "Se eliminó el cliente:\n" +
                        "ID: " + eliminado.getIdCliente() +
                        "\nNombre: " + eliminado.getNombres() + " " + eliminado.getApellidos()
                    );
                }
                break;

            case "Consultar":
                int idBuscar = Integer.parseInt(request.getParameter("id_cliente"));
                Cliente clienteEncontrado = dao.buscar(idBuscar);

                if (clienteEncontrado != null) {
                    request.setAttribute("cliente", clienteEncontrado);
                } else {
                    request.setAttribute("mensaje", "Cliente no encontrado");
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("formulario.jsp");
                dispatcher.forward(request, response);
                return;

            case "Reporte":
                int idReporte = Integer.parseInt(request.getParameter("id_cliente"));
                Cliente clienteReporte = dao.buscar(idReporte);

                if (clienteReporte != null) {
                    generarReporte(clienteReporte, response);
                } else {
                    request.setAttribute("mensaje", "Cliente no encontrado para generar el reporte");
                    RequestDispatcher dispatcher1 = request.getRequestDispatcher("formulario.jsp");
                    dispatcher1.forward(request, response);
                }
                return;

            case "ReporteGeneral":
                generarReporteGeneral(response);
                return;
        }

        response.sendRedirect("ClienteServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("clientes", dao.listar());
        RequestDispatcher dispatcher = request.getRequestDispatcher("listar.jsp");
        dispatcher.forward(request, response);
    }

   //la generación de reportes quedan igual

    private void generarReporte(Cliente cliente, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=certificado_cliente_" + cliente.getIdCliente() + ".pdf");

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("CERTIFICADO DE CLIENTE");
            contentStream.endText();

            float y = 660;
            float leading = 20;
            float fontSize = 12;

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, fontSize);
            contentStream.newLineAtOffset(100, y);

            String[] lineas = {
                "ID Cliente: " + cliente.getIdCliente(),
                "Número de Identidad: " + cliente.getNumeroIdentidad(),
                "Nombre: " + cliente.getNombres() + " " + cliente.getApellidos(),
                "Dirección: " + cliente.getDireccion(),
                "Teléfono: " + cliente.getTelefono()
            };

            for (String linea : lineas) {
                contentStream.showText(linea);
                contentStream.newLineAtOffset(0, -leading);
            }

            contentStream.endText();
            contentStream.close();

            document.save(response.getOutputStream());
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generarReporteGeneral(HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_general_clientes.pdf");

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDType1Font fontRegular = PDType1Font.HELVETICA;
            PDType1Font fontBold = PDType1Font.HELVETICA_BOLD;
            float fontSize = 9f;

            float margin = 50;
            float yStart = 730;
            float yPosition = yStart;
            float rowHeight = 15;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

            // Anchos de columna
            float[] colWidths = {40, 80, 100, 100, 100, 80}; // ID, Identidad, Nombre, Apellidos, Dirección, Teléfono
            String[] headers = {"ID", "Identidad", "Nombre", "Apellidos", "Dirección", "Teléfono"};

            // Título del sorteo
            contentStream.beginText();
            contentStream.setFont(fontBold, 14);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Reporte General de Clientes");
            contentStream.endText();

            yPosition -= 25;

            // Dibujar encabezados de tabla
            float textY = yPosition - 10;
            float nextX = margin;

            contentStream.setFont(fontBold, fontSize);
            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(nextX, textY);
                contentStream.showText(headers[i]);
                contentStream.endText();
                nextX += colWidths[i];
            }

            yPosition -= rowHeight;

            //   Obtener datos
            List<Cliente> clientes = dao.listar();

            contentStream.setFont(fontRegular, fontSize);

            for (Cliente cliente : clientes) {
                // Salto de página si es necesario
                if (yPosition < margin + rowHeight) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = yStart;
                }

                float xText = margin;
                textY = yPosition - 10;

                String[] data = {
                    String.valueOf(cliente.getIdCliente()),
                    String.valueOf(cliente.getNumeroIdentidad()),
                    cliente.getNombres(),
                    cliente.getApellidos(),
                    cliente.getDireccion(),
                    cliente.getTelefono()
                };

                for (int i = 0; i < data.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xText, textY);
                    contentStream.showText(truncateText(data[i], colWidths[i], fontRegular, fontSize));
                    contentStream.endText();
                    xText += colWidths[i];
                }

                yPosition -= rowHeight;
            }

            contentStream.close();
            document.save(response.getOutputStream());
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String truncateText(String text, float maxWidth, PDType1Font font, float fontSize) throws IOException {
        String result = text;
        while (font.getStringWidth(result) / 1000 * fontSize > maxWidth && result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        if (!result.equals(text)) {
            result += "...";
        }
        return result;
    }


    private List<String> breakLines(String text, float maxWidth, PDType1Font font, float fontSize) throws IOException {
        List<String> lines = new ArrayList<>();

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String temp = line.length() == 0 ? word : line + " " + word;
            float size = font.getStringWidth(temp) / 1000 * fontSize;
            if (size > maxWidth) {
                if (line.length() > 0) lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(temp);
            }
        }

        if (line.length() > 0) lines.add(line.toString());
        return lines;
    }

    private int wrapText(String text, float maxWidth, float fontSize) {
        try {
            return breakLines(text, maxWidth, PDType1Font.HELVETICA, fontSize).size();
        } catch (IOException e) {
            return 1;
        }
    }
}

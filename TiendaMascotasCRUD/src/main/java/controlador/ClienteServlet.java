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

                // 📧 Notificación por correo
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

                // 📧 Notificación por correo
                EmailUtil.enviarCorreo(
                    "Cliente actualizado",
                    "Se actualizó el cliente:\n" +
                    "ID: " + c2.getIdCliente() +
                    "\nNombre: " + c2.getNombres() + " " + c2.getApellidos()
                );
                break;

            case "Eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id_cliente"));
                Cliente eliminado = dao.buscar(idEliminar); // Buscar antes de eliminar
                dao.eliminar(idEliminar);

                // 📧 Notificación por correo
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

    // ... (Tus métodos de generación de reportes quedan igual, sin cambios)

    private void generarReporte(Cliente cliente, HttpServletResponse response) {
        // (Ya está implementado correctamente)
    }

    private void generarReporteGeneral(HttpServletResponse response) {
        // (Ya está implementado correctamente)
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

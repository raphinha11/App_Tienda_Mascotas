package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import modelo.Conexion;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/VerificarConexion")
public class ConexionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Conexion conexion = new Conexion();
        String mensaje;

        try (Connection con = conexion.getConnection()) {
            if (con != null && !con.isClosed()) {
                mensaje = "✅ Conexión establecida correctamente con la base de datos.";
            } else {
                mensaje = "❌ No se pudo establecer la conexión.";
            }
        } catch (Exception e) {
            mensaje = "❌ Error al conectar: " + e.getMessage();
        }

        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("verificarConexion.jsp").forward(request, response);
    }
}

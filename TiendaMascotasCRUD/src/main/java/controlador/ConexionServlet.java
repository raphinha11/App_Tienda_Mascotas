package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import modelo.Conexion;

import java.io.IOException;
import java.sql.Connection;

/* Este servlet maneja solicitudes GET para verificar la conexion con la base
 * de datos usando la clase conexion.
 * El resultado de la verificacion (Conexion establecida o error al conectar) 
 * se muestra en una pagina JSP llamada VerificarConexion.jsp.*/

@WebServlet("/VerificarConexion")
public class ConexionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// Instancia de la clase Conexion
        Conexion conexion = new Conexion();
        String mensaje;

     // Intentar establecer la conexion con la base de datos
        try (Connection con = conexion.getConnection()) {
        	// Verifica si la conexión es valida y esta abierta
            if (con != null && !con.isClosed()) {
                mensaje = "✅ Conexión establecida correctamente con la base de datos.";
            } else {
                mensaje = "❌ No se pudo establecer la conexión.";
            }
        } catch (Exception e) {
        	// Captura cualquier error en la conexión
            mensaje = "❌ Error al conectar: " + e.getMessage();
        }

        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("verificarConexion.jsp").forward(request, response);
    }
}

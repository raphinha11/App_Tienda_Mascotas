package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import modelo.Usuario;
import modelo.UsuarioDAO;

@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        Usuario u = new Usuario();
        u.setUsuario(usuario);
        u.setCorreo(correo);
        u.setContrasena(contrasena);
        u.setRol("cliente");

        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.registrar(u);

        if (exito) {
            // Redirige al login con un mensaje
            request.setAttribute("mensaje", "Registro exitoso. Inicia sesión para continuar.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("mensaje", "Error al registrar. Intente nuevamente.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}

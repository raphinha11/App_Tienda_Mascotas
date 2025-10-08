package controlador;

import modelo.Usuario;
import modelo.UsuarioDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.validar(usuario, contrasena);

        if (u != null) {
            HttpSession sesion = request.getSession();
            sesion.setAttribute("usuario", u);

            if ("admin".equals(u.getRol())) {
                response.sendRedirect("admin.jsp");
            } else if ("veterinario".equals(u.getRol())) {
                response.sendRedirect("veterinario.jsp");
            } else {
                response.sendRedirect("error.jsp");
            }
        } else {
            request.setAttribute("mensaje", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

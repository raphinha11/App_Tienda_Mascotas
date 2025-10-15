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

            switch (u.getRol()) {
                case "admin":
                    response.sendRedirect("admin.jsp");
                    break;
                case "veterinario":
                    response.sendRedirect("veterinario.jsp");
                    break;
                case "cliente":
                    response.sendRedirect("cliente.jsp");
                    break;
                default:
                    response.sendRedirect("error.jsp");
                    break;
            }
        } else {
            request.setAttribute("mensaje", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
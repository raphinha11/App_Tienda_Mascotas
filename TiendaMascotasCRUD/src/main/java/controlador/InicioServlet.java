package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import modelo.Usuario;

@WebServlet("/InicioServlet")
public class InicioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);

        if (sesion != null) {
            Usuario u = (Usuario) sesion.getAttribute("usuario");

            if (u != null) {
                String rol = u.getRol();

                if ("admin".equalsIgnoreCase(rol)) {
                    response.sendRedirect("admin.jsp");
                    return;
                } else if ("veterinario".equalsIgnoreCase(rol)) {
                    response.sendRedirect("veterinario.jsp");
                    return;
                }
            }
        }

        // Si no hay sesión o usuario, vuelve al login
        response.sendRedirect("login.jsp");
    }
}

package controlador;

import modelo.Cliente;
import modelo.ClienteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;


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
                break;

            case "Eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id_cliente"));
                dao.eliminar(idEliminar);
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
}

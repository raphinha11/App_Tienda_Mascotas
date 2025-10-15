package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import modelo.Producto;

@WebServlet("/ControladorProducto")
public class ControladorProducto extends HttpServlet {

    // Configuración de conexión
    private final String URL = "jdbc:mysql://localhost:3306/bd_tienda_mascotas";
    private final String USER = "root";
    private final String PASS = "2556229";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String buscar = request.getParameter("buscar");
        List<Producto> productos = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps;

            if (buscar != null && !buscar.trim().isEmpty()) {
                ps = con.prepareStatement(
                    "SELECT * FROM tbl_producto WHERE nombre_producto LIKE ? OR marca LIKE ?");
                ps.setString(1, "%" + buscar + "%");
                ps.setString(2, "%" + buscar + "%");
            } else {
                ps = con.prepareStatement("SELECT * FROM tbl_producto");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setCodigo_barras(rs.getInt("codigo_barras"));
                p.setNombre_producto(rs.getString("nombre_producto"));
                p.setMarca(rs.getString("marca"));
                p.setPrecio(rs.getDouble("precio"));
                productos.add(p);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enviar productos al JSP
        request.setAttribute("productos", productos);

        // Redirigir a la vista
        RequestDispatcher dispatcher = request.getRequestDispatcher("producto.jsp");
        dispatcher.forward(request, response);
    }
}

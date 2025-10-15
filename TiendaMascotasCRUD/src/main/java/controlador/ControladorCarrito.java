package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.awt.Color;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import modelo.Producto;
import modelo.CompraProducto;

@WebServlet("/ControladorCarrito")
public class ControladorCarrito extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/bd_tienda_mascotas";
    private final String USER = "root";
    private final String PASS = "2556229";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        List<Producto> carrito = (List<Producto>) sesion.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        String accion = request.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {
        case "agregar":
            int codigo = Integer.parseInt(request.getParameter("codigo_barras"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            Producto prod = obtenerProductoPorId(codigo);

            if (prod != null) {
                boolean existe = false;

                for (Producto p : carrito) {
                    if (p.getCodigo_barras() == codigo) {
                        // si ya existe, solo aumenta la cantidad
                        p.setCantidad(p.getCantidad() + cantidad);
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    prod.setCantidad(cantidad);
                    carrito.add(prod);
                }
            }

            sesion.setAttribute("carrito", carrito);
            response.sendRedirect("ControladorProducto");
            break;


            case "eliminar":
                int codE = Integer.parseInt(request.getParameter("codigo_barras"));
                carrito.removeIf(p -> p.getCodigo_barras() == codE);
                sesion.setAttribute("carrito", carrito);
                response.sendRedirect("carrito.jsp");
                break;

            case "vaciar":
                carrito.clear();
                sesion.setAttribute("carrito", carrito);
                response.sendRedirect("carrito.jsp");
                break;

            case "comprar":
                String cedulaStr = request.getParameter("cedula_cliente");
                Integer cedulaCliente = null;

                if (cedulaStr != null && !cedulaStr.isEmpty()) {
                    cedulaCliente = Integer.parseInt(cedulaStr);
                } else {
                    cedulaCliente = (Integer) sesion.getAttribute("cedula_cliente");
                    if (cedulaCliente == null) {
                        cedulaCliente = 123456; // valor temporal
                    }
                }

                if (carrito == null || carrito.isEmpty()) {
                    request.setAttribute("mensaje", "⚠ No hay productos en el carrito.");
                    request.getRequestDispatcher("carrito.jsp").forward(request, response);
                    return;
                }

                // Guardar en la BD
                guardarCompraEnBD(cedulaCliente, carrito);

                // Generar directamente el PDF como respuesta
                generarFactura(response, carrito, cedulaCliente);

                // Limpiar carrito
                carrito.clear();
                sesion.setAttribute("carrito", carrito);
                break;

        }
    }

    private Producto obtenerProductoPorId(int codigo) {
        Producto p = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tbl_producto WHERE codigo_barras=?");
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Producto();
                p.setCodigo_barras(rs.getInt("codigo_barras"));
                p.setNombre_producto(rs.getString("nombre_producto"));
                p.setMarca(rs.getString("marca"));
                p.setPrecio(rs.getDouble("precio"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
    	
    	String accion = request.getParameter("accion");
        
        if ("factura".equals(accion)) {
            HttpSession sesion = request.getSession();
            List<Producto> carrito = (List<Producto>) sesion.getAttribute("carrito");
            Integer cedulaCliente = (Integer) sesion.getAttribute("cedula_cliente");

            // ✅ Si no hay cedula_cliente en sesión, usa una temporal
            if (cedulaCliente == null) {
                cedulaCliente = 123456; // Valor temporal o visitante genérico
            }

            if (carrito != null && !carrito.isEmpty()) {
                generarFactura(response, carrito, cedulaCliente);
            } else {
                response.getWriter().println("No hay productos en el carrito para generar factura.");
            }
        } else {
            response.sendRedirect("carrito.jsp");
        }
    }
    
    private void generarFactura(HttpServletResponse response, List<Producto> carrito, int cedulaCliente) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=factura.pdf");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);
            float margin = 50;
            float y = 750;

            // Título
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 20);
            content.newLineAtOffset(margin, y);
            content.showText("Factura - Tienda de Mascotas");
            content.endText();

            y -= 40;

            // Datos del cliente
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(margin, y);
            content.showText("Cédula del Cliente: " + cedulaCliente);
            content.newLineAtOffset(0, -15);
            content.showText("Fecha de Compra: " + LocalDate.now());
            content.endText();

            y -= 40;

            // Cabecera de la tabla
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.newLineAtOffset(margin, y);
            content.showText(String.format("%-30s %-10s %-15s %-15s", "Producto", "Cant.", "Precio", "Subtotal"));
            content.endText();

            y -= 20;

            double total = 0;
            for (Producto p : carrito) {
                double subtotal = p.getPrecio() * p.getCantidad();
                total += subtotal;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 11);
                content.newLineAtOffset(margin, y);
                content.showText(String.format("%-30s %-10d %-15.2f %-15.2f",
                        p.getNombre_producto(), p.getCantidad(), p.getPrecio(), subtotal));
                content.endText();

                y -= 20;
            }

            y -= 20;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 13);
            content.newLineAtOffset(margin, y);
            content.showText(String.format("TOTAL: %.2f", total));
            content.endText();

            content.close();
            doc.save(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarCompraEnBD(int cedulaCliente, List<Producto> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            System.out.println("⚠ No hay productos para registrar.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            String sql = "INSERT INTO tbl_compra_producto (cedula_cliente, codigo_barras, fecha_compra, cantidad) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            for (Producto p : carrito) {
                System.out.println("🟢 Insertando: " + p.getNombre_producto() + " - Cant: " + p.getCantidad());

                ps.setInt(1, cedulaCliente);
                ps.setInt(2, p.getCodigo_barras());
                ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                ps.setInt(4, p.getCantidad());

                int filas = ps.executeUpdate();
                System.out.println("   → Filas insertadas: " + filas);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

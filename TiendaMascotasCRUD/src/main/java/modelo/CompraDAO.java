package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CompraDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;

    public void registrarCompra(List<CompraProducto> carrito, int cedulaCliente) {
        String sql = "INSERT INTO tbl_compra_producto (cedula_cliente, codigo_barras, fecha_compra, cantidad) VALUES (?, ?, NOW(), ?)";
        try {
            con = cn.getConnection();
            for (CompraProducto cp : carrito) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, cedulaCliente);
                ps.setInt(2, cp.getCodigo_barras());
                ps.setInt(3, cp.getCantidad());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

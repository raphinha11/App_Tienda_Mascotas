package modelo;

import java.sql.*;
import java.util.*;

public class ClienteDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_cliente";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt(1));
                c.setNumeroIdentidad(rs.getInt(2));
                c.setNombres(rs.getString(3));
                c.setApellidos(rs.getString(4));
                c.setDireccion(rs.getString(5));
                c.setTelefono(rs.getString(6));
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error al listar: " + e);
        }
        return lista;
    }

    public void agregar(Cliente c) {
        String sql = "INSERT INTO tbl_cliente (numero_identidad, nombres, apellidos, direccion, telefono) VALUES (?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getNumeroIdentidad());
            ps.setString(2, c.getNombres());
            ps.setString(3, c.getApellidos());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getTelefono());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al agregar: " + e);
        }
    }

    public Cliente buscar(int id) {
        Cliente c = null;
        String sql = "SELECT * FROM tbl_cliente WHERE id_cliente=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                c = new Cliente();
                c.setIdCliente(rs.getInt(1));
                c.setNumeroIdentidad(rs.getInt(2));
                c.setNombres(rs.getString(3));
                c.setApellidos(rs.getString(4));
                c.setDireccion(rs.getString(5));
                c.setTelefono(rs.getString(6));
            }
        } catch (Exception e) {
            System.out.println("Error al buscar: " + e);
        }
        return c;
    }

    public void actualizar(Cliente c) {
        String sql = "UPDATE tbl_cliente SET numero_identidad=?, nombres=?, apellidos=?, direccion=?, telefono=? WHERE id_cliente=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getNumeroIdentidad());
            ps.setString(2, c.getNombres());
            ps.setString(3, c.getApellidos());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getTelefono());
            ps.setInt(6, c.getIdCliente());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM tbl_cliente WHERE id_cliente=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e);
        }
    }
}

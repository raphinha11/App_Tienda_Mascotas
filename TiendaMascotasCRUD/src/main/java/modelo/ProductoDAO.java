package modelo;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
	Conexion cn = new Conexion();
	
	public List<Producto> listar() {
		List<Producto> lista = new ArrayList<>();
		String sql = "SELECT * FROM tbl_producto";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Producto c = new Producto();
				c.setCodigo_barras(rs.getInt(1));
				c.setNombre_producto(rs.getString(2));
				c.setMarca(rs.getString(3));
				c.setPrecio(rs.getDouble(4));
				lista.add(c);
				
			}
			
		} catch (Exception e) {
			System.out.println("error al listar: " + e);
		}finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
		}
		
		return lista;
		
	}
	
	public void agregar(Producto c) {
		String sql = "INSERT INTO tbl_producto (nombre_producto, marca, precio) VALUES (?, ?, ?)";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, c.getNombre_producto());
			ps.setString(2, c.getMarca());
			ps.setDouble(3, c.getPrecio());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error al agregar: " + e);
		}finally {
			 try { if (ps != null) ps.close(); } catch (Exception e) {}
	         try { if (con != null) con.close(); } catch (Exception e) {}
		}
	}

	public Producto buscar(int id) {
		Producto c = null;
		String sql = "SELECT * FROM tbl_producto WHERE codigo_barras=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				c = new Producto();
				c.setCodigo_barras(rs.getInt(1));
				c.setNombre_producto(rs.getString(2));
				c.setMarca(rs.getString(3));
				c.setPrecio(rs.getDouble(4));
			}
		} catch (Exception e) {
			System.out.println("Error al buscar: " + e);
		}finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
		}
		
		return c;
	}
	
	public void actualizar(Producto c) {
		String sql = "UPDATE tbl_producto SET nombre_producto=?, marca=?, precio=? WHERE codigo_barras=?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, c.getNombre_producto());
			ps.setString(2, c.getMarca());
			ps.setDouble(3, c.getPrecio());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error al actualizar: " + e);
		}finally {
			try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
		}
	}
	
	public void eliminar(int id) {
		String sql = "DELETE FROM tbl_producto WHERE codigo_barras=?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error al eliminar: " + e);
		}finally {
			 try { if (ps != null) ps.close(); } catch (Exception e) {}
	         try { if (con != null) con.close(); } catch (Exception e) {}
		}
		
	}
	
	public List<Producto> buscarPorNombreOMarca(String texto) {
	    List<Producto> lista = new ArrayList<>();
	    String sql = "SELECT * FROM tbl_producto WHERE nombre_producto LIKE ? OR marca LIKE ?";
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        con = cn.getConnection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, "%" + texto + "%");
	        ps.setString(2, "%" + texto + "%");
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            Producto p = new Producto();
	            p.setCodigo_barras(rs.getInt("codigo_barras"));
	            p.setNombre_producto(rs.getString("nombre_producto"));
	            p.setMarca(rs.getString("marca"));
	            p.setPrecio(rs.getDouble("precio"));
	            lista.add(p);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return lista;
	}

	
}

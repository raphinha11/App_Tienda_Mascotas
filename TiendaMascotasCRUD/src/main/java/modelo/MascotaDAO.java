package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO {
	Conexion cn = new Conexion();
	
	public List<Mascota> listar() {
		List<Mascota> lista = new ArrayList<>();
		String sql = "SELECT * FROM tbl_mascota";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Mascota c = new Mascota();
				c.setId_mascota(rs.getInt(1));
				c.setNombre(rs.getString(2));
				c.setTipo(rs.getString(3));
				c.setGenero(rs.getString(4));
				c.setRaza(rs.getString(5));
				c.setCedula_cliente(rs.getInt(6));
				lista.add(c);
			}
			
		}catch (Exception e) {
			System.out.println("Error al listar: " + e );
		}finally {
			try { if (rs != null) rs.close();} catch (Exception e) {}
			try { if (ps != null) rs.close();} catch (Exception e) {}
			try { if (con != null) rs.close();} catch (Exception e) {}
		}
		
		return lista;
	}
	
	public void agregar(Mascota c) {
		String sql = "INSERT INTO tbl_mascota (nombre, tipo, genero, raza, cedula_cliente) VALUE (?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, c.getNombre());
			ps.setString(2, c.getTipo());
			ps.setString(3, c.getGenero());
			ps.setString(4, c.getRaza());
			ps.setInt(5, c.getCedula_cliente());
			ps.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("Error al agregar: " + e);
		}finally {
			try { if (ps != null) ps.close();} catch (Exception e) {}
			try { if (con != null) con.close();} catch (Exception e) {}
		}
		
	}
	
	public Mascota buscar(int id) {
		Mascota c = null;
		String sql = "SELECT * FROM tbl_mascota WHERE id_mascota=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				c = new Mascota();
				c.setId_mascota(rs.getInt(1));
				c.setNombre(rs.getString(2));
				c.setTipo(rs.getString(3));
				c.setGenero(rs.getString(4));
				c.setRaza(rs.getString(5));
				c.setCedula_cliente(rs.getInt(6));
			}
			
		} catch (Exception e) {
			System.out.println("Error al buscar: " + e);
		}finally {
			try { if (rs != null) rs.close();} catch (Exception e) {}
			try { if (ps != null) ps.close();} catch (Exception e) {}
			try { if (con != null) con.close();} catch (Exception e) {}
		}
		return c;
	}
	
	public void actualizar(Mascota c) {
		String sql = "UPDATE tbl_mascota SET nombre =?, tipo =?, genero =?, raza =?, cedula_cliente =?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, c.getNombre());
			ps.setString(2, c.getTipo());
			ps.setString(3, c.getGenero());
			ps.setString(4, c.getRaza());
			ps.setInt(5, c.getCedula_cliente());
		} catch (Exception e) {
			System.out.println("Error al actualizar: "+ e);
		} finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
	}
	
	public void eliminar(int id) {
		String sql = "DELETE FROM tbl_mascota WHERE id_mascota=?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = cn.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("Error al eliminar: " + e);
		} finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
	}

}

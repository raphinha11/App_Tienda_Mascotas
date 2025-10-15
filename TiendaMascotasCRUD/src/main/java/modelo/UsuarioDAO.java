package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // ✅ Validar usuario
    public Usuario validar(String usuario, String contrasena) {
        Usuario u = null;
        String sql = "SELECT * FROM tbl_usuario WHERE usuario=? AND contrasena=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = new Usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRol(rs.getString("rol"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return u;
    }

    // ✅ Registrar usuario
    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO tbl_usuario (usuario, contrasena, correo, rol) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getContrasena());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getRol());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Conexión corregida
    private Connection getConnection() {
        return cn.getConnection();
    }
}

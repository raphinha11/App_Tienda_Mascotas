package modelo;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    Connection con;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bd_tienda_mascotas?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "root", "2556229"
            );
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e);
        }
        return con;
    }
}

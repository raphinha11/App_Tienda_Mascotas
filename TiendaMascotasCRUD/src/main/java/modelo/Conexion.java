package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Conexion {
    Connection con;
    /* Esta clase gestiona la conexion a una base de datos MySQL utilizando JDBC.
     * Define un metodo publico getConnetion() que carga el driver de MySQL, establece
     * una conexion a una base de datos especifica y retorna como objeto connection.*/

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

	public PreparedStatement prepareStatement(String sql) {
		// TODO Auto-generated method stub
		return null;
	}
}

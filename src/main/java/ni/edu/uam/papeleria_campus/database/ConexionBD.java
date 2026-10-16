package ni.edu.uam.papeleria_campus.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:postgresql://localhost:5432/papeleria_campus";
    private static final String USER = "postgres";
    private static final String PASSWORD = "240907";

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a PostgreSQL!");
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return con;
    }

}

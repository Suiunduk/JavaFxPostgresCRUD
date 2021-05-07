package Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {

    public Connection connection = null;

    public Connection getConnection(){
        //Ссылка для подключения к базе данных
        String url = "jdbc:postgresql://localhost:5432/employee";

        //Имя пользователя и пароль для подключения к базе данных
        String username = "postgres";
        String password = "postgres";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                System.out .println("Successfully connected to MySQL database test");
            }
        } catch (Exception ex) {
            System.out .println("An error occurred while connecting MySQL database");
            ex.printStackTrace();
        }
        return connection;
    }
}

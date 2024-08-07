package Commands;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConnectionDB 
{
    private static final String url = "jdbc:mysql://127.0.0.1:3306/moondb";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection connection;

    public static Connection getConexao()
    {
        try 
        {
            if(connection == null)
            {
                connection = DriverManager.getConnection(url, user, password);
                return connection;
            }
            else
            {
                return connection;
            }
        } 
        catch (SQLException e) 
        {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
}

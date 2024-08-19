package Commands;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class ConnectionDB
{
    private static final String url = "jdbc:sqlite:moondatabase.db";
    private static final String urlbump = "jdbc:sqlite:moonbumps.db";
    private static final String user = "root";
    private static final String userbump = "root";
    private static final String password = "root";
    private static final String passwordbump = "root";

    private static Connection connection;
    private static Connection bumpConnection;

    public static Connection getConexao()
    {
        try
        {
            if(connection == null)
            {
                connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS eclipsevip (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    nome TEXT,\n" +
                        "    memberID TEXT,\n" +
                        "    idVip TEXT,\n" +
                        "    tipo TEXT,\n" +
                        "    idCustomRole TEXT,\n" +
                        "    nameCustomRole TEXT,\n" +
                        "    loverId TEXT,\n" +
                        "    loverName TEXT,\n" +
                        "    loverIdRole TEXT,\n" +
                        "    loverRoleName TEXT,\n" +
                        "    callName TEXT,\n" +
                        "    callID TEXT\n"+
                        ");\n");

                statement.executeUpdate("create table if not exists eclipsefriend(\n" +
                        "     id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    nome TEXT,\n" +
                        "    memberID TEXT,\n" +
                        "    friendName TEXT,\n" +
                        "    friendID TEXT,\n" +
                        "     createdFriendRoleID TEXT,\n" +
                        "     createdFriendRoleName TEXT,\n" +
                        "     removed TEXT\n" +
                        ");\n");

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

    public static Connection getBumpConnection()
    {
        try
        {
            if(bumpConnection == null)
            {
                bumpConnection = DriverManager.getConnection(urlbump, userbump, passwordbump);
                Statement statement = bumpConnection.createStatement();

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS counter (\n" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                        "idMembro TEXT,\n" +
                        "nome TEXT, \n" +
                        "contador INTEGER \n" +  // Use INTEGER para números
                        ");");
                return bumpConnection;
            }
            else
            {
                return bumpConnection;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}

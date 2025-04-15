package Commands;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class ConnectionDB
{
    private static final String url = "jdbc:sqlite:moondatabase.db";
    private static final String urlbump = "jdbc:sqlite:moonbumps.db";
    private static final String urlinsta = "jdbc:sqlite:mooninsta.db";
    private static final String user = "root";
    private static final String userinsta = "root";
    private static final String userbump = "root";
    private static final String password = "root";
    private static final String passwordbump = "root";
    private static final String passwordinsta = "root";

    private static Connection connection;
    private static Connection bumpConnection;
    private static Connection instagramConnection;

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

                statement.executeUpdate("create table if not exists mensalidade(\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    nome TEXT,\n" +
                        "    idMembro TEXT,\n" +
                        "    data_inicio TEXT\n" +
                        "    data_final TEXT\n" +
                        "    tipo_vip TEXT\n" +
                        ");\n");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS moonvip\n" +
                        "(\n" +
                        "\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "\n" +
                        "    nome TEXT,\n" +
                        "\n" +
                        "    memberID TEXT,\n" +
                        "\n" +
                        "    idVip TEXT,\n" +
                        "\n" +
                        "    tipo TEXT,\n" +
                        "\n" +
                        "    idCustomRole TEXT,\n" +
                        "\n" +
                        "    nameCustomRole TEXT,\n" +
                        "\n" +
                        "    callName TEXT,\n" +
                        "\n" +
                        "    callID TEXT\n" +
                        ");");

                statement.executeUpdate("create table if not exists moonfriend(\n" +
                        "\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "\n" +
                        "    nome TEXT,\n" +
                        "\n" +
                        "    memberID TEXT,\n" +
                        "\n" +
                        "    friendName TEXT,\n" +
                        "\n" +
                        "    friendID TEXT,\n" +
                        "\n" +
                        "    createdFriendRoleID TEXT,\n" +
                        "\n" +
                        "    createdFriendRoleName TEXT,\n" +
                        "\n" +
                        "    removed TEXT\n" +
                        ");");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS minguantevip\n" +
                        "(\n" +
                        "\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "\n" +
                        "    nome TEXT,\n" +
                        "\n" +
                        "    memberID TEXT,\n" +
                        "\n" +
                        "    idVip TEXT,\n" +
                        "\n" +
                        "    tipo TEXT,\n" +
                        "\n" +
                        "    idCustomRole TEXT,\n" +
                        "\n" +
                        "    nameCustomRole TEXT\n" +
                        "\n" +
                        ");");
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

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS parceriaContador (\n" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "idMembro TEXT,\n" +
                        "nome TEXT,\n" +
                        "contador INTEGER \n" +
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
    public static Connection getInstagramConnection()
    {
        try
        {
            if(instagramConnection == null)
            {
                instagramConnection = DriverManager.getConnection(urlinsta, userinsta, passwordinsta);
                Statement statement = instagramConnection.createStatement();

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (\n" +
                        "                       id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "                       message_id VARCHAR(255) NOT NULL,  -- Armazenar o messageId do Discord\n" +
                        "                       member_id VARCHAR(255) NOT NULL,\n" +
                        "                       channel_id VARCHAR(255) NOT NULL,\n" +
                        "                       image_url VARCHAR(255),\n" +
                        "                       date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                        ");");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS likes (\n" +
                        "                       id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "                       post_id INTEGER NOT NULL,\n" +
                        "                       member_id TEXT NOT NULL,\n" +
                        "                       date_liked TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "                       UNIQUE (post_id, member_id),  -- Evita que o mesmo usuário curta duas vezes\n" +
                        "                       FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE\n" +
                        ");");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS comments (\n" +
                        "                          id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "                          post_id INTEGER NOT NULL,\n" +
                        "                          member_id TEXT NOT NULL,\n" +
                        "                          comment TEXT NOT NULL,\n" +
                        "                          date_commented TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "                          FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE\n" +
                        ");");

                return instagramConnection;
            }
            else
            {
                return instagramConnection;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

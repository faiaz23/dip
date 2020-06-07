package db;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import utils.Utils;

public class DBManager {

    private final String dbName;
    private final String dbPass;
    private final String dbUser;
    private final String host;
    private final String port;

    public DBManager(String dbName, String dbPass, String dbUser, String host, String port) {
        this.dbName = dbName;
        this.dbPass = dbPass;
        this.dbUser = dbUser;
        this.host = host;
        this.port = port;
    }

    public void createDB() throws IOException, InterruptedException {
        String cmd = String.format("createdb -U %s -O %s -E UTF-8 %s", dbUser, dbUser, dbName);
        Utils.runCMD(cmd, "Main");
    }

    public void copyDB() throws IOException, InterruptedException, SQLException {
        dropConnections();
        String bkpDBName = dbName + "_bak";
        dropDB(bkpDBName);
        System.out.println("postgres: copy database {} to {}" + dbName + bkpDBName);
        String cmd = String.format("createdb -U %s -T %s %s", dbUser, dbName, bkpDBName);
        Utils.runCMD(cmd, "Main");
    }

    public void dropConnections() throws SQLException {
        System.out.println("postgres: drop connections to "+ dbName);
        try (Connection conn = createConnection(); Statement statement = conn.createStatement()) {
            statement.execute(String.format("SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity" +
                    " WHERE pg_stat_activity.datname = '%s' AND pid <> pg_backend_pid();", dbName));
        }
    }

    public void dropDB(String dbName) throws IOException, InterruptedException {
        String cmd = String.format("dropdb -U %s --if-exists %s", dbUser, dbName);
        Utils.runCMD(cmd, "Main");
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:postgresql://%s:%s/", host, port), dbUser, dbPass);
    }


}

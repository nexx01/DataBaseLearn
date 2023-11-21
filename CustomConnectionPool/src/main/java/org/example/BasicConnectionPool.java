package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool{

    private static final int MAX_TIMEOUT = 1000;
    String url;
    String user;
    String password;
    List<Connection> connectionPool;
    List<Connection> usedConnection = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;
    private static int MAX_POOL_SIZE = 12;

    public BasicConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;
    }

    public static BasicConnectionPool create(String url, String user,
                                             String password) throws SQLException {
        var pool = new ArrayList<Connection>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url,user,password));
        }
        return new BasicConnectionPool(url, user, password, pool);
    }

    private static Connection createConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnection.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(url, user, password));
            } else {
                throw new RuntimeException(
                        "Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);

        if(!connection.isValid(MAX_TIMEOUT)){
            connection = createConnection(url, user, password);
        }

        usedConnection.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnection.remove(connection);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public int getSize() {
        return connectionPool.size() + usedConnection.size();
    }
    public void shutdown() throws SQLException {
        usedConnection.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }
}

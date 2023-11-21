package org.example;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class BasicConnectionPoolTest {

    @Test
    void whenCalledConnection_thenCorrect() throws SQLException {
        var connectionPool = BasicConnectionPool.create("jdbc:h2:mem:test", "user", "password");

        assertTrue(connectionPool.getConnection().isValid(1));

    }
}
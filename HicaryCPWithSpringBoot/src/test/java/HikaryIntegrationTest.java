import org.example.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class,
        properties =
                "spring.datasource.type=com.zaxxer.hikari.HikariDataSource"
)
public class HikaryIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void hikariConnectionPoolsIsConfigured() {
        Assertions.assertEquals("com.zaxxer.hikari.HikariDataSource",
                dataSource.getClass().getName());

    }
}

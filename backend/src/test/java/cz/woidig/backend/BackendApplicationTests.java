package cz.woidig.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {
    @Test
    void testMain() {
        String[] args = new String[0];
        BackendApplication.main(args);
    }
}

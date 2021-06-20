import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JDBMConnectTest {
    @Test
    void connectionTest() {
        JDBMConnect jdbmConnect = new JDBMConnect();
        jdbmConnect.connect();
    }
}
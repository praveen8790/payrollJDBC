import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JDBMConnectTest {
    @Test
    void connectionTest() throws SQLException {
        JDBMConnect jdbmConnect = new JDBMConnect();
        jdbmConnect.updateList();

        Assertions.assertEquals(1,jdbmConnect.updatePay(4000, jdbmConnect.searchIdByName("terisa")));
    }
}
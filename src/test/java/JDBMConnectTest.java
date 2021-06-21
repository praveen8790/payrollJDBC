import entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JDBMConnectTest {
    @Test
    void givenValueAndNameUpdatesBasicpay_AndReturnsAcknowledgement() throws SQLException {
        JDBMConnect jdbmConnect = new JDBMConnect();
        jdbmConnect.updateList();
        Assertions.assertEquals(1,
                jdbmConnect.updatePay(4000, jdbmConnect.searchIdByName("terisa")));
        jdbmConnect.printPayroll();
    }

    @Test
    void givenDateRangeReturnsNumberOfEntriesWhoJoinedInGivenRange() throws SQLException {
        JDBMConnect jdbmConnect = new JDBMConnect();
        jdbmConnect.updateList();
        ArrayList<Employee> employees = jdbmConnect.toRetrieveEmployeeInDateRange("2018-08-02", "2020-08-02");
        employees.forEach(employee -> System.out.println(employee.toString()));
        Assertions.assertEquals(1,employees.stream().count());
    }

    @Test
    void givenOperationAs_MIN_AndGenderShouldReturnMinSalary() {
        JDBMConnect jdbmConnect = new JDBMConnect();
        jdbmConnect.updateList();
        Assertions.assertEquals(30000.0,jdbmConnect.salaryManipulation(3,'M'));

    }
}
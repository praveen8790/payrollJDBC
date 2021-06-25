import entity.Employee;
import entity.Payroll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JDBMConnectTest {
    @Test
    void givenValueAndNameUpdatesBasicpay_AndReturnsAcknowledgement() throws SQLException {
        JDBMConnect jdbmConnect = new JDBMConnect();
        jdbmConnect.updateList();
        /*Assertions.assertEquals(1,
                jdbmConnect.updatePay(4000, jdbmConnect.searchIdByName("terisa")));*/
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

    @Test
    void givenEmployeeDetils_ShouldAddTheDetailsToTheDB() {
        JDBMConnect jdbmConnect = new JDBMConnect();
        Employee employee = new Employee();
        employee.setEmployee_name("ram");
        employee.setGender("M");
        employee.setStart_date(Date.valueOf("2021-05-04"));
        Payroll payroll = new Payroll();
        payroll.setBasic_pay(40000);
        payroll.setRemainingParameters();
        Assertions.assertTrue(jdbmConnect.insertIntoDB(employee,payroll));
    }
    @Test
    void givenEmployeeId_ShoulDeletesEntryFromAllTheTables() {
        JDBMConnect jdbmConnect = new JDBMConnect();
        Assertions.assertTrue(jdbmConnect.deleteFromDB(20));
    }


    @Test
    void givenMultipleEmployeeData_shouldAddIntoDBWithMulitThreading() {
        JDBMConnect jdbmConnect =  new JDBMConnect();
        Employee[] employees = new Employee[3];
        Payroll[] payrolls = new Payroll[3];
        employees[1].add("jeff","M", Date.valueOf(LocalDate.now()));
        payrolls[1].setBasic_pay(3000);
        employees[1].add("bezos","M", Date.valueOf(LocalDate.now()));
        payrolls[1].setBasic_pay(3000);
        employees[1].add("grandey","M", Date.valueOf(LocalDate.now()));
        payrolls[1].setBasic_pay(3000);
        Assertions.assertEquals(8,jdbmConnect.multipleEmployees(Arrays.asList(employees),Arrays.asList(payrolls)));
    }
}
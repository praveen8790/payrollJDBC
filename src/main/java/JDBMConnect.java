import entity.Employee;
import entity.Payroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class JDBMConnect {
    Connection connection;
    ArrayList<Employee> employeeArrayList;
    ArrayList<Payroll> payrollArrayList;

    public  JDBMConnect(){
        String url = "jdbc:mysql://localhost:3306/payroll_services";
        String user = "root";
        String pass = "praveen123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,user,pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from payroll_services.employee");
            while(resultSet.next()){

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateList(){
        this.employeeArrayList = new ArrayList<>();
        this.payrollArrayList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from payroll_services.employee");
            while(resultSet.next()){
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setEmployee_name(resultSet.getString("employee_name"));
                employee.setGender(resultSet.getString("gender"));
                employee.setAddress(resultSet.getString("address"));
                employee.setPhone_number(resultSet.getLong("phone_number"));
                employee.setStart_date(resultSet.getDate("start_date"));
                employeeArrayList.add(employee);
            }
            resultSet = statement.executeQuery("select * from payroll_services.payroll");
            while(resultSet.next()){
                Payroll payroll = new Payroll();
                payroll.setEmployee_id(resultSet.getInt("employee_id"));
                payroll.setBasic_pay(resultSet.getDouble("basic_pay"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public int searchIdByName(String searchelement)
    {
        AtomicInteger id = new AtomicInteger();
        employeeArrayList.stream().forEach(employee -> {
            if(employee.getEmployee_name().equals(searchelement)) {
                id.set(employee.getId());
            }

        });
        return id.get();
    }

    public int updatePay(double update_value,int id) throws SQLException {
        String sql = String.format("update payroll set basic_pay = %f where employee_id = %o",update_value,id);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return preparedStatement.executeUpdate();
    }


}
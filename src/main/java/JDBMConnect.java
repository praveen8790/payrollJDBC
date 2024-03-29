import entity.Employee;
import entity.Payroll;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JDBMConnect {
    private Connection connection;
    private ArrayList<Employee> employeeArrayList;
    private ArrayList<Payroll> payrollArrayList;

    public  JDBMConnect(){
        String url = "jdbc:mysql://localhost:3306/payroll_services";
        String user = "root";
        String pass = "praveen123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,user,pass);
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
                employee.setStart_date(resultSet.getDate("start_date"));
                employeeArrayList.add(employee);
            }
            ResultSet result = statement.executeQuery("select * from payroll_services.payroll");
            while(result.next()){
                Payroll payroll = new Payroll();
                payroll.setEmployee_id(result.getInt("employee_id"));
                payroll.setBasic_pay(result.getDouble("basic_pay"));
                payrollArrayList.add(payroll);
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
        int returned = preparedStatement.executeUpdate();
        this.updateList();
        return returned;
    }

    public ArrayList<Employee> toRetrieveEmployeeInDateRange(String startDate, String endDate) throws SQLException {
        String sql = String.format("select * from employee where start_date between cast(\"%s\" as date) and cast(\"%s\" as date)",startDate,endDate);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Employee> temparray = new ArrayList();
        while(resultSet.next()){
            Employee employee = new Employee();
            employee.setId(resultSet.getInt("id"));
            employee.setEmployee_name(resultSet.getString("employee_name"));
            employee.setGender(resultSet.getString("gender"));
            employee.setStart_date(resultSet.getDate("start_date"));
            temparray.add(employee);
        }
        return temparray;
    }
    public double salaryManipulation(int option,char gender){
        String[] operation = {"sum","avg","min","max","count"};
        System.out.println(operation);
        try {
            String sql = String.format("select %s(basic_pay) as %s from payroll,employee where payroll.employee_id = employee.id and employee.gender = '%c';",operation[option-1],operation[option-1],gender);
            System.out.println(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.next());
            return resultSet.getDouble(operation[option-1]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public void printPayroll() {
        payrollArrayList.stream().forEach(payroll -> System.out.println(payroll.toString()));
    }


    public boolean insertIntoDB(Employee employee,Payroll payroll){
        try {
            connection.setAutoCommit(false);
            Savepoint beforeInsertion = connection.setSavepoint();

            try {
                connection.prepareStatement(String.format("insert into employee(employee_name,gender,start_date) values (%s);"
                        ,employee.toString())).execute();
                ResultSet resultSet=connection.prepareStatement(String.format("select distinct greatest(employee.id , payroll.employee_id) as dis from payroll,employee where employee.employee_name = \"%s\";"
                        , employee.getEmployee_name())).executeQuery();
                if(resultSet.next())
                    payroll.setEmployee_id(resultSet.getInt("dis"));
                connection.prepareStatement(String.format(
                        "insert into payroll(employee_id,basic_pay,deductions,taxable_pay,tax,net_pay) values (%s);",
                        payroll)).execute();
                payroll.setRemainingParameters();
                this.updateList();
                connection.commit();
                return true;
            } catch (SQLException throwables) {
                connection.rollback(beforeInsertion);
                throwables.printStackTrace();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean deleteFromDB(int id){
        try {
            connection.setAutoCommit(false);
            Savepoint beforeDeletion = connection.setSavepoint();
            try {
                connection.prepareStatement(String.format(
                        "delete from employee where id = %d;", id
                )).execute();
                connection.commit();
                return true;
            } catch (SQLException throwables) {
                connection.rollback(beforeDeletion);
                throwables.printStackTrace();
            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public int multipleEmployees(List<Employee> employees,List<Payroll> payrolls){
        Iterator iterator =employees.listIterator();
        Map<Integer, Boolean> employAdditionStatus = new HashMap<>();
        while(iterator.hasNext()){
            int i = (int) iterator.next();
            Runnable task = () ->{
                employAdditionStatus.put(employees.get(i).hashCode(), false);
                this.insertIntoDB(employees.get(i), payrolls.get(i));
                employAdditionStatus.put(employees.get(i).hashCode(), true);

            };
            Thread thread = new Thread(task);
            thread.start();
        }
        while (employAdditionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return employeeArrayList.size();
    }
}
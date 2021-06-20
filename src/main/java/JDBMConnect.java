import java.sql.*;

public class JDBMConnect {
    public void connect(){
        String url = "jdbc:mysql://localhost:3306/payroll_services";
        String user = "root";
        String pass = "praveen123";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,user,pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from payroll_services.employee");
            while(resultSet.next()){
                String id = resultSet.getString("employee_name");
                System.out.println(id);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
package entity;

public class Payroll {
    int employee_id;
    double basic_pay;

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public double getBasic_pay() {
        return basic_pay;
    }

    public void setBasic_pay(double basic_pay) {
        this.basic_pay = basic_pay;
    }

    @Override
    public String toString() {
        return "Payroll{" +
                "employee_id=" + employee_id +
                ", basic_pay=" + basic_pay +
                '}';
    }
}

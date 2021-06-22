package entity;

public class Payroll {
    int employee_id;
    double basic_pay;
    double deductions;
    double taxable_pay;
    double tax;
    double net_pay;

    public void setRemainingParameters(){
        deductions=basic_pay*0.2;
        taxable_pay = basic_pay - deductions;
        tax=taxable_pay*0.1;
        net_pay = basic_pay - tax;
    }


    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }


    public void setBasic_pay(double basic_pay) {
        this.basic_pay = basic_pay;
    }

    @Override
    public String toString() {
        return String.format("%d,%f,%f,%f,%f,%f"
                , employee_id, basic_pay, deductions, taxable_pay, tax, net_pay);
    }
}

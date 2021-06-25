package entity;

import java.util.Date;

public class Employee {
    int id;
    String employee_name;
    String gender;
    Date start_date;
    public void add( String employee_name, String gender, Date start_date){
        this.employee_name = employee_name;
        this.gender = gender;
        this.start_date = start_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\",\"%s\"",employee_name,gender,start_date.toString());
    }
}

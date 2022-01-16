package Models;

public class Student{
    private String enrollmentNumber;
    private String firstLastName;
    private String secondLastName;
    private String names;
    private int calification;

    public Student(String enrollmentNumber, String firstLastName, String secondLastName,String names){
        this.enrollmentNumber = enrollmentNumber;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.names = names;
    }

    public void setCalification(int calification) {
        this.calification = calification;
    }

    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    public String getNames() {
        return names;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }
    
    public int getCalification() {
        return calification;
    }
}
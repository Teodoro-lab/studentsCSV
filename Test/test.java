import CSVControllers.*;
import Models.*;
//import UI.*;
import java.io.*;
import java.util.ArrayList;

public class test{

    public static void testStudentCSVReader(){
        StudentCSVReader r = new StudentCSVReader();
        ArrayList<Student> students;
        try{
            r.isValidCSV();
        } catch (Exception e){
            System.out.println(e);
        }

        students = r.getStudentsList();

        for (Student student : students) {
            System.out.println(student.getNames());
        }   
    }

    public static void testCalificationCSVWritter(){
        ArrayList<Student> students = new ArrayList<>();

        Student teo =  new Student("17000972","dawn","Rodriguez","Teo");
        teo.setCalification(100);
        students.add(teo);

        Student aura =  new Student("1701232","dawn","Rodriguez","aura");
        aura.setCalification(23);
        students.add(aura);

        Student jalis =  new Student("27323972","dawn","Rodriguez","Teo");
        jalis.setCalification(42);
        students.add(jalis);

        CalificationCSVWriter writer = new CalificationCSVWriter();
        try{
            writer.writeCSV(students);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        testStudentCSVReader();
        testCalificationCSVWritter();
    }
}


package CSVControllers;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import Models.*;
import java.util.ArrayList;

public class StudentCSVReader{
    private File file;
    private String columnSchema;

    public StudentCSVReader(){
        this.file = new File("Lista Alumnos.csv");
        this.columnSchema = "matricula,primer apellido,segundo apellido,nombres";
    }

    public ArrayList<Student> getStudentsList() throws IOException{
        try{
            isValidCSV();
        } catch(Exception e){
            throw new IOException(e.getMessage());
        }

        ArrayList<Student> studentsList = new ArrayList<Student>();
        Student student = null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st = st = br.readLine();
            
            while ((st = br.readLine()) != null){
                String[] csvRows = st.split(",");
                student = new Student(csvRows[0], csvRows[1], csvRows[2], csvRows[3]);
                studentsList.add(student);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return studentsList;
    }

    public boolean isValidCSV() throws IOException{
        if (!file.exists()){
            throw new IOException("File doesnot exists");
        }
        if (file.length() == 0){
            throw new IOException("File is empty");
        }
        if (!columnsMatch()){
            throw new IOException("Columns doesnot match with the schema");
        }
        return true;
    }

    private boolean columnsMatch(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String columns = br.readLine().trim();

            if (columnSchema.equals(columns)) {
                return true; 
            }
            
        } catch(IOException e) {
            System.out.println(e);
        }
        return false;
    }

}
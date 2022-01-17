package CSVControllers;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;

import Models.*;


public class CalificationCSVWriter{
    private File file = new File("Calificaciones.csv");
    private String columSchema = "matricula,nombre asignatura, calificacion";
    private String subjectName = "Diseño de Sofware";

    public void writeCSV(ArrayList<Student> studentsList) throws IOException {
        String row;
        FileWriter fw = new FileWriter(file);
        try{
            fw.write(columSchema + "\n");
            for(Student student : studentsList){
                row = String.format("%s,%s,%d\n", 
                    student.getEnrollmentNumber(), 
                    subjectName,
                    student.getCalification()
                );
                fw.write(row);
            }
            
        } catch (IOException e){
            System.out.println(e.getMessage());
        } finally{
            fw.close();
        }
    }

}
package UI;

import javax.swing.*;
import java.util.ArrayList;
import Models.*;
import CSVControllers.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Collections;
import java.util.regex.*;  

public class UI extends JFrame{
  // this is an autoreference variable which is used inside the 
  // context of the anonymous classes created for event handling
  private UI frame = this;

  private JLabel labelCalificationField;
  private JLabel labelName;
  private JLabel labelEnrollmentNumber;

  private JTextField textFieldCalification;
  private JTextField textFieldName;
  private JTextField textFieldEnrollmentNumber;

  private JButton buttonSubmitCalification;
  private JButton buttonNextStudent;
  private JButton buttonPrevStudent;
  private JButton buttonGenerateStudentCalificationsCSV;
  private JButton buttonGenerateCSV;
  private JButton buttonReloadFrame;

  private StudentCSVReader csvReader = new StudentCSVReader();
  private CalificationCSVWriter csvWriter = new CalificationCSVWriter();

  private ArrayList<Student> studentsList;
  private ArrayList<Boolean> studentsWithCalficiation;
  private int currentStudentIndex;
  private boolean calificationsCSVgenerated = false;

  public UI() {

    setLayout(null);
    setBounds(10,10,700,500);
    setTitle("Generador de CSV de calificaciones");
    setResizable(true);
    WindowClosingEventHandler();

    //components functions
    generateCalificationsCSV();
    reloadFrame();
    studentDataFields();
    moveToNextStudent();
    moveToPrevStudent();
    submitCalification();
    setVisible(true);
    getStudentsData();
  }

  public void generateStudentsWithCalficiation(int size){
    studentsWithCalficiation = new ArrayList<Boolean>(size);
    studentsWithCalficiation.addAll(Collections.nCopies(size, Boolean.FALSE));
  }

  /*The function uses the csvReader for fetching the information
  of the students. If there is an exeption when reading the information,
  getStudentsList method propagates the problem to this function where
  is handled and a message is shown in the UI.
  */
  public void getStudentsData(){
    try{
      studentsList = csvReader.getStudentsList();

      if(!studentsList.isEmpty()) {
        currentStudentIndex = 0;
        loadStudentDataFields();
        generateStudentsWithCalficiation(studentsList.size());
        buttonPrevStudent.setEnabled(false);
      }
    } catch(IOException e){
      showPopUp(e.getMessage() + " try the reload button");
      textFieldCalification.setEditable(false);
      buttonSubmitCalification.setEnabled(false);
      buttonPrevStudent.setEnabled(false);
      buttonNextStudent.setEnabled(false);
    }
  }

  /*The function validates if the user has generated a CSV file.
  if it has the close event is set as usual EXIT_ON_CLOSE, if it hasn't
  the function use a confirmation dialog for closing the application
  */
  private void WindowClosingEventHandler(){ 
    addWindowListener(
      new WindowAdapter() { 
        @Override 
        public void windowClosing(WindowEvent e) { 
          if (calificationsCSVgenerated){
            setDefaultCloseOperation(EXIT_ON_CLOSE);
          } else {
            int confirmed = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to exit this application without generating your califications csv?", 
            "Exit Program Message Box",
            JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
              setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
            else{
              setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            }
          }
        } 
      }
    );
  }

  /*This function is use whenever we have to block the user interface
  and sohw an erro msg
  */
  public void showPopUp(String msg){
    JOptionPane.showMessageDialog(this, msg);
  }

  /*----------------------------------------------------------------
  ---------------------Text Fields and Labels-----------------------
  ------------------------------------------------------------------
  */

  public void studentDataFields(){
    labelName = new JLabel("Nombre: ");
    labelName.setBounds(50, 50, 200, 30);

    textFieldName = new JTextField();
    textFieldName.setBounds(50, 100, 200, 30);
    textFieldName.setEditable(false);

    labelEnrollmentNumber = new JLabel("Matricula: ");    
    labelEnrollmentNumber.setBounds(50, 150, 200, 30);  
    
    textFieldEnrollmentNumber = new JTextField();
    textFieldEnrollmentNumber.setBounds(50, 200, 200, 30);
    textFieldEnrollmentNumber.setEditable(false);

    labelCalificationField = new JLabel("Introduzca la calificacion: ");
    labelCalificationField.setBounds(50, 250, 200, 30);

    textFieldCalification = new JTextField();
    textFieldCalification.setBounds(50, 300, 200, 30);

    add(labelName);
    add(textFieldName);
    add(labelEnrollmentNumber);
    add(textFieldEnrollmentNumber);
    add(labelCalificationField);
    add(textFieldCalification);
  }

  public void loadStudentDataFields(){
    textFieldName.setText(studentsList.get(currentStudentIndex).getNames());
    textFieldEnrollmentNumber.setText(studentsList.get(currentStudentIndex).getEnrollmentNumber());
    textFieldCalification.setText(String.valueOf(studentsList.get(currentStudentIndex).getCalification()));
  }


  /*----------------------------------------------------------------
  -------------------------Buttons code-----------------------------
  ------------------------------------------------------------------
  */

  public void generateCalificationsCSV(){
    buttonGenerateCSV = new JButton("Generate Calificaciones.csv");
    buttonGenerateCSV.setBounds(500, 50, 80, 40);
    buttonGenerateCSV.setEnabled(false);
    buttonGenerateCSV.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          try{
            csvWriter.writeCSV(studentsList);
            calificationsCSVgenerated = true;
          } catch (Exception err){
            showPopUp(err.getMessage());
          }
      }
    });
    add(buttonGenerateCSV);
  }

  public void reloadFrame(){
    buttonReloadFrame = new JButton("Reload");
    buttonReloadFrame.setBounds(500, 100, 80, 40);

    buttonReloadFrame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

          if(currentStudentIndex == studentsList.size()-1 && currentStudentIndex>1){
            buttonNextStudent.setEnabled(true);
          }
          currentStudentIndex = 0;
          //SwingUtilities.updateComponentTreeUI(frame);
          textFieldCalification.setEditable(true);
          buttonSubmitCalification.setEnabled(true);
          buttonGenerateCSV.setEnabled(false);
          
          getStudentsData();
          loadStudentDataFields();
      }
    });
    add(buttonReloadFrame);
  }

  public void moveToNextStudent(){
    buttonNextStudent = new JButton("Next");
    buttonNextStudent.setBounds(500, 150, 80, 40);

    buttonNextStudent.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          
          if (currentStudentIndex >= studentsList.size() -1 || studentsList.size() == 1){
            buttonNextStudent.setEnabled(false);
          }else {   
            currentStudentIndex++;
            if (currentStudentIndex >= studentsList.size() -1 || studentsList.size() == 1){
              buttonNextStudent.setEnabled(false);
              loadStudentDataFields();
            } else {
              buttonPrevStudent.setEnabled(true);
              loadStudentDataFields();
            }
          }

          if(studentsWithCalficiation.get(currentStudentIndex)){
            buttonSubmitCalification.setEnabled(false);
          } else {
            buttonSubmitCalification.setEnabled(true);
          }
      }
    });
    add(buttonNextStudent);
  }

  public void moveToPrevStudent(){
    buttonPrevStudent = new JButton("Prev");
    buttonPrevStudent.setBounds(500, 250, 80, 40);
    buttonPrevStudent.setEnabled(false);

    buttonPrevStudent.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

          if (currentStudentIndex -1 < 0 || studentsList.size() == 1) {
            buttonPrevStudent.setEnabled(false);
          }else { 
            currentStudentIndex--;
            if (currentStudentIndex -1 < 0 || studentsList.size() == 1) {
              buttonPrevStudent.setEnabled(false);
              loadStudentDataFields();
            } else{
              buttonNextStudent.setEnabled(true);
              loadStudentDataFields();
            }
          }

          if(studentsWithCalficiation.get(currentStudentIndex)){
            buttonSubmitCalification.setEnabled(false);
          } else {
            buttonSubmitCalification.setEnabled(true);
          }
      }
    });
    add(buttonPrevStudent);
  }

  public void submitCalification(){
    buttonSubmitCalification = new JButton("Submit");
    buttonSubmitCalification.setBounds(500, 200, 80, 40);

    buttonSubmitCalification.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
          String calNotValidated = textFieldCalification.getText();
          int calification; 

          if(Pattern.matches("\\d{1,3}", calNotValidated)){
            calification = Integer.parseInt(calNotValidated);
          } else{
              showPopUp("Please enter numeric response between 0 and 100");
              return;
          }    

          if (0 <= calification  && calification <= 100){
            studentsList.get(currentStudentIndex).setCalification(calification);
            studentsWithCalficiation.set(currentStudentIndex, true);
            loadStudentDataFields();
            buttonSubmitCalification.setEnabled(false);
          } else{
            showPopUp("Please enter a grade between 0 and 100");
          }

          if(studentsWithCalficiation.contains(false)){
            buttonGenerateCSV.setEnabled(true);
          }
          
      }
    });
    add(buttonSubmitCalification);
  }
}

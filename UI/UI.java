package UI;

import javax.swing.*;
import java.util.ArrayList;
import Models.*;
import CSVControllers.*;
import java.awt.event.*;
import java.io.IOException;

public class UI extends JFrame{
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
  private JButton buttonShowPopUp;
  private JButton buttonReloadFrame;

  private StudentCSVReader csvReader = new StudentCSVReader();
  private CalificationCSVWriter csvWriter = new CalificationCSVWriter();
  private ArrayList<Student> studentsList;
  private int currentStudentIndex;
  private boolean calificationsCSVgenerated = false;

  public UI() {
    setLayout(null);
    setBounds(10,10,700,500);
    setTitle("Generador de CSV de calificaciones");
    setResizable(true);
    //setDefaultCloseOperation(EXIT_ON_CLOSE);
    WindowClosingEventHandler();
    /*This is where all our custom methods and code are*/
    calificationTextField();
    generateCalificationsCSV();
    realoadFrame();
    studentDataFields();
    moveToNextStudent();
    moveToPrevStudent();

    submitCalification();
    setVisible(true);

    getStudentsData();

    /* Finish of the customn methods*/
  }

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

    add(labelName);
    add(textFieldName);
    add(labelEnrollmentNumber);
    add(textFieldEnrollmentNumber);
  }

  public void loadStudentDataFields(){
    textFieldName.setText(studentsList.get(currentStudentIndex).getNames());
    textFieldEnrollmentNumber.setText(studentsList.get(currentStudentIndex).getEnrollmentNumber());
    textFieldCalification.setText(String.valueOf(studentsList.get(currentStudentIndex).getCalification()));
  }

  public void calificationTextField(){
    labelCalificationField = new JLabel("Introduzca la calificacion: ");
    labelCalificationField.setBounds(50, 250, 200, 30);

    textFieldCalification = new JTextField();
    textFieldCalification.setBounds(50, 300, 200, 30);
    
    add(labelCalificationField);
    add(textFieldCalification);
  }

  public void generateCalificationsCSV(){
    buttonShowPopUp = new JButton("Generate Calificaciones.csv");
    buttonShowPopUp.setBounds(500, 50, 80, 40);

    buttonShowPopUp.addActionListener(new ActionListener() {
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
    add(buttonShowPopUp);
  }

  public void showPopUp(String msg){
    JOptionPane.showMessageDialog(this, msg);
  }

  public void realoadFrame(){
    buttonReloadFrame = new JButton("Reload Application");
    buttonReloadFrame.setBounds(500, 100, 80, 40);

    buttonReloadFrame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          SwingUtilities.updateComponentTreeUI(frame);
          currentStudentIndex = 0;
          textFieldCalification.setEditable(true);
          buttonSubmitCalification.setEnabled(true);
          buttonPrevStudent.setEnabled(false);
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
          int calification = Integer.parseInt(textFieldCalification.getText());
          if (0 <= calification  && calification <= 100){
            studentsList.get(currentStudentIndex).setCalification(calification);
            loadStudentDataFields();
          } else{
            showPopUp("Please enter a grade between 0 and 100");
          }
          
      }
    });
    add(buttonSubmitCalification);
  }

  public void getStudentsData(){
    try{
      studentsList = csvReader.getStudentsList();
      if(!studentsList.isEmpty()) {
        currentStudentIndex = 0;
        loadStudentDataFields();
        buttonPrevStudent.setEnabled(true);
        if(studentsList.size() > 1){
          buttonNextStudent.setEnabled(true);
        } else {
          buttonNextStudent.setEnabled(false);
        }
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
}

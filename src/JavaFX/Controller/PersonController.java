package JavaFX.Controller;

import Connection.ConnectionClass;
import JavaFX.Model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PersonController {

    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField phoneNumberField;
    @FXML
    public DatePicker dateOfBirthField;

    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;


    public TableVewController parentController;

    /**
     * Следующие строки предназначены для создания объекта класса -ConnectionClass-
     * и вызова его метода -getConnection()-
     * Переменная -connection- далее используется для отправки запросов на базу данных
     */
    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();


    Person person = null;

    public PersonController(){
        initializeFields();
    }

    public PersonController(Person person){
        this.person = person;
        initializeFields();
    }

    public void setParentController(TableVewController parentController) {
        this.parentController = parentController;
    }

    public void setPerson(Person person) {
        this.person = person;
        initializeFields();
    }

    public void initializeFields(){
        if(person != null){
            nameField.setText(person.getName());
            surnameField.setText(person.getSurname());
            phoneNumberField.setText(person.getPhone_number());
            dateOfBirthField.setUserData(person.getDate_of_birth());
        }
    }

    @FXML
    public void saveData(ActionEvent event){
        if (person != null){
            initializeFields();
            try {
                Statement statement=connection.createStatement();
                String sql="UPDATE person SET name = '"+nameField.getText() + "', surname = '" + surnameField.getText() + "', phone_number = '" + phoneNumberField.getText() + "', date_of_birth = '" + dateOfBirthField.getValue() + "'WHERE id='"+ person.getId() +"';";
                statement.executeUpdate(sql);
                System.out.println("Success!");
                ((Node)(event.getSource())).getScene().getWindow().hide();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                Statement statement=connection.createStatement();
                String sql="INSERT INTO person(name, surname, phone_number, date_of_birth) VALUES ('"+nameField.getText() + "', '" + surnameField.getText() + "', '" + phoneNumberField.getText() + "', '" + dateOfBirthField.getValue() + "');";
                statement.executeUpdate(sql);
                System.out.println("Success!");
                parentController.initializeTableValues();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}

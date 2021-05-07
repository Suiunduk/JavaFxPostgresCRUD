package JavaFX.Controller;

import Connection.ConnectionClass;
import JavaFX.Model.Person;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableVewController {

    @FXML
    public TableView<Person> personTable;
    @FXML
    public TableColumn<Person, String> idColumn;
    @FXML
    public TableColumn<Person, String> nameColumn;
    @FXML
    public TableColumn<Person, String> surnameColumn;
    @FXML
    public TableColumn<Person, String> numberColumn;
    @FXML
    public TableColumn<Person, String> dateOfBirthColumn;

    @FXML
    public Button addButton;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;


    Person person = null;

    /**
     * Следующие строки предназначены для создания объекта класса -ConnectionClass-
     * и вызова его метода -getConnection()-
     * Переменная -connection- далее используется для отправки запросов на базу данных
     */
    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();

    public TableVewController() {
    }

    /**
     * Функция -initialize()- предназначена для привязки столбцов к соответствующим переменным в классе -Person-
     * Запускается при создании таблицы, т.е. при запуске проекта.
     * Последняя строка -initializeTableValues();- вызывает метод для заполнения таблицы данными из Базы данных.
     */
    @FXML
    private void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));
        initializeTableValues();
    }

    public void addPerson(){
        person = personTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation(getClass().getResource("/JavaFX/View/person_view.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(TableVewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        PersonController personController = loader.getController();
        personController.setParentController(this);

        if(person != null){
            personController.setPerson(person);
            personController.initializeFields();
        }

        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();

    }

    public void deletePerson(){
        try {
            person = personTable.getSelectionModel().getSelectedItem();

            if(person != null) {
                String sql="DELETE FROM person WHERE id  =" + person.getId();

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();

                initializeTableValues();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableVewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Данный метод делает запрос -SELECT- в базу данных и из полученных данных формирует список
     * типа -ObservableList<Person>-, с помощью которого заполняет таблицу -personTable-
     */
    public void initializeTableValues(){
        try {
            Statement statement=connection.createStatement();

            String sql="SELECT * FROM person;";

            ResultSet resultSet=statement.executeQuery(sql);

            ObservableList<Person> personList = FXCollections.observableArrayList();

            if (resultSet.next()){
                while (resultSet.next()) {
                    Person person = new Person(new SimpleIntegerProperty(resultSet.getInt("id")),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("phone_number"),
                            resultSet.getDate("date_of_birth").toLocalDate());
                    personList.add(person);
                }
                personTable.setItems(personList);
            }else {
                System.out.println("no data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

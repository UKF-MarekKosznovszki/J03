package AdressBookSQLite;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FXMLTableViewController  {

    @FXML private TableView<Person> tableView;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML SQLiteJDBC sqlConnector;


    @FXML protected void initialize(){
        connectDatabase();
        populateTableView();
    }

    //TODO: Add remove person functionality - https://www.sqlitetutorial.net/sqlite-delete/
    //TODO: Add update person functionality - https://www.sqlitetutorial.net/sqlite-update/


    @FXML protected void addPerson(ActionEvent event) {
        ObservableList<Person> data = tableView.getItems();

        data.add(new Person(firstNameField.getText(), lastNameField.getText(), emailField.getText()));
        sqlConnector.insertPerson(lastNameField.getText(), firstNameField.getText(), emailField.getText());

        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
    }

    @FXML protected void removePerson(ActionEvent event){
        ObservableList<Person> data = tableView.getItems();
        Person person = this.tableView.getSelectionModel().getSelectedItem();
        if (person == null) return;
        data.remove(person);
        this.sqlConnector.removePerson(person.getLastName(),person.getFirstName(),person.getEmail());
    }

    @FXML protected void updatePerson(ActionEvent event) {
        if (lastNameField.getText().length() == 0 || firstNameField.getText().length() == 0 || emailField.getText().length() == 0)
        {
            return;
        }

        ObservableList<Person> data = tableView.getItems();
        TableView.TableViewSelectionModel<Person> selectionModel = tableView.getSelectionModel();

        Person person = data.get(selectionModel.getSelectedIndex());
        this.sqlConnector.updatePerson(lastNameField.getText(), firstNameField.getText(), emailField.getText(), person.getLastName(), person.getFirstName(), person.getEmail());
        data.set(selectionModel.getSelectedIndex(), new Person(firstNameField.getText(), lastNameField.getText(), emailField.getText()));

        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
    }


    @FXML protected void connectDatabase() {
        sqlConnector = new SQLiteJDBC();
    }

    @FXML protected void populateTableView() {
        ObservableList<Person> data = tableView.getItems();
        ResultSet resultSet = null;
        try {
            resultSet = sqlConnector.getResultSet();

        while(resultSet.next()) {
            String lastName = resultSet.getString("lastname");
            String firstName = resultSet.getString("firstname");
            String email = resultSet.getString("email");
            data.add(new Person(lastName,firstName,email));
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

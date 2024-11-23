package org.example.assignment10;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {

    @FXML
    private TextField nameField, rateField, hoursField, maxHoursField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> nameColumn, typeColumn;
    @FXML
    private TableColumn<Employee, Double> salaryColumn;

    private ObservableList<Employee> employeeList;

    public void initialize() {
        // Initialize the employee list
        employeeList = FXCollections.observableArrayList();

        // Set up TableView columns
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));
        salaryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().calculateSalary()));

        employeeTable.setItems(employeeList);

        // Initialize the ComboBox with employee types
        typeComboBox.setItems(FXCollections.observableArrayList("Full-time", "Part-time", "Contractor"));
    }

    // Add employee based on input
    @FXML
    private void addEmployee() {
        String name = nameField.getText();
        String type = typeComboBox.getValue();

        if (name.isEmpty() || type == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try {
            if (type.equals("Full-time")) {
                double salary = Double.parseDouble(rateField.getText());
                employeeList.add(new FullTimeEmployee(name, salary));
            } else if (type.equals("Part-time")) {
                double hourlyRate = Double.parseDouble(rateField.getText());
                int hoursWorked = Integer.parseInt(hoursField.getText());
                employeeList.add(new PartTimeEmployee(name, hourlyRate, hoursWorked));
            } else if (type.equals("Contractor")) {
                double hourlyRate = Double.parseDouble(rateField.getText());
                int maxHours = Integer.parseInt(maxHoursField.getText());
                int hoursWorked = Integer.parseInt(hoursField.getText());  // Contractor also needs hoursWorked
                employeeList.add(new Contractor(name, hourlyRate, maxHours, hoursWorked));
            }

            employeeTable.refresh();  // Ensure the table is refreshed
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numerical values for salary and hours.");
        }

        clearFields();  // Clear the input fields after adding
    }


    // Calculate and refresh salaries
    @FXML
    private void calculateSalaries() {
        // Recalculate and update the salary for all employees
        for (Employee employee : employeeList) {
            // Refresh the salary for each employee by calling calculateSalary()
            employee.calculateSalary();
        }

        // Refresh the table to display the updated salaries
        employeeTable.refresh();

        // Show an alert confirming the salary update
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Salaries Updated");
        alert.setHeaderText(null);
        alert.setContentText("Salaries have been updated.");
        alert.showAndWait();
    }


    // Remove selected employee
    @FXML
    private void removeEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employeeList.remove(selectedEmployee);
        } else {
            showAlert("Error", "Please select an employee to remove.");
        }
    }

    // Show an alert box
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear input fields after adding employee
    private void clearFields() {
        nameField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        rateField.clear();
        hoursField.clear();
        maxHoursField.clear();
    }
}

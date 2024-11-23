module org.example.assignment10 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.assignment10 to javafx.fxml;
    exports org.example.assignment10;
}
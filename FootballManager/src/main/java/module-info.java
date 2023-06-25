module com.example.footballmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires tornadofx.controls;


    opens com.example.footballmanager to javafx.fxml;
    exports com.example.footballmanager;
}
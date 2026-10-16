module ni.edu.uam.papeleria_campus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ni.edu.uam.papeleria_campus to javafx.fxml;
    exports ni.edu.uam.papeleria_campus;
}
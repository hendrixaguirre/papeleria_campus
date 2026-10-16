module ni.edu.uam.papeleria_campus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ni.edu.uam.papeleria_campus.controller to javafx.fxml;
    opens ni.edu.uam.papeleria_campus to javafx.fxml;
    exports ni.edu.uam.papeleria_campus;
    exports ni.edu.uam.papeleria_campus.controller;
    exports ni.edu.uam.papeleria_campus.model;
    exports ni.edu.uam.papeleria_campus.database;
    exports ni.edu.uam.papeleria_campus.DAO;



}
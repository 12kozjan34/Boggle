module hr.kozjan.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens hr.kozjan.demo to javafx.fxml;
    exports hr.kozjan.demo;
    exports hr.kozjan.demo.Controllers;
    opens hr.kozjan.demo.Controllers to javafx.fxml;
    exports hr.kozjan.demo.Models;
    opens hr.kozjan.demo.Models to javafx.fxml;
}
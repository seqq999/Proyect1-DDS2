module edu.ucr.c36342.proyectodesarrollo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom2;
    //requires edu.ucr.c36342.proyectodesarrollo2;


    opens edu.ucr.c36342.proyectodesarrollo2 to javafx.fxml;
    exports edu.ucr.c36342.proyectodesarrollo2;
}
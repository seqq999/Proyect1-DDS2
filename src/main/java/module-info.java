module edu.ucr.c.proyectodesarrollo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom2;
    requires java.desktop;
    //requires edu.ucr.c.proyectodesarrollo;
    //requires edu.ucr.c36342.proyectodesarrollo2;


    opens edu.ucr.c36342.proyectodesarrollo2 to javafx.fxml;
    exports edu.ucr.c36342.proyectodesarrollo2;
}
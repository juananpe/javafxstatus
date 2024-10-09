module eus.ehu.javafxstatus {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna;
    requires java.desktop;
    requires jdk.httpserver;


    opens eus.ehu.javafxstatus to javafx.fxml;
    exports eus.ehu.javafxstatus;
}
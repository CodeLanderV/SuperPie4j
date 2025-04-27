module com.superpie.superpie {
    requires com.almasb.fxgl.all;
    requires langchain4j.open.ai;
    requires langchain4j.core;
    requires langchain4j.ollama;
    requires org.controlsfx.controls;
    requires com.github.dockerjava.transport.zerodep;
    requires langchain4j;
    requires com.fasterxml.jackson.databind;
    requires mapdb;
    requires com.google.gson;
    requires com.google.common;
    requires java.desktop;  // Required for JDK HTTP Client


    opens com.superpie.superpie to javafx.fxml;
    exports com.superpie.superpie;
    exports com.superpie.superpie.Login;
    opens com.superpie.superpie.Login to javafx.fxml;
    exports com.superpie.superpie.back;
    opens com.superpie.superpie.back to javafx.fxml;
}
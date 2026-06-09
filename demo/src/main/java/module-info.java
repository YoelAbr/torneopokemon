module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires spring.security.crypto;

    opens org.example.demo to javafx.fxml, org.hibernate.orm.core;
    exports org.example.demo;

    exports Excepciones;
    opens Excepciones to javafx.fxml;

    opens Persistence.Poketorneo to org.hibernate.orm.core;
}

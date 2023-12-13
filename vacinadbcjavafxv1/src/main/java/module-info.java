module JavaFXApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;

    opens poov.vacinadbcjavafx to javafx.fxml, javafx.graphics;
    opens poov.vacinadbcjavafx.modelo to javafx.base, javafx.graphics;
    opens poov.vacinadbcjavafx.controller to javafx.fxml, javafx.graphics;
    exports poov.vacinadbcjavafx;
}
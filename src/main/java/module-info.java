module com.example.grammar_parser {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.grammar_parser to javafx.fxml;
    exports com.example.grammar_parser;
}
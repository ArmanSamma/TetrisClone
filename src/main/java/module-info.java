module com.arman.tetrisclone {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
            
    opens com.arman.tetrisclone to javafx.fxml;
    exports com.arman.tetrisclone;
}
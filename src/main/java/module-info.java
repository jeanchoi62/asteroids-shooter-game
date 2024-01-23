module com.example.asteroidsshootergame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.asteroidsshootergame to javafx.fxml;
    exports com.example.asteroidsshootergame;
}
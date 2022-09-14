import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Welcome to Hexasquid!
 * This game is based on hexapawn by Martin Gardner: https://en.wikipedia.org/wiki/Hexapawn
 * The original paper can be found here: http://cs.williams.edu/~freund/cs136-073/GardnerHexapawn.pdf
 * I call it hexasquid because instead of using pawns I use a circle, triangle and square just
 * like the invitation from the show squid game.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hexasquid");

        // Use a borderpane to separate the game from the buttons
        BorderPane root = new BorderPane();
        // HBox to hold all the button on the top of the screen
        HBox buttons = new HBox(10);
        Button reset = new Button("Reset");
        Button auto = new Button("Auto");
        buttons.getChildren().addAll(reset, auto);
        buttons.setAlignment(Pos.CENTER);
        buttons.setStyle("-fx-background-color: #21DE52");
        root.setTop(buttons);
        // Gridpane to hold the game in
        GridPane gridPane = new GridPane();
        root.setCenter(gridPane);
        // Class that represents the board the game is played on
        Board board = new Board(gridPane, 200);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        // Buttons utilize methods on the board
        reset.setOnAction(event -> board.reset());
        auto.setOnAction(event -> board.runAuto());
    }
}

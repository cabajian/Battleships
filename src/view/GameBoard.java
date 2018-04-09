package view;

import controller.BattleShip;
import controller.Observer;
import controller.ShipData;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.application.*;
import javafx.scene.*;
import model.Location;

import java.util.List;

/**
 * The main view class that contains the PegView, ShipView, and ConcreteConsoleWriter objects.
 * Creates a controller object based on the provided command line arguments.
 *
 * @author Chris Abajian
 */
public class GameBoard extends Application implements Observer<ShipData> {

    private BattleShip controller;
    private GridPane pPane;

    /**
     * start method to set the scene and stage. The player and target panes are created, and
     * the controller is created.
     *
     * @param stage the stage to set
     */
    public void start(Stage stage) {
        // create the console
        ScrollPane lContainer = new ScrollPane();
        Label display = new Label();
        lContainer.setContent(display);
        lContainer.setMinViewportHeight(50);
        ConcreteConsoleWriter console = new ConcreteConsoleWriter(display);

        // get the parameters, create the controller
        Parameters params = getParameters();
        List<String> args = params.getRaw();
        if (args.size() == 0) {
            this.controller = new BattleShip(console);
        } else if (args.size() == 1) {
            this.controller = new BattleShip(console, Integer.parseInt(args.get(0)));
        } else if (args.size() == 2){
            this.controller = new BattleShip(console, args.get(0), Integer.parseInt(args.get(1)));
        }

        // create target and player containers
        VBox tContainer = new VBox();
        VBox pContainer = new VBox();
        GridPane tPane = new GridPane();
        this.pPane = new GridPane();

        // create player and target view
        for (int i = 0; i < BattleShip.NUM_COLS; i++) {
            for (int j = 0; j < BattleShip.NUM_ROWS; j++) {
                tPane.add(new PegView(new Location(i, j), this.controller), i, j);

                Rectangle rect = new Rectangle(40, 40);
                rect.getStyleClass().add("rectangle");
                this.pPane.add(rect, i, j);
            }
        }

        // create labels for target and player grids
        Label tLabel = new Label("Target");
        Label pLabel = new Label("Player");
        tLabel.setMaxWidth(Double.MAX_VALUE);
        tLabel.setAlignment(Pos.CENTER);
        pLabel.setMaxWidth(Double.MAX_VALUE);
        pLabel.setAlignment(Pos.CENTER);
        // add labels and panes to the target and player containers
        tContainer.getChildren().addAll(tLabel, tPane);
        pContainer.getChildren().addAll(pPane, pLabel);

        // add the target container, console, and player container to the total view
        VBox totalView = new VBox();
        totalView.getChildren().addAll(tContainer, lContainer, pContainer);

        // set the scene
        Scene scene = new Scene(totalView);
        scene.getStylesheets().add("view/battleship.css");
        stage.setScene(scene);
        stage.show();

        // add the ships
        this.controller.addShips(this);
    }

    /**
     * updates the player's view. The ships are added here upon initialization.
     * Because the GameBoard updates upon a sunk ship, the ShipView's are only
     * created when the ship isn't sunk.
     *
     * @param pushValue The value that will be pushed to the observer
     */
    public void update(ShipData pushValue) {
        // update player view to add ships
        if (!pushValue.sunk()) {
            int row = pushValue.getBowLocation().getRow();
            int col = pushValue.getBowLocation().getCol();
            int size = pushValue.getSize();

            this.pPane.add(new ShipView(pushValue, 0), row, col);
            for (int i = 0; i < size; i++) {
                if (pushValue.getOrientation().equals(ShipData.Orientation.HORIZONTAL)) {
                    this.pPane.add(new ShipView(pushValue, i), row, col + i);
                } else {
                    this.pPane.add(new ShipView(pushValue, i), row + i, col);
                }
            }
        }
    }
}

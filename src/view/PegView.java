package view;

import controller.BattleShip;
import controller.Observer;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Location;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;

/**
 * PegView represents a single peg along the target board. Each peg is a rectangle background
 * with a transparent button on top. Upon click of the button, the peg is no longer
 * usable and tells the controller. The peg changes apearance depending on if it was
 * a hit or miss.
 *
 * @author Chris Abajian
 */
public class PegView extends StackPane implements Observer<Boolean> {

    private Button btn;
    private BattleShip controller;

    /**
     * constructor for PegView that sets the rectangle background and the transparent
     * button. The button action event is set to communicate with the controller, and
     * the controller registers the PegView.
     *
     * @param controller the BattleShip controller
     */
    public PegView(Location location, BattleShip controller) {
        Rectangle rect = new Rectangle(40, 40);
        rect.getStyleClass().add("rectangle");

        this.btn = new Button();
        this.controller = controller;

        this.getChildren().addAll(rect, this.btn);

        this.btn.setOnAction(event -> {
            if (this.controller.isMyTurn()) this.controller.attack(location);
        });

        this.controller.registerTarget(location, this);
    }

    /**
     * updates the appearance of the peg based on if the attack was a hit
     * or miss. Finished the turn by telling the controller it is done.
     *
     * @param wasHit
     */
    public void update(Boolean wasHit) {
        Circle peg = new Circle(10);

        Color c = wasHit ? RED : WHITE;
        peg.setFill(c);

        javafx.application.Platform.runLater(new Runnable() {
            public void run() {
                getChildren().add(peg);
                getChildren().remove(btn);
            }
        });

        this.controller.done();
    }
}

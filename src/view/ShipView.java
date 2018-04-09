package view;

import controller.Observer;
import controller.ShipData;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import model.ShipModel;

import static javafx.scene.paint.Color.*;

/**
 * ShipView is the class for each designated ship piece on the board.
 * Extends StackPane to add circle ontop of the pane, changes color
 * depending on hit status.
 *
 * @author Chris Abaian
 */
public class ShipView extends StackPane implements Observer<Integer> {

    private int point;
    private ShipData data;
    private Circle center;

    /**
     * Constructor for a ShipView object. Set the pane style via CSS class,
     * create and add a circle peg to the center, and register the pane to
     * the controller.
     *
     * @param data the ship's data
     * @param point the point of the ShipView piece along the length of the ship
     */
    public ShipView(ShipData data, int point) {
        if (point == data.getSize()-1) {
            if (data.getOrientation().equals(ShipData.Orientation.HORIZONTAL)) {
                this.getStyleClass().add("shipEnd_right");
            } else {
                this.getStyleClass().add("shipEnd_bot");
            }
        } else if (point == 0) {
            if (data.getOrientation().equals(ShipData.Orientation.HORIZONTAL)) {
                this.getStyleClass().add("shipEnd_left");
            } else {
                this.getStyleClass().add("shipEnd_top");
            }
        } else {
            this.getStyleClass().add("ship");
        }

        this.center = new Circle(10);
        this.center.setFill(DARKGRAY);

        getChildren().add(center);

        this.point = point;
        this.data = data;

        ShipModel controller = (ShipModel)data;
        controller.register(this);
    }

    /**
     * updates the ship's center peg color if hit. If the ship is sunk,
     * set the entire pane to red.
     *
     * @param point the point of the ship being updated. Because all ShipView's
     *              attached to the ship are updated, only this point should
     *              change color.
     */
    public void update(Integer point) {
        if (this.point == point) this.center.setFill(RED);
        if (this.data.sunk()) {
            this.getStyleClass().add("destroyed");
        }
    }
}
package view;

import controller.ConsoleWriter;
import javafx.scene.control.Label;

/**
 * Writes to the provided label as a console.
 *
 * @author Chris Abajian
 */
public class ConcreteConsoleWriter implements ConsoleWriter {

    private Label console;

    /**
     * constructor for the console writer
     *
     * @param console the label to set the text to
     */
    public ConcreteConsoleWriter(Label console) {
        this.console = console;
    }

    /**
     * write the text by setting the label text.
     *
     * @param text The message to write
     */
    public void write(String text) {
        javafx.application.Platform.runLater(new Runnable() {
            public void run() {
                if (!text.equals("")) console.setText(text + "\n" + console.getText());
            }
        });
    }
}
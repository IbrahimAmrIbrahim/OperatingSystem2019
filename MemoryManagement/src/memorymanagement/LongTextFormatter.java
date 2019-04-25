package memorymanagement;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

public class LongTextFormatter extends TextFormatter<Number> {

    public LongTextFormatter() {
        super(
                new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                if (object == null) {
                    return "";
                }
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return Long.valueOf(string);
            }
        }, 0,
                new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if (change.getControlNewText().isEmpty()) {
                    return change;
                }
                String text = change.getText();

                if (text.matches("[0-9]*")) {
                    return change;
                }

                return null;
            }
        });
    }
}

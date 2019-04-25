package memorymanagement;

import java.text.NumberFormat;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @param <T>
 */
public class EditableNumericIntegerTableCell<T> extends TableCell<T, Long> {

    private TextField textField;

    @Override
    public void startEdit() {
        if (editableProperty().get()) {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.requestFocus();
            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() != null ? Long.toString(getItem()) : null);
        setGraphic(null);
    }

    @Override
    public void updateItem(Long item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                    textField.selectAll();
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {
        textField = new TextField();
        textField.setTextFormatter(new LongTextFormatter());
        textField.setText(getString());

        textField.setOnAction(evt -> {
            if (textField.getText() != null && !textField.getText().isEmpty()) {
                NumberStringConverter nsc = new NumberStringConverter();
                Number n = nsc.fromString(textField.getText());
                commitEdit(Long.valueOf(n.longValue()));
            }
        });
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        textField.setOnKeyPressed((ke) -> {
            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                cancelEdit();
            }
        });

        textField.setAlignment(Pos.CENTER_RIGHT);
        this.setAlignment(Pos.CENTER_RIGHT);
    }

    private String getString() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return getItem() == null ? "" : nf.format(getItem());
    }

    @Override
    public void commitEdit(Long item) {
        if (isEditing()) {
            super.commitEdit(item);
        } else {
            final TableView<T> table = getTableView();
            if (table != null) {
                TablePosition<T, Long> position = new TablePosition<T, Long>(getTableView(),
                        getTableRow().getIndex(), getTableColumn());
                CellEditEvent<T, Long> editEvent = new CellEditEvent<T, Long>(table, position,
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(getTableColumn(), editEvent);
            }
            updateItem(item, false);
            if (table != null) {
                table.edit(-1, null);
            }

        }
    }
}

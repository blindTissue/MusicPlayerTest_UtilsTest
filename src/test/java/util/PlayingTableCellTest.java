package util;

import app.musicplayer.util.PlayingTableCell;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class  PlayingTableCellTest {

    // Testable subclass that exposes the protected method
    private static class TestablePlayingTableCell<S, T> extends PlayingTableCell<S, T> {
        public void testUpdateItem(T item, boolean empty) {
            updateItem(item, empty);
        }
    }

    private TestablePlayingTableCell<Object, Boolean> cell;

    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() {
        JavaFXInitializer.runLater(() -> {
            cell = new TestablePlayingTableCell<>();
        });
    }

    @Test
    void testUpdateItemWithNullValue() {
        JavaFXInitializer.runLater(() -> {
            cell.testUpdateItem(null, false);
            assertNull(cell.getText(), "Text should be null when item is null");
            assertNull(cell.getGraphic(), "Graphic should be null when item is null");
        });
    }

    @Test
    void testUpdateItemWhenEmpty() {
        JavaFXInitializer.runLater(() -> {
            cell.testUpdateItem(true, true);
            assertNull(cell.getText(), "Text should be null when empty is true");
            assertNull(cell.getGraphic(), "Graphic should be null when empty is true");
        });
    }

    @Test
    void testUpdateItemWithFalseValue() {
        JavaFXInitializer.runLater(() -> {
            cell.testUpdateItem(false, false);
            assertNull(cell.getText(), "Text should be null when item is false");
            assertNull(cell.getGraphic(), "Graphic should be null when item is false");
        });
    }

    @Test
    void testUpdateItemWithTrueValue() {
        JavaFXInitializer.runLater(() -> {
            cell.testUpdateItem(true, false);
            assertNull(cell.getText(), "Text should be null when item is true");
            assertNotNull(cell.getGraphic(), "Graphic should not be null when item is true");
        });
    }

    @Test
    void testUpdateItemWithTrueValueThrowException() {
        JavaFXInitializer.runLater(() -> {
            cell.testUpdateItem(true, false);
            assertNull(cell.getText(), "Text should be null when item is true");
            assertNotNull(cell.getGraphic(), "Graphic should not be null when item is true");
        });
    }
} 
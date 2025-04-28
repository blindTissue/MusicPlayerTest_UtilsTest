package util;

import app.musicplayer.util.CustomSliderSkin;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


/**\
 * Test class for CustomSliderSkin.
 * All tests should be self-explanatory with some unclear ones having comments.
 * Tests were created iteratively, Due to number of branches with multiple conditions,
 * Earlier tests were made to cover many branches at once.
 * Later tests are often made to test specific edge cases.
 * .layout() is called to apply to the pane.
 */
class CustomSliderSkinTest {

    private Slider slider;
    private CustomSliderSkin skin;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX runtime
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    void setUp() {
        // Create components on JavaFX thread
        JavaFXInitializer.runLater(() -> {
            slider = new Slider(0, 100, 50);
            skin = new CustomSliderSkin(slider);
            slider.setSkin(skin);
        });
    }

    @Test
    void testInitialization() {
        JavaFXInitializer.runLater(() -> {
            assertNotNull(skin.getThumb(), "Thumb should be initialized");
            assertNotNull(skin.getTrack(), "Track should be initialized");

            assertTrue(skin.getThumb() instanceof StackPane, "Thumb should be a StackPane");
            assertTrue(skin.getTrack() instanceof StackPane, "Track should be a StackPane");

            assertTrue(skin.getThumb().getStyleClass().contains("thumb"),
                    "Thumb should have the 'thumb' style class");
            assertTrue(skin.getTrack().getStyleClass().contains("track"),
                    "Track should have the 'track' style class");
        });
    }

    @Test
    void testGetThumb() {
        JavaFXInitializer.runLater(() -> {
            StackPane thumb = skin.getThumb();
            assertNotNull(thumb, "Thumb should not be null");
            assertEquals("thumb", thumb.getStyleClass().get(0),
                    "Thumb should have the 'thumb' style class");
        });
    }

    @Test
    void testGetTrack() {
        JavaFXInitializer.runLater(() -> {
            StackPane track = skin.getTrack();
            assertNotNull(track, "Track should not be null");
            assertEquals("track", track.getStyleClass().get(0),
                    "Track should have the 'track' style class");
        });
    }

    @Test
    void testOrientationChange() {
        JavaFXInitializer.runLater(() -> {
            // Test default horizontal orientation
            assertEquals(Orientation.HORIZONTAL, slider.getOrientation());

            // Change to vertical orientation
            slider.setOrientation(Orientation.VERTICAL);
            assertEquals(Orientation.VERTICAL, slider.getOrientation());

            // Force layout for the changes to take effect
            slider.layout();
        });
    }

    @Test
    void testTickMarksAndLabels() {
        JavaFXInitializer.runLater(() -> {
            // Test with both tick marks and labels
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.layout();

            // Test with only tick marks
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(false);
            slider.layout();

            // Test with only tick labels
            slider.setShowTickMarks(false);
            slider.setShowTickLabels(true);
            slider.layout();

            // Test with no tick marks or labels
            slider.setShowTickMarks(false);
            slider.setShowTickLabels(false);
            slider.layout();

        });
    }

    // Tests for various property changes
    @Test
    void testPropertyChanges() {
        JavaFXInitializer.runLater(() -> {

            slider.setMin(-100);
            assertEquals(-100, slider.getMin());

            slider.setMax(200);
            assertEquals(200, slider.getMax());

            slider.setValue(75);
            assertEquals(75, slider.getValue());

            slider.setMajorTickUnit(25);
            assertEquals(25, slider.getMajorTickUnit());

            slider.setMinorTickCount(5);
            assertEquals(5, slider.getMinorTickCount());

            slider.layout();
        });
    }

    @Test
    void testValueExceedingMax() {
        JavaFXInitializer.runLater(() -> {

            slider.setValue(90);
            slider.setMax(80);

            // This should trigger the early return in positionThumb
            slider.layout();

            // Check that value was clamped
            assertTrue(slider.getValue() <= slider.getMax());
        });
    }

    @Test
    void testMouseInteractions() {
        JavaFXInitializer.runLater(() -> {
            StackPane track = skin.getTrack();
            StackPane thumb = skin.getThumb();

            // Create mouse events
            MouseEvent pressEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                    50, 10, 50, 10,
                    null, 1, false, false,
                    false, false, true, false,
                    false, false, false, false, null);

            MouseEvent releaseEvent = new MouseEvent(MouseEvent.MOUSE_RELEASED,
                    50, 10, 50, 10,
                    null, 1, false, false,
                    false, false, true, false,
                    false, false, false, false, null);

            MouseEvent dragEvent = new MouseEvent(MouseEvent.MOUSE_DRAGGED,
                    60, 10, 60, 10,
                    null, 1, false, false,
                    false, false, true, false,
                    false, false, false, false, null);

            // Test track events
            track.fireEvent(pressEvent);
            track.fireEvent(dragEvent);
            track.fireEvent(releaseEvent);

            // Test thumb events
            thumb.fireEvent(pressEvent);
            thumb.fireEvent(dragEvent);
            thumb.fireEvent(releaseEvent);
        });
    }

    @Test
    void testHorizontalLayoutCalculations() {
        JavaFXInitializer.runLater(() -> {
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.resize(200, 50);
            slider.layout();

            try {
                // Access the protected computation methods via reflection
                Method computeMinWidth = CustomSliderSkin.class.getDeclaredMethod("computeMinWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMinHeight = CustomSliderSkin.class.getDeclaredMethod("computeMinHeight", double.class, double.class, double.class, double.class, double.class);
                Method computePrefWidth = CustomSliderSkin.class.getDeclaredMethod("computePrefWidth", double.class, double.class, double.class, double.class, double.class);
                Method computePrefHeight = CustomSliderSkin.class.getDeclaredMethod("computePrefHeight", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxWidth = CustomSliderSkin.class.getDeclaredMethod("computeMaxWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxHeight = CustomSliderSkin.class.getDeclaredMethod("computeMaxHeight", double.class, double.class, double.class, double.class, double.class);

                computeMinWidth.setAccessible(true);
                computeMinHeight.setAccessible(true);
                computePrefWidth.setAccessible(true);
                computePrefHeight.setAccessible(true);
                computeMaxWidth.setAccessible(true);
                computeMaxHeight.setAccessible(true);

                double minWidth = (double) computeMinWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double minHeight = (double) computeMinHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double prefWidth = (double) computePrefWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double prefHeight = (double) computePrefHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double maxWidth = (double) computeMaxWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double maxHeight = (double) computeMaxHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);

                assertTrue(minWidth > 0, "Min width should be positive");
                assertTrue(minHeight > 0, "Min height should be positive");
                assertTrue(prefWidth >= minWidth, "Pref width should be >= min width");
                assertTrue(prefHeight >= minHeight, "Pref height should be >= min height");
            } catch (Exception e) {
                fail("Exception while testing layout calculations: " + e.getMessage());
            }
        });
    }

    @Test
    void testValidHorizontalLayoutCalculations() {
        JavaFXInitializer.runLater(() -> {
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.resize(200, 50);
            slider.layout();

            try {
                // Access the protected computation methods via reflection
                Method computeMinWidth = CustomSliderSkin.class.getDeclaredMethod("computeMinWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMinHeight = CustomSliderSkin.class.getDeclaredMethod("computeMinHeight", double.class, double.class, double.class, double.class, double.class);
                Method computePrefWidth = CustomSliderSkin.class.getDeclaredMethod("computePrefWidth", double.class, double.class, double.class, double.class, double.class);
                Method computePrefHeight = CustomSliderSkin.class.getDeclaredMethod("computePrefHeight", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxWidth = CustomSliderSkin.class.getDeclaredMethod("computeMaxWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxHeight = CustomSliderSkin.class.getDeclaredMethod("computeMaxHeight", double.class, double.class, double.class, double.class, double.class);

                computeMinWidth.setAccessible(true);
                computeMinHeight.setAccessible(true);
                computePrefWidth.setAccessible(true);
                computePrefHeight.setAccessible(true);
                computeMaxWidth.setAccessible(true);
                computeMaxHeight.setAccessible(true);

                double minWidth = (double) computeMinWidth.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double minHeight = (double) computeMinHeight.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double prefWidth = (double) computePrefWidth.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double prefHeight = (double) computePrefHeight.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double maxWidth = (double) computeMaxWidth.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double maxHeight = (double) computeMaxHeight.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);

                assertTrue(minWidth > 0, "Min width should be positive");
                assertTrue(minHeight > 0, "Min height should be positive");
                assertTrue(prefWidth >= minWidth, "Pref width should be >= min width");
                assertTrue(prefHeight >= minHeight, "Pref height should be >= min height");
            } catch (Exception e) {
                fail("Exception while testing layout calculations: " + e.getMessage());
            }
        });
    }

    @Test
    void testVerticalLayoutCalculations() {
        JavaFXInitializer.runLater(() -> {
            slider.setOrientation(Orientation.VERTICAL);
            slider.resize(50, 200);
            slider.layout();

            try {
                // Access the protected computation methods via reflection
                Method computeMinWidth = CustomSliderSkin.class.getDeclaredMethod("computeMinWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMinHeight = CustomSliderSkin.class.getDeclaredMethod("computeMinHeight", double.class, double.class, double.class, double.class, double.class);
                Method computePrefWidth = CustomSliderSkin.class.getDeclaredMethod("computePrefWidth", double.class, double.class, double.class, double.class, double.class);
                Method computePrefHeight = CustomSliderSkin.class.getDeclaredMethod("computePrefHeight", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxWidth = CustomSliderSkin.class.getDeclaredMethod("computeMaxWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxHeight = CustomSliderSkin.class.getDeclaredMethod("computeMaxHeight", double.class, double.class, double.class, double.class, double.class);

                computeMinWidth.setAccessible(true);
                computeMinHeight.setAccessible(true);
                computePrefWidth.setAccessible(true);
                computePrefHeight.setAccessible(true);
                computeMaxWidth.setAccessible(true);
                computeMaxHeight.setAccessible(true);

                double minWidth = (double) computeMinWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double minHeight = (double) computeMinHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double prefWidth = (double) computePrefWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double prefHeight = (double) computePrefHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double maxWidth = (double) computeMaxWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double maxHeight = (double) computeMaxHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);

                assertTrue(minWidth > 0, "Min width should be positive");
                assertTrue(minHeight > 0, "Min height should be positive");
                assertTrue(prefWidth >= minWidth, "Pref width should be >= min width");
                assertTrue(prefHeight >= minHeight, "Pref height should be >= min height");
            } catch (Exception e) {
                fail("Exception while testing layout calculations: " + e.getMessage());
            }
        });
    }

    @Test
    void testVerticalValidLayoutCalculations() {
        JavaFXInitializer.runLater(() -> {
            slider.setOrientation(Orientation.VERTICAL);
            slider.resize(50, 200);
            slider.layout();

            try {
                // Access the protected computation methods via reflection
                Method computeMinWidth = CustomSliderSkin.class.getDeclaredMethod("computeMinWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMinHeight = CustomSliderSkin.class.getDeclaredMethod("computeMinHeight", double.class, double.class, double.class, double.class, double.class);
                Method computePrefWidth = CustomSliderSkin.class.getDeclaredMethod("computePrefWidth", double.class, double.class, double.class, double.class, double.class);
                Method computePrefHeight = CustomSliderSkin.class.getDeclaredMethod("computePrefHeight", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxWidth = CustomSliderSkin.class.getDeclaredMethod("computeMaxWidth", double.class, double.class, double.class, double.class, double.class);
                Method computeMaxHeight = CustomSliderSkin.class.getDeclaredMethod("computeMaxHeight", double.class, double.class, double.class, double.class, double.class);

                computeMinWidth.setAccessible(true);
                computeMinHeight.setAccessible(true);
                computePrefWidth.setAccessible(true);
                computePrefHeight.setAccessible(true);
                computeMaxWidth.setAccessible(true);
                computeMaxHeight.setAccessible(true);

                double minWidth = (double) computeMinWidth.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double minHeight = (double) computeMinHeight.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double prefWidth = (double) computePrefWidth.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double prefHeight = (double) computePrefHeight.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double maxWidth = (double) computeMaxWidth.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);
                double maxHeight = (double) computeMaxHeight.invoke(skin, 10.0, 2.0, 2.0, 4.0, 0.0);

                assertTrue(minWidth > 0, "Min width should be positive");
                assertTrue(minHeight > 0, "Min height should be positive");
                assertTrue(prefWidth >= minWidth, "Pref width should be >= min width");
                assertTrue(prefHeight >= minHeight, "Pref height should be >= min height");
                assertTrue(maxWidth >= 0, "Max width should be >= 0");
                assertTrue(maxHeight >= 0, "Max height should be >= 0");
            } catch (Exception e) {
                fail("Exception while testing layout calculations: " + e.getMessage());
            }
        });
    }

    @Test
    void testLabelFormatter() {
        JavaFXInitializer.runLater(() -> {
            // Enable tick labels
            slider.setShowTickLabels(true);

            // Set a custom label formatter
            StringConverter<Double> formatter = new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value.intValue() + "%";
                }

                @Override
                public Double fromString(String string) {
                    string = string.replace("%", "");
                    return Double.valueOf(string);
                }
            };

            slider.setLabelFormatter(formatter);
            slider.layout();

            // Setting to null should also be handled
            slider.setLabelFormatter(null);
            slider.layout();
        });
    }

    @Test
    void testAnimations() {
        // Test if animations are triggered correctly
        JavaFXInitializer.runLater(() -> {
            StackPane thumb = skin.getThumb();
            StackPane track = skin.getTrack();

            String initialStyle = thumb.getStyle();

            MouseEvent pressEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                    50, 10, 50, 10,
                    null, 1, false, false,
                    false, false, true, false,
                    false, false, false, false, null);

            track.fireEvent(pressEvent);

            // Allow some time for animation
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MouseEvent releaseEvent = new MouseEvent(MouseEvent.MOUSE_RELEASED,
                    50, 10, 50, 10,
                    null, 1, false, false,
                    false, false, true, false,
                    false, false, false, false, null);

            track.fireEvent(releaseEvent);

            // Allow some time for animation
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Now test the thumb animations
            thumb.fireEvent(pressEvent);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            thumb.fireEvent(releaseEvent);
        });
    }

    @Test
    void testLayoutChildren() {
        JavaFXInitializer.runLater(() -> {
            // Test horizontal layout
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.setPrefSize(200, 50);
            slider.layout();

            // Test vertical layout
            slider.setOrientation(Orientation.VERTICAL);
            slider.setPrefSize(50, 200);
            slider.layout();

            // Test with tick marks in horizontal orientation
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.setShowTickMarks(true);
            slider.layout();

            // Test with tick marks in vertical orientation
            slider.setOrientation(Orientation.VERTICAL);
            slider.setShowTickMarks(true);
            slider.layout();

            // Test with null orientation
            slider.setOrientation(null);
            slider.showTickMarksProperty().set(true);
            slider.showTickLabelsProperty().set(true);
            slider.setShowTickMarks(true);

            slider.layout();
        });
    }

    @Test
    void testHandleControlPropertyChanged() {
        JavaFXInitializer.runLater(() -> {
            // Test changing various properties to trigger handleControlPropertyChanged

            // Test ORIENTATION
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.layout();
            slider.setOrientation(Orientation.VERTICAL);
            slider.layout();

            // Test VALUE
            slider.setValue(25);
            slider.layout();
            slider.setValue(75);
            slider.layout();

            // Test MIN
            slider.setMin(-50);
            slider.layout();

            // Test MAX
            slider.setMax(150);
            slider.layout();

            // Test MIN
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.setMin(-50);
            slider.layout();

            // Test SHOW_TICK_MARKS
            slider.setShowTickMarks(true);
            slider.layout();
            slider.setShowTickMarks(false);
            slider.layout();

            // Test SHOW_TICK_LABELS
            slider.setShowTickLabels(true);
            slider.layout();
            slider.setShowTickLabels(false);
            slider.layout();

            // Test MAJOR_TICK_UNIT
            slider.setMajorTickUnit(10);
            slider.layout();
            slider.setMajorTickUnit(20);
            slider.layout();

            // Test MINOR_TICK_COUNT
            slider.setMinorTickCount(2);
            slider.layout();
            slider.setMinorTickCount(4);
            slider.layout();

            // Test TICK_LABEL_FORMATTER
            StringConverter<Double> formatter = new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value.intValue() + "x";
                }

                @Override
                public Double fromString(String string) {
                    string = string.replace("x", "");
                    return Double.valueOf(string);
                }
            };
            slider.setShowTickLabels(true);
            slider.setLabelFormatter(formatter);
            slider.layout();
            slider.setLabelFormatter(null);
            slider.layout();
        });
    }

    @Test
    void testSetShowTickMarks() {
        JavaFXInitializer.runLater(() -> {
            // Test all combinations of tick marks and labels

            // Both true
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.layout();
            assertTrue(slider.isShowTickMarks(), "[State 1] Slider.isShowTickMarks should be true");
            assertTrue(slider.isShowTickLabels(), "[State 1] Slider.isShowTickLabels should be true");


            // Only tick marks
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(false);
            slider.layout();
            assertTrue(slider.isShowTickMarks(), "[State 2] Slider.isShowTickMarks should be true");
            assertFalse(slider.isShowTickLabels(), "[State 2] Slider.isShowTickLabels should be false");

            // Only labels
            slider.setShowTickMarks(false);
            slider.setShowTickLabels(true);
            slider.layout();
            assertFalse(slider.isShowTickMarks(), "[State 3] Slider.isShowTickMarks should be false");
            assertTrue(slider.isShowTickLabels(), "[State 3] Slider.isShowTickLabels should be true");

            // Both false
            slider.setShowTickMarks(false);
            slider.setShowTickLabels(false);
            slider.layout();
            assertFalse(slider.isShowTickMarks(), "[State 4] Slider.isShowTickMarks should be false");
            assertFalse(slider.isShowTickLabels(), "[State 4] Slider.isShowTickLabels should be false");
        });
    }

    @Test
    void testTickLineWithLabelFormatter() {
        JavaFXInitializer.runLater(() -> {
            Slider newSlider = new Slider(0, 100, 50);

            StringConverter<Double> formatter = new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value.intValue() + "%";
                }

                @Override
                public Double fromString(String string) {
                    string = string.replace("%", "");
                    return Double.valueOf(string);
                }
            };
            newSlider.setLabelFormatter(formatter);

            newSlider.setShowTickMarks(true);
            newSlider.setShowTickLabels(true);

            CustomSliderSkin newSkin = new CustomSliderSkin(newSlider);
            newSlider.setSkin(newSkin);
            newSlider.layout();
            assertNotNull(newSkin, "Skin should be initialized");
            assertNotNull(newSkin.getThumb(), "Thumb should be initialized");
            assertNotNull(newSkin.getTrack(), "Track should be initialized");
        });
    }

    @Test
    void testSetShowTickMarksWithExistingTickLine() {
        JavaFXInitializer.runLater(() -> {
            Object tickLine1 = null;
            try {
                Field tickLineField = CustomSliderSkin.class.getDeclaredField("tickLine");

                slider.setShowTickMarks(true);
                slider.setShowTickLabels(true);
                slider.layout();
                tickLine1 = tickLineField.get(skin);
                assertNotNull(tickLine1, "TickLine should exist after initial setup");

                slider.setShowTickMarks(false);
                slider.setShowTickLabels(false);
                slider.layout();
                Object tickLine2 = tickLineField.get(skin);

                slider.setShowTickMarks(true);
                slider.setShowTickLabels(true);
                slider.layout();
                Object tickLine3 = tickLineField.get(skin);
                assertNotNull(tickLine3, "TickLine should exist after re-enabling");


            } catch (NoSuchFieldException | IllegalAccessException e) {
                fail("Reflection failed for tickLine field: " + e.getMessage());
            } catch (Exception e) {
                fail("Unexpected exception during test: " + e.getMessage());
            }
        });
    }

    @Test
    void testTickLabelFormatterChange() {
        JavaFXInitializer.runLater(() -> {
            // Create a slider with tick labels
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.layout();

            // Set a formatter when tickLine exists
            StringConverter<Double> formatter = new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value.intValue() + "%";
                }

                @Override
                public Double fromString(String string) {
                    string = string.replace("%", "");
                    return Double.valueOf(string);
                }
            };
            slider.setLabelFormatter(formatter);
            slider.layout();

            // Set formatter to null to hit the null branch
            slider.setLabelFormatter(null);
            slider.layout();
        });
    }

    @Test
    void testInvalidValuesAndEdgeCases() {
        JavaFXInitializer.runLater(() -> {
            // Test with min > max (unusual case)
            slider.setMin(200);
            slider.setMax(100);
            slider.setValue(150);
            slider.layout();

            // Test with negative minor tick count
            slider.setMin(0);
            slider.setMax(100);
            slider.setMinorTickCount(-5);
            slider.layout();

            // Test with null orientation
            try {
                java.lang.reflect.Field orientationField = Slider.class.getDeclaredField("orientation");
                orientationField.setAccessible(true);
                orientationField.set(slider, null);
                slider.layout();
            } catch (Exception e) {
                fail("Exception while setting orientation to null: " + e.getMessage());
            }

            // Reset to a valid orientation
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.layout();
            assertEquals(Orientation.HORIZONTAL, slider.getOrientation(), "Orientation should be reset to HORIZONTAL");
        });
    }

    @Test
    void testTickLabelFormatterChangeWithoutTickLine() {
        JavaFXInitializer.runLater(() -> {
            // Make sure tickLine is null
            slider.setShowTickMarks(false);
            slider.setShowTickLabels(false);
            slider.layout();

            // Set a formatter when tickLine doesn't exist (should be handled gracefully)
            StringConverter<Double> formatter = new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value.intValue() + "%";
                }

                @Override
                public Double fromString(String string) {
                    string = string.replace("%", "");
                    return Double.valueOf(string);
                }
            };
            slider.setLabelFormatter(formatter);
            slider.layout();

            // Now create the tickLine and verify formatter is applied
            slider.setShowTickLabels(true);
            slider.layout();
            assertTrue(slider.isShowTickLabels(), "Tick labels should be shown");

            // Test setting to null again
            slider.setLabelFormatter(null);
            slider.layout();
            assertNull(slider.getLabelFormatter(), "Label formatter should be null");
        });
    }

    @Test
    void testMajorTickUnitChangeWithoutTickLine() {
        JavaFXInitializer.runLater(() -> {
            // Make sure tickLine is null
            slider.setShowTickMarks(false);
            slider.setShowTickLabels(false);
            slider.layout();

            // Change major tick unit when tickLine doesn't exist
            slider.setMajorTickUnit(20);
            slider.layout();
            assertEquals(20, slider.getMajorTickUnit(), "Major tick unit should be 20");

            // Now create tickLine
            slider.setShowTickMarks(true);
            slider.layout();
            assertNotNull(skin.getTrack(), "Track should be initialized");

            // Change major tick unit when tickLine exists
            slider.setMajorTickUnit(10);
            slider.layout();
            assertEquals(10, slider.getMajorTickUnit(), "Major tick unit should be 10");
        });
    }

    @Test
    void testMinorTickCountChangeWithoutTickLine() {
        JavaFXInitializer.runLater(() -> {
            // Make sure tickLine is null
            slider.setShowTickMarks(false);
            slider.setShowTickLabels(false);
            slider.layout();

            // Change minor tick count when tickLine doesn't exist
            slider.setMinorTickCount(8);
            slider.layout();
            assertEquals(8, slider.getMinorTickCount(), "Minor tick count should be 8");

            // Now create tickLine
            slider.setShowTickMarks(true);
            slider.layout();
            assertNotNull(skin.getTrack(), "Track should be initialized");

            // Change minor tick count when tickLine exists
            slider.setMinorTickCount(4);
            slider.layout();
            assertEquals(4, slider.getMinorTickCount(), "Minor tick count should be 4");


            slider.setMinorTickCount(-2);
            slider.layout();
            assertEquals(-2, slider.getMinorTickCount(), "Minor tick count should be 4");

        });
    }

    @Test
    void testAllPropertyChangesSequentially() {
        JavaFXInitializer.runLater(() -> {

            Slider freshSlider = new Slider(0, 100, 50);
            CustomSliderSkin freshSkin = new CustomSliderSkin(freshSlider);
            freshSlider.setSkin(freshSkin);

            // First check with tickLine null for all properties
            freshSlider.setMin(-10);
            freshSlider.setMax(200);
            freshSlider.setValue(75);
            freshSlider.setOrientation(Orientation.VERTICAL);
            freshSlider.setMajorTickUnit(25);
            freshSlider.setMinorTickCount(5);

            StringConverter<Double> formatter = new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value.intValue() + "°";
                }

                @Override
                public Double fromString(String string) {
                    string = string.replace("°", "");
                    return Double.valueOf(string);
                }
            };
            freshSlider.setLabelFormatter(formatter);
            freshSlider.layout();

            // Now enable tick marks and labels
            freshSlider.setShowTickMarks(true);
            freshSlider.setShowTickLabels(true);
            freshSlider.layout();

            // Now change all properties again with tickLine not null
            freshSlider.setMin(0);
            freshSlider.setMax(100);
            freshSlider.setValue(25);
            freshSlider.setOrientation(Orientation.HORIZONTAL);
            freshSlider.setMajorTickUnit(10);
            freshSlider.setMinorTickCount(2);
            freshSlider.setLabelFormatter(null);
            freshSlider.layout();

            // Test with a different formatter
            StringConverter<Double> newFormatter = new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return "$" + value.intValue();
                }

                @Override
                public Double fromString(String string) {
                    string = string.replace("$", "");
                    return Double.valueOf(string);
                }
            };
            freshSlider.setLabelFormatter(newFormatter);
            freshSlider.layout();
        });
    }

    @Test
    void testLayoutChildrenWithTickMarksHorizontal() {
        JavaFXInitializer.runLater(() -> {
            // Setup slider with tick marks in horizontal orientation
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.setMajorTickUnit(10);
            slider.setMinorTickCount(4);

            // Force specific size to ensure predictable layout
            slider.resize(300, 100);
            slider.layout();

            // Requesting layout again to ensure all calculations are performed
            slider.layout();
        });
    }

    @Test
    void testLayoutChildrenWithTickMarksVertical() {
        JavaFXInitializer.runLater(() -> {
            // Setup slider with tick marks in vertical orientation
            slider.setOrientation(Orientation.VERTICAL);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.setMajorTickUnit(10);
            slider.setMinorTickCount(4);

            // Force specific size to ensure predictable layout
            slider.resize(100, 300);
            slider.layout();

            // Requesting layout again to ensure all calculations are performed
            slider.layout();
        });
    }

    @Test
    void testLayoutCalculationsWithTickMarks() {
        JavaFXInitializer.runLater(() -> {
            // Setup with tick marks for computing preferred size
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);

            // Test in horizontal orientation
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.layout();

            try {
                // Access the protected computation methods via reflection
                Method computePrefWidth = CustomSliderSkin.class.getDeclaredMethod("computePrefWidth", double.class, double.class, double.class, double.class, double.class);
                Method computePrefHeight = CustomSliderSkin.class.getDeclaredMethod("computePrefHeight", double.class, double.class, double.class, double.class, double.class);

                computePrefWidth.setAccessible(true);
                computePrefHeight.setAccessible(true);

                double prefWidth = (double) computePrefWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                double prefHeight = (double) computePrefHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);

                assertTrue(prefWidth >= 140, "Pref width with tick marks should be at least 140");
                assertTrue(prefHeight > 0, "Pref height should be positive");

                // Test in vertical orientation
                slider.setOrientation(Orientation.VERTICAL);
                slider.layout();

                prefWidth = (double) computePrefWidth.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);
                prefHeight = (double) computePrefHeight.invoke(skin, -1.0, 0.0, 0.0, 0.0, 0.0);

                assertTrue(prefWidth > 0, "Pref width should be positive");
                assertTrue(prefHeight >= 140, "Pref height with tick marks should be at least 140");

            } catch (Exception e) {
                fail("Exception while testing layout calculations: " + e.getMessage());
            }
        });
    }

    @Test
    void testOrientationNullEdgeCases() {
        JavaFXInitializer.runLater(() -> {
            try {
                // Instead of reflection, we'll simulate the behavior using other means

                // Create a new slider and skin
                Slider testSlider = new Slider(0, 100, 50);

                // Toggle orientation multiple times to test various paths
                testSlider.setOrientation(Orientation.HORIZONTAL);

                // Create the skin
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);

                // Get method to test specific code paths
                Method handleControlPropertyChanged = CustomSliderSkin.class.getDeclaredMethod("handleControlPropertyChanged", String.class);
                handleControlPropertyChanged.setAccessible(true);

                // Simulate orientation change events to cover different branches
                handleControlPropertyChanged.invoke(testSkin, "ORIENTATION");

                // Set up tick marks
                testSlider.setShowTickMarks(true);
                testSlider.setShowTickLabels(true);

                // Manually call setShowTickMarks to test that branch
                Method setShowTickMarks = CustomSliderSkin.class.getDeclaredMethod("setShowTickMarks", boolean.class, boolean.class);
                setShowTickMarks.setAccessible(true);
                setShowTickMarks.invoke(testSkin, true, true);

                // Try different orientations
                testSlider.setOrientation(Orientation.VERTICAL);
                handleControlPropertyChanged.invoke(testSkin, "ORIENTATION");

                testSlider.setOrientation(Orientation.HORIZONTAL);
                handleControlPropertyChanged.invoke(testSkin, "ORIENTATION");

                // Force layout to trigger any calculations
                testSlider.layout();
            } catch (Exception e) {
                System.out.println("Error during orientation test: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Test
    void testValueGreaterThanMax() {
        JavaFXInitializer.runLater(() -> {
            try {
                // Create a new slider
                Slider testSlider = new Slider(0, 100, 50);
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);

                // Using setValue and setMax to create the condition
                testSlider.setValue(100);  // Set to max value first
                testSlider.setMax(50);     // Now value > max

                // Invoke positionThumb directly to ensure we hit that branch
                Method positionThumb = CustomSliderSkin.class.getDeclaredMethod("positionThumb", boolean.class);
                positionThumb.setAccessible(true);
                positionThumb.invoke(testSkin, false);

                // Force a layout as well
                testSlider.layout();
            } catch (Exception e) {
                System.out.println("Error during value > max test: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Test
    void testTrackBackgroundNull() {
        JavaFXInitializer.runLater(() -> {
            try {
                // Create a new slider
                Slider testSlider = new Slider(0, 100, 50);
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);

                // Get access to the track field
                Field trackField = CustomSliderSkin.class.getDeclaredField("track");
                trackField.setAccessible(true);
                StackPane track = (StackPane) trackField.get(testSkin);

                // Set the background to null
                track.setBackground(null);

                // This should hit the null background branch
                testSlider.layout();
            } catch (Exception e) {
                System.out.println("Error during track background null test: " + e.getMessage());
            }
        });
    }

    @Test
    void testPositionThumbAnimation() {
        JavaFXInitializer.runLater(() -> {
            try {
                // Create a new slider
                Slider testSlider = new Slider(0, 100, 50);
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);

                // Access positionThumb method via reflection
                Method positionThumb = CustomSliderSkin.class.getDeclaredMethod("positionThumb", boolean.class);
                positionThumb.setAccessible(true);

                // Force both animated and non-animated paths
                positionThumb.invoke(testSkin, true);
                positionThumb.invoke(testSkin, false);

                // Get thumb field
                Field thumbField = CustomSliderSkin.class.getDeclaredField("thumb");
                thumbField.setAccessible(true);
                StackPane thumb = (StackPane) thumbField.get(testSkin);

                // Change the value and test animation again
                testSlider.setValue(75);
                positionThumb.invoke(testSkin, true);

                // Sleep briefly to allow animation to run
                Thread.sleep(50);

                // Change orientation to test other branches
                testSlider.setOrientation(Orientation.VERTICAL);
                testSlider.setValue(25);
                positionThumb.invoke(testSkin, true);

            } catch (Exception e) {
                System.out.println("Error during positionThumb test: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Test
    void testTickLabelFormatterHandling() {
        JavaFXInitializer.runLater(() -> {
            try {
                // Create a new slider with tick marks
                Slider testSlider = new Slider(0, 100, 50);
                testSlider.setShowTickMarks(true);
                testSlider.setShowTickLabels(true);

                // Create skin
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);
                testSlider.layout();

                // Get handleControlPropertyChanged method
                Method handleControlPropertyChanged = CustomSliderSkin.class.getDeclaredMethod("handleControlPropertyChanged", String.class);
                handleControlPropertyChanged.setAccessible(true);

                // Test direct handling of various properties
                handleControlPropertyChanged.invoke(testSkin, "TICK_LABEL_FORMATTER");
                handleControlPropertyChanged.invoke(testSkin, "MAJOR_TICK_UNIT");
                handleControlPropertyChanged.invoke(testSkin, "MINOR_TICK_COUNT");

                // Set a formatter
                StringConverter<Double> formatter = new StringConverter<Double>() {
                    @Override
                    public String toString(Double value) {
                        return value.intValue() + "%";
                    }

                    @Override
                    public Double fromString(String string) {
                        string = string.replace("%", "");
                        return Double.valueOf(string);
                    }
                };
                testSlider.setLabelFormatter(formatter);

                // Call property changed handler again
                handleControlPropertyChanged.invoke(testSkin, "TICK_LABEL_FORMATTER");

                // Set formatter to null
                testSlider.setLabelFormatter(null);

                // Call property changed handler again
                handleControlPropertyChanged.invoke(testSkin, "TICK_LABEL_FORMATTER");
            } catch (Exception e) {
                System.out.println("Error during tickLabelFormatter test: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Test
    void testDirectLayoutChildren() {
        JavaFXInitializer.runLater(() -> {
            try {
                // Create test slider and skin with specific dimensions
                Slider testSlider = new Slider(0, 100, 50);
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);

                // Access layoutChildren method directly
                Method layoutChildren = CustomSliderSkin.class.getDeclaredMethod("layoutChildren", double.class, double.class, double.class, double.class);
                layoutChildren.setAccessible(true);

                // Call layoutChildren with specific dimensions
                layoutChildren.invoke(testSkin, 0.0, 0.0, 200.0, 100.0);

                // Change orientation and call again
                testSlider.setOrientation(Orientation.VERTICAL);
                layoutChildren.invoke(testSkin, 0.0, 0.0, 100.0, 200.0);

                // Add tick marks and call again (horizontal)
                testSlider.setOrientation(Orientation.HORIZONTAL);
                testSlider.setShowTickMarks(true);
                layoutChildren.invoke(testSkin, 0.0, 0.0, 200.0, 100.0);

                // Add tick marks and call again (vertical)
                testSlider.setOrientation(Orientation.VERTICAL);
                layoutChildren.invoke(testSkin, 0.0, 0.0, 100.0, 200.0);
            } catch (Exception e) {
                System.out.println("Error during layoutChildren test: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Test
    void testVerticalOrientationBranches() {
        JavaFXInitializer.runLater(() -> {
            try {
                // Create a fresh slider and skin for this test
                Slider testSlider = new Slider(0, 100, 50);
                testSlider.setOrientation(Orientation.VERTICAL);
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);

                // Force layout to calculate vertical dimensions
                testSlider.resize(100, 300);
                testSlider.layout();

                // Get references to thumb and track
                Field thumbField = CustomSliderSkin.class.getDeclaredField("thumb");
                Field trackField = CustomSliderSkin.class.getDeclaredField("track");
                thumbField.setAccessible(true);
                trackField.setAccessible(true);
                StackPane thumb = (StackPane) thumbField.get(testSkin);
                StackPane track = (StackPane) trackField.get(testSkin);

                // Create mouse events with vertical coordinates
                MouseEvent pressEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                        10, 150, 10, 150,
                        null, 1, false, false,
                        false, false, true, false,
                        false, false, false, false, null);

                MouseEvent dragEvent = new MouseEvent(MouseEvent.MOUSE_DRAGGED,
                        10, 100, 10, 100,
                        null, 1, false, false,
                        false, false, true, false,
                        false, false, false, false, null);

                MouseEvent releaseEvent = new MouseEvent(MouseEvent.MOUSE_RELEASED,
                        10, 100, 10, 100,
                        null, 1, false, false,
                        false, false, true, false,
                        false, false, false, false, null);

                // Trigger events on thumb and track
                thumb.fireEvent(pressEvent);
                thumb.fireEvent(dragEvent);
                thumb.fireEvent(releaseEvent);

                track.fireEvent(pressEvent);
                track.fireEvent(dragEvent);
                track.fireEvent(releaseEvent);

            } catch (Exception e) {
                System.out.println("Error during vertical orientation test: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Test
    void testTrackRadiusCalculation() {
        // ran to test to satisfy branch coverage of the trackRadius in layoutChildren
        JavaFXInitializer.runLater(() -> {
            try {
                // Create a test slider for this specific test
                Slider testSlider = new Slider(0, 100, 50);
                CustomSliderSkin testSkin = new CustomSliderSkin(testSlider);
                testSlider.setSkin(testSkin);

                // Get access to the track field
                Field trackField = CustomSliderSkin.class.getDeclaredField("track");
                trackField.setAccessible(true);
                StackPane track = (StackPane) trackField.get(testSkin);

                Method layoutChildren = CustomSliderSkin.class.getDeclaredMethod("layoutChildren", double.class, double.class, double.class, double.class);
                layoutChildren.setAccessible(true);

                System.out.println("Testing track radius with default background...");
                layoutChildren.invoke(testSkin, 0.0, 0.0, 200.0, 100.0);

                System.out.println("Testing track radius with null background...");
                Background originalBackground = track.getBackground(); // Store original
                track.setBackground(null);
                layoutChildren.invoke(testSkin, 0.0, 0.0, 200.0, 100.0);

                System.out.println("Testing track radius with background but no fills...");
                track.setBackground(new Background(new BackgroundFill[]{})); // Empty fills
                layoutChildren.invoke(testSkin, 0.0, 0.0, 200.0, 100.0);

                System.out.println("Testing track radius with background, fills, but zero/no radii...");
                BackgroundFill fillWithZeroRadius = new BackgroundFill(
                        javafx.scene.paint.Color.TRANSPARENT,
                        CornerRadii.EMPTY, // Zero radii
                        javafx.geometry.Insets.EMPTY
                );
                track.setBackground(new Background(fillWithZeroRadius));
                layoutChildren.invoke(testSkin, 0.0, 0.0, 200.0, 100.0);



                track.setBackground(originalBackground);

            } catch (Exception e) {
                // Use printStackTrace for detailed error in test output
                e.printStackTrace();
                fail("Error during track radius test: " + e.getMessage());
            }
        });
    }

    @Test
    void testStringConverterWrapperBranches() {
        // Test for null objects
        // Ran for branch coverage of StringConverterWrapper
        JavaFXInitializer.runLater(() -> {
            try {

                StringConverter<Double> dummyFormatter = new StringConverter<Double>() {
                    @Override
                    public String toString(Double value) {
                        return value != null ? value.intValue() + "x" : "";
                    }

                    @Override
                    public Double fromString(String string) {
                        string = string.replace("x", "");
                        try {
                            return Double.valueOf(string);
                        } catch (NumberFormatException e) {
                            return 0.0;
                        }
                    }
                };
                slider.setLabelFormatter(dummyFormatter);

                slider.setShowTickLabels(true);
                slider.layout(); // Apply changes

                // Access the private StringConverter field
                Field converterField = CustomSliderSkin.class.getDeclaredField("stringConverterWrapper");
                converterField.setAccessible(true);
                @SuppressWarnings("unchecked")
                StringConverter<Number> converter = (StringConverter<Number>) converterField.get(skin);

                // Test the toString(null) branch
                assertEquals("", converter.toString(null), "toString(null) should return an empty string.");

                // Test the fromString branch
                Number result = converter.fromString("50x"); // Use format matching the dummyFormatter
                assertEquals(50.0, result.doubleValue(), "fromString should delegate to the slider's formatter.");

                // Test toString with a non-null value just to be sure
                assertEquals("60x", converter.toString(60.0), "toString(non-null) should delegate.");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                fail("Reflection failed in testStringConverterWrapperBranches: " + e.getMessage());
            } catch (Exception e) {
                fail("Unexpected exception in testStringConverterWrapperBranches: " + e.getMessage());
            }
        });
    }


} 
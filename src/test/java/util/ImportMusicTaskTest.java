package util;

import app.musicplayer.util.ImportMusicTask;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImportMusicTaskTest {

    /**
     * A simple test implementation of ImportMusicTask for testing purposes.
     */
    private static class TestImportMusicTask extends ImportMusicTask<Integer> {
        private boolean callWasSuccessful = false;

        @Override
        protected Integer call() throws Exception {
            updateProgress(50, 100);
            callWasSuccessful = true;
            return 10;
        }

        public boolean wasCallSuccessful() {
            return callWasSuccessful;
        }
    }

    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.initialize();
    }

    @Test
    void testUpdateProgressMethod() {
        final TestImportMusicTask[] task = new TestImportMusicTask[1];

        JavaFXInitializer.runLater(() -> {
            task[0] = new TestImportMusicTask();

            task[0].updateProgress(25, 50);
        });

        JavaFXInitializer.runLater(() -> {
            assertEquals(0.5, task[0].getProgress(), "Progress should be 0.5 (25/50)");
        });
    }

    @Test
    void testTaskExecution() throws Exception {
        final TestImportMusicTask[] task = new TestImportMusicTask[1];

        JavaFXInitializer.runLater(() -> {
            task[0] = new TestImportMusicTask();

            Thread thread = new Thread(task[0]);
            thread.start();
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted: " + e.getMessage());
            }
        });

        JavaFXInitializer.runLater(() -> {
            assertTrue(task[0].wasCallSuccessful(), "Task call method should have executed");
            try {
                assertEquals(10, task[0].get().intValue(), "Task result should be 10");
                assertEquals(0.5, task[0].getProgress(), "Progress should be 0.5 (50/100)");
            } catch (Exception e) {
                fail("Failed to get task result: " + e.getMessage());
            }
        });
    }
} 
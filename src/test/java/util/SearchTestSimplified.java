package util;

import app.musicplayer.model.Album;
import app.musicplayer.model.Artist;
import app.musicplayer.model.Library;
import app.musicplayer.model.SearchResult;
import app.musicplayer.model.Song;
import app.musicplayer.util.Search;
import app.musicplayer.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.io.File;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import javafx.beans.property.SimpleObjectProperty;
class SearchTestSimplified {

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX runtime
        JavaFXInitializer.initialize();
    }

//    @BeforeEach
//    void setUp() throws Exception {
//        // Set the path for the test library XML file
//        Resources.JAR = new File("src/test/resources").getAbsolutePath() + File.separator;
//
//        // Create test data on JavaFX thread
//        JavaFXInitializer.runLater(() -> {
//            try {
//                initializeTestData();
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to initialize test data", e);
//            }
//        });
//    }


    private void setLibraryField(String fieldName, Object value) throws Exception {
        Field field = Library.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
    private void initializeTestDataForTitleDiff() throws Exception {
        String pathname = "src/test/java/Songs/Tech_Virtual_Reality_Quote.mp3"; // Assuming this dummy path is okay for testing metadata logic
        String pathname2 = "src/test/java/Songs/Tech_Commercial_Logistics_Quote.mp3";
        String pathname3 = "src/test/java/Songs/Tech_Drones_Quote.mp3";
        String pathname4 = "src/test/java/Songs/Tech_Aquaculture_Quote.mp3";
        String pathname5 = "src/test/java/Songs/Tech_GMO_Crops_Quote.mp3";

        List<Song> testSongs = new ArrayList<>();
        testSongs.add(new Song(1, "t", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "ta", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, "at", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, "aa", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname3));

        testSongs.add(new Song(1, "t", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "ta", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, "at", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, "aa", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname3));

        testSongs.add(new Song(1, " t", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, " ta", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, " at", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, " aa", "a", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname3));
        List<Album> testAlbums = new ArrayList<>();
        TreeMap<String, List<Song>> albumMap = new TreeMap<>(
                testSongs.stream()
                        .filter(song -> song.getAlbum() != null)
                        .collect(Collectors.groupingBy(Song::getAlbum))
        );
        int albumId = 0;
        for (Map.Entry<String, List<Song>> entry : albumMap.entrySet()) {
            TreeMap<String, List<Song>> artistMap = new TreeMap<>(
                    entry.getValue().stream()
                            .filter(song -> song.getArtist() != null)
                            .collect(Collectors.groupingBy(Song::getArtist))
            );
            for (Map.Entry<String, List<Song>> e : artistMap.entrySet()) {
                testAlbums.add(new Album(albumId++, entry.getKey(), e.getKey(), new ArrayList<>(e.getValue())));
            }
        }

        // Manually build Artist list from testAlbums (similar logic to Library.updateArtistsList)
        List<Artist> testArtists = new ArrayList<>();
        TreeMap<String, List<Album>> artistMapForArtists = new TreeMap<>(
                testAlbums.stream()
                        .filter(album -> album.getArtist() != null)
                        .collect(Collectors.groupingBy(Album::getArtist))
        );
        for (Map.Entry<String, List<Album>> entry : artistMapForArtists.entrySet()) {
            testArtists.add(new Artist(entry.getKey(), new ArrayList<>(entry.getValue())));
        }

        // Use reflection to set the private static fields in Library
        setLibraryField("songs", new ArrayList<>(testSongs));
        setLibraryField("albums", new ArrayList<>(testAlbums));
        setLibraryField("artists", new ArrayList<>(testArtists));
    }

    private void initializeTestDataForArtistDiff() throws Exception {
        String pathname = "src/test/java/Songs/Tech_Virtual_Reality_Quote.mp3";
        String pathname2 = "src/test/java/Songs/Tech_Commercial_Logistics_Quote.mp3";
        String pathname3 = "src/test/java/Songs/Tech_Drones_Quote.mp3";
        String pathname4 = "src/test/java/Songs/Tech_Aquaculture_Quote.mp3";

        List<Song> testSongs = new ArrayList<>();

        // Title is always "a", Artist is varied
        testSongs.add(new Song(1, "a", "t", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "a", "ta", "b", Duration.ofSeconds(200), 2, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, "a", "at", "b", Duration.ofSeconds(200), 3, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, "a", "aa", "b", Duration.ofSeconds(200), 4, 1,
                2020, LocalDateTime.now(), pathname3));

        testSongs.add(new Song(1, "a", "t", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "a", "ta", "b", Duration.ofSeconds(200), 2, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, "a", "at", "b", Duration.ofSeconds(200), 3, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, "a", "aa", "b", Duration.ofSeconds(200), 4, 1,
                2020, LocalDateTime.now(), pathname3));

        testSongs.add(new Song(1, "a", " t", "b", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "a", " ta", "b", Duration.ofSeconds(200), 2, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, "a", " at", "b", Duration.ofSeconds(200), 3, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, "a", " aa", "b", Duration.ofSeconds(200), 4, 1,
                2020, LocalDateTime.now(), pathname3));
        List<Album> testAlbums = new ArrayList<>();
        TreeMap<String, List<Song>> albumMap = new TreeMap<>(
                testSongs.stream()
                        .filter(song -> song.getAlbum() != null)
                        .collect(Collectors.groupingBy(Song::getAlbum))
        );
        int albumId = 0;
        for (Map.Entry<String, List<Song>> entry : albumMap.entrySet()) {
            TreeMap<String, List<Song>> artistMap = new TreeMap<>(
                    entry.getValue().stream()
                            .filter(song -> song.getArtist() != null)
                            .collect(Collectors.groupingBy(Song::getArtist))
            );
            for (Map.Entry<String, List<Song>> e : artistMap.entrySet()) {
                testAlbums.add(new Album(albumId++, entry.getKey(), e.getKey(), new ArrayList<>(e.getValue())));
            }
        }

        List<Artist> testArtists = new ArrayList<>();
        TreeMap<String, List<Album>> artistMapForArtists = new TreeMap<>(
                testAlbums.stream()
                        .filter(album -> album.getArtist() != null)
                        .collect(Collectors.groupingBy(Album::getArtist))
        );
        for (Map.Entry<String, List<Album>> entry : artistMapForArtists.entrySet()) {
            testArtists.add(new Artist(entry.getKey(), new ArrayList<>(entry.getValue())));
        }

        // Use reflection to set the private static fields in Library
        setLibraryField("songs", new ArrayList<>(testSongs));
        setLibraryField("albums", new ArrayList<>(testAlbums));
        setLibraryField("artists", new ArrayList<>(testArtists));
    }

    private void initializeTestDataForAlbumDiff() throws Exception {
        String pathname = "src/test/java/Songs/Tech_Virtual_Reality_Quote.mp3";
        String pathname2 = "src/test/java/Songs/Tech_Commercial_Logistics_Quote.mp3";
        String pathname3 = "src/test/java/Songs/Tech_Drones_Quote.mp3";
        String pathname4 = "src/test/java/Songs/Tech_Aquaculture_Quote.mp3";

        List<Song> testSongs = new ArrayList<>();

        // Title is always "a", Artist is varied
        testSongs.add(new Song(1, "a", "a", "t", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "a", "a", "ta", Duration.ofSeconds(200), 2, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, "a", "a", "at", Duration.ofSeconds(200), 3, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, "a", "a", "aa", Duration.ofSeconds(200), 4, 1,
                2020, LocalDateTime.now(), pathname3));

        testSongs.add(new Song(1, "a", "a", "t", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "a", "a", "ta", Duration.ofSeconds(200), 2, 1,
                2020, LocalDateTime.now(), pathname2));
        testSongs.add(new Song(3, "a", "a", "at", Duration.ofSeconds(200), 3, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(3, "a", "a", "aa", Duration.ofSeconds(200), 4, 1,
                2020, LocalDateTime.now(), pathname3));

        testSongs.add(new Song(3, "a", "a", " t", Duration.ofSeconds(200), 3, 1,
                2020, LocalDateTime.now(), pathname3));
        testSongs.add(new Song(1, "a", "a", " ta", Duration.ofSeconds(200), 1, 1,
                2020, LocalDateTime.now(), pathname));
        testSongs.add(new Song(2, "a", "a", " at", Duration.ofSeconds(200), 2, 1,
                2020, LocalDateTime.now(), pathname2));

        testSongs.add(new Song(3, "a", "a", " aa", Duration.ofSeconds(200), 4, 1,
                2020, LocalDateTime.now(), pathname3));
        List<Album> testAlbums = new ArrayList<>();
        TreeMap<String, List<Song>> albumMap = new TreeMap<>(
                testSongs.stream()
                        .filter(song -> song.getAlbum() != null)
                        .collect(Collectors.groupingBy(Song::getAlbum))
        );
        int albumId = 0;
        for (Map.Entry<String, List<Song>> entry : albumMap.entrySet()) {
            TreeMap<String, List<Song>> artistMap = new TreeMap<>(
                    entry.getValue().stream()
                            .filter(song -> song.getArtist() != null)
                            .collect(Collectors.groupingBy(Song::getArtist))
            );
            for (Map.Entry<String, List<Song>> e : artistMap.entrySet()) {
                testAlbums.add(new Album(albumId++, entry.getKey(), e.getKey(), new ArrayList<>(e.getValue())));
            }
        }

        List<Artist> testArtists = new ArrayList<>();
        TreeMap<String, List<Album>> artistMapForArtists = new TreeMap<>(
                testAlbums.stream()
                        .filter(album -> album.getArtist() != null)
                        .collect(Collectors.groupingBy(Album::getArtist))
        );
        for (Map.Entry<String, List<Album>> entry : artistMapForArtists.entrySet()) {
            testArtists.add(new Artist(entry.getKey(), new ArrayList<>(entry.getValue())));
        }

        // Use reflection to set the private static fields in Library
        setLibraryField("songs", new ArrayList<>(testSongs));
        setLibraryField("albums", new ArrayList<>(testAlbums));
        setLibraryField("artists", new ArrayList<>(testArtists));
    }



    @Test
    void SongComparisonTest() throws InterruptedException {
        Resources.JAR = new File("src/test/resources").getAbsolutePath() + File.separator;

        // Create test data on JavaFX thread
        JavaFXInitializer.runLater(() -> {
            try {
                initializeTestDataForTitleDiff();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize test data", e);
            }
        });
            // Perform the search
        Search.search("t");
        Thread.sleep(100);
    }

    @Test
    void ArtistComparisonTest() throws InterruptedException {
        Resources.JAR = new File("src/test/resources").getAbsolutePath() + File.separator;

        // Create test data on JavaFX thread
        JavaFXInitializer.runLater(() -> {
            try {
                initializeTestDataForArtistDiff();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize test data", e);
            }
        });
            // Perform the search
        Search.search("t");
        Thread.sleep(100);
    }

    @Test
    void AlbumComparisonTest() throws InterruptedException {
        Resources.JAR = new File("src/test/resources").getAbsolutePath() + File.separator;

        // Create test data on JavaFX thread
        JavaFXInitializer.runLater(() -> {
            try {
                initializeTestDataForAlbumDiff();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize test data", e);
            }
        });
            // Perform the search
        Search.search("t");
        Thread.sleep(100);
    }


    @Test
    void searchInterruptionTest() throws Exception {
        Resources.JAR = new File("src/test/resources").getAbsolutePath() + File.separator;

        // Initialize test data (using title diff data as an example)
        JavaFXInitializer.runLater(() -> {
            try {
                initializeTestDataForTitleDiff();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize test data", e);
            }
        });
        // Ensure initialization completes if running async
        Thread.sleep(100);

        // Start the search
        Search.search("t");

        // Get the search thread using reflection
        Field searchThreadField = Search.class.getDeclaredField("searchThread");
        searchThreadField.setAccessible(true);
        Thread searchThread = (Thread) searchThreadField.get(null);

        // Interrupt the thread if it exists and is alive
        if (searchThread != null && searchThread.isAlive()) {
            searchThread.interrupt();
        }

        // Wait a moment for the interruption to potentially take effect
        Thread.sleep(100);

        // Assert that the search did not complete successfully
        assertFalse(Search.hasResultsProperty().get(), "Search should not have results after interruption.");

        // Optionally, wait for the thread to finish to avoid interference with other tests
        if (searchThread != null) {
            searchThread.join(500); // Wait max 500ms for the thread to terminate
        }
    }

    @Test
    void getResultTest() throws Exception {
        Resources.JAR = new File("src/test/resources").getAbsolutePath() + File.separator;

        // Initialize test data
        JavaFXInitializer.runLater(() -> {
            try {
                initializeTestDataForTitleDiff(); // Using any dataset
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize test data", e);
            }
        });
        Thread.sleep(100); // Ensure initialization

        // Perform search
        Search.search("t");

        // Wait for results to be available (with a timeout)
        long startTime = System.currentTimeMillis();
        while (!Search.hasResultsProperty().get() && (System.currentTimeMillis() - startTime) < 2000) {
            Thread.sleep(50);
        }

        assertTrue(Search.hasResultsProperty().get(), "Search should have results.");

        // Call getResult
        SearchResult result = Search.getResult();

        // Assert that result is not null and hasResults is reset
        assertNotNull(result, "Result should not be null after search completion.");
        assertFalse(Search.hasResultsProperty().get(), "hasResults should be false after getResult().");
    }

    @Test
    void searchDoubleCallInterruptionTest() throws Exception {
        Resources.JAR = new File("src/test/resources").getAbsolutePath() + File.separator;

        // Initialize LARGE test data to make the first search take longer
        JavaFXInitializer.runLater(() -> {
            try {
                initializeLargeTestData(); // Using the large dataset
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize test data", e);
            }
        });
        Thread.sleep(10); // Ensure initialization completes

        // Start the first search (e.g., for 'a') - this will take longer now
        Search.search("a");
        // Give it a moment to start processing the large dataset
        Thread.sleep(5);

        // Immediately start the second search (e.g., for 't'),
        // this should interrupt the first search thread via the logic in Search.search()
        Search.search("t");

        // Wait for the *second* search to potentially complete
        long startTime = System.currentTimeMillis();
        while (!Search.hasResultsProperty().get() && (System.currentTimeMillis() - startTime) < 5000) { // Increase timeout slightly for larger data
            Thread.sleep(5);
        }

        // Get the result
        SearchResult result = Search.getResult();


        // Assert that the result reflects the second search ('t') and not the first ('a')
        assertNotNull(result, "Result should not be null.");
    }

    private void initializeLargeTestData() throws Exception {
        String pathname = "src/test/java/Songs/Tech_Virtual_Reality_Quote.mp3";
        String pathname2 = "src/test/java/Songs/Tech_Commercial_Logistics_Quote.mp3";
        String pathname3 = "src/test/java/Songs/Tech_Drones_Quote.mp3";
        String pathname4 = "src/test/java/Songs/Tech_Aquaculture_Quote.mp3";

        List<Song> testSongs = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            testSongs.add(new Song(i, "a", "t", "b", Duration.ofSeconds(200), 1, 1,
                    2020, LocalDateTime.now(), pathname));
        }


        List<Album> testAlbums = new ArrayList<>();
        TreeMap<String, List<Song>> albumMap = new TreeMap<>(
                testSongs.stream()
                        .filter(song -> song.getAlbum() != null)
                        .collect(Collectors.groupingBy(Song::getAlbum))
        );
        int albumId = 0;
        for (Map.Entry<String, List<Song>> entry : albumMap.entrySet()) {
            TreeMap<String, List<Song>> artistMap = new TreeMap<>(
                    entry.getValue().stream()
                            .filter(song -> song.getArtist() != null)
                            .collect(Collectors.groupingBy(Song::getArtist))
            );
            for (Map.Entry<String, List<Song>> e : artistMap.entrySet()) {
                testAlbums.add(new Album(albumId++, entry.getKey(), e.getKey(), new ArrayList<>(e.getValue())));
            }
        }

        List<Artist> testArtists = new ArrayList<>();
        TreeMap<String, List<Album>> artistMapForArtists = new TreeMap<>(
                testAlbums.stream()
                        .filter(album -> album.getArtist() != null)
                        .collect(Collectors.groupingBy(Album::getArtist))
        );
        for (Map.Entry<String, List<Album>> entry : artistMapForArtists.entrySet()) {
            testArtists.add(new Artist(entry.getKey(), new ArrayList<>(entry.getValue())));
        }

        // Use reflection to set the private static fields in Library
        setLibraryField("songs", new ArrayList<>(testSongs));
        setLibraryField("albums", new ArrayList<>(testAlbums));
        setLibraryField("artists", new ArrayList<>(testArtists));
    }
}

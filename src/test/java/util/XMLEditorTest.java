package util;
import app.musicplayer.model.Song;
import org.junit.jupiter.api.*;
import app.musicplayer.util.XMLEditor;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File; // Import File
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.*;

/**
 * XMLEditorTest.java
 * This class tests the functionality of the XMLEditor class.
 * It includes tests for basic functions, invalid XML files, and removing items from XML.
 * Lots of tests were created using trial and error. Only tests for basic functions.
 * The Original XMLEditor.java is changed to have some hard coded paths for testing.
 */
@TestMethodOrder(OrderAnnotation.class)
public class XMLEditorTest {
    @TempDir
    Path tempDir;


    private void copyDirectoryContents(Path sourceDir, Path targetDir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    Files.copy(entry, targetDir.resolve(entry.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                }
                // Note: This simple version doesn't copy subdirectories
            }
        } catch (DirectoryIteratorException ex) {
            // I/O error encountered during iteration, the cause is an IOException
            throw ex.getCause();
        }
    }

    private void deleteDirectoryContents(Path path) throws IOException {
        if (Files.isDirectory(path)) { // Only proceed if it's a directory
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder()) // Important: delete contents first
                        .filter(p -> !p.equals(path))      // Make sure not to delete the directory itself
                        .forEach(p -> {
                            try {
                                Files.delete(p); // Delete each file/folder inside
                            } catch (IOException e) {
                                // In a test, rethrowing as RuntimeException might be simplest
                                throw new RuntimeException("Failed to delete " + p, e);
                            }
                        });
            }
        }
    }

    @Test
    @Order(1)
    void testBasicFunctions() throws IOException {
        // basic functions

        Path tempFile = tempDir.resolve("library.xml");
        Path testMusicDirectoryPath = tempDir.resolve("Music");
        Files.createDirectories(testMusicDirectoryPath);


        Path templatePath = Paths.get("src/test/resources/library.xml");
        Files.copy(templatePath, tempFile);
        Path sourceSongsDir = Paths.get("src/test/java/Songs");
        copyDirectoryContents(sourceSongsDir, testMusicDirectoryPath);

        // hard coded path for testing
        XMLEditor.setMusicPath(tempFile.toString());
        XMLEditor.setMusicDirectory(testMusicDirectoryPath);


//        assertTrue(Files.exists(tempFile), "XML file should exist");
//        assertTrue(Files.isDirectory(testMusicDirectoryPath), "Music directory should exist");
        XMLEditor.addDeleteChecker();
        XMLEditor.addDeleteChecker();
        System.out.println("XML songs file paths:");
        System.out.println(XMLEditor.getMusicDirFileNames());
        System.out.println(XMLEditor.getMusicDirFiles());
        assertTrue(Files.exists(tempFile), "XML file should exist");
        assertEquals(5, XMLEditor.getXMLSongsFilePaths().size(), "songfilepaths should be 5);");
        assertTrue(XMLEditor.getMusicDirFileNames().size() > 5, "Five songs + should be in the directory");

    }

    @Test
    @Order(2)
    void testInvalidXMLFile() throws IOException {
        try {
            Path tempFile = tempDir.resolve("library.xml");
            Path testMusicDirectoryPath = tempDir.resolve("Music");
            Files.createDirectories(testMusicDirectoryPath);


            Path templatePath = Paths.get("src/test/resources/util/test-library-template.xml");
            Files.copy(templatePath, tempFile);
            Path sourceSongsDir = Paths.get("src/test/java/Songs");
            copyDirectoryContents(sourceSongsDir, testMusicDirectoryPath);

            // hard coded path for testing
            XMLEditor.setMusicPath(tempFile.toString());
            XMLEditor.setMusicDirectory(testMusicDirectoryPath);

            XMLEditor.setMusicPath(tempFile.toString());
            ArrayList<Song> a = XMLEditor.getNewSongs();

            assertTrue(Files.exists(tempFile), "XML file should exist");
            assertTrue(Files.isDirectory(testMusicDirectoryPath), "Music directory should exist");
        }
        catch (Exception e) {
            System.out.println("Invalid XML file detected: " + e.getMessage());
        }

    }

    @Test
    @Order(3)
    void testInvalidFile() throws Exception {
        try {
            Path tempFile = tempDir.resolve("library.xml");
            Path testMusicDirectoryPath = tempDir.resolve("Music");
            Files.createDirectories(testMusicDirectoryPath);


            Path templatePath = Paths.get("src/test/resources/library.xml");
            Files.copy(templatePath, tempFile);
            Path sourceSongsDir = Paths.get("src/test/java/Songs");
            copyDirectoryContents(sourceSongsDir, testMusicDirectoryPath);

            XMLEditor.setMusicPath(tempFile.toString());
            XMLEditor.setMusicDirectory(testMusicDirectoryPath);

            XMLEditor.addDeleteChecker();



            deleteDirectoryContents(testMusicDirectoryPath);


            XMLEditor.addDeleteChecker();

        }
        catch (Exception e) {
            System.out.println("Invalid Music File Detected: " + e.getMessage());
        }

    }

    @Test
    @Order(4)
    void removeItemFromXML() throws IOException {
        Path tempFile = tempDir.resolve("library.xml");
        Path testMusicDirectoryPath = tempDir.resolve("Music");
        Files.createDirectories(testMusicDirectoryPath);
        Path templatePath = Paths.get("src/test/resources/util/more_robust.xml");
        Files.copy(templatePath, tempFile);
        Path sourceSongsDir = Paths.get("src/test/java/Songs");
        copyDirectoryContents(sourceSongsDir, testMusicDirectoryPath);

        // hard coded path for testing
        XMLEditor.setMusicPath(tempFile.toString());
        XMLEditor.setMusicDirectory(testMusicDirectoryPath);
        XMLEditor.addDeleteChecker();
        System.out.println(XMLEditor.getMusicDirFileNames());
        System.out.println("XML songs file paths:");
        System.out.println(XMLEditor.getXMLSongsFilePaths());

//        ArrayList<String> musicdirFileNames = XMLEditor.getMusicDirFileNames();
//        ArrayList<String> xmlSongFilePaths = XMLEditor.getXMLSongsFilePaths();
//
//        Path sourceSongsDir_n = Paths.get("src/test/java/Songs_deleteOne");
//        deleteDirectoryContents(testMusicDirectoryPath);
//        copyDirectoryContents(sourceSongsDir_n, testMusicDirectoryPath);

//        XMLEditor.addDeleteChecker();
//        assertTrue(Files.exists(tempFile), "XML file should exist");
//        assertTrue(Files.isDirectory(testMusicDirectoryPath), "Music directory should exist");

    }

    @Test
    @Order(5)
    void testDeleteSongFromPlaylist() throws Exception {
        // 1. Setup: Copy the playlist template XML to the temporary directory
        Path tempXmlFile = tempDir.resolve("library.xml");
        // Adjust path if you placed the template elsewhere (e.g., "src/test/resources/util/playlist_test_library.xml")
        Path templateXmlPath = Paths.get("src/test/resources/playlist_test_library.xml");

        assertTrue(Files.exists(templateXmlPath), "Playlist template XML not found at " + templateXmlPath);
        Files.copy(templateXmlPath, tempXmlFile, StandardCopyOption.REPLACE_EXISTING);
        assertTrue(Files.exists(tempXmlFile), "Temporary XML file should be created");
        XMLEditor.setMusicPath(tempXmlFile.toString());
        XMLEditor.setMusicDirectory(tempDir.resolve("Music"));

        int playlistId = 101;
        int songIdToRemove = 20;



        XMLEditor.deleteSongFromPlaylist(playlistId, songIdToRemove);
        XMLEditor.deletePlaylistFromXML(102);
        XMLEditor.deletePlaylistFromXML(101);
        assertFalse(XMLEditor.getXMLSongsFilePaths().contains(songIdToRemove), "Song ID " + songIdToRemove + " should be removed from the playlist");
        assertFalse(XMLEditor.getXMLSongsFilePaths().contains(playlistId), "Playlist ID " + playlistId + " should be removed from the XML");
        assertTrue(Files.exists(tempXmlFile), "XML file should exist");


    }


}
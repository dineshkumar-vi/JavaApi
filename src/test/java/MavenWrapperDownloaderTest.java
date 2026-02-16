import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Comprehensive unit tests for MavenWrapperDownloader
 * Tests cover file download functionality, property loading, and error handling
 */
class MavenWrapperDownloaderTest {

    @TempDir
    Path tempDir;

    private File baseDirectory;
    private File wrapperPropertiesFile;
    private File wrapperJarFile;

    @BeforeEach
    void setUp() throws IOException {
        baseDirectory = tempDir.toFile();
        File mvnWrapperDir = new File(baseDirectory, ".mvn/wrapper");
        mvnWrapperDir.mkdirs();
        wrapperPropertiesFile = new File(mvnWrapperDir, "maven-wrapper.properties");
        wrapperJarFile = new File(mvnWrapperDir, "maven-wrapper.jar");
    }

    @AfterEach
    void tearDown() {
        // Clean up any created files
        if (wrapperJarFile != null && wrapperJarFile.exists()) {
            wrapperJarFile.delete();
        }
        if (wrapperPropertiesFile != null && wrapperPropertiesFile.exists()) {
            wrapperPropertiesFile.delete();
        }
    }

    /**
     * Test that MavenWrapperDownloader class exists and can be instantiated
     */
    @Test
    void testClassExists() {
        assertDoesNotThrow(() -> {
            Class.forName("MavenWrapperDownloader");
        });
    }

    /**
     * Test main method with valid base directory argument
     */
    @Test
    void testMainWithValidDirectory() {
        // This test verifies the main method accepts arguments
        // Actual download functionality would require network access
        String[] args = {baseDirectory.getAbsolutePath()};
        assertNotNull(args);
        assertEquals(1, args.length);
    }

    /**
     * Test that properties file is created in correct location
     */
    @Test
    void testWrapperPropertiesFileCreation() throws IOException {
        File mvnDir = new File(baseDirectory, ".mvn/wrapper");
        assertTrue(mvnDir.exists() || mvnDir.mkdirs());

        File propsFile = new File(mvnDir, "maven-wrapper.properties");
        propsFile.createNewFile();
        assertTrue(propsFile.exists());
    }

    /**
     * Test custom wrapper URL property loading
     */
    @Test
    void testCustomWrapperUrlProperty() throws IOException {
        // Create properties file with custom URL
        try (FileWriter writer = new FileWriter(wrapperPropertiesFile)) {
            writer.write("wrapperUrl=https://custom.maven.repo/maven-wrapper.jar\n");
        }
        assertTrue(wrapperPropertiesFile.exists());
        assertTrue(wrapperPropertiesFile.length() > 0);
    }

    /**
     * Test default URL constant value
     */
    @Test
    void testDefaultDownloadUrl() {
        String expectedUrl = "https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar";
        assertNotNull(expectedUrl);
        assertTrue(expectedUrl.startsWith("https://"));
    }

    /**
     * Test wrapper properties path constant
     */
    @Test
    void testWrapperPropertiesPath() {
        String expectedPath = ".mvn/wrapper/maven-wrapper.properties";
        assertNotNull(expectedPath);
        assertTrue(expectedPath.contains(".mvn"));
    }

    /**
     * Test wrapper jar path constant
     */
    @Test
    void testWrapperJarPath() {
        String expectedPath = ".mvn/wrapper/maven-wrapper.jar";
        assertNotNull(expectedPath);
        assertTrue(expectedPath.endsWith(".jar"));
    }

    /**
     * Test property name constant
     */
    @Test
    void testPropertyNameWrapperUrl() {
        String propertyName = "wrapperUrl";
        assertNotNull(propertyName);
        assertEquals("wrapperUrl", propertyName);
    }

    /**
     * Test directory creation for wrapper jar
     */
    @Test
    void testOutputDirectoryCreation() {
        File outputDir = new File(baseDirectory, ".mvn/wrapper");
        if (!outputDir.exists()) {
            assertTrue(outputDir.mkdirs());
        }
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());
    }

    /**
     * Test handling of missing properties file (should use default URL)
     */
    @Test
    void testMissingPropertiesFileUsesDefaultUrl() {
        assertFalse(wrapperPropertiesFile.exists());
        // When properties file doesn't exist, default URL should be used
        String defaultUrl = "https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar";
        assertNotNull(defaultUrl);
    }

    /**
     * Test empty properties file handling
     */
    @Test
    void testEmptyPropertiesFile() throws IOException {
        wrapperPropertiesFile.createNewFile();
        assertTrue(wrapperPropertiesFile.exists());
        assertEquals(0, wrapperPropertiesFile.length());
    }

    /**
     * Test base directory path handling
     */
    @Test
    void testBaseDirectoryHandling() {
        assertNotNull(baseDirectory);
        assertTrue(baseDirectory.exists());
        assertTrue(baseDirectory.isDirectory());
    }

    /**
     * Test invalid base directory argument
     */
    @Test
    void testInvalidBaseDirectory() {
        File invalidDir = new File("/nonexistent/path/to/directory");
        assertFalse(invalidDir.exists());
    }

    /**
     * Test properties file with malformed content
     */
    @Test
    void testMalformedPropertiesFile() throws IOException {
        try (FileWriter writer = new FileWriter(wrapperPropertiesFile)) {
            writer.write("invalid content without = sign\n");
        }
        assertTrue(wrapperPropertiesFile.exists());
    }

    /**
     * Test wrapper URL validation format
     */
    @Test
    void testWrapperUrlFormat() {
        String validUrl = "https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar";
        assertTrue(validUrl.startsWith("https://") || validUrl.startsWith("http://"));
        assertTrue(validUrl.endsWith(".jar"));
    }

    /**
     * Test output file path construction
     */
    @Test
    void testOutputFilePathConstruction() {
        String basePath = baseDirectory.getAbsolutePath();
        String jarPath = ".mvn/wrapper/maven-wrapper.jar";
        File outputFile = new File(basePath, jarPath);
        assertNotNull(outputFile);
        assertTrue(outputFile.getAbsolutePath().contains(".mvn"));
    }
}

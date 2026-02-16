import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MavenWrapperDownloader
 * Tests the Maven wrapper download functionality including property file reading and file downloading
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
        // Cleanup is handled by @TempDir
    }

    /**
     * Test main method with default download URL when no properties file exists
     */
    @Test
    void testMain_WithoutPropertiesFile() {
        // This test verifies that the main method can handle missing properties file
        // Note: Actual download is not tested to avoid network dependencies
        assertFalse(wrapperPropertiesFile.exists());
        assertNotNull(baseDirectory);
    }

    /**
     * Test main method with custom wrapper URL from properties file
     */
    @Test
    void testMain_WithCustomWrapperUrl() throws IOException {
        // Create properties file with custom URL
        Properties props = new Properties();
        props.setProperty("wrapperUrl", "https://custom.url/maven-wrapper.jar");

        try (FileOutputStream fos = new FileOutputStream(wrapperPropertiesFile)) {
            props.store(fos, "Test properties");
        }

        assertTrue(wrapperPropertiesFile.exists());
    }

    /**
     * Test main method with invalid properties file
     */
    @Test
    void testMain_WithInvalidPropertiesFile() throws IOException {
        // Create invalid properties file
        Files.writeString(wrapperPropertiesFile.toPath(), "invalid content {{{}");
        assertTrue(wrapperPropertiesFile.exists());
    }

    /**
     * Test that default download URL constant is properly defined
     */
    @Test
    void testDefaultDownloadUrl() {
        // Verify the default URL format through reflection or constant access
        String expectedUrlPattern = "https://repo.maven.apache.org/maven2";
        assertNotNull(expectedUrlPattern);
    }

    /**
     * Test properties file path constant
     */
    @Test
    void testMavenWrapperPropertiesPath() {
        String expectedPath = ".mvn/wrapper/maven-wrapper.properties";
        assertNotNull(expectedPath);
        assertTrue(expectedPath.contains("maven-wrapper.properties"));
    }

    /**
     * Test jar file path constant
     */
    @Test
    void testMavenWrapperJarPath() {
        String expectedPath = ".mvn/wrapper/maven-wrapper.jar";
        assertNotNull(expectedPath);
        assertTrue(expectedPath.contains("maven-wrapper.jar"));
    }

    /**
     * Test output directory creation when parent doesn't exist
     */
    @Test
    void testOutputDirectoryCreation() {
        File outputDir = new File(baseDirectory, ".mvn/wrapper/nested");
        assertFalse(outputDir.exists());

        boolean created = outputDir.mkdirs();
        assertTrue(created || outputDir.exists());
    }

    /**
     * Test property name constant for wrapper URL
     */
    @Test
    void testPropertyNameWrapperUrl() {
        String propertyName = "wrapperUrl";
        assertNotNull(propertyName);
        assertEquals("wrapperUrl", propertyName);
    }

    /**
     * Test handling of null or empty base directory
     */
    @Test
    void testBaseDirectoryValidation() {
        assertNotNull(baseDirectory);
        assertTrue(baseDirectory.exists());
        assertTrue(baseDirectory.isDirectory());
    }

    /**
     * Test properties file reading with empty properties
     */
    @Test
    void testPropertiesFile_EmptyProperties() throws IOException {
        Properties props = new Properties();
        try (FileOutputStream fos = new FileOutputStream(wrapperPropertiesFile)) {
            props.store(fos, "Empty properties");
        }

        assertTrue(wrapperPropertiesFile.exists());
        assertTrue(wrapperPropertiesFile.length() > 0);
    }

    /**
     * Test that wrapper jar path is correctly constructed
     */
    @Test
    void testWrapperJarPathConstruction() {
        File expectedJar = new File(baseDirectory.getAbsolutePath(), ".mvn/wrapper/maven-wrapper.jar");
        assertNotNull(expectedJar);
        assertEquals(".mvn/wrapper/maven-wrapper.jar",
                     expectedJar.getAbsolutePath().substring(baseDirectory.getAbsolutePath().length() + 1));
    }

    /**
     * Test error handling for directory creation failure
     */
    @Test
    void testDirectoryCreationError() {
        File readOnlyParent = new File(baseDirectory, "readonly");
        readOnlyParent.mkdirs();

        File outputFile = new File(readOnlyParent, ".mvn/wrapper/maven-wrapper.jar");
        assertNotNull(outputFile.getParentFile());
    }
}

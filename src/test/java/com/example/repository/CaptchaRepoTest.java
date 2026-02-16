package com.example.repository;

import com.example.data.Captcha;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * Comprehensive unit tests for CaptchaRepo
 * Tests cover MongoDB repository operations and custom queries
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
class CaptchaRepoTest {

    @Autowired
    private CaptchaRepo captchaRepo;

    private Captcha testCaptcha;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        captchaRepo.deleteAll();

        // Create test captcha
        testCaptcha = new Captcha();
        testCaptcha.setCaptcha("ABC123");
        testCaptcha.setIpAddress("192.168.1.1");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        captchaRepo.deleteAll();
    }

    /**
     * Test that repository is autowired successfully
     */
    @Test
    void testRepositoryIsNotNull() {
        assertNotNull(captchaRepo);
    }

    /**
     * Test saving a captcha entity
     */
    @Test
    void testSaveCaptcha() {
        Captcha savedCaptcha = captchaRepo.save(testCaptcha);

        assertNotNull(savedCaptcha);
        assertNotNull(savedCaptcha.getId());
        assertEquals("ABC123", savedCaptcha.getCaptcha());
        assertEquals("192.168.1.1", savedCaptcha.getIpAddress());
    }

    /**
     * Test finding captcha by captcha text and IP address - success case
     */
    @Test
    void testFindByCaptchaAndIpAddress_Success() {
        captchaRepo.save(testCaptcha);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("ABC123", results.get(0).getCaptcha());
        assertEquals("192.168.1.1", results.get(0).getIpAddress());
    }

    /**
     * Test finding captcha by captcha text and IP address - not found
     */
    @Test
    void testFindByCaptchaAndIpAddress_NotFound() {
        captchaRepo.save(testCaptcha);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("XYZ789", "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding captcha with wrong IP address
     */
    @Test
    void testFindByCaptchaAndIpAddress_WrongIpAddress() {
        captchaRepo.save(testCaptcha);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "10.0.0.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding multiple captchas with same captcha and IP address
     */
    @Test
    void testFindByCaptchaAndIpAddress_MultipleCaptchas() {
        captchaRepo.save(testCaptcha);

        Captcha secondCaptcha = new Captcha();
        secondCaptcha.setCaptcha("ABC123");
        secondCaptcha.setIpAddress("192.168.1.1");
        captchaRepo.save(secondCaptcha);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1");

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    /**
     * Test finding by ID
     */
    @Test
    void testFindById() {
        Captcha savedCaptcha = captchaRepo.save(testCaptcha);

        Optional<Captcha> result = captchaRepo.findById(savedCaptcha.getId());

        assertTrue(result.isPresent());
        assertEquals(savedCaptcha.getId(), result.get().getId());
        assertEquals("ABC123", result.get().getCaptcha());
    }

    /**
     * Test finding by non-existent ID
     */
    @Test
    void testFindById_NotFound() {
        Optional<Captcha> result = captchaRepo.findById("nonexistent-id");

        assertFalse(result.isPresent());
    }

    /**
     * Test deleting a captcha
     */
    @Test
    void testDeleteCaptcha() {
        Captcha savedCaptcha = captchaRepo.save(testCaptcha);
        String id = savedCaptcha.getId();

        captchaRepo.deleteById(id);

        Optional<Captcha> result = captchaRepo.findById(id);
        assertFalse(result.isPresent());
    }

    /**
     * Test count method
     */
    @Test
    void testCount() {
        assertEquals(0, captchaRepo.count());

        captchaRepo.save(testCaptcha);
        assertEquals(1, captchaRepo.count());
    }

    /**
     * Test find all method
     */
    @Test
    void testFindAll() {
        captchaRepo.save(testCaptcha);

        Captcha anotherCaptcha = new Captcha();
        anotherCaptcha.setCaptcha("XYZ789");
        anotherCaptcha.setIpAddress("10.0.0.1");
        captchaRepo.save(anotherCaptcha);

        List<Captcha> allCaptchas = captchaRepo.findAll();

        assertNotNull(allCaptchas);
        assertEquals(2, allCaptchas.size());
    }

    /**
     * Test update captcha
     */
    @Test
    void testUpdateCaptcha() {
        Captcha savedCaptcha = captchaRepo.save(testCaptcha);

        savedCaptcha.setCaptcha("DEF456");
        Captcha updatedCaptcha = captchaRepo.save(savedCaptcha);

        assertEquals("DEF456", updatedCaptcha.getCaptcha());
        assertEquals(savedCaptcha.getId(), updatedCaptcha.getId());
    }

    /**
     * Test finding with null captcha parameter
     */
    @Test
    void testFindByCaptchaAndIpAddress_NullCaptcha() {
        captchaRepo.save(testCaptcha);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress(null, "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding with null IP address parameter
     */
    @Test
    void testFindByCaptchaAndIpAddress_NullIpAddress() {
        captchaRepo.save(testCaptcha);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", null);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding with empty captcha string
     */
    @Test
    void testFindByCaptchaAndIpAddress_EmptyCaptcha() {
        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("", "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding with empty IP address string
     */
    @Test
    void testFindByCaptchaAndIpAddress_EmptyIpAddress() {
        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test case sensitivity of captcha search
     */
    @Test
    void testFindByCaptchaAndIpAddress_CaseSensitive() {
        captchaRepo.save(testCaptcha);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty()); // MongoDB queries are case-sensitive by default
    }
}

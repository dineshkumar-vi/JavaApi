package com.example.repository;

import com.example.data.Captcha;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CaptchaRepo
 * Tests MongoDB repository operations for Captcha entity
 */
@DataMongoTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/test"
})
class CaptchaRepoTest {

    @Autowired
    private CaptchaRepo captchaRepo;

    private Captcha testCaptcha1;
    private Captcha testCaptcha2;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        captchaRepo.deleteAll();

        // Create test data
        testCaptcha1 = new Captcha();
        testCaptcha1.setCaptcha("ABC123");
        testCaptcha1.setIpAddress("192.168.1.1");

        testCaptcha2 = new Captcha();
        testCaptcha2.setCaptcha("XYZ789");
        testCaptcha2.setIpAddress("192.168.1.2");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        captchaRepo.deleteAll();
    }

    /**
     * Test saving a captcha to the repository
     */
    @Test
    void testSaveCaptcha() {
        Captcha saved = captchaRepo.save(testCaptcha1);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("ABC123", saved.getCaptcha());
        assertEquals("192.168.1.1", saved.getIpAddress());
    }

    /**
     * Test finding captcha by captcha value and IP address - success case
     */
    @Test
    void testFindByCaptchaAndIpAddress_Success() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("ABC123", results.get(0).getCaptcha());
        assertEquals("192.168.1.1", results.get(0).getIpAddress());
    }

    /**
     * Test finding captcha by captcha value and IP address - not found
     */
    @Test
    void testFindByCaptchaAndIpAddress_NotFound() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("INVALID", "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding captcha with wrong IP address
     */
    @Test
    void testFindByCaptchaAndIpAddress_WrongIpAddress() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.99");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding captcha with wrong captcha value
     */
    @Test
    void testFindByCaptchaAndIpAddress_WrongCaptcha() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("WRONG", "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding multiple captchas with same captcha and IP
     */
    @Test
    void testFindByCaptchaAndIpAddress_MultipleCaptchas() {
        // Save two captchas with same values
        Captcha captcha1 = new Captcha();
        captcha1.setCaptcha("SAME123");
        captcha1.setIpAddress("192.168.1.1");
        captchaRepo.save(captcha1);

        Captcha captcha2 = new Captcha();
        captcha2.setCaptcha("SAME123");
        captcha2.setIpAddress("192.168.1.1");
        captchaRepo.save(captcha2);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("SAME123", "192.168.1.1");

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    /**
     * Test finding captcha with null captcha value
     */
    @Test
    void testFindByCaptchaAndIpAddress_NullCaptcha() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress(null, "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding captcha with null IP address
     */
    @Test
    void testFindByCaptchaAndIpAddress_NullIpAddress() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", null);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding captcha with empty string captcha
     */
    @Test
    void testFindByCaptchaAndIpAddress_EmptyCaptcha() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("", "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding captcha with empty string IP address
     */
    @Test
    void testFindByCaptchaAndIpAddress_EmptyIpAddress() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test finding all captchas
     */
    @Test
    void testFindAll() {
        captchaRepo.save(testCaptcha1);
        captchaRepo.save(testCaptcha2);

        List<Captcha> all = captchaRepo.findAll();

        assertNotNull(all);
        assertEquals(2, all.size());
    }

    /**
     * Test deleting a captcha
     */
    @Test
    void testDeleteCaptcha() {
        Captcha saved = captchaRepo.save(testCaptcha1);
        assertNotNull(saved.getId());

        captchaRepo.deleteById(saved.getId());

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1");
        assertTrue(results.isEmpty());
    }

    /**
     * Test case sensitivity of captcha search
     */
    @Test
    void testFindByCaptchaAndIpAddress_CaseSensitive() {
        captchaRepo.save(testCaptcha1);

        List<Captcha> results = captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1");

        assertNotNull(results);
        assertTrue(results.isEmpty()); // Should not find lowercase version
    }

    /**
     * Test repository is properly annotated
     */
    @Test
    void testRepositoryAnnotation() {
        assertTrue(CaptchaRepo.class.isAnnotationPresent(org.springframework.stereotype.Repository.class));
    }
}

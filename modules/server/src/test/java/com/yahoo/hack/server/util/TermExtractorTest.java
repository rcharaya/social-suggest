package com.yahoo.hack.server.util;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @since 10/11/11
 */
public class TermExtractorTest {

    private TermExtractor extractor;

    @BeforeTest
    public void setupTest() {
        extractor = new TermExtractor();
    }


    @Test
    public void testDoubleExtract() throws Exception {
        List<String> result = extractor.extract("i liked steve jobs and bill gates");
        assertEquals(result.size(), 2);
        assertTrue(result.contains("steve jobs"));
        assertTrue(result.contains("bill gates"));
        System.out.println(result);
    }

    @Test
    public void testSingleExtract() throws Exception {
        List<String> result = extractor.extract("i liked steve jobs");
        assertEquals(result.size(), 1);
        assertTrue(result.contains("steve jobs"));
    }

}

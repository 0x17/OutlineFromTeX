package org.andreschnabel.outlinefromtex.tests;

import org.andreschnabel.outlinefromtex.Indices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndicesTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testToString() {
        assertEquals("", (new Indices()).toString());
        assertEquals("1", (new Indices(1)).toString());
        assertEquals("1.20", (new Indices(1, 20)).toString());
        assertEquals("1.2.2", (new Indices(1, 2, 2)).toString());
        assertEquals("1.8.4", (new Indices(1, 8, 4)).toString());
        assertEquals("1.8.4.2", (new Indices(1, 8, 4, 2)).toString());
    }

    @Test
    void testEquals() {
        assertEquals(new Indices(1,2,3), new Indices(1,2,3));
        assertNotEquals(new Indices(1,2,3), new Indices(1,2));
    }

    @Test
    void nextChapter() {
        assertEquals(new Indices(1), (new Indices()).nextChapter());
        assertEquals(new Indices(3), (new Indices(2)).nextChapter());
        assertEquals(new Indices(3), (new Indices(2, 3)).nextChapter());
        assertEquals(new Indices(3), (new Indices(2, 3, 5, 8)).nextChapter());
    }

    @Test
    void nextSection() {
        assertEquals(new Indices(1,1), (new Indices()).nextSection());
        assertEquals(new Indices(2,1), (new Indices(2)).nextSection());
        assertEquals(new Indices(2,4), (new Indices(2, 3)).nextSection());
        assertEquals(new Indices(2,4), (new Indices(2, 3, 5, 8)).nextSection());
    }

    @Test
    void nextSubsection() {
        assertEquals(new Indices(1,1,1), (new Indices()).nextSubsection());
        assertEquals(new Indices(2,1,1), (new Indices(2)).nextSubsection());
        assertEquals(new Indices(2,3,1), (new Indices(2, 3)).nextSubsection());
        assertEquals(new Indices(2,3,6), (new Indices(2, 3, 5)).nextSubsection());
        assertEquals(new Indices(2,3,6), (new Indices(2, 3, 5, 8)).nextSubsection());
    }

    @Test
    void nextSubsubsection() {
        assertEquals(new Indices(1,1,1,1), (new Indices()).nextSubsubsection());
        assertEquals(new Indices(2,1,1,1), (new Indices(2)).nextSubsubsection());
        assertEquals(new Indices(2,3,1,1), (new Indices(2, 3)).nextSubsubsection());
        assertEquals(new Indices(2,3,5,1), (new Indices(2, 3, 5)).nextSubsubsection());
        assertEquals(new Indices(2,3,5,9), (new Indices(2, 3, 5, 8)).nextSubsubsection());
        assertEquals(new Indices(2,3,5,9), (new Indices(2, 3, 5, 8, 2)).nextSubsubsection());
    }

    @Test
    void nextParagraph() {
        assertEquals(new Indices(1,1,1,1,1), (new Indices()).nextParagraph());
        assertEquals(new Indices(2,1,1,1,1), (new Indices(2)).nextParagraph());
        assertEquals(new Indices(2,3,1,1,1), (new Indices(2, 3)).nextParagraph());
        assertEquals(new Indices(2,3,5,1,1), (new Indices(2, 3, 5)).nextParagraph());
        assertEquals(new Indices(2,3,5,8,1), (new Indices(2, 3, 5, 8)).nextParagraph());
        assertEquals(new Indices(2,3,5,8,3), (new Indices(2, 3, 5, 8, 2)).nextParagraph());
        assertEquals(new Indices(2,3,5,8,3), (new Indices(2, 3, 5, 8, 2, 1)).nextParagraph());
    }
}
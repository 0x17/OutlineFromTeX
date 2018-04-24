package org.andreschnabel.outlinefromtex.tests;

import org.andreschnabel.outlinefromtex.Utils;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    private final String FN = "test.txt";
    private final String CONTENTS = "First line\nSecond line\nLast line";

    @BeforeAll
    static void setUp() throws IOException {
        createTestfiles();
    }

    @AfterAll
    static void tearDown() throws IOException {
        deleteTestfiles();
    }

    @Test
    void slurp() throws IOException {
        FileWriter fw = new FileWriter(FN);
        fw.write(CONTENTS);
        fw.close();

        assertEquals(CONTENTS, Utils.slurp(FN));

        Files.delete(Paths.get(FN));
    }

    @Test
    void spit() throws IOException {
        Utils.spit(CONTENTS, FN);

        FileReader fr = new FileReader(FN);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        while(br.ready()) {
            sb.append(br.readLine()).append("\n");
        }
        fr.close();

        assertEquals(CONTENTS+"\n", sb.toString());

        Files.delete(Paths.get(FN));
    }

    private static void createTestfiles() throws IOException {
        Utils.spit("First line", "1file.delme");
        Utils.spit("Second line", "2file.delme");
        Utils.spit("Third line\nFourth line", "3file.delme");
        Files.createDirectory(Paths.get("SomeDir"));
        Utils.spit("Fifth line", "SomeDir/4file.delme");
    }

    private static void deleteTestfiles() throws IOException {
        for(int i=0; i<3; i++) {
            Files.deleteIfExists(Paths.get(String.valueOf(i + 1) + "file.delme"));
        }
        Files.deleteIfExists(Paths.get("SomeDir/4file.delme"));
        Files.deleteIfExists(Paths.get("SomeDir"));
    }

    private Predicate<String> extensionChecker = s -> s.endsWith("delme");

    @Test
    void recursivelyCollectLinesFromFilesWithExtension() throws IOException {
        assertEquals("First line\nSecond line\nThird line\nFourth line\nFifth line\n", Utils.recursivelyCollectLinesFromFilesMatchingPredicate(".", extensionChecker));
    }

    @Test
    void filenamesMatchingPredicateInPath() throws IOException {
        assertArrayEquals(new String[]{"1file.delme", "2file.delme", "3file.delme"}, Utils.filenamesMatchingPredicateInPath(".", extensionChecker));
    }

    @Test
    void filesWithExtensionInPathRecursive() throws IOException {
        String dirPathStr = Paths.get("./SomeDir").toString();
        assertArrayEquals(new String[]{"1file.delme", "2file.delme", "3file.delme", dirPathStr+"/4file.delme"}, Utils.filenamesMatchingPredicateInPathRecursive(".", extensionChecker));
    }
}
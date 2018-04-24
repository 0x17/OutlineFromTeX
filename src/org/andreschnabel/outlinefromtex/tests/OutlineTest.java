package org.andreschnabel.outlinefromtex.tests;

import org.andreschnabel.outlinefromtex.Outline;
import org.andreschnabel.outlinefromtex.Pair;
import org.andreschnabel.outlinefromtex.Utils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OutlineTest {

    private final String smallExampleTeX =
            "\\chapter{First chapter}\n" +
                    "\\section{First section}\n" +
                    "\\subsection{First subsection}\n" +
                    "\\subsubsection{First subsubsection}\n" +
                    "% First paragraph\n" +
                    "Some contents...\n" +
                    "% Second paragraph\n" +
                    "Some contents...\n" +
                    "\\section{Second section}\n";

    private final Outline expectedOutline = new Outline(
            new Pair<>("First chapter", new int[]{1}),
            new Pair<>("First section", new int[]{1, 1}),
            new Pair<>("First subsection", new int[]{1, 1, 1}),
            new Pair<>("First subsubsection", new int[]{1, 1, 1, 1}),
            new Pair<>("First paragraph", new int[]{1, 1, 1, 1, 1}),
            new Pair<>("Second paragraph", new int[]{1, 1, 1, 1, 2}),
            new Pair<>("Second section", new int[]{1, 2})
    );

    @Test
    void fromStr() {
        assertEquals(expectedOutline, Outline.fromStr(smallExampleTeX));
    }

    @Test
    void fromTeXInPath() throws IOException {
        writeSubrangeToFile(0, 1, "1FirstFile.tex");
        writeSubrangeToFile(2, 3, "2SecondFile.tex");
        writeSubrangeToFile(4, 8, "3ThirdFile.tex");
        assertEquals(expectedOutline, Outline.fromTeXInPath("."));
        String[] names = { "First", "Second", "Third" };
        Utils.enumerate(names).forEach(pair -> {
            try {
                Files.deleteIfExists(Paths.get(String.valueOf(pair.getKey()+1) + pair.getValue() + "File.tex"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void writeSubrangeToFile(int fromIncl, int toIncl, String fn) throws IOException {
        String[] subrange = Arrays.copyOfRange(smallExampleTeX.split("\n"), fromIncl, toIncl + 1);
        Utils.spit(String.join("\n", subrange)+"\n", fn);
    }
}
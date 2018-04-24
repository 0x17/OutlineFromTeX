package org.andreschnabel.outlinefromtex;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Pattern;

public class Outline {

    private final NamedEntry[] entries;

    @SafeVarargs
    public Outline(Pair<String, int[]>... entries) {
        this.entries = Arrays.stream(entries).map(pair -> new NamedEntry(pair.getKey(), pair.getValue())).toArray(NamedEntry[]::new);
    }

    public Outline(NamedEntry... entries) {
        this.entries = entries;
    }

    public static Outline fromStr(String s) {
        LinkedList<NamedEntry> parsedEntries = new LinkedList<>();
        Indices previousIndices = new Indices();

        for(String line : s.split("\n")) {
            Optional<NamedEntry> ne = NamedEntry.parseEntryFromLine(line, previousIndices);
            ne.ifPresent(parsedEntries::add);
            if(!parsedEntries.isEmpty())
                previousIndices = parsedEntries.getLast().getIndices();
        }
        return new Outline(parsedEntries.toArray(new NamedEntry[0]));
    }

    public static Outline fromTeXInPath(String rootPath) throws IOException {
        return fromStr(Utils.recursivelyCollectLinesFromFilesMatchingPredicate(rootPath, Pattern.compile("^\\d.*\\.tex$").asPredicate()));
    }

    public NamedEntry[] getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Outline outline = (Outline) o;
        return Arrays.equals(entries, outline.entries);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(entries);
    }

    @Override
    public String toString() {
        return "Outline{" +
                "entries=" + (entries == null ? null : Arrays.asList(entries)) +
                '}';
    }

    public String toText() {
        StringBuilder sb = new StringBuilder();
        for(var entry : entries) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }
}

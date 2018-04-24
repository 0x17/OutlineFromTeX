package org.andreschnabel.outlinefromtex;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedEntry {

    private String name;
    private Indices indices;

    public NamedEntry(String name, int[] indices) {
        this.name = name;
        this.indices = new Indices(indices);
    }

    public NamedEntry(String name, Indices indices) {
        this.name = name;
        this.indices = indices;
    }

    public static Optional<NamedEntry> parseEntryFromLine(String line, Indices previousIndices) {
        Optional<Pair<String, Keyword>> tk = parseTitleAndKeywordFromLine(line);
        if(tk.isPresent()) {
            String title = tk.get().getKey();
            Keyword kw = tk.get().getValue();
            return Optional.of(new NamedEntry(title, previousIndices.nextKeyword(kw)));
        } else if(line.startsWith("% ") && !line.contains("!TeX spellcheck")) {
            return Optional.of(new NamedEntry(line.substring(2), previousIndices.nextParagraph()));
        }
        return Optional.empty();
    }

    private static Optional<Pair<String, Keyword>> parseTitleAndKeywordFromLine(String line) {
        var p = Pattern.compile("^\\\\(\\w+)\\{(.+)}$");
        var m = p.matcher(line);
        if(m.find()) {
            if(m.groupCount() == 2) {
                String kwStr = m.group(1).toUpperCase();
                if(Arrays.stream(Keyword.values()).map(Enum::toString).anyMatch(kw -> kw.equals(kwStr)))
                    return Optional.of(new Pair<>(m.group(2), Keyword.valueOf(kwStr)));
                else
                    return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return indices.toString() + " " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntry that = (NamedEntry) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(indices, that.indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, indices);
    }

    public String getName() {
        return name;
    }

    public Indices getIndices() {
        return indices;
    }
}

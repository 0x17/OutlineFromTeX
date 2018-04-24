package org.andreschnabel.outlinefromtex;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Indices {

    private final int[] indices;

    public Indices(int... indices) {
        this.indices = Arrays.stream(indices).toArray();
    }

    @Override
    public String toString() {
        return Arrays.stream(indices).mapToObj(String::valueOf).collect(Collectors.joining("."));
    }

    public Indices nextChapter() {
        if(indices.length == 0) return new Indices(1);
        return new Indices(indices[0] + 1);
    }

    public Indices nextSection() {
        if(indices.length == 0) return new Indices(1,1);
        return new Indices(indices[0],
                indices.length >= 2 ? indices[1] + 1 : 1);
    }

    public Indices nextSubsection() {
        if(indices.length == 0) return new Indices(1,1,1);
        return new Indices(indices[0],
                indices.length >= 2 ? indices[1] : 1,
                indices.length >= 3 ? indices[2] + 1 : 1);
    }

    public Indices nextSubsubsection() {
        if(indices.length == 0) return new Indices(1,1,1,1);
        return new Indices(indices[0],
                indices.length >= 2 ? indices[1] : 1,
                indices.length >= 3 ? indices[2] : 1,
                indices.length >= 4 ? indices[3] + 1 : 1);
    }

    public Indices nextParagraph() {
        if(indices.length == 0) return new Indices(1,1,1,1,1);
        return new Indices(indices[0],
                indices.length >= 2 ? indices[1] : 1,
                indices.length >= 3 ? indices[2] : 1,
                indices.length >= 4 ? indices[3] : 1,
                indices.length >= 5 ? indices[4] + 1 : 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indices indices1 = (Indices) o;
        return Arrays.equals(indices, indices1.indices);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(indices);
    }

    public Indices nextKeyword(Keyword kw) {
        switch(kw) {
            case CHAPTER:
                return nextChapter();
            case SECTION:
                return nextSection();
            case SUBSECTION:
                return nextSubsection();
            case SUBSUBSECTION:
                return nextSubsubsection();
            case PARAGRAPH:
                return nextParagraph();
        }
        return this;
    }
}

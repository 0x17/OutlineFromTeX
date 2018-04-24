package org.andreschnabel.outlinefromtex;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Utils.spit(Outline.fromTeXInPath("C:\\Users\\a.schnabel\\Seafile\\Dropbox\\Scheduling\\Thesis\\Dissertation\\contents").toText(), "outline.txt");
    }
}

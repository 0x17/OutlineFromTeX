package org.andreschnabel.outlinefromtex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {

    public static String slurp(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    public static void spit(String contents, String filename) throws IOException {
        Files.write(Paths.get(filename), contents.getBytes(), StandardOpenOption.CREATE);
    }

    public static String[] filenamesMatchingPredicateInPathRecursive(String rootPath, Predicate<String> pred) throws IOException {
        List<String> filenames = new LinkedList<>(Arrays.asList(filenamesMatchingPredicateInPath(rootPath, pred)));
        Files
            .list(Paths.get(rootPath))
            .filter(path -> Files.isDirectory(path))
            .forEach(path -> {
                try {
                    filenames.addAll(Arrays.stream(filenamesMatchingPredicateInPath(path.toString(), pred)).collect(Collectors.toList()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        return filenames.toArray(new String[0]);
    }

    public static String[] filenamesMatchingPredicateInPath(String rootPath, Predicate<String> pred) throws IOException {
        return Files
                .list(Paths.get(rootPath)).filter(path -> Files.isRegularFile(path) && pred.test(path.getFileName().toString()))
                .map(path -> path.toAbsolutePath().toString())
                .toArray(String[]::new);
    }

    public static String recursivelyCollectLinesFromFilesMatchingPredicate(String rootPath, Predicate<String> pred) throws IOException {
        StringBuilder sb = new StringBuilder();
        for(String fn : filenamesMatchingPredicateInPathRecursive(rootPath, pred)) {
            sb.append(slurp(fn)).append("\n");
        }
        return sb.toString();
    }

    public static <T> Stream<Pair<Integer, T>> enumerate(T[] names) {
        return IntStream.range(0, names.length).mapToObj(i -> new Pair<>(i, names[i]));
    }
}

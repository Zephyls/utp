package zad1;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class ProgLang {
    private final Map<String, List<String>> langsMap = new LinkedHashMap<>();
    private final Map<String, List<String>> progsMap = new LinkedHashMap<>();

    public ProgLang(String nazwaPliku) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(nazwaPliku));
        for (String line : lines) {
            String[] parts = line.split("\\t");
            String language = parts[0];
            
            List<String> programmers = Arrays.stream(parts)
                                             .skip(1)
                                             .distinct()
                                             .collect(Collectors.toList());

            langsMap.put(language, programmers);

            for (String programmer : programmers) {
                progsMap.computeIfAbsent(programmer, k -> new ArrayList<>()).add(language);
            }
        }
    }

    public Map<String, List<String>> getLangsMap() {
        return langsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new ArrayList<>(e.getValue()), 
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


    public Map<String, List<String>> getProgsMap() {
        return progsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new ArrayList<>(e.getValue()), 
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


    public Map<String, List<String>> getLangsMapSortedByNumOfProgs() {
        return sorted(langsMap, Comparator
                .<Map.Entry<String, List<String>>>comparingInt(e -> -e.getValue().size())
                .thenComparing(Map.Entry::getKey));
    }

    public Map<String, List<String>> getProgsMapSortedByNumOfLangs() {
        return sorted(progsMap, Comparator
                .<Map.Entry<String, List<String>>>comparingInt(e -> -e.getValue().size())
                .thenComparing(Map.Entry::getKey));
    }


    public Map<String, List<String>> getProgsMapForNumOfLangsGreaterThan(int n) {
        return filtered(progsMap, entry -> entry.getValue().size() > n);
    }

    private <K, V> Map<K, V> sorted(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        return map.entrySet().stream()
                .sorted(comparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private <K, V> Map<K, V> filtered(Map<K, V> map, java.util.function.Predicate<Map.Entry<K, V>> predicate) {
        return map.entrySet().stream()
                .filter(predicate)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}

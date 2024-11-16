package zad1;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.Predicate;

public class ProgLang {
    private final Map<String, List<String>> langsMap = new HashMap<>();
    private final Map<String, List<String>> progsMap = new HashMap<>();

    // Constructor đọc file và khởi tạo dữ liệu
    public ProgLang(String nazwaPliku) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(nazwaPliku));
        for (String line : lines) {
            String[] parts = line.split("\t");
            String language = parts[0];
            List<String> programmers = Arrays.asList(parts).subList(1, parts.length);

            // Cập nhật langsMap
            langsMap.put(language, new ArrayList<>(programmers));

            // Cập nhật progsMap
            for (String programmer : programmers) {
                progsMap.computeIfAbsent(programmer, k -> new ArrayList<>()).add(language);
            }
        }
    }

    // Trả về bản sao không thay đổi của langsMap
    public Map<String, List<String>> getLangsMap() {
        return langsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new ArrayList<>(e.getValue()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // Trả về bản sao không thay đổi của progsMap
    public Map<String, List<String>> getProgsMap() {
        return progsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new ArrayList<>(e.getValue()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // Sắp xếp langsMap theo số lượng lập trình viên
    public Map<String, List<String>> getLangsMapSortedByNumOfProgs() {
        return sorted(langsMap, Comparator
                .<Map.Entry<String, List<String>>>comparingInt(e -> -e.getValue().size())
                .thenComparing(Map.Entry::getKey));
    }

    // Sắp xếp progsMap theo số lượng ngôn ngữ
    public Map<String, List<String>> getProgsMapSortedByNumOfLangs() {
        return sorted(progsMap, Comparator
                .<Map.Entry<String, List<String>>>comparingInt(e -> -e.getValue().size())
                .thenComparing(Map.Entry::getKey));
    }

    // Lọc progsMap để chỉ giữ các lập trình viên biết hơn n ngôn ngữ
    public Map<String, List<String>> getProgsMapForNumOfLangsGreaterThan(int n) {
        return filtered(progsMap, entry -> entry.getValue().size() > n);
    }

    // Phương thức sắp xếp chung
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

    // Phương thức lọc chung
    private <K, V> Map<K, V> filtered(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
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

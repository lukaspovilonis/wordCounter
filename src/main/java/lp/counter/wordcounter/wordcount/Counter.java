package lp.counter.wordcounter.wordcount;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
public class Counter implements ICounter {

    public Map<String, Long> wordCounter(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        return br.lines()
                .flatMap(line -> Arrays.stream(line.trim().split("\\s")))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
                .filter(word -> word.length() > 0)
                .map(word -> new SimpleEntry<>(word, 1))
                .collect(groupingBy(SimpleEntry::getKey, counting()));
    }
}

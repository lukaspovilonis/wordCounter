package lp.counter.wordcounter.services;

import lp.counter.wordcounter.wordcount.ICounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Service
public class FileService {

    ForkJoinPool ioPool = new ForkJoinPool(4);

    @Autowired
    ICounter counter;

    public Map<String, Long> fileManegerWithThreads(MultipartFile[] files) throws ExecutionException, InterruptedException {
        List<Map<String, Long>> wordCountList = new ArrayList<>();
        ioPool.submit(
                () -> Arrays.stream(files).parallel().forEach(
                        multipartFile -> {
                            try {
                                Map<String, Long> wordCount = counter.wordCounter(multipartFile);
                                wordCountList.add(wordCount);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                )
        ).get();

        Optional<Map<String, Long>> reduce = wordCountList
                .stream()
                .reduce((firstMap, secondMap) -> Stream.concat(firstMap.entrySet().stream(),secondMap.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (countInFirstMap,
                                                                        countInSecondMap) -> countInFirstMap + countInSecondMap)));
        return reduce.get().entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

}
package lp.counter.wordcounter.wordcount;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ICounter {
    Map<String, Long> wordCounter(MultipartFile file) throws IOException;
}

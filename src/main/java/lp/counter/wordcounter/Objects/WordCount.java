package lp.counter.wordcounter.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordCount {
    private String key;
    private Long value;


    public WordCount(String key, Long value) {
        this.key = key;
        this.value = value;
    }
}
package lp.counter.wordcounter.controllers;

import lp.counter.wordcounter.Objects.WordCount;
import lp.counter.wordcounter.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class FileController {

    @Autowired
    FileService fileService;

    @GetMapping("/")
    public  String index()
    {
        return "upload";
    }

    @PostMapping("/uploadFiles")
    @ResponseBody
    public List<WordCount> uploadFiles(@RequestParam("files") MultipartFile[] files) throws ExecutionException, InterruptedException {

        Map<String, Long> wordCount =  fileService.fileManegerWithThreads(files);


        return wordCount.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue))
                .map(e -> new WordCount(e.getKey(), e.getValue())).collect(Collectors.toList()).stream()
                .sorted(Comparator.comparing(WordCount::getKey))
                .collect(Collectors.toList());
    }
}
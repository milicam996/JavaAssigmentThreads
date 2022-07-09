package com.javaAssigment.chatApp.service;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
@Scope("singleton")
public class HelloWorldService {

    @Async("asyncExecutor")
    public CompletableFuture<String> GetByLangAPI(String text, String lang) throws InterruptedException {
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://microsoft-translator-text.p.rapidapi.com/translate?to%5B0%5D="+lang+"&api-version=3.0&from=sr-Latn&profanityAction=NoAction&textType=plain"))
                    .header("content-type", "application/json")
                    .header("X-RapidAPI-Host", "microsoft-translator-text.p.rapidapi.com")
                    .header("X-RapidAPI-Key", "7c486caf66msh7bec070c1113d78p1ea0f7jsne1436838fe73")
                    .method("POST", HttpRequest.BodyPublishers.ofString("[\r\n    {\r\n        \"Text\": \""+ text +".\"\r\n    }\r\n]"))
                    .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        String[] split1 = response.body().split("text\":\"", 2);
        String[] split2 = split1[1].split("\"");
        return CompletableFuture.completedFuture(split2[0]);

    }


}


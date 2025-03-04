package com.builandrun.urlshorter.controller;

import com.builandrun.urlshorter.controller.dto.ShortenUrlRequest;
import com.builandrun.urlshorter.controller.dto.ShortenUrlResponse;
import com.builandrun.urlshorter.entities.UrlEntity;
import com.builandrun.urlshorter.repositories.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;

    @PostMapping("/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request,
                                           HttpServletRequest servletRequest){

        String id;
        do {
            id = RandomStringUtils.randomAlphanumeric(5,10);
        } while (urlRepository.existsById(id));

        urlRepository.save(new UrlEntity(id, request.url(), LocalDateTime.now().plusMinutes(2)));

        String redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);

        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") String id) {
        Optional<UrlEntity> url = urlRepository.findById(id);

        if (url.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

}

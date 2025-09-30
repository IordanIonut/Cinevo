package com.cinovo.backend;

import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Client;
import com.cinovo.backend.TMDB.Controller;
import com.cinovo.backend.TMDB.DTO.GenresResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

package com.itunes2spotify.api;

import com.itunes2spotify.api.domain.PlaylistDomain;
import com.itunes2spotify.api.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class HelloWorld {

    @Autowired
    PlaylistRepository playlistRepository;

    @RequestMapping("/playlists")
    public Iterable<PlaylistDomain> getPlaylists() {
        return playlistRepository.findAll();
    }



    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorld.class, args);
    }

}
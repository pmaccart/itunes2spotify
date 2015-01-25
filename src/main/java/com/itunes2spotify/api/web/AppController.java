package com.itunes2spotify.api.web;

import com.itunes2spotify.api.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

    @Autowired
    SpotifyService spotifyService;

    @RequestMapping("/")
    public String getApp(Model model) {
        model.addAttribute("user", spotifyService.getUser());
        return "app";
    }

    @RequestMapping("/react")
    public String getReactApp(Model model) {
        model.addAttribute("user", spotifyService.getUser());
        return "react_app";
    }
}

package com.itunes2spotify.api.endpoint;

import com.itunes2spotify.api.model.spotify.SpotifyUser;
import com.itunes2spotify.api.service.SpotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class SpotifyEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyEndpoint.class);

    @Autowired
    SpotifyService spotifyService;

    @RequestMapping("/api/spotify/me")
    public SpotifyUser getUser(HttpServletResponse response) {
        LOGGER.debug("Handling request for profile as JSON");
        response.setContentType("json");
        return spotifyService.getUser();
    }

    @RequestMapping(value = "/api/spotify/search", method = RequestMethod.GET)
    public Map<String, Object> search(@RequestParam("track") String track,
                                      @RequestParam(value = "artist", required = false) String artist) {
        LOGGER.debug("Handling search request: track={}, artist={}", track, artist);
        return spotifyService.search(track, artist);
    }

    public void setSpotifyService(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }
}

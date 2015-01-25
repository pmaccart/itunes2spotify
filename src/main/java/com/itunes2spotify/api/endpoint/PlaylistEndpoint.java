package com.itunes2spotify.api.endpoint;

import com.itunes2spotify.api.domain.PlaylistDomain;
import com.itunes2spotify.api.model.Playlist;
import com.itunes2spotify.api.repository.PlaylistRepository;
import com.itunes2spotify.api.service.LibraryService;
import com.itunes2spotify.api.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistEndpoint {

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    SpotifyService spotifyService;

    @Autowired
    LibraryService libraryService;

    @RequestMapping("")
    public List<Playlist> getPlaylists() {
        return libraryService.getPlaylists();
    }

    @RequestMapping("/{playlistId}")
    public Playlist getPlaylistById(@PathVariable String playlistId) {
        return libraryService.getPlaylist(playlistId);
    }

    @RequestMapping("/name/{name}")
    public Iterable<PlaylistDomain> getPlaylistsByName(@PathVariable String name) {
        return playlistRepository.findByNameIgnoringCase(name);
    }

    @RequestMapping("/search/name/{name}")
    public String searchByName(@PathVariable String name) {
        return spotifyService.findTrack(name, null);
    }

    @RequestMapping("/{playlistId}/link")
    public PlaylistDomain linkPlaylist(@PathVariable String playlistId) {
        return libraryService.linkPlaylist(playlistId);
    }

    @RequestMapping("/{playlistId}/publish")
    public Playlist publishPlaylist(@PathVariable String playlistId) throws Exception{
        return libraryService.publishPlaylist(playlistId);
    }




}

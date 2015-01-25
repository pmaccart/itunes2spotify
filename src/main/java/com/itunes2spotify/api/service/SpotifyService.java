package com.itunes2spotify.api.service;

import com.itunes2spotify.api.model.Playlist;
import com.itunes2spotify.api.model.spotify.SpotifyUser;

import java.util.Map;

public interface SpotifyService {

    public String findTrack(String name, String artist);

    public Map<String, Object> search(String name, String artist);

    public SpotifyUser getUser();

    public String publishPlaylist(Playlist playlist) throws Exception;

}

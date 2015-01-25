package com.itunes2spotify.api.service;

import com.itunes2spotify.api.domain.PlaylistDomain;
import com.itunes2spotify.api.model.Playlist;

import java.util.List;

public interface LibraryService {

    public List<Playlist> getPlaylists();

    public Playlist getPlaylist(String playlistId);

    public PlaylistDomain linkPlaylist(String playlistId);

    public Playlist publishPlaylist(String playlistId) throws Exception;
}

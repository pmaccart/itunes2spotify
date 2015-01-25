package com.itunes2spotify.api.service.impl;

import com.itunes2spotify.api.domain.PlaylistDomain;
import com.itunes2spotify.api.domain.TrackDomain;
import com.itunes2spotify.api.model.Playlist;
import com.itunes2spotify.api.model.Track;
import com.itunes2spotify.api.repository.PlaylistRepository;
import com.itunes2spotify.api.repository.TrackRepository;
import com.itunes2spotify.api.service.LibraryService;
import com.itunes2spotify.api.service.SpotifyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryServiceImpl.class);

    @Autowired
    SpotifyService spotifyService;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    TrackRepository trackRepository;

    @Override
    public List<Playlist> getPlaylists() {
        Iterable<PlaylistDomain> playlistDomains = playlistRepository.findAll();
        List<Playlist> playlists = new ArrayList<>();
        for (PlaylistDomain playlistDomain : playlistDomains) {
            Playlist playlist = new Playlist();
            playlist.setPlaylistId(playlistDomain.getPlaylistId());
            playlist.setName(playlistDomain.getName());
            playlist.setSpotifyId(playlistDomain.getSpotifyId());
            playlists.add(playlist);
        }

        return playlists;
    }

    @Override
    public Playlist getPlaylist(String playlistId) {
        PlaylistDomain playlistDomain = playlistRepository.findOne(playlistId);

        Playlist playlist = new Playlist();
        playlist.setPlaylistId(playlistDomain.getPlaylistId());
        playlist.setName(playlistDomain.getName());
        playlist.setSpotifyId(playlistDomain.getSpotifyId());

        List<Track> tracks = new ArrayList<>();
        for (TrackDomain trackDomain : playlistDomain.getTracks()) {
            Track track = new Track();
            track.setTrackId(trackDomain.getTrackId());
            track.setName(trackDomain.getName());
            track.setArtist(trackDomain.getArtist());
            track.setAlbum(trackDomain.getAlbum());
            track.setGenre(trackDomain.getGenre());
            track.setYear(trackDomain.getYear());
            track.setPlayCount(trackDomain.getPlaycount());
            track.setSpotifyUri(trackDomain.getSpotifyUri());

            tracks.add(track);
        }
        Collections.sort(tracks, new Comparator<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                if (o1.getPlayCount() < o2.getPlayCount()) {
                    return 1;
                }
                else if (o1.getPlayCount().equals(o2.getPlayCount())) {
                    return 0;
                }
                else {
                    return -1;
                }
            }
        });

        playlist.setTracks(tracks);

        return playlist;
    }

    @Override
    public PlaylistDomain linkPlaylist(String playlistId) {
        LOGGER.debug("Linking playlist {}", playlistId);

        try {
            PlaylistDomain playlist = playlistRepository.findOne(playlistId);

            List<TrackDomain> updatedTracks = new ArrayList<>();
            for (TrackDomain track : playlist.getTracks()) {
                if (StringUtils.isEmpty(track.getSpotifyUri())) {
                    String uri = spotifyService.findTrack(track.getName(), track.getArtist());
                    if (StringUtils.isEmpty(uri)) {
                        uri = spotifyService.findTrack(track.getName(), null);
                    }
                    if (uri != null) {
                        track.setSpotifyUri(uri);
                        updatedTracks.add(track);
                    }
                }
            }

            if (!updatedTracks.isEmpty()) {
                trackRepository.save(updatedTracks);
            }

            return playlist;
        }
        catch (NoResultException nre) {
            LOGGER.error("Unable to find result for playlist {}.", playlistId);
            return null;
        }
        catch (Exception e) {
            LOGGER.error("Error linking playlist.", e);
            throw e;
        }

    }

    @Override
    public Playlist publishPlaylist(String playlistId) throws Exception{
        LOGGER.debug("Publishing playlist {}", playlistId);
        Playlist playlist = getPlaylist(playlistId);
        Assert.notNull(playlist, "Unable to find playlist with ID " + playlistId);

        String spotifyPlaylistId = spotifyService.publishPlaylist(playlist);
        PlaylistDomain playlistDomain = playlistRepository.findOne(playlistId);
        playlistDomain.setSpotifyId(spotifyPlaylistId);
        playlistRepository.save(playlistDomain);

        playlist.setSpotifyId(spotifyPlaylistId);
        return playlist;
    }
}

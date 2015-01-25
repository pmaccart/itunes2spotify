package com.itunes2spotify.api.domain;

import javax.persistence.*;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
public class PlaylistDomain {

    @Id
    @Column(name = "playlist_id")
    String playlistId;

    @Column(name = "spotify_id")
    String spotifyId;

    @Column(name = "name")
    String name;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "playlist_tracks",
            joinColumns = {@JoinColumn(name = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "track_id")})
    private List<TrackDomain> tracks = new ArrayList<TrackDomain>();


    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TrackDomain> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDomain> tracks) {
        this.tracks = tracks;
    }
}

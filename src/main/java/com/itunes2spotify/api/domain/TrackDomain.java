package com.itunes2spotify.api.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tracks")
public class TrackDomain {

    @Id
    @Column(name = "track_id")
    String trackId;

    @Column(name = "name")
    String name;

    @Column(name = "artist")
    String artist;

    @Column(name = "album")
    String album;

    @Column(name = "genre")
    String genre;

    @Column(name = "year")
    Integer year;

    @Column(name = "playcount")
    Integer playcount = 0;

    @Column(name = "spotify_uri")
    String spotifyUri;

    @ManyToMany(mappedBy = "tracks")
    List<PlaylistDomain> playlists;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String albumn) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPlaycount() {
        return playcount;
    }

    public void setPlaycount(Integer playcount) {
        this.playcount = playcount;
    }

    public String getSpotifyUri() {
        return spotifyUri;
    }

    public void setSpotifyUri(String spotifyUri) {
        this.spotifyUri = spotifyUri;
    }
}

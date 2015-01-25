package com.itunes2spotify.api.service.impl;

import com.itunes2spotify.api.model.Playlist;
import com.itunes2spotify.api.model.Track;
import com.itunes2spotify.api.model.spotify.SpotifyUser;
import com.itunes2spotify.api.service.SpotifyService;
import com.wrapper.spotify.Api;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

@Service
public class SpotifyServiceImpl implements SpotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyServiceImpl.class);

    static String BASE_URI = "https://api.spotify.com";

    Api api;
    String clientId = "8930664be62445d99430042e2eddd1eb";
    String clientSecret = "6784583ed6f742208b479414c9a851c4";
    String redirectURI = "http://localhost:9000/redirect";

    RestOperations spotifyRestTemplate;
    RestOperations trustedClientRestTemplate;


    public SpotifyServiceImpl() {
        api = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectURI(redirectURI)
                .build();
    }

    public String findTrack(String name, String artist) {
//        TrackSearchRequest trackSearchRequest = api.searchTracks(name + " " + artist)
//                .market("US").build();
        try {

            Map<String, Object> searchResult = search(name, artist);
            LOGGER.debug("Got search results: {}", searchResult);
            Map<String, Object> tracks = (Map<String, Object>) searchResult.get("tracks");
            if (tracks != null) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");
                if (items != null && items.size() > 0) {
                    LOGGER.debug("Found {} track results.", items.size());
                    Map<String, Object> item  = items.get(0);
                    return (String) item.get("uri");
                }
                else {
                    LOGGER.debug("No search results found.");
                    return null;
                }
            }
            else {
                LOGGER.debug("Weird search result received.");
                return null;
            }
        }
        catch (Exception e) {
            LOGGER.error("Error performing search for {}.", name, e);
            return null;
        }

    }

    public Map<String, Object> search(String name, String artist) {
        try {
            StringBuilder query = new StringBuilder("");
            if (name != null) {
                query.append("track:").append(StringUtils.substring(name, 0, 10)).append("*");
            }
            if (artist != null) {
                query.append(" artist:").append(StringUtils.substring(artist, 0, 10)).append("*");
            }

            URI uri = URI.create(BASE_URI + "/v1/search?market=US&type=track&q=" + URLEncoder.encode(query.toString(), "UTF-8"));
            LOGGER.debug("Executing search: {}", uri.toString());
            Map<String, Object> searchResult = spotifyRestTemplate.getForObject(uri, Map.class);

            return searchResult;
        }
        catch (Exception e) {
            LOGGER.error("Error performing search.", e);
            return null;
        }
    }

    public SpotifyUser getUser() {

        try {
            return spotifyRestTemplate.getForObject(BASE_URI + "/v1/me", SpotifyUser.class);
        }
        catch (Exception e) {
            LOGGER.error("Error loading user profile from spotify.", e.getMessage());
            throw e;
        }

    }

    @Override
    public String publishPlaylist(Playlist playlist) throws Exception{
        try {
            if (StringUtils.isEmpty(playlist.getSpotifyId())) {
                String spotifyId = createPlaylist("iTunes - " + playlist.getName());
                playlist.setSpotifyId(spotifyId);
            }

            addSongs(playlist.getSpotifyId(), playlist.getTracks());

            return playlist.getSpotifyId();
        }
        catch(Exception e) {
            LOGGER.error("Error publishing playlist.", e);
            throw e;
//            return null;
        }

    }

    protected String createPlaylist(String name) throws Exception{
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", name);
        requestData.put("public", false);
        Map<String, Object> playlistResponse = spotifyRestTemplate.postForObject(BASE_URI + "/v1/users/pmaccart/playlists", requestData, Map.class);
        if (playlistResponse != null && playlistResponse.containsKey("id")) {
            return (String) playlistResponse.get("id");
        }
        else {
            LOGGER.error("Erro creating playlist; no response.");
            throw new Exception("Error creating playlist; no response.");
        }

    }

    protected void addSongs(String spotifyId, List<Track> origTracks) throws Exception{
        try {
            List<Track> tracks = new ArrayList<>(origTracks.size());

            for (Track track : origTracks) {
                tracks.add(track);
            }

            boolean first = true;
            while (!tracks.isEmpty()) {
                List<Track> tracksToAdd = tracks.subList(0, Math.min(tracks.size(), 100));
                List<String> uris = new ArrayList<String>();

                for (Track track : tracksToAdd) {
                    if (StringUtils.isNotEmpty(track.getSpotifyUri())) {
                        uris.add(track.getSpotifyUri());
                    }
                }
                tracks.subList(0, Math.min(tracks.size(), 100)).clear();

                Map<String, Object> requestData = new HashMap<>();
                requestData.put("uris", uris);
                if (first) {
                    LOGGER.debug("Adding first set of songs to playlist.");
                    spotifyRestTemplate.put(BASE_URI + "/v1/users/pmaccart/playlists/" + spotifyId + "/tracks", requestData);
                    first = false;
                }
                else {
                    LOGGER.debug("Adding remaining songs to playlist.");
                    spotifyRestTemplate.postForObject(BASE_URI + "/v1/users/pmaccart/playlists/" + spotifyId + "/tracks", requestData, Map.class);
                }
            }

        }
        catch(Exception e) {
            LOGGER.error("Error adding songs.", e);
            throw e;
        }
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public void setSpotifyRestTemplate(RestOperations spotifyRestTemplate) {
        this.spotifyRestTemplate = spotifyRestTemplate;
    }

    public void setTrustedClientRestTemplate(RestOperations trustedClientRestTemplate) {
        this.trustedClientRestTemplate = trustedClientRestTemplate;
    }
}

package com.itunes2spotify.api.endpoint;

import com.itunes2spotify.api.domain.TrackDomain;
import com.itunes2spotify.api.repository.TrackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;

@RestController
@RequestMapping("/api/tracks")
public class TrackEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackEndpoint.class);

    @Autowired
    TrackRepository trackRepository;

    @RequestMapping(value = "/{trackId}", method = RequestMethod.GET)
    public TrackDomain getTrack(@PathVariable String trackId) {
        return trackRepository.findOne(trackId);
    }

    @RequestMapping(value = "/{trackId}/spotifyUri/{spotifyUri}", method= RequestMethod.PUT)
    public TrackDomain linkTrack(@PathVariable String trackId, @PathVariable String spotifyUri) {
        TrackDomain track = trackRepository.findOne(trackId);
        if (track == null) {
            throw new NoResultException("No result found for track id=" + trackId);
        }

        track.setSpotifyUri(spotifyUri);
        trackRepository.save(track);
        return track;
    }

}

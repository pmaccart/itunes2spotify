package com.itunes2spotify.api.repository;

import com.itunes2spotify.api.domain.TrackDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public interface TrackRepository extends CrudRepository<TrackDomain, String> {

    public Iterable<TrackDomain> findByNameIgnoringCase(String name);
}

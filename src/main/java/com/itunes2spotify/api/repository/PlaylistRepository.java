package com.itunes2spotify.api.repository;

import com.itunes2spotify.api.domain.PlaylistDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlaylistRepository extends CrudRepository<PlaylistDomain, String> {

    Iterable<PlaylistDomain> findByNameIgnoringCase(String name);

}

package com.itunes2spotify.api.repository;

import com.itunes2spotify.api.domain.SessionDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<SessionDomain, String> {

}

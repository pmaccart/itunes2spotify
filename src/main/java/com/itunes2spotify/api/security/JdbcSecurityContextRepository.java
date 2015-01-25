package com.itunes2spotify.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itunes2spotify.api.domain.SessionDomain;
import com.itunes2spotify.api.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JdbcSecurityContextRepository implements SecurityContextRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSecurityContextRepository.class);

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        LOGGER.debug("Loading security context from JDBC repository.");

        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        HttpSession httpSession = request.getSession(false);

        SecurityContext context = readSecurityContextFromSession(httpSession);
        if (context == null) {
            context = SecurityContextHolder.createEmptyContext();
        }

        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("Saving security context to JDBC repository");
        String serializedContext = serializeSecurityContext(context);
        HttpSession session = request.getSession(false);
        if (session == null) {
            LOGGER.debug("No session exists, not saving context.");
            return;
        }
        String sessionId = session.getId();

        SessionDomain sessionDomain = new SessionDomain();
        sessionDomain.setSessionId(sessionId);
        sessionDomain.setData(serializedContext);

        sessionRepository.save(sessionDomain);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        String sessionId = session.getId();
        SessionDomain sessionDomain = sessionRepository.findOne(sessionId);
        return (sessionDomain != null);

    }

    private SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
        if (httpSession == null) {
            LOGGER.debug("No httpSession. Returning null security context.");
            return null;
        }

        String sessionId = httpSession.getId();
        SessionDomain sessionDomain = sessionRepository.findOne(sessionId);
        if (sessionDomain == null) {
            LOGGER.debug("No session found with ID {}", sessionId);
            return null;
        }

        SecurityContext securityContext = readSessionData(sessionDomain.getData());
        if (securityContext == null) {
            LOGGER.warn("Unable to read security context from session data {}.", sessionDomain.getData());
            return null;
        }

        return securityContext;
    }

    private SecurityContextImpl readSessionData(String sessionData) {
        try {
            return objectMapper.readValue(sessionData, SecurityContextImpl.class);
        }
        catch (Exception e) {
            LOGGER.error("Error parsing session data.", e);
            return null;
        }
    }

    private String serializeSecurityContext(SecurityContext securityContext) {
        try {
            return objectMapper.writeValueAsString(securityContext);
        }
        catch (Exception e) {
            LOGGER.error("Error serializing security context.", e);
            return null;
        }
    }
}

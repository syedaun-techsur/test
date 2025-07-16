package solutions.techsur.common.microservice.services.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import solutions.techsur.common.microservice.services.SecurityService;
import solutions.techsur.common.microservice.utils.JwtUtils;

import java.util.Collections;
import java.util.Set;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieves the current user's JWT token.
     * 
     * @return Jwt object of the current user or null if unavailable
     */
    @Override
    public Jwt getCurrentUserJwt() {
        return JwtUtils.getCurrentUserJwt();
    }

    /**
     * Gets the current user's ID from the JWT.
     * 
     * @return User ID as a String or null if not found
     */
    @Override
    public String getCurrentUserId() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return null;
        }
        return JwtUtils.getUserId(jwt);
    }

    /**
     * Gets the current user's site ID from the JWT.
     * 
     * @return Site ID as Long or null if not found or invalid
     */
    @Override
    public Long getCurrentUserSiteId() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return null;
        }
        String userSiteIdString = JwtUtils.getUserSiteId(jwt);
        if (userSiteIdString == null) {
            return null;
        }
        try {
            return Long.parseLong(userSiteIdString);
        } catch (NumberFormatException e) {
            // Log if logging is enabled (not added here to avoid new dependencies)
            return null;
        }
    }

    /**
     * Gets the current user's region ID from the JWT.
     * 
     * @return Region ID as Long or null if not found or invalid
     */
    @Override
    public Long getCurrentUserRegionId() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return null;
        }
        String userRegionIdString = JwtUtils.getUserRegionId(jwt);
        if (userRegionIdString == null) {
            return null;
        }
        try {
            return Long.parseLong(userRegionIdString);
        } catch (NumberFormatException e) {
            // Log if logging is enabled (not added here to avoid new dependencies)
            return null;
        }
    }

    /**
     * Retrieves the current user's roles from the JWT.
     * 
     * @return Set of role names or empty set if none found
     */
    @Override
    public Set<String> getCurrentUserRoles() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return Collections.emptySet();
        }
        return JwtUtils.getUserRoles(jwt);
    }

    /**
     * Retrieves the current user's bearer token.
     * 
     * @return Bearer token string or null if unavailable
     */
    @Override
    public String getCurrentUserBearerToken() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return null;
        }
        return JwtUtils.getBearerToken(jwt);
    }

    /**
     * Serializes the current user's JWT to a JSON string.
     * 
     * @return JSON string representation of the JWT
     * @throws JsonProcessingException if serialization fails
     */
    @Override
    public String getCurrentUserJwtJsonString() throws JsonProcessingException {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return null;
        }
        if (objectMapper == null) {
            throw new IllegalStateException("ObjectMapper not initialized");
        }
        return objectMapper.writeValueAsString(jwt);
    }
}
package solutions.techsur.common.microservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

/**
 * SecurityService provides methods to access the current user's security context details,
 * including JWT, user ID, roles, bearer token, site and region identifiers.
 */
public interface SecurityService {

    /**
     * Retrieves the JWT of the currently authenticated user.
     *
     * @return the Jwt object representing the current user's JWT
     */
    Jwt getCurrentUserJwt();

    /**
     * Retrieves the unique identifier of the currently authenticated user.
     *
     * @return the user ID as a String
     */
    String getCurrentUserId();

    /**
     * Retrieves the set of roles assigned to the currently authenticated user.
     *
     * @return a Set of String representing the user's roles
     */
    Set<String> getCurrentUserRoles();

    /**
     * Retrieves the bearer token for the current authenticated user.
     *
     * @return the bearer token as a String
     */
    String getCurrentUserBearerToken();

    /**
     * Retrieves the site ID associated with the current user.
     *
     * @return the site ID as a Long
     */
    Long getCurrentUserSiteId();

    /**
     * Retrieves the region ID associated with the current user.
     *
     * @return the region ID as a Long
     */
    Long getCurrentUserRegionId();

    /**
     * Retrieves the JWT of the current user as a JSON string.
     *
     * @return the JWT JSON string representation
     * @throws JsonProcessingException when JSON processing fails
     */
    String getCurrentUserJwtJsonString() throws JsonProcessingException;
}
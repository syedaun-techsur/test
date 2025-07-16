package solutions.techsur.common.microservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Set;

/**
 * SecurityService provides methods to access information about the currently authenticated user,
 * including JWT token details, user ID, roles, and other user-related security information.
 */
public interface SecurityService {

    /**
     * Retrieves the JWT token of the current authenticated user.
     *
     * @return the JWT token object
     */
    Object getCurrentUserJwt();

    /**
     * Returns the user ID of the current authenticated user as a String.
     *
     * @return user ID as String
     */
    String getCurrentUserId();

    /**
     * Returns the set of roles assigned to the current authenticated user.
     *
     * @return a Set of role names
     */
    Set<String> getCurrentUserRoles();

    /**
     * Returns the Bearer token of the current authenticated user.
     *
     * @return Bearer token as String
     */
    String getCurrentUserBearerToken();

    /**
     * Returns the site ID associated with the current authenticated user.
     *
     * @return site ID as Long
     */
    Long getCurrentUserSiteId();

    /**
     * Returns the region ID associated with the current authenticated user.
     *
     * @return region ID as Long
     */
    Long getCurrentUserRegionId();

    /**
     * Returns the current user's JWT token as a JSON string representation.
     *
     * @return JWT as JSON string
     * @throws JsonProcessingException if JWT serialization fails
     */
    String getCurrentUserJwtJsonString() throws JsonProcessingException;
}
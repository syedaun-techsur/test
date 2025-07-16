package solutions.techsur.common.microservice.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for JWT handling in the context of Spring Security.
 */
public class JwtUtils {

    /**
     * Retrieves the current authenticated user's JWT token from the security context.
     *
     * @return Jwt token of the current user or null if not available.
     */
    public static Jwt getCurrentUserJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            return (Jwt) principal;
        }
        return null;
    }

    /**
     * Extracts user roles combining realm and client roles from the JWT.
     *
     * @param jwt the JWT token to extract roles from.
     * @return set of roles assigned to the user.
     */
    public static Set<String> getUserRoles(final Jwt jwt) {
        if (jwt == null) {
            return Collections.emptySet();
        }

        final Set<String> roles = new HashSet<>();

        final Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null) {
            @SuppressWarnings("unchecked")
            final List<String> realmRoles = (List<String>) realmAccess.getOrDefault("roles", Collections.emptyList());
            roles.addAll(realmRoles);
        }

        final String clientName = jwt.getClaimAsString("azp");
        final Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null && clientName != null) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.getOrDefault(clientName, Collections.emptyMap());
            if (clientAccess != null) {
                @SuppressWarnings("unchecked")
                final List<String> clientRoles = (List<String>) clientAccess.getOrDefault("roles", Collections.emptyList());
                roles.addAll(clientRoles);
            }
        }

        return roles;
    }

    /**
     * Returns the bearer token string from a JWT.
     *
     * @param jwt the JWT token.
     * @return the bearer token string.
     */
    public static String getBearerToken(final Jwt jwt) {
        if (jwt == null) {
            return "";
        }
        return "Bearer " + jwt.getTokenValue();
    }

    /**
     * Gets the user's subject (user ID) from JWT.
     *
     * @param jwt the JWT token.
     * @return user ID or null if jwt is null.
     */
    public static String getUserId(final Jwt jwt) {
        return jwt != null ? jwt.getSubject() : null;
    }

    /**
     * Gets the user site ID from the JWT claims.
     *
     * @param jwt the JWT token.
     * @return site ID or null if not present.
     */
    public static String getUserSiteId(final Jwt jwt) {
        return jwt != null ? jwt.getClaimAsString("siteId") : null;
    }

    /**
     * Gets the user region ID from the JWT claims.
     *
     * @param jwt the JWT token.
     * @return region ID or null if not present.
     */
    public static String getUserRegionId(final Jwt jwt) {
        return jwt != null ? jwt.getClaimAsString("regionId") : null;
    }

    /**
     * Gets the user ID if available, otherwise returns "SYSTEM".
     *
     * @return user ID or "SYSTEM" if no user JWT is found.
     */
    public static String getUserIdOrSystem() {
        Jwt jwt = getCurrentUserJwt();
        return jwt == null ? "SYSTEM" : getUserId(jwt);
    }

    /**
     * Gets the full name of the user from the JWT claims.
     *
     * @return formatted full name or fallback values if claims are missing.
     */
    public static String getUserFullName() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return "System";
        }
        final String familyName = jwt.getClaimAsString("family_name");
        final String givenName = jwt.getClaimAsString("given_name");

        if (familyName == null && givenName == null) {
            return "N/A";
        } else if (familyName == null) {
            return givenName;
        } else if (givenName == null) {
            return familyName;
        }
        return String.format("%s, %s", familyName, givenName);
    }

    /**
     * Gets the preferred username from the JWT claims.
     *
     * @return preferred username or fallback values if claims are missing.
     */
    public static String getUserName() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return "System";
        }
        final String username = jwt.getClaimAsString("preferred_username");
        return (username == null || username.isEmpty()) ? "N/A" : username;
    }
}
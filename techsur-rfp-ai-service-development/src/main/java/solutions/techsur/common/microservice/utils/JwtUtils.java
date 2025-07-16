package solutions.techsur.common.microservice.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JwtUtils {

    public static Jwt getCurrentUserJwt() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        final Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            return (Jwt) principal;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getUserRoles(final Jwt jwt) {
        if (jwt == null) {
            return Collections.emptySet();
        }

        final Map<String, Object> realm = jwt.getClaimAsMap("realm_access");
        final List<String> realmRoles = (realm != null) ? (List<String>) realm.getOrDefault("roles", Collections.emptyList())
                : Collections.emptyList();

        final Set<String> roles = new HashSet<>(realmRoles);

        final String clientName = jwt.getClaimAsString("azp");
        final Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null && clientName != null) {
            final Map<String, Object> client = (Map<String, Object>) resourceAccess.getOrDefault(clientName,
                    Collections.emptyMap());
            final List<String> clientRoles = (List<String>) client.getOrDefault("roles", Collections.emptyList());
            roles.addAll(clientRoles);
        }

        return roles;
    }

    public static String getBearerToken(final Jwt jwt) {
        if (jwt == null) {
            return "";
        }
        return "Bearer " + jwt.getTokenValue();
    }

    public static String getUserId(final Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        return jwt.getSubject();
    }

    public static String getUserSiteId(final Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        return jwt.getClaimAsString("siteId");
    }

    public static String getUserRegionId(final Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        return jwt.getClaimAsString("regionId");
    }

    public static String getUserIdOrSystem() {
        final Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return "SYSTEM";
        }
        return getUserId(jwt);
    }

    public static String getUserFullName() {
        final Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return "System";
        }
        final String familyName = jwt.getClaimAsString("family_name");
        final String givenName = jwt.getClaimAsString("given_name");

        if ((familyName == null || familyName.isEmpty()) && (givenName == null || givenName.isEmpty())) {
            return "N/A";
        }
        if (familyName == null || familyName.isEmpty()) {
            return givenName;
        }
        if (givenName == null || givenName.isEmpty()) {
            return familyName;
        }
        return familyName + ", " + givenName;
    }

    public static String getUserName() {
        final Jwt jwt = getCurrentUserJwt();
        if (jwt == null) {
            return "System";
        }
        final String username = jwt.getClaimAsString("preferred_username");
        if (username == null || username.isEmpty()) {
            return "N/A";
        }
        return username;
    }
}
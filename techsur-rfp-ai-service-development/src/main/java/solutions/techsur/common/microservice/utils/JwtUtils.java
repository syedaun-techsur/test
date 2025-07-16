package solutions.techsur.common.microservice.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public class JwtUtils {
	public static Jwt getCurrentUserJwt() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof Jwt) {
			return (Jwt) authentication.getPrincipal();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Set<String> getUserRoles(Jwt jwt) {
		// Realm Roles (applies to all clients)
		final Map<String, Object> realm = jwt.getClaimAsMap("realm_access");
		final List<String> realmRoles = (List<String>) realm.getOrDefault("roles", Collections.emptyList());

		final Set<String> roles = new HashSet<>(realmRoles);

		// Client roles (only the client currently being used)
		final String clientName = jwt.getClaimAsString("azp");
		final Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaimAsMap("resource_access");
		if (resourceAccess != null) {
			final Map<String, Object> client = (Map<String, Object>) resourceAccess.getOrDefault(clientName,
					Collections.emptyMap());
			final List<String> clientRoles = (List<String>) client.getOrDefault("roles", Collections.emptyList());
			roles.addAll(clientRoles);
		}

		return roles;
	}

	public static String getBearerToken(Jwt jwt) {
		String token = jwt.getTokenValue();
		return "Bearer " + token;
	}

	public static String getUserId(Jwt jwt) {
		return jwt.getSubject();
	}
	
    public static String getUserSiteId(Jwt jwt) {
        return  jwt.getClaimAsString("siteId");
    }
    
    public static String getUserRegionId(Jwt jwt) {
        return  jwt.getClaimAsString("regionId");
    }

	public static String getUserIdOrSystem() {
		Jwt jwt = getCurrentUserJwt();
		if (jwt == null) {
			return "SYSTEM";
		}
		return getUserId(jwt);
	}
	
	public static String getUserFullName() {
		Jwt jwt = getCurrentUserJwt();
		if (jwt == null) {
			return "System";
		}
		String familyName = jwt.getClaimAsString("family_name");
		String givenName = jwt.getClaimAsString("given_name");
		
		if (familyName == null && givenName == null) {
			return "N/A";
		}
		else {
			return String.format("%s, %s", familyName, givenName);
		}
	}

	public static String getUserName() {
		Jwt jwt = getCurrentUserJwt();
		if (jwt == null) {
			return "System";
		}
		String username = jwt.getClaimAsString("preferred_username");
		if (username == null) {
			return "N/A";
		}
		return username;
	}
}

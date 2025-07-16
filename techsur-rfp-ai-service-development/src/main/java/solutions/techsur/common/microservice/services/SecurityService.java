package solutions.techsur.common.microservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

public interface SecurityService {
	Jwt getCurrentUserJwt();

	String getCurrentUserId();

	Set<String> getCurrentUserRoles();

	String getCurrentUserBearerToken();

	Long getCurrentUserSiteId();

	Long getCurrentUserRegionId();

	String getCurrentUserJwtJsonString() throws JsonProcessingException;
}

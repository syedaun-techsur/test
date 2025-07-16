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
	ObjectMapper objectMapper;

	@Override
	public Jwt getCurrentUserJwt() {
		return JwtUtils.getCurrentUserJwt();
	}

	@Override
	public String getCurrentUserId() {
		Jwt jwt = getCurrentUserJwt();
		if (jwt == null) {
			return null;
		}
		return JwtUtils.getUserId(jwt);
	}
	
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
        return Long.valueOf(userSiteIdString);
    }
    
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
        return Long.valueOf(userRegionIdString);
    }

	@Override
	public Set<String> getCurrentUserRoles() {
		Jwt jwt = getCurrentUserJwt();
		if (jwt == null) {
			return Collections.emptySet();
		}

		return JwtUtils.getUserRoles(jwt);
	}

	@Override
	public String getCurrentUserBearerToken() {
		Jwt jwt = getCurrentUserJwt();
		if (jwt == null) {
			return null;
		}
		return JwtUtils.getBearerToken(jwt);
	}
	
	@Override
	public String getCurrentUserJwtJsonString() throws JsonProcessingException {
		Jwt jwt = getCurrentUserJwt();
		return objectMapper.writeValueAsString(jwt);
	}
}

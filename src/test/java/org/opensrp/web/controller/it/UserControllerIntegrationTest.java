package org.opensrp.web.controller.it;

import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.api.domain.Time;
import org.opensrp.domain.Location;
import org.smartregister.domain.User;
import org.opensrp.web.rest.it.BaseResourceTest;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;

public class UserControllerIntegrationTest extends BaseResourceTest {

	@Value("#{opensrp['openmrs.url']}")
	private String opensrpSiteUrl;

	@Value("#{opensrp['openmrs.username']}")
	private String rootUserName;

	@Value("#{opensrp['openmrs.password']}")
	private String rootPassword;

	@Test
	public void setUp() throws Exception {
	/*	String url = "http://192.168.22.152:8080/openmrs/loginServlet";
		Map<String, String> parameterForRootLogIn = new HashMap<>();
		parameterForRootLogIn.put("uname", rootUserName);
		parameterForRootLogIn.put("pw", rootPassword);
		MvcResult result = postCallWithFormUrlEncode(url, parameterForRootLogIn, status().isOk());
		Cookie[] cookies = result.getResponse().getCookies();
		sessionCookie = result.getResponse().getCookie("JSESSIONID").getValue();
		System.out.println(sessionCookie);*/
	}

	@Test
	@Ignore
	public void shouldAuthenticateUserWithValidUsernameAndPassword() throws Exception {
		String url = "/security/authenticate";
		JsonNode returnedJsonNode = postCallWithBasicAuthorizationHeader(url, "sumon", "Sumon@123", status().isOk());
		System.out.println(returnedJsonNode);
		mapper.treeToValue(returnedJsonNode.get("user"), User.class);
		mapper.treeToValue(returnedJsonNode.get("locations"), Location.class);
		mapper.treeToValue(returnedJsonNode.get("team"), Map.class);
		mapper.treeToValue(returnedJsonNode.get("time"), Time.class);
	}
}

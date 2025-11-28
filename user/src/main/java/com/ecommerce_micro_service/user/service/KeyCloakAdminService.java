package com.ecommerce_micro_service.user.service;

import com.ecommerce_micro_service.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KeyCloakAdminService {

    @Value("${keycloak.admin.username}")
    private String adminUsername;
    @Value("${keycloak.admin.password}")
    private String adminPassword;
    @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl;
    @Value("${keycloak.admin.realm}")
    private String realm;
    @Value("${keycloak.admin.client-id}")
    private String clientId;
    @Value("${keycloak.admin.client-uid}")
    private String clientUid;

    private final RestTemplate restTemplate=new RestTemplate();

    public String getAdminAccessToken(){
        MultiValueMap<String,String>params=new LinkedMultiValueMap<>();
        params.add("client_id",clientId);
        params.add("username",adminUsername);
        params.add("password",adminPassword);
        params.add("grant_type","password");

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //http:localhost:8443/realms/ecom-app/protocol/openid-connect/token
        HttpEntity<MultiValueMap<String,String>> entity=
                new HttpEntity<>(params,headers);
        ResponseEntity<Map> response=restTemplate.postForEntity(
                "http://keycloak:8080/realms/Ecom-App/protocol/openid-connect/token",
                entity,
                Map.class
        );
        return (String)response.getBody().get("access_token");
    }

    public String createUser(String token, UserRequest userRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", userRequest.getUsername());
        userPayload.put("email", userRequest.getEmail());
        userPayload.put("enabled", true);
        userPayload.put("firstName", userRequest.getFirstName());
        userPayload.put("lastName", userRequest.getLastName());

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", userRequest.getPassword());
        credential.put("temporary", false);

        userPayload.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userPayload, headers);

        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users";

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                entity,
                String.class
        );

        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            throw new RuntimeException("Failed to create user in keycloak " + response.getBody());
        }

        // Extract Keycloak user id
        URI location = response.getHeaders().getLocation(); // in response header while creating user from keycloak in location you will get keycloak-id
        if (location == null) {
            throw new RuntimeException("Keycloak did not return Location Header " + response.getBody());
        }

        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public Map<String,Object> getClientRolesRepresentation(String token, String roleName) {
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity=new HttpEntity<>(headers);
        ResponseEntity<Map>response=restTemplate.exchange(
                keycloakServerUrl+"/admin/realms/"+realm+"/clients/"+clientUid+"/roles/"+roleName,
                HttpMethod.GET,
                entity,
                Map.class
        );
        //System.out.println(response.getBody()); // dataType Map<String,Object>
        if(!HttpStatus.OK.equals(response.getStatusCode())){
            throw new RuntimeException("Failed to fetch role representation from Keycloak");
        }
        return response.getBody();
    }

    public Void assignClientRoleToUser(String username, String roleName, String userId){
        String token=getAdminAccessToken();
        Map<String,Object> roleRepresentation=getClientRolesRepresentation(token,roleName);
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<List<Map<String,Object>>> entity=new HttpEntity<>(List.of(roleRepresentation),headers);
        ResponseEntity<Void> response=restTemplate.postForEntity(
                keycloakServerUrl+"/admin/realms/"+realm+"/users/"+userId+"/role-mappings/clients/"+clientUid,
                entity,
                Void.class
        );
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("Failed to assign role to user in Keycloak");
        }
        return null;
    }


}

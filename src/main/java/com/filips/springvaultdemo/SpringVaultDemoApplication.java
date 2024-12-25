package com.filips.springvaultdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultResponseSupport;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringVaultDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringVaultDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var endpoint = new VaultEndpoint();
        endpoint.setScheme("http");
        endpoint.setHost("127.0.0.1");
        endpoint.setPort(8200);
        VaultTemplate vaultTemplate = new VaultTemplate(endpoint,
                new TokenAuthentication("<INSERT TOKEN>"));

        Map<String, Map<String,?>> data = new HashMap<>();
        data.put("data", Map.of("username", "world", "password", 777));

        // Write the secrets to Vault
        vaultTemplate.write("secret/data/myapp", data);

        // Read the data, note that Vault returns a nested structure for the "data" field
        VaultResponse response = vaultTemplate.read("secret/data/myapp");

        // Extract 'data' from the response, which holds the actual secret
        Object secretData = response.getData().get("data");

        System.out.println(secretData);
    }
}

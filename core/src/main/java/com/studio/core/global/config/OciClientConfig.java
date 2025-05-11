package com.studio.core.global.config;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
@Configuration
public class OciClientConfig {

    private ConfigFileReader.ConfigFile config;

    @Bean
    public ObjectStorageClient objectStorageClient() throws Exception {
        // Load config from classpath
        try (InputStream configInputStream = new ClassPathResource("oci/config").getInputStream()) {
            config = ConfigFileReader.parse(configInputStream, "DEFAULT");
        }

        // Copy private key from classpath to temp file
        String keyFileName = config.get("key_file");
        ClassPathResource keyResource = new ClassPathResource("oci/" + keyFileName);
        File tempKeyFile = File.createTempFile("oci-private-key", ".pem");
        tempKeyFile.deleteOnExit(); // Clean up after app shutdown

        try (InputStream in = keyResource.getInputStream();
            OutputStream out = new FileOutputStream(tempKeyFile)) {
            in.transferTo(out);
        }

        AuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
            .tenantId(config.get("tenancy"))
            .userId(config.get("user"))
            .fingerprint(config.get("fingerprint"))
            .privateKeySupplier(() -> {
                try {
                    return new FileInputStream(tempKeyFile);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to reload RSA key", e);
                }
            })
            .region(Region.fromRegionId(config.get("region")))
            .build();

        ObjectStorageClient client = new ObjectStorageClient(provider);
        client.setRegion(Region.fromRegionId(config.get("region")));
        return client;
    }

    @Bean
    public String regionId() throws Exception {
        if (config == null) {
            try (InputStream configInputStream = new ClassPathResource("oci/config").getInputStream()) {
                config = ConfigFileReader.parse(configInputStream, "DEFAULT");
            }
        }
        return config.get("region");
    }
}

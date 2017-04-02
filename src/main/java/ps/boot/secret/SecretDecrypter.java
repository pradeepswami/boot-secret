package ps.boot.secret;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import ps.boot.secret.util.NullEmptyCheck;

public class SecretDecrypter implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final String SECRET_OVERRIDE = "-SecretDecryterOverride";

	public static final String SECRET = "{secret}";

	private static final Logger LOG = LoggerFactory.getLogger(SecretDecrypter.class);

	public static final String CRYPTO_ALIAS = "crypto.alias";
	public static final String CRYPTO_STOREPASS = "crypto.storePassword";
	public static final String CRYPTO_KEYPASS = "crypto.keyPassword";
	public static final String CRYPTO_KEYSTORE = "crypto.keystore";

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {

		processProperties(applicationContext);

	}

	protected void processProperties(ConfigurableApplicationContext applicationContext) {
		RSACrypto rsaCrypto = createCrypto(applicationContext);
		if (rsaCrypto == null) {
			return;
		}
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		for (PropertySource<?> propertySource : environment.getPropertySources()) {
			Map<String, Object> propertyOverrides = new LinkedHashMap<>();
			if (propertySource instanceof MapPropertySource) {
				MapPropertySource mapProp = (MapPropertySource) propertySource;
				decryptSecret(propertyOverrides, mapProp.getSource(), rsaCrypto);
			}
			if (!propertyOverrides.isEmpty()) {
				MapPropertySource mapPropertySource = new MapPropertySource(propertySource.getName(), propertyOverrides);
				environment.getPropertySources().addBefore(propertySource.getName() + SECRET_OVERRIDE,
						mapPropertySource);
			}

		}
	}

	void decryptSecret(final Map<String, Object> propertyOverrides, final Map<String, Object> source,
			RSACrypto rsaCrypto) {
		for (Map.Entry<String, Object> map : source.entrySet()) {
			if (map.getValue() instanceof String && ((String) map.getValue()).toLowerCase().contains(SECRET)) {
				LOG.info("Found cipher string for key -> {}", map.getKey());
				String cipherStr = ((String) map.getValue()).replace(SECRET, "");
				propertyOverrides.put(map.getKey(), rsaCrypto.decrypt(cipherStr));
			}
		}
	}

	RSACrypto createCrypto(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment environment = applicationContext.getEnvironment();

		String keystoreLocation = environment.getProperty(CRYPTO_KEYSTORE);
		String keypass = environment.getProperty(CRYPTO_KEYPASS);
		String storepass = environment.getProperty(CRYPTO_STOREPASS);
		String alias = environment.getProperty(CRYPTO_ALIAS);

		boolean containEmpty = NullEmptyCheck.create().check(storepass).check(keystoreLocation).check(alias)
				.check(keypass).containEmpty();

		if (containEmpty) {
			LOG.warn("One or many Crypto params are not configured. Configure {}, {}, {} & {}", CRYPTO_ALIAS,
					CRYPTO_KEYPASS, CRYPTO_KEYSTORE, CRYPTO_STOREPASS);
			return null;
		}

		Resource resource = applicationContext.getResource(keystoreLocation);

		KeystoreAdapter keystoreAdapter;
		try {
			keystoreAdapter = new KeystoreAdapter(alias, resource.getInputStream(), storepass.toCharArray(),
					keypass.toCharArray());
		} catch (IOException e) {
			throw new RuntimeException("Exception reading keystoer " + keystoreLocation, e);
		}
		return new RSACryptoImpl(keystoreAdapter);
	}
}

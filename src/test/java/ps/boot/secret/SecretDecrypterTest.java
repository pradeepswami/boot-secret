package ps.boot.secret;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@RunWith(MockitoJUnitRunner.class)
public class SecretDecrypterTest {

	private static final String KEY_XYZ_PASSWORD = "xyz.password";

	@Spy
	private SecretDecrypter secretDecrypter;

	@Mock
	private RSACrypto rsaCrypto;

	@Mock
	private ConfigurableApplicationContext mockApplicationContext;

	@Mock
	private ConfigurableEnvironment configurableEnvironment;

	@Test
	public void decryptSecret_withsecret() throws Exception {
		when(rsaCrypto.decrypt("somecipherString")).thenReturn("pwd string");
		Map<String, Object> overrides = new HashMap<>();
		Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put(KEY_XYZ_PASSWORD, SecretDecrypter.SECRET + "somecipherString");

		secretDecrypter.decryptSecret(overrides, sourceMap, rsaCrypto);

		assertThat(overrides.get(KEY_XYZ_PASSWORD), equalTo("pwd string"));

	}

	@Test
	public void decryptSecret_withoutsecret() throws Exception {
		Map<String, Object> overrides = new HashMap<>();
		Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put(KEY_XYZ_PASSWORD, "someString");

		secretDecrypter.decryptSecret(overrides, sourceMap, rsaCrypto);

		assertTrue(overrides.isEmpty());

	}

	@Test
	public void processProperties_withsecret() throws Exception {
		doReturn(rsaCrypto).when(secretDecrypter).createCrypto(Matchers.any());
		when(rsaCrypto.decrypt("somecipherString")).thenReturn("pwd string");
		when(mockApplicationContext.getEnvironment()).thenReturn(configurableEnvironment);

		secretDecrypter.processProperties(mockApplicationContext);

	}
}

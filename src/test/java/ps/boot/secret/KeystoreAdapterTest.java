package ps.boot.secret;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ps.boot.secret.KeystoreAdapter;

@RunWith(MockitoJUnitRunner.class)
public class KeystoreAdapterTest {

	private static final String SAMPLE = "sample";
	private KeystoreAdapter keystoreAdapter;

	@Test
	public void getPublicKey_when_typical() {
		keystoreAdapter = new KeystoreAdapter(SAMPLE, this.getClass().getResourceAsStream("/sample.jks"),
				SAMPLE.toCharArray(), SAMPLE.toCharArray());

		PublicKey publicKey = keystoreAdapter.getPublicKey();
		PrivateKey privateKey = keystoreAdapter.getPrivateKey();

		assertThat(publicKey, is(notNullValue()));
		assertThat(privateKey, is(notNullValue()));
	}

}

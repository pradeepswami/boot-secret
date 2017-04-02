package ps.boot.secret;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import ps.boot.secret.KeystoreAdapter;
import ps.boot.secret.RSACryptoImpl;

public class RSACryptoImplTest {

	private RSACrypto secretEncryptDecrypt;
	private static final String SAMPLE = "sample";
	private static final String CIPHER = "RDk52cjoaFYV48+M6gOFMQrN4uFEzc4wiqgfyMCbCwDB2l0B94r90nYw+D3cnO8E0CsLAKkt55+zZS4WJNK0qrvyG5fRdbyLvwaEZyqH1wIr3tT5udLsZPCg7FFFsiA0xzjA36Jq5mU4t8UgV2e80ukikZ8zOL22e3KxCEX0AiAj7bQP6/mCZe+v21jIE7+rRlGfD0nNezRetsuZxqrG5EZVHKBZ2CfSV+3PSvG1uudbp1uKUo/VcBaiY0Ta4hKdBBFx58CFnNZhthzTE93cq58y3BgPUdtcZ28fKR4ffEENavH5VLimH4BSq4f5TqH5i7y1YFDj+DXuQaDTpFv4yw==";

	@Test
	public void encrypt_withvalidkey() {
		KeystoreAdapter adapter = new KeystoreAdapter(SAMPLE, this.getClass().getResourceAsStream("/sample.jks"),
				SAMPLE.toCharArray(), SAMPLE.toCharArray());
		secretEncryptDecrypt = new RSACryptoImpl(adapter);

		String cypher = secretEncryptDecrypt.encrypt("secret message");
		assertThat(cypher, is(notNullValue()));
	}

	@Test
	public void decrypt_withvalidkey() {
		KeystoreAdapter adapter = new KeystoreAdapter(SAMPLE, this.getClass().getResourceAsStream("/sample.jks"),
				SAMPLE.toCharArray(), SAMPLE.toCharArray());
		secretEncryptDecrypt = new RSACryptoImpl(adapter);

		String text = secretEncryptDecrypt.decrypt(CIPHER);

		assertThat(text, equalTo("secret message"));
	}

}

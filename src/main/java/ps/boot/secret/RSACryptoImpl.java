package ps.boot.secret;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACryptoImpl implements RSACrypto {

	private static final String ALGO = "RSA";
	private KeystoreAdapter keystoreAdapter;

	public RSACryptoImpl(KeystoreAdapter keystoreAdapter) {
		this.keystoreAdapter = keystoreAdapter;
	}

	/* (non-Javadoc)
	 * @see ps.boot.secret.RSACryptoI#encrypt(java.lang.String)
	 */
	@Override
	public String encrypt(String secret) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, keystoreAdapter.getPublicKey());
			byte[] encrypted = cipher.doFinal(secret.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Unable to encrypt", e);
		}
	}

	/* (non-Javadoc)
	 * @see ps.boot.secret.RSACryptoI#decrypt(java.lang.String)
	 */
	@Override
	public String decrypt(String cypher) {
		try {
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, keystoreAdapter.getPrivateKey());
			byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(cypher));
			return new String(decrypt);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Unable to decrypt cypher text " + cypher, e);
		}
	}

}

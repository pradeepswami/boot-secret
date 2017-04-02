package ps.boot.secret;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class KeystoreAdapter {

	private String alias;
	private InputStream keystore;
	private char[] storePassword;
	private char[] keyPassword;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public KeystoreAdapter(String alias, InputStream keystore, char[] storePassword, char[] keyPassword) {
		this.alias = alias;
		this.keystore = keystore;
		this.storePassword = storePassword;
		this.keyPassword = keyPassword;
		init();
	}

	PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	protected void init() {
		try {
			KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
			store.load(keystore, storePassword);
			privateKey = (PrivateKey) store.getKey(this.alias, this.keyPassword);
			publicKey = store.getCertificate(this.alias).getPublicKey();
		} catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| UnrecoverableKeyException e) {
			throw new RuntimeException("Exception loading keys from jks store", e);
		}
	}

}

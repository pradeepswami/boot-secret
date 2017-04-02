package ps.boot.secret;

public interface RSACrypto {

	public abstract String encrypt(String secret);

	public abstract String decrypt(String cypher);

}
package ps.boot.secret.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ps.boot.secret.util.NullEmptyCheck;

public class NullEmptyCheckTest {

	@Test
	public void testNullEmptyCheck() {
		assertFalse(NullEmptyCheck.create().check("test").check("test2").containEmpty());

	}

	@Test
	public void testNullEmptyCheck_withNull() {
		assertTrue(NullEmptyCheck.create().check("test").check(null).containEmpty());

	}

	@Test
	public void testNullEmptyCheck_withEmpty() {
		assertTrue(NullEmptyCheck.create().check("test").check("").containEmpty());

	}

}

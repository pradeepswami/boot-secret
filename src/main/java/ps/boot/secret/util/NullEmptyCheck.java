package ps.boot.secret.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class NullEmptyCheck {

	private List<Boolean> emptyList = new ArrayList<>();

	private NullEmptyCheck() {
	}

	public static NullEmptyCheck create() {
		return new NullEmptyCheck();
	}

	public NullEmptyCheck check(String str) {
		emptyList.add(!StringUtils.isEmpty(str));
		return this;
	}

	public boolean containEmpty() {
		if (emptyList.isEmpty()) {
			return true;
		}

		return emptyList.contains(Boolean.FALSE);
	}

}

package com.jive.twitter.oauth.utils;

import com.jive.twitter.utils.Constants;

public class TimeStampGenerator {
	public interface TimestampGenerator {
		public String get();
	}

	static class OAuthTimestampGenerator implements TimestampGenerator {

		@Override
		public String get() {
			return String.valueOf(System.currentTimeMillis() / 1000);
		}

	}

	static class DummyTimestampGenerator implements TimestampGenerator {
		@Override
		public String get() {
			return "1386658844";
		}
	}

	static TimestampGenerator instance = new OAuthTimestampGenerator();
	static {
		if (Constants.debugSignatureGen) {
			instance = new DummyTimestampGenerator();
		}
	}

	public static TimestampGenerator get() {
		return instance;
	}
}

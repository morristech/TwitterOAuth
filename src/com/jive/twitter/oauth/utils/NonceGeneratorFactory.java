package com.jive.twitter.oauth.utils;

import java.util.Random;

import com.jive.twitter.utils.Constants;

public class NonceGeneratorFactory {
	public interface NonceGenerator {
		public String get();
	}

	static class OAuthNonceGenerator implements NonceGenerator {
		private static Random RAND = new Random();
		@Override
		public String get() {
			long timestamp = System.currentTimeMillis();
			final long nonce = timestamp + RAND.nextInt();
			return String.valueOf(nonce);
		}

	}

	static class DummyNonceGenerator implements NonceGenerator {
		@Override
		public String get() {
			return "abcdefghi";
		}
	}

	static NonceGenerator instance = new OAuthNonceGenerator();

	static {
		if (Constants.debugSignatureGen) {
			instance = new DummyNonceGenerator();
		}
	}

	public static NonceGenerator get() {
		return instance;
	}
}

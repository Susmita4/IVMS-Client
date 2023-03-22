package com.videonetics.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHAUtility {

	private static final String UPPER = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
	private static final String LOWER = "abcdefghijklmnpqrstuvwxyz";
	private static final String NUMBER = "123456789";
	private static final String SPECIAL = "!@#$%&*+?";
	private static SecureRandom randGen = new SecureRandom();

	public static String encrypt512(String input) throws Exception {
		try {
			// getInstance() method is called with algorithm SHA-512
			MessageDigest md = MessageDigest.getInstance("SHA-512");

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			// Add preceding 0s to make it 32 bit
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			// return the HashText
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e.getMessage(), e.getCause());
		}
	}

	private static String encrypt256(String input) throws Exception {
		try {
			// getInstance() method is called with algorithm SHA-512
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			// Add preceding 0s to make it 32 bit
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			// return the HashText
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e.getMessage(), e.getCause());
		}
	}

	private static String generateAuthCookie() {
		randGen = new SecureRandom();
		StringBuffer buf = new StringBuffer();
		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));
		for (int i = 0; i <= 4; i++) {
			buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		}
		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));

		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));

		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));

		return buf.toString();
	}

	public static int abs(int x){
	    if (x >= 0) {
	        return x;
	    }
	    return -x;
	}
	private static String generateSalt() {

		randGen = new SecureRandom();
		StringBuffer buf = new StringBuffer();
		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));
		for (int i = 0; i <= 4; i++) {
			buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		}
		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));

		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));

		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));

		return buf.toString();
	}

	private static String generatePassword() {

		randGen = new SecureRandom();
		StringBuffer buf = new StringBuffer();
		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(NUMBER.charAt(abs(randGen.nextInt()) % NUMBER.length()));
		for (int i = 0; i <= 4; i++) {
			buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		}
		buf.append(UPPER.charAt(abs(randGen.nextInt()) % UPPER.length()));
		buf.append(LOWER.charAt(abs(randGen.nextInt()) % LOWER.length()));
		buf.append(SPECIAL.charAt(abs(randGen.nextInt()) % SPECIAL.length()));

		return buf.toString();
	}



}

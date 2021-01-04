package com.yonglilian.common.util;

public final class Base64 {
	private Base64() {
	}

	public static final char[] encode(byte arg[]) {
		return encode(arg, 0, arg.length);
	}

	public static final byte[] decode(String arg) {
		return decode(arg.toCharArray());
	}

	public static final byte[] decode(char arg[]) {
		return decode(arg, 0, arg.length);
	}

	public static char[] encode(byte value[], int startIndex, int endIndex) {
		if (value == null || value.length < 1 || startIndex >= endIndex || startIndex < 0 || endIndex > value.length)
			return new char[0];
		int length = ((endIndex - startIndex) / 3) * 4;
		int n = (endIndex - startIndex) % 3;
		if (n != 0)
			length += 4;
		n = endIndex - n;
		char buf[] = new char[length];
		int i = startIndex;
		for (length = 0; i < n; length += 4) {
			int b1 = value[i];
			int b2 = value[i + 1];
			int b3 = value[i + 2];
			buf[length] = BASE64_CHAR[b1 >> 2 & 0x3f];
			buf[length + 1] = BASE64_CHAR[b1 << 4 & 0x30 | b2 >> 4 & 0xf];
			buf[length + 2] = BASE64_CHAR[b2 << 2 & 0x3c | b3 >> 6 & 3];
			buf[length + 3] = BASE64_CHAR[b3 & 0x3f];
			i += 3;
		}
		switch (endIndex - n) {
		case 1: // '\001'
		{
			int b1 = value[n];
			buf[length] = BASE64_CHAR[b1 >> 2 & 0x3f];
			buf[length + 1] = BASE64_CHAR[b1 << 4 & 0x30];
			buf[length + 2] = '=';
			buf[length + 3] = '=';
			break;
		}
		case 2: // '\002'
		{
			int b1 = value[n];
			int b2 = value[n + 1];
			buf[length] = BASE64_CHAR[b1 >> 2 & 0x3f];
			buf[length + 1] = BASE64_CHAR[b1 << 4 & 0x30 | b2 >> 4 & 0xf];
			buf[length + 2] = BASE64_CHAR[b2 << 2 & 0x3c];
			buf[length + 3] = '=';
			break;
		}
		}
		return buf;
	}

	public static byte[] decode(char value[], int startIndex, int endIndex) {
		if (value == null || value.length < 1 || startIndex >= endIndex || startIndex < 0 || endIndex > value.length
				|| value[startIndex] == '=')
			return new byte[0];
		while (value[--endIndex] == '=')
			;
		endIndex++;
		int length = (endIndex - startIndex) % 4;
		if (length > 0)
			length--;
		length += ((endIndex - startIndex) / 4) * 3;
		byte buf[] = new byte[length];
		int n = endIndex - (endIndex - startIndex) % 4;
		int i = startIndex;
		for (length = 0; i < n; length += 3) {
			int b1 = BASE64_BYTE[value[i] & 0x7f];
			int b2 = BASE64_BYTE[value[i + 1] & 0x7f];
			int b3 = BASE64_BYTE[value[i + 2] & 0x7f];
			int b4 = BASE64_BYTE[value[i + 3] & 0x7f];
			buf[length] = (byte) (b1 << 2 | b2 >> 4);
			buf[length + 1] = (byte) (b2 << 4 & 0xf0 | b3 >> 2);
			buf[length + 2] = (byte) (b3 << 6 & 0xc0 | b4);
			i += 4;
		}
		switch (endIndex - n) {
		case 2: // '\002'
		{
			int b1 = BASE64_BYTE[value[i] & 0x7f];
			int b2 = BASE64_BYTE[value[i + 1] & 0x7f];
			buf[length] = (byte) (b1 << 2 | b2 >> 4);
			break;
		}
		case 3: // '\003'
		{
			int b1 = BASE64_BYTE[value[i] & 0x7f];
			int b2 = BASE64_BYTE[value[i + 1] & 0x7f];
			int b3 = BASE64_BYTE[value[i + 2] & 0x7f];
			buf[length] = (byte) (b1 << 2 | b2 >> 4);
			buf[length + 1] = (byte) (b2 << 4 & 0xf0 | b3 >> 2);
			break;
		}
		}
		return buf;
	}

	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X03 = 3;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X04 = 4;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X06 = 6;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X0F = 15;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X30 = 48;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X3C = 60;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X3F = 63;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0X7F = 127;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0XC0 = 192;
	@SuppressWarnings("unused")
	private static final int MAGIC_NUM_0XF0 = 240;
	private static final char BASE64_CHAR[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };
	private static final byte BASE64_BYTE[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1,
			-1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,
			30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1,
			-1 };
}
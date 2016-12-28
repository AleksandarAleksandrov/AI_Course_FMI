package util;

import java.nio.CharBuffer;

public class CustomUtils {

	public static String spaces(int spaces) {
		return CharBuffer.allocate(spaces).toString().replace('\0', ' ');
	}

}

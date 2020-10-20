package functions;

public class FileHandleUtil {
	/**
	 * @brief	指定されたフォルダがフォルダ形式ではない場合にフォルダ形式に変更する
	 * @param	フォルダ名
	 * @return	フォルダ形式の文字列
	 */
	public static String fixToDirFormat(String dir) {
		int index = dir.lastIndexOf("\\");

		return index == dir.length() ? dir : dir + "\\";
	}
}

package functions;

import java.io.File;
import java.util.ArrayList;

/**
 * @brief フォルダ内のファイル一覧を取得する
 *
 */
public class GetFiles {
	/**
	 * @brief	現在のフォルダ内から特定の拡張子のファイル一覧を取得
	 * @param	対象のフォルダ
	 * @param	拡張子
	 * @return	拡張子に一致するファイル一覧
	 */
	public static ArrayList<File> getSpecificFiles(String dir, String format) {
		ArrayList<File> fileList = new ArrayList<File>();
		File folder = new File(dir);
		File[] list = folder.listFiles();

		for (File file : list) {
			if (!isFormat(file.getName(), format)) continue;
			fileList.add(file);
		}

		return fileList;
	}

	/**
	 * @brief	ファイルの拡張子が一致しているか
	 * @param	ファイル名
	 * @param	拡張子
	 * @return	一致している
	 */
	private static boolean isFormat(String fileName, String format) {
		int extentionPlace = fileName.lastIndexOf(".");
		String extention = fileName.substring(extentionPlace+1, fileName.length());
		return format.equals(extention);
	}

	/**
	 * @brief	サブフォルダも含めてフォルダ内のファイルをすべて取得
	 * @param	対象のフォルダ
	 * @return	ファイル一覧
	 */
	public static ArrayList<File> getAllFiles(String dir) {
		ArrayList<File> fileList = new ArrayList<File>();
		File folder = new File(dir);
		File[] list = folder.listFiles();

		for(File file : list) {
			if (file.isDirectory()) {
				ArrayList<File> tempFileList = getAllFiles(file.getAbsolutePath());
				for(File tempFile : tempFileList) {
					fileList.add(tempFile);
				}
			} else {
				fileList.add(file);
			}
		}

		return fileList;
	}
}

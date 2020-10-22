package functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @brief	ファイルを対象フォルダでzipに変更する
 */
public class ChangeToZip {
	// 保存先のフォルダ
	private String destinationDir;

	public ChangeToZip() {}

	public ChangeToZip(String destinationDir) {
		this.destinationDir = FileHandleUtil.fixToDirFormat(destinationDir);
	}

	public void setDestinationDir(String destinationDir) {
		this.destinationDir = FileHandleUtil.fixToDirFormat(destinationDir);
	}

	/**
	 * docxファイルを保存先にzipファイルとして保存する
	 * @param	docxファイルのリスト
	 */
	public void changeDocxToZip(ArrayList<File> fileList) {
		this.copyToDestination(fileList);
		this.renameToZip();
	}

	/**
	 * ファイルを保存先にコピーする
	 * @param	コピーするファイルのリスト
	 */
	private void copyToDestination(ArrayList<File> fileList) {
		for (File file : fileList) {
			try {
				Path basePath = Paths.get(file.getAbsolutePath());
				Path destPath = Paths.get(this.destinationDir + file.getName());

				Files.copy(basePath, destPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * docxをzipに変更する
	 */
	private void renameToZip() {
		ArrayList<File> fileList = GetFiles.getSpecificFiles(this.destinationDir, "docx");

		for(File file : fileList) {
			String filePath = file.getAbsolutePath();
			int index = filePath.lastIndexOf(".");
			String withoutExtention = filePath.substring(0, index);
			File zipFile = new File(withoutExtention+".zip");

			file.renameTo(zipFile);
		}
	}
}

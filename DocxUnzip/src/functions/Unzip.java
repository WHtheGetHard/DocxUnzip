package functions;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.openqa.selenium.io.Zip;

public class Unzip {
	// zipファイルが保存されているフォルダ
	private String zipDir;

	public Unzip() {}

	public Unzip(String zipDir) {
		this.zipDir = FileHandleUtil.fixToDirFormat(zipDir);
	}

	public void setZipDir(String zipDir) {
		this.zipDir = FileHandleUtil.fixToDirFormat(zipDir);
	}

	/**
	 * @brief	フォルダ内のzipファイルを解凍する
	 */
	public void execUnzip() {
		ArrayList<File> fileList = GetFiles.getSpecificFiles(this.zipDir, "zip");

		for(File file : fileList) {
			String fileName = file.getAbsolutePath();
			int index = fileName.lastIndexOf(".");
			String unzipDir = fileName.substring(0, index) + "\\";
			try (FileInputStream fs = new FileInputStream(fileName)) {
				Zip.unzip(fs, new File(unzipDir));
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

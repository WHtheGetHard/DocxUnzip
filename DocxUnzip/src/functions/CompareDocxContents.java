package functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import fieldsformats.ExpectAndIgnore;

public class CompareDocxContents {
	private static ExpectAndIgnore expectAndIgnore = new ExpectAndIgnore("C:\\JavaDevs\\DocxUnzip\\initFile\\ExpectAndIgnore.json");
	/**
	 * 2つのフォルダ内のdocxを解凍したフォルダのxml同士を比較
	 * @param	基準となるフォルダ
	 * @param	比較するフォルダ
	 * @return	比較結果
	 */
	public static String compDir(String baseDir, String compDir) {
		File base = new File(baseDir);
		File[] baseFileList = base.listFiles();

		File comp = new File(compDir);
		File[] compFileList = comp.listFiles();

		int baseListSize = baseFileList.length;
		int compListSize = compFileList.length;
		boolean unMatch = false;
		StringBuilder unMatchContents = new StringBuilder("フォルダ同士の比較結果\n");
		if (baseListSize != compListSize) {
			unMatch = true;
			unMatchContents.append("フォルダ内のフォルダ(docx)数が違います\n");
		}

		if (unMatch) {
			for (File baseFile : baseFileList) {
				for (File compFile : compFileList) {
					if (!baseFile.getName().equals(compFile.getName())) continue;
					unMatchContents.append(baseFile.getName() + "フォルダの比較結果\n");
					String compResult = compFile(baseFile, compFile);
					if (compResult == null) {
						unMatchContents.append("差異なし\n");
						unMatchContents.append("差異あり\n");
					} else {
						unMatchContents.append(compResult);
					}
				}
			}
		} else {
			for (int i = 0; i < baseListSize; ++i) {
				unMatchContents.append(baseFileList[i].getName() + "フォルダの比較結果\n");
				String compResult = compFile(baseFileList[i], compFileList[i]);
				if (compResult == null) {
					unMatchContents.append("差異なし\n");
				} else {
					unMatchContents.append("差異あり\n");
					unMatchContents.append(compResult);
				}
			}
		}

		if (unMatch) {
			return unMatchContents.toString();
		} else {
			unMatchContents.append("差異なし");
			return unMatchContents.toString();
		}
	}

	/**
	 * docxを解凍したフォルダ単位でxml同士を比較
	 * @param	基準となるファイル
	 * @param	比較するファイル
	 * @return	比較結果の文字列
	 */
	public static String compFile(File baseFile, File compFile) {
		ArrayList<File> baseFileList = GetFiles.getAllFiles(baseFile.getAbsolutePath());
		ArrayList<File> compFileList = GetFiles.getAllFiles(compFile.getAbsolutePath());

		int baseFileNumber = baseFileList.size();
		int compFileNumber = compFileList.size();

		boolean unMatch = false;
		StringBuilder unMatchContents = new StringBuilder();
		if (baseFileNumber != compFileNumber) {
			unMatch = true;
			unMatchContents.append("ファイル数が一致していません\n");
		}

		if (unMatch) {
			for (File baseXml : baseFileList) {
				for (File compXml : compFileList) {
					if (!baseXml.getName().equals(compXml.getName())) continue;
					if (expectAndIgnore.getIgnoreFiles().contains(baseXml.getName())) continue;
					//if (baseXml.getName().equals("document.xml")) continue;

					ArrayList<String> baseTexts = getFileString(baseXml);
					ArrayList<String> compTexts = getFileString(compXml);

					String textCompResult = compTexts(baseTexts, compTexts);
					if (textCompResult == null) continue;
					unMatchContents.append(baseXml.getName() + "ファイルの差異\n");
					unMatch = true;
					unMatchContents.append(textCompResult);
				}
			}
		} else {
			for(int fileCounter = 0; fileCounter < baseFileNumber; ++fileCounter) {
				if (expectAndIgnore.getIgnoreFiles().contains(baseFileList.get(fileCounter).getName())) continue;
				//if (baseFileList.get(fileCounter).getName().equals("document.xml")) continue;
				ArrayList<String> baseTexts = getFileString(baseFileList.get(fileCounter));
				ArrayList<String> compTexts = getFileString(compFileList.get(fileCounter));

				String textCompResult = compTexts(baseTexts, compTexts);
				if (textCompResult == null) continue;
				unMatchContents.append(baseFileList.get(fileCounter).getName() + "ファイルの差異\n");
				unMatch = true;
				unMatchContents.append(textCompResult);
			}
		}

		return unMatch ? unMatchContents.toString() : null;
	}

	/**
	 * xmlファイルのテキストを">"単位の文字列のリストにする
	 * @param	文字列リストを生成するファイル
	 * @return	">"単位で区切った文字列リスト
	 */
	private static ArrayList<String> getFileString(File file) {
		ArrayList<String> textLines = new ArrayList<String>();
		Path filePath = Paths.get(file.getAbsolutePath());
		String text = "";
		try {
			text = Files.readString(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] splitTexts = text.split(">");
		for(String splitText : splitTexts) {
			textLines.add(splitText+">");
		}

		return textLines;
	}

	/**
	 * 2つの文字列リストを比較する
	 * @param	基準となる文字列リスト
	 * @param	比較する文字列リスト
	 * @return	比較結果の文字列（一致している場合はnull)
	 */
	private static String compTexts(ArrayList<String> baseTexts, ArrayList<String> compTexts) {
		int baseTextsNumber = baseTexts.size();
		int compTextsNumber = compTexts.size();

		if (baseTextsNumber != compTextsNumber) return "行数が一致していません。\n";

		StringBuilder unMatchContents = new StringBuilder();
		boolean unMatch = false;
		ArrayList<String> baseDiff = new ArrayList<String>();
		ArrayList<String> compDiff = new ArrayList<String>();
		for (int textCounter = 0; textCounter < baseTextsNumber; ++textCounter) {
			if (baseTexts.get(textCounter).equals(compTexts.get(textCounter))) continue;
			baseDiff.add(baseTexts.get(textCounter));
			compDiff.add(compTexts.get(textCounter));
			unMatch = true;
		}

		int baseDiffNumber = baseDiff.size();
		for(int misMatch = 0; misMatch < baseDiffNumber; ++misMatch) {
			unMatchContents.append("Base:" + baseDiff.get(misMatch)+"\n");
			unMatchContents.append("Comp:" + compDiff.get(misMatch)+"\n");
		}

		return unMatch ? unMatchContents.toString() : null;
	}
}

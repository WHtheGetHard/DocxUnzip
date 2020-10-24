package fieldsformats;

import java.util.ArrayList;
import java.util.HashMap;

import json_reader.JSONParse;

/**
 * docx内のxmlファイルで無視するファイルと差異が発生することが分かっているファイルとタグ
 */

public class ExpectAndIgnore {
	public ExpectAndIgnore(String settingPath) {
		JSONParse jsonParse = new JSONParse();

		jsonParse.readComplexJSON(settingPath);

		HashMap<String, ArrayList<String>> keyValues = new HashMap<String, ArrayList<String>>();
		keyValues = jsonParse.getKeyValue();
		this.setExpectAndIgnore(keyValues);
	}


	// 無視するxmlファイルのリスト
	private ArrayList<String> ignoreFiles = new ArrayList<String>();

	// 差異が発生するxmlファイルのリスト
	private ArrayList<String> expectFiles = new ArrayList<String>();

	// 差異が発生するxmlファイルとそのタグ名の一覧
	private HashMap<String, ArrayList<String>> expectTags = new HashMap<String, ArrayList<String>>();

	/**
	 * 無視するxmlファイルのリストを取得
	 * @return 無視するxmlファイルのリスト
	 */
	public ArrayList<String> getIgnoreFiles() {
		return this.ignoreFiles;
	}

	/**
	 * 差異が発生するxmlファイルのリストを取得
	 * @return 差異が発生するxmlファイルのリスト
	 */
	public ArrayList<String> getExpectFiles() {
		return this.expectFiles;
	}

	/**
	 * 差異が発生するxmlファイルとそのタグ名の一覧を取得
	 * @retrun 差異が発生するxmlファイルとそのタグ名の一覧
	 */
	public HashMap<String, ArrayList<String>> getExpectTags() {
		return this.expectTags;
	}

	/**
	 * 読み込んだjsonファイルをメンバ変数に設定する
	 * @param jsonファイルの読込結果
	 */
	private void setExpectAndIgnore(HashMap<String, ArrayList<String>> keyValues) {
		for(String key : keyValues.keySet()) {
			switch (key) {
			case "ignore":
				for (String value : keyValues.get(key)) {
					this.ignoreFiles.add(value);
				}
				break;
			case "target":
				for (String value : keyValues.get(key)) {
					this.expectFiles.add(value);
				}
				break;
			default :
				ArrayList<String> expectTag = new ArrayList<String>();
				for (String value : keyValues.get(key)) {
					expectTag.add(value);
				}
				this.expectTags.put(key, expectTag);
				break;
			}
		}
	}
}

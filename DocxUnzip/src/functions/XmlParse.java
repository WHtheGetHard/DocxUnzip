package functions;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParse extends DefaultHandler {
	// 解析したxmlのタグのリスト
	private ArrayList<String> xmlLists = new ArrayList<String>();

	/**
	 * 解析したxmlのタグのリストを取得する
	 * @return 解析したxmlのタグのリスト
	 */
	public ArrayList<String> getXmlLists() {
		return this.xmlLists;
	}

	// イベント発生前の接頭辞付きの修飾名（タグ）
	private String prevQName = "";

	// 開始タグの属性を結合するためのStringBuilder
	private StringBuilder sb = new StringBuilder();

	/**
	 * 開始タグに到達した際に開始タグをxmlListsに追加する
	 * @param	名前空間URI
	 * @param	接頭辞なしのローカル名
	 * @param	接頭辞付きの修飾名
	 * @param	要素に付加された属性
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		this.sb.setLength(0);
		this.sb.append("<" + qName);
		for (int i = 0; i < attributes.getLength(); ++i) {
			this.sb.append(" " + attributes.getQName(i) + "=\"" + attributes.getValue(i) + "\"");
		}
		this.sb.append(">");
		this.xmlLists.add(sb.toString());

		this.prevQName = "";
		this.prevQName = qName;
	}

	/**
	 * テキストノードに到達した際にテキストデータをxmlListsに追加する
	 * @param 文字
	 * @param 文字配列内の開始位置
	 * @param 文字配列から使用される文字数
	 */
	public void characters(char[] ch, int offset, int length) {
		String text = new String(ch, offset, length);
		if (text != null && !"".equals(text)) {
			this.xmlLists.add(text);
		}
	}

	/**
	 * 終了タグに到達した際に終了タグをxmlListsに追加する
	 * @param	名前空間URI
	 * @param	接頭辞なしのローカル名
	 * @param	接頭辞付きの修飾名
	 */
	public void endElement(String uri, String localName, String qName) {
		if (qName.equals(this.prevQName)) {
			int xmlListsLast = this.xmlLists.size() - 1;
			String tempReplace = this.xmlLists.get(xmlListsLast).replaceAll(">", "/>");
			this.xmlLists.set(xmlListsLast, tempReplace);
		} else {
			this.xmlLists.add("</" + qName + ">");
		}
	}
}

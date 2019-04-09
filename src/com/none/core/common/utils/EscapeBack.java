package com.none.core.common.utils;

public class EscapeBack {
	public  static String escapeBack(String string){
		String str = string.replace("&#39;", "'").replace("&quot;", "\"").replace("$amp;", "&").replace("&#47;", "/").replace("&lt;", "<").replace("&gt;", ">");
		return str;
	}
	public  static String escape(String string){
		String str = string.replace("&","$amp;").replace("'","&#39;").replace("\"","&quot;").replace("/","&#47;").replace("<","&lt;").replace(">","&gt;");
		return str;
	}
	public static void main(String[] args) {
		System.out.println(escape("wei'nan"));
	}

}

package com.none.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_br = "<[bB][rR][\\s]?[\\/]?>";
    private static final String regEx_space = "\n";//定义空格回车换行符
    
//    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
    
    /**
     * @param htmlStr
     * @return
     *  删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签
        
        Pattern p_br = Pattern.compile(regEx_br, Pattern.CASE_INSENSITIVE);
        Matcher m_br = p_br.matcher(htmlStr);
        htmlStr = m_br.replaceAll("\n"); // 替换<br>标签为"\n",后面再替换回来
        System.out.println(htmlStr);

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("<br/>"); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }
    
    public static String getTextFromHtml(String htmlStr){
    	htmlStr = delHTMLTag(htmlStr);
//    	System.out.println(htmlStr);
//    	htmlStr = htmlStr.replaceAll(" ", "");
//    	htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);
    	return htmlStr;
    }
    
    public static void main(String[] args) {
//    	String str = "<div style='text-align:center;'> 整治“四风”   清弊除垢<br/><span style='font-size:14px;'> </span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
//		System.out.println(getTextFromHtml(str));
		
    	String str = "abc<br/>cde<br/>jhk<html><script>ewrwrw</script></html>lmn";
    			
//    	String content="";
//		String reg="/<[^<>]+>/g";   //<>标签中不能包含标签实现过滤HTML标签
//		String contents= str.replace(reg,"");
//		System.out.println(contents);
//    	String str = "<div style='text-align:center;'> 整治“四风”   清弊除垢<br/><span style='font-size:14px;'> </span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
		System.out.println(getTextFromHtml(str));
		
	}
    
}

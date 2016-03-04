package duson.java.utils.wordfilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多叉树关键词过滤
 */
public class WordFilterUtil {

	private static Node tree;

	static {
		tree = new Node();
		InputStream is = WordFilterUtil.class.getResourceAsStream("/words.dict");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String word;
			while ((word = reader.readLine()) != null) {
				insertWord(word, 0);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static void insertWord(String word,int level){
		Node node = tree;
		for(int i=0;i<word.length();i++){
			node = node.addChar(word.charAt(i));
		}
		node.setEnd(true);
		node.setLevel(level);
	}

	private static boolean isPunctuationChar(String c) {
		String regex = "[\\pP\\pZ\\pS\\pM\\pC]";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(c);
		return m.find();
	}

	private static PunctuationOrHtmlFilteredResult filterPunctation(String originalString){
		StringBuffer filteredString=new StringBuffer();
		ArrayList<Integer> charOffsets=new ArrayList<Integer>();
		for(int i=0;i<originalString.length();i++){
			String c = String.valueOf(originalString.charAt(i));
			if (!isPunctuationChar(c)) {
				filteredString.append(c);
				charOffsets.add(i);
			}
		}
		PunctuationOrHtmlFilteredResult result = new PunctuationOrHtmlFilteredResult();
		result.setOriginalString(originalString);
		result.setFilteredString(filteredString);
		result.setCharOffsets(charOffsets);
		return result;
	}

	private static PunctuationOrHtmlFilteredResult filterPunctationAndHtml(String originalString){
		StringBuffer filteredString=new StringBuffer();
		ArrayList<Integer> charOffsets=new ArrayList<Integer>();
		for(int i=0,k=0;i<originalString.length();i++){
			String c = String.valueOf(originalString.charAt(i));
			if (originalString.charAt(i) == '<') {
				for(k=i+1;k<originalString.length();k++) {
					if (originalString.charAt(k) == '<') {
						k = i;
						break;
					}
					if (originalString.charAt(k) == '>') {
						break;
					}
				}
				i = k;
			} else {
				if (!isPunctuationChar(c)) {
					filteredString.append(c);
					charOffsets.add(i);
				}
			}
		}
		PunctuationOrHtmlFilteredResult result = new PunctuationOrHtmlFilteredResult();
		result.setOriginalString(originalString);
		result.setFilteredString(filteredString);
		result.setCharOffsets(charOffsets);
		return result;
	}

	private static FilteredResult filter(PunctuationOrHtmlFilteredResult pohResult, char replacement){
		StringBuffer sentence =pohResult.getFilteredString();
		ArrayList<Integer> charOffsets = pohResult.getCharOffsets();
		StringBuffer resultString = new StringBuffer(pohResult.getOriginalString());
		StringBuffer badWords = new StringBuffer();
		int level=0;
		Node node = tree;
		int start=0,end=0;
		for(int i=0;i<sentence.length();i++){
			start=i;
			end=i;
			node = tree;
			for(int j=i;j<sentence.length();j++){
				node = node.findChar(sentence.charAt(j));
				if(node == null){
					break;
				}
				if(node.isEnd()){
					end=j;
					level = node.getLevel();
				}
			}
			if(end>start){
				for(int k=start;k<=end;k++){
					resultString.setCharAt(charOffsets.get(k), replacement);
				}
				if(badWords.length()>0)badWords.append(",");
				badWords.append(sentence.substring(start, end+1));
				i=end;
			}
		}
		FilteredResult result = new FilteredResult();
		result.setOriginalContent(pohResult.getOriginalString());
		result.setFilteredContent(resultString.toString());
		result.setBadWords(badWords.toString());
		result.setLevel(level);
		return result;
	}
	
	/**
	 * 简单句子过滤
	 * 不处理特殊符号，不处理html，简单句子的过滤
	 * 不能过滤中间带特殊符号的关键词，如：黄_色_漫_画
	 * @param sentence 需要过滤的句子
	 * @param replacement 关键词替换的字符
	 * @return 过滤后的句子
	 */
	public static String simpleFilter(String sentence, char replacement){
		StringBuffer sb=new StringBuffer();
		Node node = tree;
		int start=0,end=0;
		for(int i=0;i<sentence.length();i++){
			start=i;
			end=i;
			node = tree;
			for(int j=i;j<sentence.length();j++){
				node = node.findChar(sentence.charAt(j));
				if(node == null){
					break;
				}
				if(node.isEnd()){
					end=j;
				}
			}
			if(end>start){
				for(int k=start;k<=end;k++){
					sb.append(replacement);
				}
				i=end;
			}else{
				sb.append(sentence.charAt(i));
			}
		}
		return sb.toString();
	}
	/**
	 * 纯文本过滤，不处理html标签，直接将去除所有特殊符号后的字符串拼接后进行过滤，可能会去除html标签内的文字，比如：如果有关键字“fuckfont”，过滤fuck<font>a</font>后的结果为****<****>a</font>
	 * @param originalString 原始需过滤的串
	 * @param replacement 替换的符号
	 * @return
	 */
	public static FilteredResult filterText(String originalString, char replacement){
		return filter(filterPunctation(originalString), replacement);
	}
	/**
	 * html过滤，处理html标签，不处理html标签内的文字，略有不足，会跳过<>标签内的所有内容，比如：如果有关键字“fuck”，过滤<a title="fuck">fuck</a>后的结果为<a title="fuck">****</a>
	 * @param originalString 原始需过滤的串
	 * @param replacement 替换的符号
	 * @return
	 */
	public static FilteredResult filterHtml(String originalString, char replacement){
		return filter(filterPunctationAndHtml(originalString), replacement);
	}
	
	public static void main(String[] args){
		System.out.println(WordFilterUtil.simpleFilter("网站黄色漫画网站",'*'));
		FilteredResult result = WordFilterUtil.filterText("网站黄.色,漫,画,网站",'*');
		System.out.println(result.getFilteredContent());
		System.out.println(result.getBadWords());
		System.out.println(result.getLevel());
		result = WordFilterUtil.filterHtml("网站<font>黄</font>.<色<script>,漫,画,网站",'*');
		System.out.println(result.getFilteredContent());
		System.out.println(result.getBadWords());
		System.out.println(result.getLevel());
		
		String str = "我#们#的社 会中  国是我们$多么和谐的一个中###国##人%de民和谐#社#会啊色 ：魔司法";
		// str = "近日，上万名法国民众发起了示威游行，抗议萨科齐政府采取新政策，强行驱逐吉普赛人出境。多个人权组织、反种族主义团体、工会和左翼政党都要求法国停止这项政策。但是法国政府显得一意孤行，他们认为，吉普赛人非法营地是非法交易、教唆儿童行乞、卖淫等犯罪行为的温床，态度坚决地要将吉普赛人遣返。出人意料的是，在法国政府强硬的态度背后，其实是大部分法国民众的支持。在许多欧洲国家，吉普赛人都一样饱受歧视，从未融入过当地社会。为什么吉普赛人一直在城市的边缘流浪。";
		// 5000字
		// str = "黄色小说<script>function aaa(){alert('胡锦涛一党专制');}</script><p><a class=\"vico\" href=\"http://news.qq.com/a/20100910/000341.htm\"><font color=\"#1f376d\">胡</font>锦<font color=\"#1f376d\">涛考察人民大学 向教师祝贺节日，89风波购买了二十四端口交换机送给老师！GCD，黄瓜葫芦！</font></a></p>";
		long start = System.currentTimeMillis();
		result = WordFilterUtil.filterHtml(str,'*');
		long end = System.currentTimeMillis();
		System.out.println("====Time====" + (end - start));
		System.out.println("original:"+result.getOriginalContent());
		System.out.println("result:"+result.getFilteredContent());
		System.out.println("badWords:"+result.getBadWords());
		System.out.println("level:"+result.getLevel());
		start = System.currentTimeMillis();
		result = WordFilterUtil.filterText(str,'*');
		end = System.currentTimeMillis();
		System.out.println("====Time====" + (end - start));
		System.out.println("original:"+result.getOriginalContent());
		System.out.println("result:"+result.getFilteredContent());
		System.out.println("badWords:"+result.getBadWords());
		System.out.println("level:"+result.getLevel());

	}
	
	private static class PunctuationOrHtmlFilteredResult {
		private String originalString;
		private StringBuffer filteredString;
		private ArrayList<Integer> charOffsets;
		
		public String getOriginalString() {
			return originalString;
		}
		public void setOriginalString(String originalString) {
			this.originalString = originalString;
		}
		public StringBuffer getFilteredString() {
			return filteredString;
		}
		public void setFilteredString(StringBuffer filteredString) {
			this.filteredString = filteredString;
		}
		public ArrayList<Integer> getCharOffsets() {
			return charOffsets;
		}
		public void setCharOffsets(ArrayList<Integer> charOffsets) {
			this.charOffsets = charOffsets;
		}
	}

}

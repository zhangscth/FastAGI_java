

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;


//获取关键字的类
public class KeyWordTools {
	private  String testString1 = "";
	
	public KeyWordTools(String txt){
		testString1=txt;
	}

	/*public  ArrayList<String> testStandard(String testString) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		Analyzer analyzer = new StandardAnalyzer();
		Reader r = new StringReader(testString);
		StopFilter sf = (StopFilter) analyzer.tokenStream("", r);
		System.err.println("=====standard analyzer====");
		System.err.println("分析方法：默认没有词只有字");
		Token t;
		while ((t = sf.next()) != null) {
			result.add(t.termText());
			System.out.println(t.termText());
		}
		return result;
	}*/

	public  ArrayList<String> testCJK(String testString) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		Analyzer analyzer = new CJKAnalyzer();
		Reader r = new StringReader(testString);
		StopFilter sf = (StopFilter) analyzer.tokenStream("", r);
		System.err.println("=====cjk analyzer====");
		System.err.println("分析方法:交叉双字分割");
		Token t;
		while ((t = sf.next()) != null) {
			result.add(t.termText());
			System.out.println(t.termText());
		}
		return result;
	}

	public  ArrayList<String> testChiniese(String testString) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		Analyzer analyzer = new ChineseAnalyzer();
		Reader r = new StringReader(testString);
		TokenFilter tf = (TokenFilter) analyzer.tokenStream("", r);
		System.err.println("=====chinese analyzer====");
		System.err.println("分析方法:基本等同StandardAnalyzer");
		Token t;
		while ((t = tf.next()) != null) {
			result.add(t.termText());
			System.out.println(t.termText());
		}
		return result;
	}

	/*public  ArrayList<String> testJe(String testString) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		// Analyzer analyzer = new MIK_CAnalyzer();
		Analyzer analyzer = new IK_CAnalyzer();
		Reader r = new StringReader(testString);
		TokenStream ts = (TokenStream) analyzer.tokenStream("", r);
		System.err.println("=====je analyzer====");
		System.err.println("分析方法:字典分词,具体不明");
		Token t;
		while ((t = ts.next()) != null) {
			result.add(t.termText());
			System.out.println(t.termText());
		}
		return result;
	}
	*/

	//获取分词之后的数据集合
	public  ArrayList<String> getKeyWords() {
		// String testString = testString1;
		String testString = testString1;
		System.out.println(testString);
		
		ArrayList<String> result = new ArrayList<String>();

		//result.addAll(testStandard(testString));
		try {
			result.addAll(testCJK(testString));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// testPaoding(testString);
		
		//testChiniese(testString);
		try {
			result.addAll(testChiniese(testString));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//testJe(testString);
		
		Iterator<String> it = result.iterator();
		while(it.hasNext()){
			String val = it.next();
			System.out.println("KeyWordTools   "+val+".....................");
			
		}
		
		return result;
	}
	
	
	

}
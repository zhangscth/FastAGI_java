

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

public class TextToVoice {
	private static final String serverURL = "http://tsn.baidu.com/text2audio";
	private static String token = "";

	// put your own params here
	private static final String apiKey = "yM4W04ChMEkbaCzHo9U2srFz";
	private static final String secretKey = "AOcu6AiihGK8Eq4aB48BA1ycS5FOi9hw";
	private static final String cuid = "5168174";
	private static String txtneedtran="";
	private static String soundPath="/tmp/asterisk/record/";
	
	public TextToVoice(String txt){
		txtneedtran=txt;
	}
	
	public static InputStream  getBaiduInputStream(){
		InputStream  is=null;
		try {
			getToken();
			HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
					+ "?tex="+txtneedtran+"&lan=zh&tok=" + token+"&ctp=1&cuid=15996940198").openConnection();

			// add request header
			//conn.setRequestMethod("POST");
			conn.setDoInput(true);  
			conn.connect();  
			is = conn.getInputStream();
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
		return is;
	}

	private static void getToken() throws Exception {
		String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials"
				+ "&client_id=" + apiKey + "&client_secret=" + secretKey;
		HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL)
				.openConnection();
		token = new JSONObject(printResponse(conn)).getString("access_token");
	}

	private static String printResponse(HttpURLConnection conn)
			throws Exception {
		if (conn.getResponseCode() != 200) {
			// request error
			return "";
		}
		InputStream is = conn.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		System.out.println(response);
		System.out.println(new JSONObject(response.toString()).toString(4));
		return response.toString();
	}
	
	public static void getRadio(String radioname){
		InputStream is =getBaiduInputStream();//获取到网络返回的文件流
		try {
			OutputStream os = new FileOutputStream(soundPath+radioname+".pcm");
			//int bytesWritten = 0;
			int byteCount = 0;

			byte[] bytes = new byte[1024];

			try {
				while ((byteCount = is.read(bytes)) != -1)
				{
				          os.write(bytes, 0, byteCount);
				           //bytesWritten += byteCount;
				}
				is.close();
				os.close();
				System.out.println("TextToVoice  ....  文字转换语音成功");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}

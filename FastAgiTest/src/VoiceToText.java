

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

public class VoiceToText {

	private static final String serverURL = "http://vop.baidu.com/server_api";
	private static String token = "";
	private String testFileName = "";

	// put your own params here
	private static final String apiKey = "yM4W04ChMEkbaCzHo9U2srFz";
	private static final String secretKey = "AOcu6AiihGK8Eq4aB48BA1ycS5FOi9hw";
	private static final String cuid = "5168174";

	public String dealVoice(String fileName) throws Exception {
		testFileName = fileName;
		String ConvertResult = "";
		getToken();
		method1();
		ConvertResult = method2();
		return ConvertResult;
	}

	private static void getToken() throws Exception {
		String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials"
				+ "&client_id=" + apiKey + "&client_secret=" + secretKey;
		HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL)
				.openConnection();
		token = new JSONObject(printResponse(conn)).getString("access_token");
	}

	private void method1() throws Exception {
		File pcmFile = new File(testFileName);
		HttpURLConnection conn = (HttpURLConnection) new URL(serverURL)
				.openConnection();

		// construct params
		JSONObject params = new JSONObject();
		params.put("format", "wav");
		params.put("rate", 8000);
		params.put("channel", 1);
		params.put("token", token);
		params.put("cuid", cuid);
		params.put("len", pcmFile.length());
		params.put("speech",
				DatatypeConverter.printBase64Binary(loadFile(pcmFile)));

		// add request header
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/json; charset=utf-8;");

		conn.setDoInput(true);
		conn.setDoOutput(true);

		// send request
		System.out.println(" start .....speech.."
				+ DatatypeConverter.printBase64Binary(loadFile(pcmFile)));
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		System.out.println(" end .......");
		wr.writeBytes(params.toString());
		wr.flush();
		wr.close();

		printResponse(conn);
	}

	private String method2() throws Exception {
		String result = "";
		File pcmFile = new File(testFileName);
		HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
				+ "?cuid=" + cuid + "&token=" + token).openConnection();

		// add request header
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "audio/wav; rate=8000");

		conn.setDoInput(true);
		conn.setDoOutput(true);

		// send request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.write(loadFile(pcmFile));
		wr.flush();
		wr.close();

		// printResponse(conn);
		// result = new
		// JSONObject(printResponse(conn)).get("result").toString();
		result = new JSONObject(printResponse(conn)).get("result").toString();
		return result;
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

	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			is.close();
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}

	/*
	 * 创建新文件 文件名为 redio + length
	 */
//	public void createFile(String file_name) {
//		String path = "F:\\javaweb\\java\\workspace\\BYSJ\\WebContent\\vedio\\";
//		File f = new File(path);
//		if (!f.exists()) {
//			f.mkdirs();
//		}
//		File[] fileObjects = f.listFiles();
//		int len = fileObjects.length;
//		//int file_len = len+1;
//		File file = new File(f, "radio"+file_name+".wav");
//	
//		if (!file.exists()) {
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}

	/*
	 * 检索最新文件
	 */
//	public String findFile() {
//		String file = "";
//		String path = "F:\\javaweb\\java\\workspace\\BYSJ\\WebContent\\vedio\\";
//		File f = new File(path);
//		String[] fileNames = f.list();
//		File[] fileObjects = f.listFiles();
//		
//		int len = fileObjects.length;
//		//File resultFile = fileObjects[len-1];
//		File resultFile = fileObjects[0];
//		file = resultFile.getName();
//		return file;
//	}
	
	
	public boolean checkFileisExist(String fileName) {
		Boolean flag=true;
		File file = new File(fileName);
		if(file.exists()){
			flag=false;
		}
		
		return flag;
	}
}

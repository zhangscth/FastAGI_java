import java.sql.SQLException;


public class Test {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		new TextToVoice("百度语音提供技术支持").getRadio("百度");
		try {
			String result = new VoiceToText().dealVoice("/tmp/asterisk/record/record.wav");
			System.out.println("result"+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String result = GetKeyWordsValue.getValue("[中国 ,]");
		System.out.println("result"+result);
		
		
	}

}

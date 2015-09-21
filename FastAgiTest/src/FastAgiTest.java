import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;




/*
 * 业务需求（设计要求）
 * 用户拨打该系统，
 * 1：系统问好，并向用户提供三个选择
 * 		1）选0 ：人工服务，拨打10086
 * 		2）选1 ：系统自动回复（智能回复）
 * 		3）选2 ：挂断
 * 		4）其他选择：挂断
 * 系统自动回复服务：录音，并将录音文件转化成文字，在数据库中检索相关信息，将返回的信息转化为语音，播放给用户，完了之后，在
 * 			提示用户是否继续使用该系统，提供三个选择，和上一步一样
 * 人工服务：拨打相关电话
 * 挂断：
 */
public class FastAgiTest extends BaseAgiScript {
	
	//private static String callerID="";
	private static String playSoundPath="/var/lib/asterisk/sounds/";    //记得填写

	// private static
	private static String path = "/tmp/asterisk/record/"; // 录音文件存在的目录

	@Override
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub

		//answer();
		//exec("Playback","hello"); // 播放目录提示音
		System.out.println(" 播放hello提示音。。。。。。。");
		exec("Playback","hello");
		//callerID = getVariable("callerID"); // 获取主叫电话号码

		// 如果该电话号码曾经使用该系统，则会在该目录下存在一个类似 “radio.wav”的录音文件，
		// 所以需要先将其删除，否则读取录音文件的时候将可能出错
		String fileName = path + "radio.wav";

		System.out.println(" 播放目录提示音。。。。。。。。");
		Character option = getOption("hello", "012");
		System.out.println("处理选择.............");
		dealOption(option);

	}
	
	
	//处理用户的选择
	public void dealOption(Character option) throws AgiException{
		switch (option) {
		case '0':manService();
			;
			break;// 人工服务
		case '1':autoService();
			;
			break;// 智能服务
		case '2':hangUpService();
			;
			break;//挂断	
		default:hangUpService();
			break;// 挂断
		}
		
	}

	// public boolean checkFileExit(String ){
	// return true;
	// }

	/*
	 * 智能服务  ;系统自动回复
	 */
	public void autoService() {
		System.out.println("系统自动回复中。。。。。。。。。。。");
		try {
			System.out.println(" 录音。。。。。。。。。。。");
			exec("Record", "/tmp/asterisk/record/record:wav"); // 录音，文件命名形式为：radio123456.wav
			System.out.println("播放录音。。。。。。。。。");
			exec("PlayBack", "hello");//播放“等待提示音”

			VoiceToText vt = new VoiceToText();
			String fileName = path+"record.wav";
			//如果录音文件存在，则说明该电话曾经使用过该系统，删除该录音，再重新录音，并转换成文字
//			Boolean isExist = vt.checkFileisExist(path+fileName); // 录音文件命名形式：radio123456.wav
//			if (isExist) {
//				new File(fileName).delete();
//			}

			try {
				String txtRecord = vt.dealVoice(fileName); //处理录音文件，将语音转换成文字
				//从转换的文字中获取关键字，并将从数据库中读取相应字段 为：result
				String sql_value_result =GetKeyWordsValue.getValue(txtRecord);
				//--------------------------------------------------------------------------------------------
				//String sql_value_result ="中国人民共和国";
				//文字转换成语音function,用于下一句播放 
				String backRadio = "backRadio";
				System.out.println(" 将文字转换为语音文件。。。。文字结果。。。"+sql_value_result);
				new TextToVoice(sql_value_result).getRadio(backRadio);
				System.out.println(" 播放转换之后的文件");
				exec("PlayBack",path+backRadio); //播放合成语音
				//exec("PlayBack","hello");
				System.out.println("用户选择。。。。。。。");
				Character option = getOption("hello", "012");
				dealOption(option);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("处理语音失败。。。。。。。");
				exec("PlayBack","dealFail");
				hangUpService();
				e.printStackTrace();
			}

		} catch (AgiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//人工服务
	public void manService() throws AgiException {
		System.out.println("人工接听。。。。。。。");
		exec("Dial","910086");
		hangUpService();
	}
	
	//挂断
	public void hangUpService() throws AgiException {
		System.out.println("挂断电话。。。。。。。");
		exec("Playback","hangup-goodbye");
		exec("Hangup");
	}
	
	
	

}

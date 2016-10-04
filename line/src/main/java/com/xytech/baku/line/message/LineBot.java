/**
 * 
 */
package com.xytech.baku.line.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * @author steven
 *
 */
public class LineBot {

	public static final String CHANNEL_ID = "1482860609";
	public static final String CHANNEL_SECRET = "41297fd6a6721d3bc75e752e3f850262";
	public static final String CHANNEL_ACCESS_TOKEN = "CMcCURfI5oXzoA9oQ2Mepa3424Yc1BpZtr3LyhqKQzWxDeBZA2PnWYBX8e3yOpJuzeP9gSEcLF4R8lOQTNeT4FibXY0QojP2aBLvQoGfnhFB47ZBUjHL17nlxc4RWAc0l2np7uzEoDHhVrBXCJwRTwdB04t89/1O/w1cDnyilFU=";
	public static final String PUSH_MESSAGE_ENDPOINT_URL = "https://api.line.me/v2/bot/message/push";

	
	public static final String STEVEN_MID = "u2847335982120bd78211387a7e983c1a";
	public static final String STEVEN_ACCESS_TOKEN = "WINxARctcEGq19eXPSYtE3o9xu+keUrpff7xWFkgX1uVL5ecU4w/jxL4XxfdWYuClUSByF+1oC2azBC01yLsqSOTzif+g3qk7/VLmTwVPURynHVoJ1WNL+03iUQC8t32Z++NuWhBrYyZ9yh2ZS/3EouGd6GkfFq42k3YgjrPs38=";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JSONObject obj = new JSONObject("{\"events\":[{\"type\":\"message\",\"replyToken\":\"d65bd2def9954d698bdf59e30878566e\",\"source\":{\"userId\":\"U2332dbb13b4bfe434773849f879eea8d\",\"type\":\"user\"},\"timestamp\":1475506631524,\"message\":{\"type\":\"text\",\"id\":\"5004340524760\",\"text\":\"你好\"}}]}");
		
		JSONArray events = obj.getJSONArray("events");
		System.out.println(events.toString());
		
		for(int i = 0; i < events.length(); i++) {
			System.out.println(events.getJSONObject(i).getString("type"));
			System.out.println(events.getJSONObject(i).getString("replyToken"));
			System.out.println(events.getJSONObject(i).get("source"));
			System.out.println(events.getJSONObject(i).get("timestamp"));
			System.out.println(events.getJSONObject(i).get("message"));
		}
//		LineBot bot = new LineBot();
//		try {
////			bot.getContent();
//			bot.pushMessage();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void getContent() throws IOException {
		LineMessagingService service = LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build();
		Response<ResponseBody> response = service.getMessageContent("5004605354261").execute();
		if (response.isSuccessful()) {
		    ResponseBody content = response.body();
		    System.out.println(content.toString());
		} else {
		    System.out.println(response.code() + " " + response.message());
		}
	}
	
	public void pushMessage() throws ClientProtocolException, IOException {
//		HttpPost httpPost = new HttpPost(PUSH_MESSAGE_ENDPOINT_URL);
//		httpPost.setHeader("Authorization", "Bearer " + CHANNEL_ACCESS_TOKEN);
//		httpPost.setHeader("Content-Type", "application/json");
		

		
		TextMessage textMessage = new TextMessage("hello");
		
		PushMessage message = new PushMessage(
				"U2332dbb13b4bfe434773849f879eea8d",
		        textMessage
		);
//		ReplyMessage message = new ReplyMessage("3691f93994f84415b8a0ccbd6cc3eda9", textMessage);

		LineMessagingService service = LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build();
		
//		Response<UserProfileResponse> response = service.getProfile("U2332dbb13b4bfe434773849f879eea8d").execute();
		
		
		Response<BotApiResponse> response = service
//				.replyMessage(message)
                .pushMessage(message)
                .execute();
		
		if (response.isSuccessful()) {
		    BotApiResponse content = response.body();
		    System.out.println(content.getMessage());
		} else {
		    System.out.println(response.code() + " " + response.message());
		}

		
		
		
		
		
		
//		Map<String, Object> jsonMap = new HashMap<String, Object>();
//		jsonMap.put("to", "0910106993");
//
//		Map<String, String> jsonMessage = new HashMap<String, String>();
//		jsonMessage.put("type", "text");
//		jsonMessage.put("text", "Hello!");
//		jsonMap.put("messages", jsonMessage);
//		JSONObject jsonObj = new JSONObject(jsonMap);
//		System.out.println(jsonObj.toString());
//
//		StringEntity entity = new StringEntity("{\"to\":\"0910106993\", \"messages\":[{\"text\":\"Hello!\",\"type\":\"text\"}]}", "UTF-8");
////		StringEntity entity = new StringEntity(jsonObj.toString(), "UTF-8");
//		
//		httpPost.setEntity(entity);
//		
//		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//		HttpResponse httpResponse = httpClient.execute(httpPost);
//		HttpEntity httpEntity = httpResponse.getEntity();
//		
//		JSONObject jsonObject = getContent(httpEntity.getContent());
	}

	private JSONObject getContent(InputStream in) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			byte[] bb = new byte[8192];
			int len = -1;
			while(true) {
				len = in.read(bb, 0, 8192);
				if(len < 0) break;
				out.write(bb, 0, len);
			}
			out.flush();
			return processJsonData(new String(out.toByteArray(), "UTF-8"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
	
	private JSONObject processJsonData(String jsonData) {
		System.out.println("Data:" + jsonData);
		
		JSONObject obj = new JSONObject(jsonData);
		
		return obj;
	}

}

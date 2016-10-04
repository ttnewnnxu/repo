package com.xytech.baku.line.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.client.LineMessagingServiceBuilder;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Servlet implementation class GetMessage
 */
@WebServlet("/GetMessage")
public class GetMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String CHANNEL_ID = "1482860609";
	public static final String CHANNEL_SECRET = "41297fd6a6721d3bc75e752e3f850262";
	public static final String CHANNEL_ACCESS_TOKEN = "CMcCURfI5oXzoA9oQ2Mepa3424Yc1BpZtr3LyhqKQzWxDeBZA2PnWYBX8e3yOpJuzeP9gSEcLF4R8lOQTNeT4FibXY0QojP2aBLvQoGfnhFB47ZBUjHL17nlxc4RWAc0l2np7uzEoDHhVrBXCJwRTwdB04t89/1O/w1cDnyilFU=";
	public static final String PUSH_MESSAGE_ENDPOINT_URL = "https://api.line.me/v2/bot/message/push";
	public static final String GET_MESSAGE_ENDPOINT_URL = "https://api.line.me/v2/bot/message/{messageId}/content";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String messageId = request.getParameter("messageId");
//		getContent(messageId);
		getContentManual(messageId);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public void getContent(String messageId) throws IOException {
		LineMessagingService service = LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build();
		Response<ResponseBody> response = service.getMessageContent(messageId).execute();
		if (response.isSuccessful()) {
		    ResponseBody content = response.body();
		    System.out.println(content.toString());
		} else {
		    System.out.println(response.code() + " " + response.message());
		}
	}
	
	public void getContentManual(String messageId) throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(GET_MESSAGE_ENDPOINT_URL.replace("{messageId}", messageId));
		request.setHeader("Authorization", "Bearer " + CHANNEL_ACCESS_TOKEN);
		request.setHeader("Content-Type", "application/json");
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(request);
		HttpEntity httpEntity = httpResponse.getEntity();
		
		
		JSONObject jsonObject = getContent(httpEntity.getContent());

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

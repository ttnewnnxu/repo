package com.xytech.baku.line.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import retrofit2.Response;


/**
 * Servlet implementation class LineServlet
 */
@WebServlet("/LineWebhook")
public class LineWebhookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String CHANNEL_ACCESS_TOKEN = "CMcCURfI5oXzoA9oQ2Mepa3424Yc1BpZtr3LyhqKQzWxDeBZA2PnWYBX8e3yOpJuzeP9gSEcLF4R8lOQTNeT4FibXY0QojP2aBLvQoGfnhFB47ZBUjHL17nlxc4RWAc0l2np7uzEoDHhVrBXCJwRTwdB04t89/1O/w1cDnyilFU=";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LineWebhookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			JSONArray events = getEvents(getJsonString(request.getInputStream()));
			replyMessage(events);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			response.setStatus(200);
		}
	}
	
	private void replyMessage(JSONArray events) throws IOException {
		LineMessagingService service = LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build();
		JSONObject message = events.getJSONObject(0).getJSONObject("message");
		TextMessage echoMessage = new TextMessage(message.getString("text"));
		ReplyMessage replyMessage = new ReplyMessage(events.getJSONObject(0).getString("replyToken"), echoMessage);
		Response<BotApiResponse> response = service.replyMessage(replyMessage).execute();
		if(!response.isSuccessful()) {
			System.out.println(response.message());
		}
	}
	
	private String getJsonString(InputStream in) throws IOException {
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
			return new String(out.toByteArray(), "UTF-8");
		}
		finally {
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private JSONArray getEvents(String jsonData) {
		System.out.println(jsonData);
		return new JSONObject(jsonData).getJSONArray("events");
	}
	
}

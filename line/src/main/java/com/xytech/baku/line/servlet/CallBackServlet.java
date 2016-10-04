package com.xytech.baku.line.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * Servlet implementation class CallBackServlet
 */
@WebServlet("/callback")
public class CallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallBackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("CallBack...");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String code = request.getParameter("code");
		if(code != null) {
			System.out.println("code:" + code);
			getAccessToken(code, response);
//			response.getWriter().write(code);
		}
	}

	
	private void getAccessToken(String code, HttpServletResponse response) throws IOException {
		String channel_id = "1481048969";
		String channel_secret = "0fa92b3cdd65ee91d3e7109e34e8db4e";
		StringBuffer url = new StringBuffer("https://api.line.me/v1/oauth/accessToken");		
		
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("grant_type", "authorization_code"));
		formparams.add(new BasicNameValuePair("client_id", channel_id));
		formparams.add(new BasicNameValuePair("client_secret", channel_secret));
		formparams.add(new BasicNameValuePair("code", code));
		formparams.add(new BasicNameValuePair("redirect_uri", "https://baku.xytech.com.tw/line/LineServlet"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		HttpPost httpPost = new HttpPost(url.toString());
		httpPost.setEntity(entity);
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		JSONObject jsonObject = getContent(httpEntity.getContent());
	
		getProfile(jsonObject.getString("access_token"));
	}

	private void getProfile(String accessToken) throws ClientProtocolException, IOException {
		StringBuffer url = new StringBuffer("https://api.line.me/v1/profile");
		HttpGet httpGet = new HttpGet(url.toString());
		httpGet.setHeader("Authorization", "Bearer " + accessToken);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpGet);
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
//		System.out.println("Mid:" + obj.getString("mid"));
//		System.out.println("AccessToken:" + obj.getString("access_token"));
//		System.out.println("AccessToke Type:" + obj.getString("token_type"));
//		System.out.println("Expire:" + obj.getLong("expires_in"));
//		System.out.println("Refresh Token:" + obj.getString("refresh_token"));
		
		return obj;
	}

}

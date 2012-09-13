package xdi2.connector.facebook.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookApi {

	private static final Logger log = LoggerFactory.getLogger(FacebookApi.class);

	private String appId;
	private String appSecret;
	private HttpClient httpClient;

	public FacebookApi() {

		this.appId = null;
		this.appSecret = null;
		this.httpClient = new DefaultHttpClient();
	}

	public void init() {

	}

	public void destroy() {

		this.httpClient.getConnectionManager().shutdown();
	}

	public void startOAuth(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String url = "https://api-sandbox.personal.com/oauth/authorize?client_id=2hmsfwb28jkmtuetxzk82x7r&response_type=code&redirect_uri="+req.getRequestURL().toString()+"&scope=read_0000&update=false";

		resp.setContentType("text/plain");
		
		resp.sendRedirect(url);
	}
	
	public static StringBuffer postit(String code){
		BufferedReader rd = null;
		StringBuffer sb = new StringBuffer();
    	try {
            String postURL = "https://api-sandbox.personal.com/oauth/access_token";
            // Construct data
            String data = URLEncoder.encode("grant_type", "UTF-8") + "=" + URLEncoder.encode("authorization_code", "UTF-8");
            data += "&" + URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(code, "UTF-8");
            data += "&" + URLEncoder.encode("client_id", "UTF-8") + "=" + URLEncoder.encode("2hmsfwb28jkmtuetxzk82x7r", "UTF-8");
            data += "&" + URLEncoder.encode("client_secret", "UTF-8") + "=" + URLEncoder.encode("CyhuffsrBqdTfzTAsdMB9D6v", "UTF-8");
            data += "&" + URLEncoder.encode("redirect_uri", "UTF-8") + "=" + URLEncoder.encode("http://localhost:8080", "UTF-8");
            // Send data
            URL url = new URL(postURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
    		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
			while ((line = rd.readLine()) != null)
			{
				sb.append(line);
			}
            wr.close();
            rd.close();
        } catch (Exception e) {
        	System.out.println("Exception: "+e);
        }
    	return sb;
    }

	public String exchangeCodeForAccessToken(HttpServletRequest req) throws IOException, HttpException {

		String code = req.getParameter("code");
		StringBuffer sb = postit(code);
		
		JSONObject jObject;
		String accessToken = null;
		try {
			jObject = new JSONObject(sb.toString());
			accessToken = jObject.getString("access_token");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.debug("Error In method exchangeCodeForAccessToken: " + e.toString());
		}

		log.debug("Access Token: " + accessToken);
		return accessToken;
	}

	public JSONObject getUser(String accessToken) throws IOException, JSONException {

		if (accessToken == null) throw new NullPointerException();
		
		log.debug("Retrieving User for Access Token '" + accessToken + "'");

		String url = "https://api-sandbox.personal.com/api/v1/gems/?client_id=2hmsfwb28jkmtuetxzk82x7r";
		String res = getit(url,accessToken,null);
		
		String instanceID = null;
		String encInstanceId = null;
		try {
			JSONObject jj = new JSONObject(res);
			org.json.JSONArray gemContents = jj.getJSONArray("gems");

			instanceID = gemContents.getJSONObject(0).getString("gem_instance_id");

			//@SuppressWarnings("deprecation")
			encInstanceId = URLEncoder.encode(instanceID);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		url = "https://api-sandbox.personal.com/api/v1/gems/"+encInstanceId+"/?client_id=2hmsfwb28jkmtuetxzk82x7r";
		String secure_pass = "CyhuffsrBqdTfzTAsdMB9D6v";
		String gemData = getit(url,accessToken,secure_pass);
		
		JSONObject nameGemObject = new JSONObject();
		try {
			JSONObject tempObj = new JSONObject(gemData).getJSONObject("gem").getJSONObject("data");
			nameGemObject = tempObj;
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		log.debug("User: " + nameGemObject);
		return nameGemObject;
	}

	private static String uriWithoutQuery(String url) {

		return url.contains("?") ? url.substring(url.indexOf("?")) : url;
	}

	public String getAppId() {

		return this.appId;
	}

	public void setAppId(String appId) {

		this.appId = appId;
	}

	public String getAppSecret() {

		return this.appSecret;
	}

	public void setAppSecret(String appSecret) {

		this.appSecret = appSecret;
	}
}

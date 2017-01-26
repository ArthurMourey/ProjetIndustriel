package miage.projetindustriel.connexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionHttp {
	private HttpURLConnection connection = null;
		
	public ConnectionHttp(String Base_URL) {
		connection = ConnectionToUrl(Base_URL);
	}
	
	public HttpURLConnection getConnection() {
		return connection;
	}
	
	private HttpURLConnection ConnectionToUrl(String Base_URL){
		HttpURLConnection conn = null;
		
		try {
			URL url = new URL(Base_URL);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(15000);
			conn.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	public String readStream(HttpURLConnection conn){
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			InputStream inputStream = conn.getInputStream();
			if(null == inputStream){
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			while((line = reader.readLine()) != null){
				sb.append(line + '\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return sb.toString();
	}
	
	public void disconnect(){
		connection.disconnect();
	}
}

package com.github.alex1304.jdash.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;

import com.github.alex1304.jdash.component.GDComponent;
import com.github.alex1304.jdash.exceptions.GDAPIException;
import com.github.alex1304.jdash.util.Constants;

/**
 * Handles HTTP requests to Geometry Dash servers
 * 
 * @author Alex1304
 *
 */
public class GDHttpClient {

	private long accountID;
	private String password;
	private boolean isAuthenticated;
	private String host;

	/**
	 * @param accountID
	 *            - The GD account ID
	 * @param password
	 *            - The GD account password
	 */
	public GDHttpClient(long accountID, String password) {
		this.accountID = accountID;
		this.password = password;
		this.isAuthenticated = true;
		this.host = Constants.GD_API_URL;
	}
	
	/**
	 * Constructor that creates an anonymous (logged out) client.
	 */
	public GDHttpClient() {
		this(0, null);
		this.isAuthenticated = false;
	}
	
	/**
	 * Fetches the Geometry Dash API through a GDHttpRequest object. This method
	 * is NOT asynchroneous, make sure to call this method in an async task or a
	 * separate thread if you need to make some UI updates during loading time!
	 * 
	 * @param request
	 *            - the request object that contains the URL to request and the
	 *            POST parameters
	 * 
	 * @param <T>
	 *            - The type of the response object returned
	 * 
	 * @return a GDHttpResponse
	 * @throws GDAPIException
	 *             if the request fails for some reason. It contains a message
	 *             describing the error.
	 */
	public <T extends GDComponent> T fetch(GDHttpRequest<T> request) throws GDAPIException  {
		StringBuffer reqBody = new StringBuffer();
		StringBuffer response = new StringBuffer();
		
		try {
			HttpURLConnection con;
			con = (HttpURLConnection) new URL(host + request.getPath()).openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			
			// Sending the request to the server
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			
			request.getParams().putAll(request.requiresAuthentication()
					? Constants.globalHttpRequestParamsWithAuthentication(accountID, password)
					: Constants.globalHttpRequestParams());
			
			for (Entry<String, String> param: request.getParams().entrySet())
				reqBody.append(param.getKey() + "=" + param.getValue() + "&");
			
			reqBody.deleteCharAt(reqBody.length() - 1);
			
			wr.writeBytes(reqBody.toString());
			wr.flush();
			wr.close();
			
			// Fetching response
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line + "\n");
			}
			
			while (response.toString().endsWith("\n"))
				response.deleteCharAt(response.length() - 1);
			
			GDHttpResponseBuilder<T> builder = request.getResponseBuilder();
			return builder.build(response.toString());
		} catch (IOException | RuntimeException e) {
			throw new GDAPIException(e, "POST " + request.getPath() + "\n\n" + reqBody.toString().replaceAll("gjp=.*", "gjp=******"), response.toString());
		}
	}

	/**
	 * Gets the GD account ID
	 * 
	 * @return long
	 */
	public long getAccountID() {
		return accountID;
	}

	/**
	 * Gets the GD account password
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets whether the client is authenticated with a GD account
	 * 
	 * @return boolean
	 */
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	/**
	 * Gets the host
	 *
	 * @return String
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host
	 *
	 * @param host - String
	 */
	public void setHost(String host) {
		this.host = host;
	}
}

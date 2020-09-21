import java.nio.charset.StandardCharsets;

    String account   = "{ACCOUNT_ID}";
    String authToken = "{AUTH_TOKEN}"
 
 
    String url = "https://api.twilio.com/2010-04-01/Accounts/"+ account+"/SMS/Messages.json";	
 
  	HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
    con.setDoOutput(true);  
    con.setRequestMethod("POST");  
  	con.setUseCaches( false );
 	con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
    con.setRequestProperty("Content-Type",  "application/x-www-form-urlencoded; charset=UTF-8");
    String userpass 		= account+":"+authToken;
	String basicAuth 		=  new String(Base64.getEncoder().encode(userpass.getBytes())); 
	con.setRequestProperty ("Authorization", "Basic " + basicAuth);    

	/* build the Param List **/	
    Map<String, String> mapParameters = new HashMap<String, String>();
	mapParameters.put("From", "+{from_phone_number}");
	mapParameters.put("To", "+{to_phone_number}");
	mapParameters.put("Body", "Hello From Cali");

 	String message  = encodeMap(mapParameters);
    byte[] messageinBytes       = message.getBytes( StandardCharsets.UTF_8 ); 
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.write(messageinBytes);
    con.getOutputStream().close();
  
      try {
			statusCode = con.responseCode
			println "Connection status code: $statusCode "
			if (statusCode==401) {
				println "Not authorized"
			}
			if (statusCode==200) {
				println "Authentication Successful"
				println "Server Response:"
				println "-----"
				response=displayServerResponse(con)
				println "-----"
			}
			if (statusCode==400) {
				println "Bad request"
				println "Server response:"
				println "-----"
				response=displayServerResponse(con)
				println "-----"
			}
			if (statusCode==201) {
				println "SMS Successfully Sent"
				 br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		    	String strCurrentLine;
		        while ((strCurrentLine = br.readLine()) != null) {
		               System.out.println(strCurrentLine);
		        } 
			}
		} catch (Exception e) {
			println "Error connecting to the URL"
			println e.getMessage()
		} finally {
			if (con != null) {
				con.disconnect();
			}
		} 
 

 def displayServerResponse(connection) {
		InputStream is;
		if (connection.getResponseCode()==201) {
			is=connection.getInputStream();
		} else {
			is=connection.getErrorStream();
		} 
		println "Response Content-Type:"+connection.getContentType()
		if (connection.getContentType().contains("application/json")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\n");
			}
			br.close();
			println sb
			return sb.toString()
		}  
		
		return ""
	} 


	def String encodeMap(Map<String, String> map) throws UnsupportedEncodingException {
	StringBuilder sb = new StringBuilder();
	
	for (Map.Entry<String, String> entry : map.entrySet()) { 
		if (sb.length() > 0) {
			sb.append("&");
		} 
		sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()), urlEncodeUTF8(entry.getValue().toString()) )); 
	}
	
	return sb.toString();
}

def String urlEncodeUTF8(String s) throws UnsupportedEncodingException {
	return URLEncoder.encode(s, "UTF-8");
}

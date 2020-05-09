/*

	Still in progress building an alternative to nanohttpd so I can embed this server on an android device.

*/


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger; 
import java.util.Collections;
import java.util.ArrayList;



public class Server implements Runnable{
	
	static AtomicInteger ID = new AtomicInteger();
	static boolean running = true;
	static ConcurrentHashMap<String,ConcurrentHashMap<Integer,String>> MAP = new ConcurrentHashMap<String,ConcurrentHashMap<Integer,String>>();
	Socket socket;

	public Server(Socket socket) {
		this.socket = socket;
	}

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8080);
			while(running) {
				new Thread(new Server(server.accept())).start();
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String parseRequestValue(String request,String key,String end) {
		if(end == null) end = "\r\n";
		int i;
		if((i = request.indexOf(key)) != -1) {
			String str = request.substring(i);
			return str.substring(key.length(),str.indexOf(end)).trim();
		}else return null;
	}
	
	HashMap<String,String> parseCookies(String cookie){
		if (cookie == null) return null;
		HashMap<String,String> map = new HashMap<String,String>();
		String tokens[] = cookie.split(";");
		for(String token : tokens){
			token = token.trim();
			String temp[] = token.split("=",2);	
			map.put(temp[0],temp[1]);
		}
		return map;
	}

	@Override
	public void run() {
		try {
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			final int bufferSize = 8*1024;
			byte buffer[] = new byte[bufferSize];
			int count = 0;
			int requestHeaderSize = -1;
			String requestHeader = null;
			
			while(requestHeaderSize == -1) {
				count += in.read(buffer, count, bufferSize);
				for (int i = 3; i < buffer.length; i++) {
					if(buffer[i-3] == 13 && buffer[i-2] == 10 && buffer[i-1] == 13 && buffer[i] == 10) {
						requestHeaderSize = i+1;
						requestHeader = new String(Arrays.copyOf(buffer, requestHeaderSize));
						System.out.println(requestHeader);
						break;
					}
				}
			}

				
			String path = requestHeader.substring(requestHeader.indexOf("/"), requestHeader.indexOf("HTTP")).trim();
			
			
			if(requestHeader.startsWith("GET")) {	//send data
				if (path.equals("/")) {	//index.html
					File index = new File("index.html");
					InputStream fis = new FileInputStream(index);
					String response="HTTP/1.1 200 OK\r\n" +
								    "Content-Type: text/html\r\n" +
								    "Connection: close\r\n"+
								    "Content-Length: "+index.length()+"\r\n" +
								    "\r\n";
					out.write(response.getBytes());
					long read = 0;
					while(read != index.length()) {
						read += fis.read(buffer, 0, bufferSize);
						out.write(buffer);
					}
					fis.close();
				}else if(path.equals("/login")) {
					String cookie = parseRequestValue(requestHeader, "Cookie:", null);
					HashMap<String,String> cookies = parseCookies(cookie);
					String message = "welcome " + cookies.get("name");
					String response="HTTP/1.1 200 OK\r\n" +
								    "Content-Type: text/html\r\n" +
								    "Connection: close\r\n"+
								    "Content-Length: "+message.length()+"\r\n" +
								    "\r\n";
					response += message;				
					out.write(response.getBytes());
				}else if(path.equals("/msg")) {
					String cookie = parseRequestValue(requestHeader, "Cookie:", null);
					HashMap<String,String> cookies = parseCookies(cookie);
					String msg = cookies.get("name") + ";;" + cookies.get("msg") + ";;" + cookies.get("date");
					String group = cookies.get("group");
					int id = ID.incrementAndGet();
					if (!MAP.containsKey(group)) {
						ConcurrentHashMap<Integer,String> map = new ConcurrentHashMap<Integer,String>();
						map.put(id,msg);
						MAP.put(group,map);
					}	
					else {
						ConcurrentHashMap<Integer,String> map = MAP.get(group);
						map.put(id,msg);
						// may cause error
					}
					String response="HTTP/1.1 200 OK\r\n" +
								    "Connection: close\r\n"+
								    "\r\n";
					out.write(response.getBytes());	
				}else if (path.equals("/ping")) {
					String cookie = parseRequestValue(requestHeader, "Cookie:", null);
					HashMap<String,String> cookies = parseCookies(cookie);
					String group = cookies.get("group");
					if(MAP.containsKey(group)){
						ConcurrentHashMap<Integer,String> map = MAP.get(group);
						ArrayList<Integer> list = new ArrayList<Integer>();
						for(Integer key : map.keySet()){
							list.add(key);
						}
						Collections.sort(list);
						String message = "";
						for(int key : list){
							message += key + ";;" + map.get(key) + "\n";
						}
						String response="HTTP/1.1 200 OK\r\n" +
								    "Content-Type: text/html\r\n" +
								    "Connection: close\r\n"+
								    "Content-Length: "+message.length()+"\r\n" +
								    "\r\n";
						response += message;				
						out.write(response.getBytes());
					}else {
						String response="HTTP/1.1 200 OK\r\n" +
										"Connection: close\r\n"+
										"\r\n";
						out.write(response.getBytes());
					}			
				}
				else {	//all other files
					String fileName = path.substring(1);
					fileName = URLDecoder.decode(fileName,"UTF-8");
					File file = new File(fileName);
					if(file.exists()){	//if file exists
						InputStream fis = new FileInputStream(file.getPath());
						String response="HTTP/1.1 200 OK\r\n" +
										"Accept-Ranges: bytes\r\n" +
										"Content-Length:"+file.length()+"\r\n" +
										"Connection: close\r\n" +
										"Content-Type: application/octet-stream\r\n" +
										"Content-Disposition: attachment; filename=\""+file.getName()+"\"\r\n\r\n";
						out.write(response.getBytes());
						long read = 0;
						while(read != file.length()) {
							read += fis.read(buffer, 0, bufferSize);
							out.write(buffer);
						}
						fis.close();							
					}else {	//if file does not exist
						String notFound = "<h1>FILE NOT FOUND<h1>";
						String response =  "HTTP/1.1 404 Not Found\r\n" +
							    "Content-Type: text/html\r\n" +
							    "Connection: close\r\n"+
							    "Content-Length: "+ notFound.length() + "\r\n" +
							    "\r\n" + notFound;
						out.write(response.getBytes());
					}
				}
				
			} else if(requestHeader.startsWith("POST")) {	//save data
				String fileName = parseRequestValue(requestHeader, "File-Name:", null);
				fileName = URLDecoder.decode(fileName,"UTF-8");
				long length = Long.parseLong(parseRequestValue(requestHeader, "Content-Length:", null));
				OutputStream fos = new FileOutputStream(new File(fileName));
				long wrote = count - requestHeaderSize;
				fos.write(Arrays.copyOfRange(buffer, requestHeaderSize,count));
				while(wrote != length) {
					count = in.read(buffer, 0, bufferSize);
					wrote += count;
					fos.write(buffer, 0, count);
				}
				fos.close();
				String message = "<a href=" + URLEncoder.encode(fileName, "UTF-8") + ">" + fileName + "</a>"; 
				String response =  "HTTP/1.1 200 OK\r\n" +
					    "Content-Type: text/html\r\n" +
					    "Connection: close\r\n"+
					    "Content-Length: " + message.length() + "\r\n" +
					    "\r\n";
				response += message;		
				out.write(response.getBytes());
			}
			
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}

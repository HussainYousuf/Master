import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Client2 implements Runnable{
	
	Socket client;
	static int countOfThreads;
	InputStream in;
	String myName="";
	OutputStream out;
	int bufferSize = 64*1024;
	byte buffer[] = new byte[bufferSize];
	String msg,index,listOfFiles,response;
	int count;



	
	public Client2(Socket socket) throws IOException {
		this.client = socket;
		in = socket.getInputStream();
		out = socket.getOutputStream();
		listOfFiles = "<ol>";
		for (File file : new File(".").listFiles()) if (file.isFile()) listOfFiles += "<li><a href=" + URLEncoder.encode(file.getName(), "UTF-8") + ">" + file.getName() + "</a></li>"; 
		listOfFiles += "</ol>";
		BufferedReader reader = new BufferedReader(new FileReader(new File("5.html")));
		String line="";
		index = "";
		while((line = reader.readLine()) != null){
			index += line + "\n";
			if(line.equals("<body>")) index += listOfFiles + "\n";
		}
	}

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8080);
		while(true) new Thread(new Client2(server.accept())).start();
		
	}

	public void run() {
		
		
		try{
				count = in.read(buffer,0,bufferSize);
				msg = new String(buffer);
				String log = msg.substring(300);
				System.out.println(log);				
				
				
				if(msg.startsWith("POST")){
					myName += "POST"+countOfThreads++;
					boolean quit = false;
					
					while(true){
						
						for (int i = 0; i < count-3; i++) {
	
							if(buffer[i] == '\r' && buffer[i+1] == '\n' 
									&& buffer[i+2] == '\r' && buffer[i+3] == '\n'){
								
								if(i+4 < count){	// contains raw data body
								
									String fileName = "My-File-Name:";
									
									int fileNameIndex = msg.indexOf(fileName) + fileName.length();
									fileName = "";
									while(msg.charAt(fileNameIndex) != '\r'){
										fileName += msg.charAt(fileNameIndex++);
									}
									
									fileName = fileName.trim();
									
									System.out.println("FileName is " + fileName);
		
									String length = "My-Content-Length:";
										
									int lengthIndex = msg.indexOf(length) + length.length();
									length = "";
									while(msg.charAt(lengthIndex) != '\r'){
										length += msg.charAt(lengthIndex++);
									}
									
									length = length.trim();
									
									System.out.println("length is " + length);
										
									FileOutputStream writer = new FileOutputStream(new File(fileName));
									
									long LEN = Long.parseLong(length);
								


									byte BUFFER[] = new byte[bufferSize];
									int index = 0;
									long total = 0;
									
									for (int j = i+4; j < count; j++) {
										BUFFER[index++] = buffer[j];
										if(index == LEN){
											System.out.println("BREAKING");
											writer.write(BUFFER, 0, index);
											quit = true;
											break;
										}
									}

									
									if(!quit){
										writer.write(BUFFER, 0, index);
										total = index;
									}
									while(!quit){
										count = in.read(BUFFER,0,BUFFER.length);
										writer.write(BUFFER, 0, count);
										total += count;
										if(total == LEN){
											System.out.println("NOW BREAKING at " + total + " index");
											quit = true;
											break;
										}
									}
									writer.close();
									System.out.println("Sending done response");
									response =  "HTTP/1.1 200 OK\r\n" +
										    "Content-Type: text/plain\r\n" +
										    "Connection: close\r\n"+
										    "Content-Length: 4\r\n" +
										    "\r\n" +
										    "done";
									out.write(response.getBytes());
									out.flush();
					
									break;
		
								}else{ // empty post request
									System.out.println("just an empty post request");
									if(in.available() <= 0){ // no raw data is followed
										System.out.println("DANGER no raw data is follwed");
										quit = true;
										System.out.println("Empty post");
										response =  "HTTP/1.1 200 OK\r\n" +
											    "Content-Type: text/plain\r\n" +
											    "Connection: close\r\n"+
											    "Content-Length: 7\r\n" +
											    "\r\n" +
											    "notdone";
										out.write(response.getBytes());
										out.flush();
									}
								}
							
							}	
							
						} // end of for
					
						if(!quit){
							while(in.available() <= 0);
							count += in.read(buffer,count,bufferSize-count);
						}else{
							break;
						}
						
					}// end of while

				}// end of post
				
				
				
				if(msg.startsWith("GET")){
					myName += "GET"+countOfThreads++;
					System.out.println("SOME HOW REAcHED GET");
					String path = msg.substring(msg.indexOf("/"), msg.indexOf("HTTP")).trim();
					if(path.equals("/")){
						response =  "HTTP/1.1 200 OK\r\n" +
							    "Content-Type: text/html\r\n" +
							    "Connection: close\r\n"+
							    "Content-Length:"+index.length()+"\r\n" +
							    "\r\n" +
							    index;
						out.write(response.getBytes());
					}
					else {
						String fileName = path.substring(1);
						fileName = URLDecoder.decode(fileName,"UTF-8");
						File file = new File(fileName);
						if(file.exists()){
				            out.write("HTTP/1.1 200 OK\r\n".getBytes());
				            out.write("Accept-Ranges: bytes\r\n".getBytes());
				            out.write(("Content-Length:"+file.length()+"\r\n").getBytes());
				            out.write("Connection: close\r\n".getBytes());
				            out.write("Content-Type: application/octet-stream\r\n".getBytes());
				            out.write(("Content-Disposition: attachment; filename=\""+file.getName()+"\"\r\n").getBytes());
				            out.write("\r\n".getBytes());
				            byte buffer2[] = new byte[bufferSize];
				            InputStream reader = new FileInputStream(file.getPath());
				            long count2 = 0;
				            int read = 0;
				            while(count2 != file.length()){
				            	read = reader.read(buffer2, 0, buffer2.length);
				            	count2 += read;
				            	out.write(buffer2,0,read);
				            }
				            
				            
						}else {
							String notFound = "<h1>FILE NOT AVAILABLE<h1>";
							response =  "HTTP/1.1 404 Not Found\r\n" +
								    "Content-Type: text/html\r\n" +
								    "Connection: close\r\n"+
								    "Content-Length: "+ notFound.length() + "\r\n" +
								    "\r\n" + notFound;

							out.write(response.getBytes());
						};
					}
					out.flush();
				} // END OF GET
				
				
				
			//}// end of while
			System.out.println(myName + " REMOVED");	
			in.close();
			out.close();
			client.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}

}

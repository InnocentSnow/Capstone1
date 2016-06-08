import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
 
public class SendClient {
	public static final String IP = "52.78.21.53";
	public static final int PORT = 5000;

    public static void main(String[] args){
	int t=0;
        Socket socket = null;
        try {

            socket = new Socket(IP, PORT);
		    System.out.println("Server Connected");
            FileSender fs = new FileSender(socket);
            fs.start();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
 class FileSender extends Thread {
    Socket socket;
    DataOutputStream dos;
    FileInputStream fis;
    BufferedInputStream bis;
 
    public FileSender(Socket socket) {
        this.socket = socket;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void run() {
	int t=0;
	while(true){
	
	try {
	    String data2;
            System.out.println("Sending Start.");
	    Runtime clsRuntime = Runtime.getRuntime();
	    Process clsProc = clsRuntime.exec("raspistill -w 960 -h 640 -t 1 -q 10 -o capture.jpg");

	try{
		clsProc.waitFor();
	    } catch (Exception e){
		e.printStackTrace();

	    }

            String fName = "capture.jpg";

            File f = new File(fName);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);

	    dos.writeInt((int)f.length());
            System.out.println("Sended FileSize : " + f.length());
 
            int r = 0;
            int len;
            int size = 4096;
            byte[] data = new byte[size];
            int i = 0;

	    while ((len = bis.read(data)) != -1) {
                dos.write(data, 0, len);
		r += len;
            }
 
            dos.flush();
	    t++;
         //   dos.close();
            bis.close();
            fis.close();
            System.out.println("Done!!." + r);
        } catch (IOException e) {
            e.printStackTrace();
        }
}
	//try{dos.close();}
	//catch(Exception e){ e.printStackTrace();}
 
    }
}

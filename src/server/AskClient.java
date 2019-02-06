package server;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class AskClient {
	
	private Socket client=null;
	public AskClient(Socket client){
		this.client=client;
	}
	public void askScreenShoot(){
		if(client!=null){
			try {
				
				OutputStream ops = client.getOutputStream();// 获取socket的输出流
				BufferedWriter dos = new BufferedWriter(new OutputStreamWriter(ops));
				dos.write("ask screenshoot");
				dos.newLine();
				dos.flush();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
	}
	public void closeScreenShoot(){
		if(client!=null){
			try {
				
				OutputStream ops = client.getOutputStream();// 获取socket的输出流
				BufferedWriter dos = new BufferedWriter(new OutputStreamWriter(ops));
				dos.write("close screenshoot");
				dos.newLine();
				dos.flush();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
	}
	public void askTakePhoto(){
		if(client!=null){
			try {
				
				OutputStream ops = client.getOutputStream();// 获取socket的输出流
				BufferedWriter dos = new BufferedWriter(new OutputStreamWriter(ops));
				dos.write("ask take photo");
				dos.newLine();
				dos.flush();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	public void closeTakePhoto(){

		if(client!=null){
			try {
				
				OutputStream ops = client.getOutputStream();// 获取socket的输出流
				BufferedWriter dos = new BufferedWriter(new OutputStreamWriter(ops));
				dos.write("close take photo");
				dos.newLine();
				dos.flush();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
	
	}

}

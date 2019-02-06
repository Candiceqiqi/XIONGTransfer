package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientGuider implements Runnable {
	private ServerSocket server;
	private boolean runningflag = true;
	private List<Socket> clientList=new ArrayList<Socket>();//多个用户的集合


	private ServerFrame sf;
	public ClientGuider(ServerSocket server,ServerFrame sf) {
		this.server = server;
		this.sf=sf;
	}

	public List<Socket> getClientList() {
		return clientList;
	}    

	public void removeClientList(Socket client){
		clientList.remove(client);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (runningflag) { // 使服务器可以等待连接多个客户端
				Socket client = server.accept();// 监听客户端是否连入服务器
				client.setKeepAlive(true);//开启对套接字的监听
				System.out.println("client has connected.");
				clientList.add(client);//放入用户集合
				sf.updateClientlistUI();//用该线程顺便更新clientButtongroup
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

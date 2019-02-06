package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientGuider implements Runnable {
	private ServerSocket server;
	private boolean runningflag = true;
	private List<Socket> clientList=new ArrayList<Socket>();//����û��ļ���


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
			while (runningflag) { // ʹ���������Եȴ����Ӷ���ͻ���
				Socket client = server.accept();// �����ͻ����Ƿ����������
				client.setKeepAlive(true);//�������׽��ֵļ���
				System.out.println("client has connected.");
				clientList.add(client);//�����û�����
				sf.updateClientlistUI();//�ø��߳�˳�����clientButtongroup
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

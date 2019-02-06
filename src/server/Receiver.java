package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

public class Receiver extends Thread {

	private Socket client;
	private boolean runningflag = true;
	private DataInputStream dis;
	private byte[] receiveBytes = null;
	private ScreenFrame scf;
	private ServerFrame sf;

	public Receiver(Socket client, ServerFrame sf) {

		this.client = client;
		this.sf = sf;
		InputStream is;
		try {
			is = client.getInputStream();
			dis = new DataInputStream(is); // 得到客户端发过来的输出流
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close() {
		runningflag=false;

	}

	public void run() {
		while (runningflag) {
			this.reveiveCommand();
		}

	}

	private void reveiveCommand() {
		int flag;
		try {
			flag = dis.readInt();
			if (dis != null) {
				int length = dis.readInt();
				this.receiveBytes = new byte[length];
				dis.readFully(receiveBytes);// 接收传过来图像的数组放入screenBytes数组
			}
			if (flag == 10) {
				// System.out.println("1010");
				sf.getSf().setScreenBytes(receiveBytes);

			} else if (flag == 20) {
				// System.out.println("2020");
				sf.getCpf().setClientPhotoBytes(receiveBytes);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

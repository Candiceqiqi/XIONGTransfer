package Client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public class MyScreenShoot extends Robot implements Runnable {

	private Socket socket = null;
	private boolean runningflag = true;
	private DataOutputStream dos = null;
	private Object lock=new Object();

	public MyScreenShoot(Socket socket,Object lock) throws AWTException {
		super();
		this.socket = socket;
		this.lock=lock;
		OutputStream ops;
		try {
			ops = socket.getOutputStream();
			dos = new DataOutputStream(ops);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ��ȡsocket�������

	}

	@Override
	public void run() {
		while (runningflag) {

			try {
				synchronized (this.lock) {
					this.sendScreenShoot(this.screenShoot());// 1.���� 2.���ͽ���
				}
				
				Thread.sleep(100);// ����ÿ�뷢10��
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}


	public void setRunningflag(boolean runningflag) {
		this.runningflag = runningflag;
	}

	public byte[] screenShoot() {
		// �õ���Ļ��ͼ
		BufferedImage screenImage = this.getScreenCapture();
		byte[] screenBytes = this.imageToBytes(screenImage);// ����Ļ��ͼת������
		return screenBytes;

	}

	public void sendScreenShoot(byte[] screenBytes) {

		try {
			dos.writeInt(10);
			dos.writeInt(screenBytes.length);// �������ĳ��Ȳ�д�������
			dos.write(screenBytes);// ����Ļͼ�������д�������
			dos.flush();// ���������

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public BufferedImage getScreenCapture() {// 1.����
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// ��ȡ��Ļ�Ĵ�С

		Rectangle screenRect = new Rectangle(0, 0, screenSize.width,
				screenSize.height);// ����һ������

		BufferedImage screenImage = this.createScreenCapture(screenRect);// ���ݾ��εĴ�С��λ��������robot�Դ�����

		return screenImage;

	}

	public byte[] imageToBytes(BufferedImage screenImage) {// ͼƬת���byte����

		ByteArrayOutputStream bos = new ByteArrayOutputStream();// �ô����������ͼƬ�ı���
		try {
			ImageIO.write(screenImage, "JPEG", bos);// �ø÷�����screenImage��JPEG��ͼ��ѹ����ʽ�������bos
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte[] screenBytes = bos.toByteArray();
		return screenBytes;

	}

}

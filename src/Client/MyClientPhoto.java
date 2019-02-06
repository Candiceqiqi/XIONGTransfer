package Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;

public class MyClientPhoto implements Runnable {

	private Socket socket;
	private boolean runningflag = true;
	private DataOutputStream dos = null;
	private Object lock = new Object();

	// JMF
	private CaptureDeviceInfo di = null;
	private MediaLocator ml = null;
	static Player player = null;
	private Buffer buf = null;
	private BufferToImage btoi = null;
	private Image img = null;

	// private ImagePanel imgpanel;

	public MyClientPhoto(Socket socket, Object lock) {

		this.socket = socket;
		this.lock = lock;
		OutputStream ops;
		try {
			ops = socket.getOutputStream();
			dos = new DataOutputStream(ops);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ��ȡsocket�������
		this.StartPlayer();

	}

	public void setRunningflag(boolean runningflag) {
		this.runningflag = runningflag;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (runningflag) {
			try {
				byte[] photoBytes = this.clientPhoto();
				if (photoBytes != null) {
					synchronized (this.lock) {
						this.sendClientPhoto(photoBytes);// 1.���� 2.������Ƭ
					}

				} else {
					System.out.println("cannot get image!");
				}
				Thread.sleep(100);// ����ÿ�뷢10��
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (player != null) {
			player.close();
		}

	}

	private void sendClientPhoto(byte[] clientPhotoBytes) {
		try {
			dos.writeInt(20);
			dos.writeInt(clientPhotoBytes.length);// �������ĳ��Ȳ�д�������
			dos.write(clientPhotoBytes);// ����Ļͼ�������д�������
			dos.flush();// ���������

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void StartPlayer() {
		String str1 = "vfw:Logitech USB Video Camera:0";
		String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";

		di = CaptureDeviceManager.getDevice(str2);
		// �õ�����ͷplayer
		ml = di.getLocator();
		try {
			player = Manager.createRealizedPlayer(ml);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (player != null) {
			player.start();
		}

	}

	private BufferedImage takeClientPhoto() {// *********************************�õ�ͼƬ
		FrameGrabbingControl fgc = (FrameGrabbingControl) player
				.getControl("javax.media.control.FrameGrabbingControl");
		buf = fgc.grabFrame(); // ��ȡ��ǰ��������Buffer��
		btoi = new BufferToImage((VideoFormat) buf.getFormat());

		img = btoi.createImage(buf); // show the image

		if (img == null) {
			System.out.println("no image");
			return null;
		} else {

			BufferedImage clientPhoto = new BufferedImage(img.getWidth(null),
					img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = clientPhoto.createGraphics();
			g2.drawImage(img, null, null);

			return clientPhoto;
		}

	}

	public byte[] clientPhoto() {

		BufferedImage clientPhoto = this.takeClientPhoto();// �õ��û���Ƭ
		if (clientPhoto != null) {
			byte[] clientPhotoBytes = this.imageToBytes(clientPhoto);// ���û���Ƭת������
			return clientPhotoBytes;
		} else {
			return null;
		}

	}

	public byte[] imageToBytes(BufferedImage clientPhoto) {// �û���Ƭת���byte����

		ByteArrayOutputStream bos = new ByteArrayOutputStream();// �ô����������ͼƬ�ı���
		try {
			ImageIO.write(clientPhoto, "JPEG", bos);// �ø÷�����clientPhoto��JPEG��ͼ��ѹ����ʽ�������bos
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte[] clientPhotoBytes = bos.toByteArray();
		return clientPhotoBytes;

	}

}

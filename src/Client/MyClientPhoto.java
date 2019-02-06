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
		}// 获取socket的输出流
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
						this.sendClientPhoto(photoBytes);// 1.照相 2.发送照片
					}

				} else {
					System.out.println("cannot get image!");
				}
				Thread.sleep(100);// 控制每秒发10次
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
			dos.writeInt(clientPhotoBytes.length);// 获得数组的长度并写入输出流
			dos.write(clientPhotoBytes);// 将屏幕图像的数组写到输出流
			dos.flush();// 发送输出流

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void StartPlayer() {
		String str1 = "vfw:Logitech USB Video Camera:0";
		String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";

		di = CaptureDeviceManager.getDevice(str2);
		// 得到摄像头player
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

	private BufferedImage takeClientPhoto() {// *********************************得到图片
		FrameGrabbingControl fgc = (FrameGrabbingControl) player
				.getControl("javax.media.control.FrameGrabbingControl");
		buf = fgc.grabFrame(); // 获取当前祯并存入Buffer类
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

		BufferedImage clientPhoto = this.takeClientPhoto();// 得到用户照片
		if (clientPhoto != null) {
			byte[] clientPhotoBytes = this.imageToBytes(clientPhoto);// 将用户照片转成数组
			return clientPhotoBytes;
		} else {
			return null;
		}

	}

	public byte[] imageToBytes(BufferedImage clientPhoto) {// 用户照片转码成byte数组

		ByteArrayOutputStream bos = new ByteArrayOutputStream();// 用此输出来进行图片的编码
		try {
			ImageIO.write(clientPhoto, "JPEG", bos);// 用该方法将clientPhoto用JPEG的图像压缩方式到输出流bos
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte[] clientPhotoBytes = bos.toByteArray();
		return clientPhotoBytes;

	}

}

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
		}// 获取socket的输出流

	}

	@Override
	public void run() {
		while (runningflag) {

			try {
				synchronized (this.lock) {
					this.sendScreenShoot(this.screenShoot());// 1.截屏 2.发送截屏
				}
				
				Thread.sleep(100);// 控制每秒发10次
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
		// 得到屏幕截图
		BufferedImage screenImage = this.getScreenCapture();
		byte[] screenBytes = this.imageToBytes(screenImage);// 将屏幕截图转成数组
		return screenBytes;

	}

	public void sendScreenShoot(byte[] screenBytes) {

		try {
			dos.writeInt(10);
			dos.writeInt(screenBytes.length);// 获得数组的长度并写入输出流
			dos.write(screenBytes);// 将屏幕图像的数组写到输出流
			dos.flush();// 发送输出流

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public BufferedImage getScreenCapture() {// 1.截屏
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 获取屏幕的大小

		Rectangle screenRect = new Rectangle(0, 0, screenSize.width,
				screenSize.height);// 创建一个矩形

		BufferedImage screenImage = this.createScreenCapture(screenRect);// 根据矩形的大小和位置来截屏robot自带方法

		return screenImage;

	}

	public byte[] imageToBytes(BufferedImage screenImage) {// 图片转码成byte数组

		ByteArrayOutputStream bos = new ByteArrayOutputStream();// 用此输出来进行图片的编码
		try {
			ImageIO.write(screenImage, "JPEG", bos);// 用该方法将screenImage用JPEG的图像压缩方式到输出流bos
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte[] screenBytes = bos.toByteArray();
		return screenBytes;

	}

}

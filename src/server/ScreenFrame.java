package server;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ScreenFrame extends JFrame implements Runnable {

	private String ipAddress;// 要截屏的用户ip地址
	private Socket clientSocket = null;
	private JPanel clientJp = null;
	private JPanel clientJp2 = null;
	private BufferedImage screenImage = null;
	private boolean runningflag = true;
	private DataInputStream dis = null;
	private JButton saveImage = null;
	private byte[] screenBytes = null;

	public boolean isRunningflag() {
		return runningflag;
	}

	public void setRunningflag(boolean runningflag) {
		this.runningflag = runningflag;
	}

	public void setScreenBytes(byte[] screenBytes) {
		this.screenBytes = screenBytes;
	}

	public ScreenFrame(Socket clientSocket, AskClient ask) {
		this.clientSocket = clientSocket;// 得到要截屏用户的socket
		InputStream is;
		try {
			is = clientSocket.getInputStream();
			dis = new DataInputStream(is); // 得到客户端发过来的输出流
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void initUI() {
		this.setTitle("client's screen");// 设置窗体标题
		this.setSize(500, 600);
		this.setDefaultCloseOperation(2);// 设置默认窗口关闭方式
		clientJp = new JPanel();
		clientJp2 = new JPanel();
		ButtonListener buttonListener = new ButtonListener(this);
		saveImage = new JButton("保存截屏");
		this.add(clientJp, BorderLayout.CENTER);// 在窗口上铺上桌布（截的图片）
		this.add(clientJp2, BorderLayout.NORTH);// 在窗口上铺上桌布（截的图片）
		clientJp2.add(saveImage);
		saveImage.addActionListener(buttonListener);

		this.setVisible(true);// 显示窗体

	}

	private void screenShoot() {
		try {
			if (screenBytes != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(screenBytes);// 将screenBytes数组放入输入流
				screenImage = ImageIO.read(bis);// 得到图片
				if (screenImage != null) {
					// 将截图画到canvas
					Graphics g = clientJp.getGraphics();
					g.drawImage(screenImage, 0, 0, clientJp.getWidth(),
							clientJp.getHeight(), null);
				} else {
					System.out.println("no image!");
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	public void saveScreenImage() { // 将图像保存

		try {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg",
					"gif");
			// 设置文件类型
			chooser.setFileFilter(filter);
			// 打开选择器面板
			int returnVal = chooser.showSaveDialog(new JPanel());
			// 保存文件从这里入手，输出的是文件名
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("你打开的文件夹是: "
						+ chooser.getSelectedFile().getPath());
				String path = chooser.getSelectedFile().getPath();
				try {

					File f = new File(path + ".jpg");
					System.out.println(f.getAbsolutePath());
					f.createNewFile();
					FileOutputStream fos = new FileOutputStream(f);

					fos.write(screenBytes);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (screenImage != null) {
			g.drawImage(screenImage, 0, 0, clientJp.getWidth(),
					clientJp.getHeight(), null);

		}

	}

	// 1.接受截图 2.画上去
	@Override
	public void run() {

		while (runningflag) {

			try {
				this.screenShoot();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}

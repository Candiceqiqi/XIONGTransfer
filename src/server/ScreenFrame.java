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

	private String ipAddress;// Ҫ�������û�ip��ַ
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
		this.clientSocket = clientSocket;// �õ�Ҫ�����û���socket
		InputStream is;
		try {
			is = clientSocket.getInputStream();
			dis = new DataInputStream(is); // �õ��ͻ��˷������������
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void initUI() {
		this.setTitle("client's screen");// ���ô������
		this.setSize(500, 600);
		this.setDefaultCloseOperation(2);// ����Ĭ�ϴ��ڹرշ�ʽ
		clientJp = new JPanel();
		clientJp2 = new JPanel();
		ButtonListener buttonListener = new ButtonListener(this);
		saveImage = new JButton("�������");
		this.add(clientJp, BorderLayout.CENTER);// �ڴ����������������ص�ͼƬ��
		this.add(clientJp2, BorderLayout.NORTH);// �ڴ����������������ص�ͼƬ��
		clientJp2.add(saveImage);
		saveImage.addActionListener(buttonListener);

		this.setVisible(true);// ��ʾ����

	}

	private void screenShoot() {
		try {
			if (screenBytes != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(screenBytes);// ��screenBytes�������������
				screenImage = ImageIO.read(bis);// �õ�ͼƬ
				if (screenImage != null) {
					// ����ͼ����canvas
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

	public void saveScreenImage() { // ��ͼ�񱣴�

		try {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg",
					"gif");
			// �����ļ�����
			chooser.setFileFilter(filter);
			// ��ѡ�������
			int returnVal = chooser.showSaveDialog(new JPanel());
			// �����ļ����������֣���������ļ���
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("��򿪵��ļ�����: "
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

	// 1.���ܽ�ͼ 2.����ȥ
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

package server;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
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

public class ClientphotoFrame extends JFrame implements Runnable {
	private JPanel clientJp = null;
	private JPanel clientJp2 = null;
	private Socket clientSocket;
	private DataInputStream dis = null;
	private boolean runningflag = true;
	private byte[] clientPhotoBytes = null;
	private BufferedImage clientPhoto = null;

	public boolean isRunningflag() {
		return runningflag;
	}

	public void setRunningflag(boolean runningflag) {
		this.runningflag = runningflag;
	}

	public byte[] getClientPhotoBytes() {
		return clientPhotoBytes;
	}

	public void setClientPhotoBytes(byte[] clientPhotoBytes) {
		this.clientPhotoBytes = clientPhotoBytes;
	}

	public ClientphotoFrame(Socket clientSocket, AskClient ask) {
		this.clientSocket = clientSocket;
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
		this.setTitle("client's photo");// ���ô������
		this.setSize(500, 600);
		this.setDefaultCloseOperation(2);// ����Ĭ�ϴ��ڹرշ�ʽ
		clientJp = new JPanel();
		clientJp2 = new JPanel();

		ButtonListener buttonListener = new ButtonListener(this);
		JButton saveClientPhoto = new JButton("������Ƭ");
		this.add(clientJp, BorderLayout.CENTER);// �ڴ����������������ص�ͼƬ��
		this.add(clientJp2, BorderLayout.NORTH);// �ڴ����������������ص�ͼƬ��
		clientJp2.add(saveClientPhoto);
		saveClientPhoto.addActionListener(buttonListener);
		this.setVisible(true);// ��ʾ����

	}

	private void takePhoto() {

		try {
			
			if (clientPhotoBytes != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(
						clientPhotoBytes);// ��screenBytes�������������
				clientPhoto = ImageIO.read(bis);// �õ�ͼƬ
				if (clientPhoto != null) {
					// ����ͼ����canvas
					Graphics g = clientJp.getGraphics();
					g.drawImage(clientPhoto, 0, 0, clientJp.getWidth(),
							clientJp.getHeight(), null);
				} else {
					System.out.println("no image!");
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveClientPhoto() { // ��ͼ�񱣴�

		try {
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		         "jpg", "gif");
		    //�����ļ�����
		    chooser.setFileFilter(filter);
		    //��ѡ�������
		    int returnVal = chooser.showSaveDialog(new JPanel());  
	                      //�����ļ����������֣���������ļ���
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("��򿪵��ļ�����: " +
		            chooser.getSelectedFile().getPath());
		       String path = chooser.getSelectedFile().getPath();
		       try {
		    	 
		    	   File f = new File(path+".jpg");
		    	   System.out.println(f.getAbsolutePath());
		    	   f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				
				fos.write(clientPhotoBytes);
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
		if (clientPhoto != null) {
			g.drawImage(clientPhoto, 0, 0, clientJp.getWidth(),
					clientJp.getHeight(), null);

		}

	}

	// 1.���ܽ�ͼ 2.����ȥ
	@Override
	public void run() {

		while (runningflag) {

			try {
				this.takePhoto();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}

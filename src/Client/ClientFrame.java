package Client;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientFrame extends JFrame { // Java的界面包

	private JPanel jpl = null;
	Socket socket = null;

	public static void main(String[] args) {

		ClientFrame cf = new ClientFrame();

		cf.initUI();

	}

	public ClientFrame() {

	}

	public void initUI() {
		this.setVisible(true);// 显示窗体
		this.setTitle("受控端");// 设置窗体标题
		this.setSize(300, 600);
		this.setResizable(false);
		this.setDefaultCloseOperation(3);// 设置默认窗口关闭方式
		jpl = new JPanel();
		this.add(jpl);
		jpl.setLayout(null);
		Color background = new Color(0.5568f, 0.2627f, 0, 0.6f);
		jpl.setBackground(background);
		JButton connectButton = new JButton("开启被监控");
		JButton closeButton = new JButton("关闭被监控");

		Color button = new Color(0.996f, 0.76868f, 0, 1f);
		connectButton.setBounds(80, 200, 150, 80);
		closeButton.setBounds(80, 300, 150, 80);
		connectButton.setBackground(button);
		closeButton.setBackground(button);
		connectButton.setFocusPainted(false);
		closeButton.setFocusPainted(false);
		Font font = new Font("黑体", Font.BOLD, 20);
		connectButton.setFont(font);
		closeButton.setFont(font);
		jpl.add(connectButton);
		jpl.add(closeButton);
		ButtonListener bl = new ButtonListener(this);
		connectButton.addActionListener(bl);// 加入按钮监听器
		closeButton.addActionListener(bl);
		this.paint(this.getGraphics());// 窗体有变动的时候重绘窗体
		this.setVisible(true);

	}

	public void trytoConnectServer(String ip, int port) {

		try {
			this.socket = new Socket(ip, port);
			System.out.println("connect successfully!");// 客户端连接服务器
			RespondAsk respondAsk = new RespondAsk(socket);
			Thread waitScreenshoot = new Thread(respondAsk);
			waitScreenshoot.start();

		} catch (Exception e) {

			// e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

}

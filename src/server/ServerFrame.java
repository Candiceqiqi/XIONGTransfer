package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class ServerFrame extends JFrame implements Runnable { // Java的界面包

	private JPanel jp = null;
	private JPanel jp1 = null;
	private JPanel jp2 = null;
	private ClientGuider clientGuider = null;
	private ButtonGroup buttoungroup = null;
	private JScrollPane jsp = null;
	private boolean runningflag = true;
	private RadiobuttonListener selectedListener;
	private ScreenFrame sf;
	private ClientphotoFrame cpf;
	private Color button;
	private Color background;

	private JButton getScreen;
	private JButton getPhoto;
	private JButton logOut;
	private JButton closeAll;

	public static void main(String[] args) {
		ServerFrame sf = new ServerFrame();
		sf.initUI();
	}

	public void initUI() {
		this.setTitle("主控端");// 设置窗体标题
		this.setSize(300, 600);// 设置窗体大小
		this.setDefaultCloseOperation(0);// 设置默认窗口关闭方式

		background = new Color(0.5568f, 0.2627f, 0, 0.6f);// 设置背景颜色
		button = new Color(0.996f, 0.76868f, 0, 1f);// 设置按钮颜色
		jp = new JPanel();
		jp.setBackground(background);
		this.add(jp);
		jp.setLayout(null);// 设置流布局

		this.setResizable(false);
		JButton s_server = new JButton("启动监控系统");// 添加启动服务器的按钮
		s_server.setBounds(80, 200, 150, 80);
		s_server.setMargin(new Insets(0, 0, 0, 0));
		jp.add(s_server);

		Font font = new Font("黑体", Font.BOLD, 20);

		ButtonListener bl = new ButtonListener(this);
		s_server.addActionListener(bl);// 加入按钮监听器
		s_server.setBackground(button);
		s_server.setFont(font);

		WindowsListener wl = new WindowsListener(this);
		s_server.setFocusPainted(false);
		this.addWindowListener(wl);
		this.setVisible(true);// 显示窗体

	}

	public void changeUI() {// 设置窗口2的布局

		this.remove(jp);// 移除控件jp
		this.setSize(400, 600);
		this.setLayout(null);
		this.setTitle("监控系统已经打开");
		button = new Color(0.7529f, 0.380f, 0.1058f, 1f);
		background = new Color(0.98f, 0.769f, 0, 1f);

		// 布置用户列表容器
		jp1 = new JPanel();// jp1放到jsp上面去
		jsp = new JScrollPane(jp1);// 带滚动条的一块
		jsp.setBackground(background);
		jsp.setBounds(new Rectangle(400, 505));
		jp1.setBounds(new Rectangle(370, 500));
		BoxLayout bly = new BoxLayout(jp1, BoxLayout.Y_AXIS);// 盒子布局
		jp1.setLayout(bly);
		jp1.setBackground(background);
		TitledBorder inputPanelBorder = new TitledBorder(
				"--------扫描主机---------------------------------------------------------------------------");
		jsp.setBorder(inputPanelBorder);
		// 测试
		for (int i = 1; i < 50; i++) {

			jp1.add(new JButton(i + ""));
		}

		// 布置功能按钮容器
		jp2 = new JPanel();
		BoxLayout bly2 = new BoxLayout(jp2, BoxLayout.Y_AXIS);// 盒子布局
		jp2.setLayout(bly2);
		jp2.setSize(new Dimension(90, 10));
		jp2.setBackground(background);
		jp2.setBounds(0, 503, 400, 100);

		getScreen = new JButton("监控桌面");
		getPhoto = new JButton("查看人脸");
		logOut = new JButton("断开监控");
		closeAll = new JButton("退出系统");
		getScreen.setBackground(button);
		getPhoto.setBackground(button);
		logOut.setBackground(button);
		closeAll.setBackground(button);
		getScreen.setForeground(Color.WHITE);
		getPhoto.setForeground(Color.WHITE);
		logOut.setForeground(Color.WHITE);
		closeAll.setForeground(Color.WHITE);
		TitledBorder inputPanelBorder2 = new TitledBorder(
				"--------监控操作---------------------------------------------------------------------------");
		jsp.setBorder(inputPanelBorder);
		jp2.setBorder(inputPanelBorder2);
		Box vtemp = Box.createHorizontalBox();
		vtemp.add(getScreen);
		vtemp.add(getPhoto);
		vtemp.add(logOut);
		vtemp.add(closeAll);
		jp2.add(vtemp);

		this.add(jsp);
		this.add(jp2);

		ButtonListener bl = new ButtonListener(this);

		getScreen.addActionListener(bl);// 添加按钮监听器
		getPhoto.addActionListener(bl);
		logOut.addActionListener(bl);
		closeAll.addActionListener(bl);

		this.paint(this.getGraphics());// 窗体有变动的时候重绘窗体
		this.setVisible(true);// 设置窗口可见

		Thread allclients = new Thread(this);
		allclients.start();
	}

	public void updateClientlistUI() {

		List<Socket> clientList = clientGuider.getClientList();

		jp1.removeAll();// 每次重绘前清空jp1
		buttoungroup = new ButtonGroup();

		for (int i = 0; i < clientList.size(); i++) {
			Socket client = clientList.get(i);// 得到连入客户端的socket
			try {
				String clientAddress = client.getInetAddress().getHostAddress();// 得到连入客户端的ip地址的string
				if (client.getKeepAlive()) { // 判断是否还在线
					JRadioButton clientButton = new JRadioButton(clientAddress);
					clientButton.setBackground(null);
					clientButton.addActionListener(selectedListener);// 加入按钮监听
					if (clientAddress.equals(selectedListener.getSelected())) {
						clientButton.setSelected(true);
						clientButton.setEnabled(false);
					}
					if (getScreen.isEnabled() == false
							|| getPhoto.isEnabled() == false) {
						clientButton.setEnabled(false);
						logOut.setEnabled(false);
					} else {
						logOut.setEnabled(true);
					}
					buttoungroup.add(clientButton);// 将连入的客户端加进去
					jp1.add(clientButton);
				} else {
					if (clientAddress.equals(selectedListener.getSelected())) {
						this.logOutClinet();
					}
					System.out.println("close!");
					clientGuider.removeClientList(client);
					client.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jsp.updateUI();
		jp2.updateUI();
	}

	public void createScreenFrame() {// 查看桌面图像的截图
		if (selectedListener.getSelected() != "") {
			getScreen.setEnabled(false);
			logOut.setEnabled(false);
			// 让客户端截屏发送过来
			Socket client = selectedListener.getClientSocket();
			AskClient ask = new AskClient(client);
			ask.askScreenShoot();

			sf = new ScreenFrame(client, ask);

			WindowsListener WindowLs = new WindowsListener(this, ask, sf);
			sf.addWindowListener(WindowLs);

			sf.initUI();
			Thread receiveScreenShoot = new Thread(sf);
			receiveScreenShoot.start();
		}
	}

	public void createClientphotoFrame() {// 查看用户人脸的窗口
		if (selectedListener.getSelected() != "") {
			getPhoto.setEnabled(false);
			logOut.setEnabled(false);
			Socket client = selectedListener.getClientSocket();
			AskClient ask = new AskClient(client);
			ask.askTakePhoto();

			cpf = new ClientphotoFrame(client, ask);

			WindowsListener WindowLs = new WindowsListener(this, ask, cpf);
			cpf.addWindowListener(WindowLs);

			cpf.initUI();
			Thread receiveClientPhoto = new Thread(cpf);
			receiveClientPhoto.start();
		}
	}

	public void logOutClinet() {
		if (selectedListener.getSelected() != "") {
			selectedListener.setSelected("");
			Receiver receiver = selectedListener.getReceiver();
			receiver.close();
			Socket client = selectedListener.getClientSocket();
			clientGuider.removeClientList(client);
			try {
				client.setKeepAlive(false);
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeSystem() {
		if (selectedListener.getSelected() != "") {
			this.logOutClinet();
		} else {

		}
		System.exit(0);
	}

	public void CreateServer(int port) { // 指定端口号
		try {
			ServerSocket server = new ServerSocket(port);// 创建服务器
			System.out.println("create server successfully!");
			clientGuider = new ClientGuider(server, this);// 创建线程，等待客户端连接
			selectedListener = new RadiobuttonListener(
					clientGuider.getClientList(), this);
			Thread th = new Thread(clientGuider);
			th.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			while (runningflag) {
				this.updateClientlistUI();
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public JButton getGetScreen() {
		return getScreen;
	}

	public void setGetScreen(JButton getScreen) {
		this.getScreen = getScreen;
	}

	public JButton getGetPhoto() {
		return getPhoto;
	}

	public void setGetPhoto(JButton getPhoto) {
		this.getPhoto = getPhoto;
	}

	public JButton getLogOut() {
		return logOut;
	}

	public void setLogOut(JButton logOut) {
		this.logOut = logOut;
	}

	public ClientphotoFrame getCpf() {
		return cpf;
	}

	public void setCpf(ClientphotoFrame cpf) {
		this.cpf = cpf;
	}

	public ScreenFrame getSf() {
		return sf;
	}

	public void setSf(ScreenFrame sf) {
		this.sf = sf;
	}

}

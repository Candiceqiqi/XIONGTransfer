package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ButtonListener implements ActionListener {

	private ClientFrame cf;

	public ButtonListener(ClientFrame cf) {
		this.cf = cf;

	}

	@Override
	public void actionPerformed(ActionEvent e) {// 当事件e放生时就调用该方法
		System.out.println("try to connect!");

		try {
			if (e.getActionCommand().equals("开启被监控")) {
				cf.trytoConnectServer("127.0.0.1", 9999);// 尝试连接指定ip的服务器的指定端口
			} else if (e.getActionCommand().equals("关闭被监控")) {
				System.exit(0);
				System.out.println("断开连接");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}

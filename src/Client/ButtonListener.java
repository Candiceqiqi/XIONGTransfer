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
	public void actionPerformed(ActionEvent e) {// ���¼�e����ʱ�͵��ø÷���
		System.out.println("try to connect!");

		try {
			if (e.getActionCommand().equals("���������")) {
				cf.trytoConnectServer("127.0.0.1", 9999);// ��������ָ��ip�ķ�������ָ���˿�
			} else if (e.getActionCommand().equals("�رձ����")) {
				System.exit(0);
				System.out.println("�Ͽ�����");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}

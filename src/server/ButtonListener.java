package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ButtonListener implements ActionListener {

	private ServerFrame sf;
	private ScreenFrame scf;
	private ClientphotoFrame cpf;
	
	public ButtonListener(ServerFrame sf) {
		this.sf = sf;

	}
	public ButtonListener(ScreenFrame scf) {
		this.scf = scf;

	}
	public ButtonListener(ClientphotoFrame cpf) {
		this.cpf = cpf;

	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {// ���¼�e����ʱ�͵��ø÷���
		System.out.println("button was clicked!");
		if (e.getActionCommand().equals("�������ϵͳ")) {
			sf.CreateServer(9999);
			sf.changeUI();
		}else if(e.getActionCommand().equals("�������")){
			sf.createScreenFrame();
		}else if(e.getActionCommand().equals("�鿴����")){
			sf.createClientphotoFrame();
		}else if(e.getActionCommand().equals("�������")){
			scf.saveScreenImage();
		}else if(e.getActionCommand().equals("������Ƭ")){
			cpf.saveClientPhoto();
		}else if(e.getActionCommand().equals("�Ͽ����")){
			int i = JOptionPane.showConfirmDialog(null, "ȷ���Ͽ�?", "����!", 2);
			if (i == 0) {
				sf.logOutClinet();
			}
		}else if(e.getActionCommand().equals("�˳�ϵͳ")){
			int i = JOptionPane.showConfirmDialog(null, "ȷ���˳�ϵͳ?", "����!", 2);
			if (i == 0) {
				sf.closeSystem();
			}
		}

	}
	

}

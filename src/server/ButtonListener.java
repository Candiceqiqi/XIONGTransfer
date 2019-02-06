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
	public void actionPerformed(ActionEvent e) {// 当事件e放生时就调用该方法
		System.out.println("button was clicked!");
		if (e.getActionCommand().equals("启动监控系统")) {
			sf.CreateServer(9999);
			sf.changeUI();
		}else if(e.getActionCommand().equals("监控桌面")){
			sf.createScreenFrame();
		}else if(e.getActionCommand().equals("查看人脸")){
			sf.createClientphotoFrame();
		}else if(e.getActionCommand().equals("保存截屏")){
			scf.saveScreenImage();
		}else if(e.getActionCommand().equals("保存照片")){
			cpf.saveClientPhoto();
		}else if(e.getActionCommand().equals("断开监控")){
			int i = JOptionPane.showConfirmDialog(null, "确定断开?", "警告!", 2);
			if (i == 0) {
				sf.logOutClinet();
			}
		}else if(e.getActionCommand().equals("退出系统")){
			int i = JOptionPane.showConfirmDialog(null, "确定退出系统?", "警告!", 2);
			if (i == 0) {
				sf.closeSystem();
			}
		}

	}
	

}

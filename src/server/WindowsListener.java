package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class WindowsListener extends WindowAdapter {

	private boolean runningflag = true;
	private AskClient ask;
	private ScreenFrame snf;
	private ClientphotoFrame cpf;
	private ServerFrame sf;

	public WindowsListener(ServerFrame sf, AskClient ask, ScreenFrame snf) {
		this.sf = sf;
		this.ask = ask;
		this.snf = snf;

	}

	public WindowsListener(ServerFrame sf, AskClient ask, ClientphotoFrame cpf) {
		this.sf = sf;
		this.ask = ask;
		this.cpf = cpf;

	}

	public WindowsListener(ServerFrame sf) {
		this.sf = sf;
	}

	public void windowClosed(WindowEvent e) {
		// 1.关闭自己的线程
		runningflag = false;
		if (e.getSource().getClass().equals(ScreenFrame.class)) {
			System.out.println("close sreen");
			snf.setRunningflag(runningflag);
			ask.closeScreenShoot();
			sf.getGetScreen().setEnabled(true);
		} else if (e.getSource().getClass().equals(ClientphotoFrame.class)) {
			System.out.println("close photo");
			cpf.setRunningflag(runningflag);
			ask.closeTakePhoto();
			sf.getGetPhoto().setEnabled(true);
		}

	}

	public void windowClosing(WindowEvent e) {
		if (e.getSource().getClass().equals(ServerFrame.class)) {
			System.out.println("close system");
			int i = JOptionPane.showConfirmDialog(null, "确定退出系统?", "警告!", 2);
			if (i == 0) {
				System.exit(0);
			}
		}
	}

}

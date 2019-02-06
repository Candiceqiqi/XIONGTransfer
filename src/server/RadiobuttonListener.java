package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.List;

import javax.swing.JRadioButton;

public class RadiobuttonListener implements ActionListener {

	private String selected = "";
	private Socket clientSocket = null;
	private Receiver receiver = null;
	private List<Socket> clientList;
	private ServerFrame sf;
//	private boolean isSelected = false;

	public RadiobuttonListener(List<Socket> clientList, ServerFrame sf) {

		this.clientList = clientList;
		this.sf = sf;

	}
	

	public Socket getClientSocket() {
		return clientSocket;
	}



	public void actionPerformed(ActionEvent e) {
//		if(isSelected == false){
//			isSelected=true;
			JRadioButton jrb = (JRadioButton)e.getSource();
			jrb.setEnabled(false);
//			this.
			selected = e.getActionCommand();// 得到被鼠标选中的用户
			for (int i = 0; i < clientList.size(); i++) {
				Socket client = clientList.get(i);// 得到连入客户端的socket
				if (selected.equals(client.getInetAddress().getHostAddress())) {
					this.clientSocket = client;
					receiver = new Receiver(clientSocket, sf);
					receiver.start();
					break;
				}
			}

//		}
	}



	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getSelected() {
		return selected;
	}

//	public void setIsSelected(boolean isSelected) {
//		this.isSelected = isSelected;
//	}
//	public boolean getIsSelected() {
//		return isSelected;
//	}
	
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}


}

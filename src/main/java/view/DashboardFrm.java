package view;

import javax.swing.*;
import java.time.LocalDateTime;

public class DashboardFrm extends JFrame {
    private JTextArea txaIP;
    private JLabel lblIP;

    public DashboardFrm() {
        this.setTitle("[SERVER] - Game đuổi hình bắt chữ");
        this.setSize(500, 500);

        this.lblIP = new JLabel("IP CLIENT connecting:");
        this.lblIP.setBounds(50, 50, 400, 50);

        txaIP = new JTextArea();
        txaIP.setLineWrap(true);
        txaIP.setWrapStyleWord(true);
        txaIP.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(txaIP);
        scrollPane.setBounds(50, 100, 400, 300);

        this.add(this.lblIP);
        this.add(scrollPane);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);

    }

    public void setIPClientConnected(String IP, int PORT) {
        this.txaIP.setText("[Client] - [" + IP + ":" + PORT + "] - [" + LocalDateTime.now() + "]\n" + this.txaIP.getText());
    }

}

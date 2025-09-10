import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class frameMain extends JFrame {

    private frameMain mainFrame; // เพิ่มตัวแปรเพื่อเก็บ instance ของ frameMain

    public frameMain() {
        mainFrame = this; // ตั้งค่าตัวแปร mainFrame ให้เป็น instance ของ frameMain
        setBounds(450, 200, 1080, 700);
        setUndecorated(true); // ทำให้ไร้ขอบ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(173, 216, 230));

        // ปุ่มสามปุ่ม
        JPanel main = new JPanel(new GridLayout(3, 1, 20, 20));
        main.setBackground(new Color(173, 216, 230));
        main.setOpaque(false);
        main.setBounds(350, 300, 350, 200);

        JButton sbutton = new JButton("Start");
        sbutton.setBackground(new Color(145, 255, 147));
        sbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent exx) {
                JFrame frameStart = new JFrame("Process");
                process panelStart = new process();
                frameStart.add(panelStart);
                frameStart.setBounds(200, 150, 1260, 830);
                frameStart.setUndecorated(true); // ทำให้ไร้ขอบ
                frameStart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameStart.setVisible(true);
                setVisible(false);
                //เป็นต้นแบบในการรอค่า DISPOSE จากฝั้ง Start
                frameStart.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        mainFrame.setVisible(true); // แสดงหน้าต่างหลักอีกครั้งเมื่อหน้าต่างใหม่ปิด
                    }
                });
            }
        });

        JButton Cbutton = new JButton("Credit");
        Cbutton.setBackground(new Color(237, 252, 0));
        Cbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Animated Images Example");
                Credit panel = new Credit();
                panel.setBounds(200, 150, 1260, 830);
                frame.add(panel);
                frame.setBounds(200, 150, 1260, 830);
                frame.setUndecorated(true); // ทำให้ไร้ขอบ
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                setVisible(false);
                //เป็นต้นแบบในการรอค่า DISPOSE จากฝั้ง credit
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true); // แสดงหน้าต่างหลักอีกครั้งเมื่อหน้าต่างใหม่ปิด
                    }
                });
            }
        });

        JButton Ebutton = new JButton("Exit");
        Ebutton.setBackground(new Color(246, 71, 71));
        Ebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // ปิดโปรแกรมทั้งหมด
            }
        });

        main.add(sbutton);
        main.add(Cbutton);
        main.add(Ebutton);
        add(main, BorderLayout.CENTER);

        JPanel imJPanel = new JPanel();
        imJPanel.setLayout(new GridLayout(1, 1)); // กำหนดขนาดที่เหมาะสม
        imJPanel.setBounds(0, 0, 1080, 700);

         //โหลดรูปภาพและสร้าง Label
        ImageIcon imageIcon = new ImageIcon("PM2.5-scaled.jpg");
        JLabel imageLabel = new JLabel(imageIcon);
        imJPanel.add(imageLabel);
        add(imJPanel);
    }

}


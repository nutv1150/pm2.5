import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Credit extends JPanel implements ActionListener {
    

    public Credit() {
        setLayout(null);

       
        
        // โหลดรูปภาพและสร้าง Label
        ImageIcon imageIcon = new ImageIcon("1.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(150, 150, 300, 300);
        add(imageLabel);

        ImageIcon imageIcon1 = new ImageIcon("2.png");
        JLabel imageLabel1 = new JLabel(imageIcon1);
        imageLabel1.setBounds(450, 250, 300, 300);
        add(imageLabel1);

        ImageIcon imageIcon2 = new ImageIcon("3.png");
        JLabel imageLabel2 = new JLabel(imageIcon2);
        imageLabel2.setBounds(750, 150, 300, 300);
        add(imageLabel2);

        // ปุ่มกดย้อนกลับ
        JButton ExitButton = new JButton("Return to Menu");
        ExitButton.setBounds(550, 0, 150, 70);
        Font font2 = new Font("Arial", Font.BOLD, 13);
        ExitButton.setFont(font2);
        // สร้างปุ่มให้ไม่มีขอบ
        ExitButton.setBorderPainted(false);
        ExitButton.setFocusPainted(false);
        ExitButton.setContentAreaFilled(false);

        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // เมื่อปุ่ม Close ถูกกด, จะปิด framecradet และแสดง frameMain
                Window window = SwingUtilities.getWindowAncestor(ExitButton); 
                if (window != null) {//หรือมีการกดที่ปุ่ม ExitButton
                    window.dispose(); // ปิดหน้าต่างปัจจุบัน (framecradet)
                }
            }
        });
        
            // เมื่อปุ่ม Close ถูกกด, จะปิด framecradet และแสดง frameMain
            
        add(ExitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

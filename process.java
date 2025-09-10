import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class process extends JPanel implements ActionListener {

    // เก็บข้อมูลจากไฟล์ในรูปแบบ List ของ array
    private  Map<Integer, String[]> dataMap = new HashMap<>(); //เก็บข้อูลของ data ที่นำเข้ามา
    private  Map<Integer, String> datarandom = new HashMap<>();//เก็บdata สุ่มประชากรในช่วงที่เรานำเข้ามา
    private int row=200; //กำหนดขนาดปุ่ม
    private double  dataPM;//เก็บข้อมูล pm2.5
    private double inputValue; //นวนประชากรที่นำเข้า
    private double ppS = 0; //ประชากรที่ป่วย
    private double ppG = 0; //ประชากรที่สุขภาพดี
    private double pp = 0; //ประชากรทั้งหมด
    private double ppAvg = 0; //ร้อยละที่ป่วย
    private Random random = new Random(); //สร้างเพือ นำไปสุ่ม
    private int selectedButtonIndex = 0; //เก็บค่าต่ำแหน่งปุ่มที่คลิ๊ก

    // JTextArea สำหรับแสดงข้อมูล
    private JTextArea textarea = new JTextArea();
    private final ArrayList<JButton> buttonList = new ArrayList<>();//เก็บปุ่ม
    private int[] ran = new int[4];//เก็บค่าที่ random ตามเปอร์เซนคนป่วย

    public process() {
        
        setLayout(null);
        setBackground(new Color(10, 76, 163));
        
        ran[0] = random.nextInt(9) + 0;
        ran[1] = random.nextInt(19) + 10;
        ran[2] = random.nextInt(29) + 20;
        ran[3] = random.nextInt(50) + 30;

       // JTextArea สำหรับแสดงข้อมูล
        JPanel panelta = new JPanel(null);
        Font font1 = new Font("", Font.BOLD, 18);
        panelta.setBounds(840, 57, 350, 350);
         textarea.setBackground(Color.YELLOW);
         textarea.setSize(350, 350);
         textarea.setFont(font1);
         panelta.add(textarea);

       // Panel สำหรับปุ่ม 20x10
        JPanel panelB = new JPanel(new GridLayout(10, 20, 3, 3));
        panelB.setBounds(61, 57, 700, 550);
        panelB.setBackground(new Color(226, 246, 255));
        
        // สร้างปุ่ม 200 ปุ่ม
        for (int i = 0; i < row; i++) {
            JButton buttoni = new JButton();
            
            final int currentRow = i;
        
            // เก็บปุ่มใน buttonList
            buttonList.add(buttoni);
        
            buttoni.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] dataAtRow = dataMap.get(currentRow);//แปลงเป็น String array ที่ต่ำแหน่งcurrentRow หรือ i
                    JButton clickedButton = (JButton) e.getSource();//ส่งปุ่มที่ถูกกด
                    int buttonIndex = buttonList.indexOf(clickedButton);//และเช๊คต่อว่าclickedButton คือปุ่มเท่าไหร่ และเก็บใน buttonIndex
                    
            
                    try {
                        if (dataAtRow != null && dataAtRow.length > 0) {//เช็คค่าว่าเป็นไม่ใช่null และ ขนาดมาตราฐานของdataAtRow กว่า0
                            dataPM = Double.parseDouble(dataAtRow[0]); //แปลง dataAtRow[0] เป็น double
                            Startupprocess(currentRow);//นำต่ำแหน่งเข้าเพื่อไปคำนวนต่อใน mathod 
                            selectedButtonIndex = buttonIndex;// เก็บค่าต่ำแหน่งของปุ่มเพื่อนำไปใช้ต่อ
                        } else {
                            //เมื่อไม่มีข้อมูลจะแสดงข้อความนี้
                            textarea.setText("No data loaded for row " + currentRow);
                        }
                    } catch (NumberFormatException ex) {
                        //เมื่อข้อมมูลไม่ครบคือขาดจำนวนประชากร จะแสดงข้อความนี้และตำแหน่งของปุ่ม
                        textarea.setText("not data People " + currentRow);
                    }
                }
            });
            panelB.add(buttoni);
        }
        
        

    

        add(panelta);
        add(panelB);

        // เลือกไฟล์
        JPanel panelsf = new JPanel(null);
        panelsf.setBounds(840, 470, 350, 30);
        JTextField textfield = new JTextField();
        textfield.setText("Select File");
        textfield.setSize(350, 30);
        panelsf.add(textfield);

        add(panelsf);

        // ปุ่มเปิดไฟล์
        JPanel panelBsf = new JPanel(null);
        panelBsf.setBounds(840, 500, 350, 30);
        JButton buttonsf = new JButton("Open File");
        buttonsf.setSize(350, 30);
        panelBsf.add(buttonsf);
        buttonsf.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(); //obj เอาไว้ดึงหน้าต่างเลือกไฟล์

                int result = fileChooser.showOpenDialog(panelBsf);//หน้าต่างที่ให้เราเลือกไฟล์

                // ตรวจสอบว่าผู้ใช้คลิกปุ่ม "Open" หรือไม่
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();//เก็บค่าไฟลที่เลือกลง selectedfile
                    textfield.setText(selectedFile.getAbsolutePath());//แสดงชื่อ ไฟล์ที่เลือกในช่องด้านบน
                    readDataFromFile(selectedFile);//เรียกใช้ mathod อ่านค่าใน file ที่ส่งเข้าไป
                }
            }

        });

        add(panelBsf);

        // ใส่ข้อมูลจำนวนประชากร
        JPanel panelsp = new JPanel(null);
        panelsp.setBounds(840, 540, 350, 30);
        JTextField textfieldsp = new JTextField();
        textfieldsp.setSize(350, 30);
        panelsp.add(textfieldsp);

        // ปุ่มใส่ข้อมูลช่วงที่จะเริ่มสุ่ม
        JPanel panelsp2 = new JPanel(null);
        panelsp2.setBounds(840, 590, 170, 30);
        JTextField textfieldsp2 = new JTextField();
        textfieldsp2.setSize(350, 30);
        panelsp2.add(textfieldsp2);

        // ปุ่มใส่ข้อมูลช่วงที่จะสิ้นสุดการสุ่ม
        JPanel panelsp3 = new JPanel(null);
        panelsp3.setBounds(1010, 590, 180, 30);
        JTextField textfieldsp3 = new JTextField();
        textfieldsp3.setSize(350, 30);
        panelsp3.add(textfieldsp3);

        add(panelsp);
        add(panelsp2);
        add(panelsp3);
        
        // ปุ่มกดokตอนใส่ข้อมูลคนแบบมีช่วง
        JPanel panelBsp = new JPanel(null);
        panelBsp.setBounds(840, 620, 350, 30);
        JButton buttonsp = new JButton("OK");
        buttonsp.setSize(350, 30); 
        panelBsp.add(buttonsp);
        buttonsp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datarandom.clear();//ใช้ map เดียวกันแต่ทำการเคลียก่อน
                Random random = new Random();//สร้าง obj เพื่อใช่งาน random
                try {
                    int start = Integer.parseInt(textfieldsp2.getText());//เก็บค่าที่กรอกใน textfieldsp2
                    int end = Integer.parseInt(textfieldsp3.getText());//เก็บค่าที่กรอกใน textfieldsp3
                    for(int i=0 ;i<row;i++){
                        String ran=Integer.toString(random.nextInt(end - start +1)+start); //นำค่าเริ่มต้นและสิ้นสุดมากำหนดช่วงให้ random สุ่ม
                        datarandom.put(i,ran);// add ลง map
                    }
                } catch (NumberFormatException ex) {//ทำงานโดยที่ว่าโปรแกรมต้องการแปลงstring เป็นตัวเลข
                    //เมื่อข้อมมูลไม่ใช่ตัวเลข
                    textarea.setText("Population is not a number");
                }
                    
                    
            }
            
        });
        
        add(panelBsp);
        //ปุ่มokประชากรช่องเดียว
        JPanel panelok2 =new JPanel(null);
        panelok2.setBounds(840, 570, 350, 25);
        JButton buttonok2 = new JButton("OK");
        buttonok2.setSize(350, 20);
        panelok2.add(buttonok2);
        buttonok2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textfieldsp.getText();
                try {
                    inputValue = Double.parseDouble(text);
                } catch (NumberFormatException  ex) {//ทำงานโดยที่ว่าโปรแกรมต้องการแปลงstring เป็นตัวเลข
                     //เมื่อข้อมมูลไม่ใช่ตัวเลข
                    textarea.setText("Population is not a number");
                };
            }
        });
        add(panelok2);
        
        // รูปฝนจริง
        
        JPanel panelft = new JPanel();
        panelft.setOpaque(false);//panelโปร่งใส
        panelft.setLayout(new GridLayout(1, 1));
        panelft.setBounds(1060, 660, 130, 90);

       // โหลดรูปภาพและสร้าง JLabel
       JButton imageButton = new JButton();
       imageButton.setBorderPainted(false);
       imageButton.setFocusPainted(false);
       imageButton.setContentAreaFilled(false);
       ImageIcon icon = new ImageIcon("fontms.png");
       imageButton.setIcon(icon);//ใช้รูปภาพมาแทนรูปลักของปุ่ม
       imageButton.setBorderPainted(false); // ซ่อนขอบปุ่ม

       // เพิ่ม ActionListener ให้กับ JButton
       imageButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               // สิ่งที่จะเกิดขึ้นเมื่อคลิกปุ่มโยนไปทำยัง mathod rainFTam
               rainFTam();
               
           }
       });

       panelft.add(imageButton);
        add(panelft);

        // รูปฝนเทียม
        JPanel panelrain = new JPanel();
        panelrain.setOpaque(false);
        panelrain.setLayout(new GridLayout(1, 1));
        panelrain.setBounds(840, 660, 130, 90);

        JButton imageButton1 = new JButton();
        imageButton1.setBorderPainted(false);
       imageButton1.setFocusPainted(false);
       imageButton1.setContentAreaFilled(false);
        ImageIcon icon2 = new ImageIcon("fontrm.png");
        imageButton1.setIcon(icon2);
        imageButton1.setBorderPainted(false);

        imageButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //คลิ๊กแล้วจะมีการคำนวนใน mathod rainfantam
                rainfantam();
            }
        });
        panelrain.add(imageButton1);

        add(panelrain);

        // ปุ่มกดย้อนกลับ
        JButton ExitButton = new JButton("Return to Menu");
        ExitButton.setBounds(1100, 760, 150, 70);
        Font font2 = new Font("Arial", Font.BOLD, 13);

        ExitButton.setFont(font2);
        // สร้างปุ่มให้ไม่มีขอบ
        ExitButton.setBorderPainted(false);
        ExitButton.setFocusPainted(false);
        ExitButton.setContentAreaFilled(false);

        ExitButton.addActionListener(e -> {
            // เมื่อปุ่ม Close ถูกกด, จะปิด frameStart และแสดง frameMain
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose(); // ปิดหน้าต่างปัจจุบัน (frameStart)
                //โดยการ dispose เพื่อทำให้เข้า action ของ class frame
            }
        });
        add(ExitButton);

        // ช่องของการบอกว่าสีนี้กี่เปอร์เซ็น
        JPanel MGreen = new JPanel();
        JPanel InGreen = new JPanel();
        JLabel inLabel = new JLabel();
        MGreen.setBounds(45, 650, 180, 110);
        MGreen.setBackground(new Color(145, 255, 147));
        InGreen.setBounds(55, 660, 160, 50);
        InGreen.setBackground(new Color(48, 165, 7));
        inLabel.setText("มีคนป่วย 0-9%");
        Font font = new Font("Leelawadee", Font.BOLD, 15);
        inLabel.setFont(font);
        inLabel.setBounds(70, 718, 130, 30);
        add(inLabel);
        add(InGreen);
        add(MGreen);

        JPanel MYellow = new JPanel();
        JPanel InYellow = new JPanel();
        JLabel InLabel2 = new JLabel();
        MYellow.setBounds(230, 650, 180, 110);
        MYellow.setBackground(new Color(255, 254, 163));
        InYellow.setBounds(240, 660, 160, 50);
        InYellow.setBackground(new Color(231, 231, 21));
        InLabel2.setText("มีคนป่วย 10-19%");
        Font fontY = new Font("Leelawadee", Font.BOLD, 15);
        InLabel2.setFont(fontY);
        InLabel2.setBounds(260, 718, 140, 30);
        add(InLabel2);
        add(InYellow);
        add(MYellow);

        JPanel M_Orange = new JPanel();
        JPanel InOrange = new JPanel();
        JLabel InLabel3 = new JLabel();
        M_Orange.setBounds(415, 650, 180, 110);
        M_Orange.setBackground(new Color(255, 211, 148));
        InOrange.setBounds(425, 660, 160, 50);
        InOrange.setBackground(new Color(254, 151, 3));
        InLabel3.setText("มีคนป่วย 20-29%");
        Font fontO = new Font("Leelawadee", Font.BOLD, 15);
        InLabel3.setFont(fontO);
        InLabel3.setBounds(447, 718, 140, 30);
        add(InLabel3);
        add(InOrange);
        add(M_Orange);

        JPanel MRed = new JPanel();
        JPanel InRed = new JPanel();
        JLabel InLabel4 = new JLabel();
        MRed.setBounds(600, 650, 180, 110);
        MRed.setBackground(new Color(254, 136, 136));
        InRed.setBounds(610, 660, 160, 50);
        InRed.setBackground(new Color(241, 31, 9));
        InLabel4.setText("มีคนป่วยเกิน 30%");
        Font fontR = new Font("Leelawadee", Font.BOLD, 15);
        InLabel4.setFont(fontR);
        InLabel4.setBounds(633, 718, 140, 30);
        add(InLabel4);
        add(InRed);
        add(MRed);
        //เบล๊คกาวของ บอกเปอร์เซ็นคนป่วย
        JPanel backpanel = new JPanel();
        backpanel.setBounds(35, 640, 755, 135);
        add(backpanel);
        JPanel backpanel20_10 = new JPanel();
        backpanel20_10.setBounds(35, 30, 755, 605);
        add(backpanel20_10);
        JPanel backpanelteaxareaA= new JPanel();
        backpanelteaxareaA.setBounds(810, 30, 400, 405);
        add(backpanelteaxareaA);

    }

    private void readDataFromFile(File file) {
        dataMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
    
            while ((line = reader.readLine()) != null) {//อ่านจนไม่มีข้อมูล หรือข้อมมูลเป็น null
                if (!line.trim().isEmpty()) {//เช๊คว่าบรรทัดที่ลบช่องว่างแล้วไม่เป็นสตริงว่าง
                    String[] data = line.split("\\s+");//แบ่งข้อมมูลใน line ออกเป็นส่วนๆหรือ tab
                    for (String number : data) {//ลูปทำงานเก็บ data ใน munber ของแต่ละข้อมูลจนครบหรือไม่มีข้อมูลเข้ามาแล้ว
                        if (!number.trim().isEmpty()) {//เช็คnumber ไม่เป็นค่าว่าง
                            char[] c = number.toCharArray();// เปลี่ยนเป็นchar array เพื่อเช็คว่าเป็นตัวเลขไหม
                            if (Character.isDigit(c[0])) { // เช็คตัวเลขก่อนทำงาน
                            dataMap.put(index, new String[]{number});
                            index++;
                            } else if (c[0] == '-') { // เช็คเครื่องหมายลบ
                            // ตรวจสอบว่ามีตัวเลขตามหลังเครื่องหมายลบไหม
                             if (c.length > 1 && Character.isDigit(c[1])) {
                                 // เพิ่มการจัดการข้อมูลที่เป็นเลขติดลบ
                            dataMap.put(index, new String[]{number});
                            index++;
                                } 
                            } 
                        }
                    }
                }
            }
            COLORBUTTON(); // เรียกใช้ COLORBUTTON หลังจากอ่านข้อมูล
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void Startupprocess(int buttonIndex){
        
        if (inputValue != 0) {
            pp = inputValue;
            
            // ตรวจสอบค่าของ dataPM
            if (dataPM < 0) {
                textarea.setText("Negative data");
            } else if (dataPM > 250) {
                textarea.setText("Too much information");
            } else {
                // การคำนวณข้อมูลตามช่วงของ dataPM
                if (dataPM >= 0 && dataPM <= 50) {
                    ppS = (double) inputValue / 100 * ran[0];
                } else if (dataPM >= 51 && dataPM <= 100) {
                    ppS = (double) inputValue / 100 * ran[1];
                } else if (dataPM >= 101 && dataPM <= 150) {
                    ppS = (double) inputValue / 100 * ran[2];
                } else if (dataPM >= 151 && dataPM <= 250) {
                    ppS = (double) inputValue / 100 * ran[3];
                }
        
                // การคำนวณข้อมูลที่เหลือ
                ppG = inputValue - ppS;
                ppAvg = (double) (ppS * 100) / inputValue;
                
                // แสดงผลข้อมูล
                textarea.setText(String.format("Dust volume: %.2f" +
                                            "\nPeople: %.2f" +
                                            "\nHealthy population: %.2f" +
                                            "\nSick population: %.2f" +
                                            "\nPercentage of the population: %.2f",
                                            dataPM, pp, ppG, ppS, ppAvg));
            }
        
        } else {
            if(buttonIndex >= 0 && buttonIndex < 200) {
             int sam = Integer.parseInt(datarandom.get(buttonIndex));
             pp = sam;
                    if (dataPM < 0) {
                        textarea.setText("Negative data");
                    } else if (dataPM > 250) {
                        textarea.setText("Too much information");
                    } else {
                            if(dataPM >= 0 && dataPM <= 50){
                                ppS =(double)  sam / 100 * ran[0];
                            } else if(dataPM >= 51 && dataPM <= 100){
                                ppS =(double)  sam / 100 * ran[1];
                            } else if(dataPM >= 101 && dataPM <= 150){
                                ppS =(double)  sam / 100 * ran[2];
                            } else if(dataPM >= 151 && dataPM <= 250){
                                ppS =(double) sam / 100 * ran[3];
                            }
    
        ppG = (double) sam - ppS;
        ppAvg = (double) (ppS * 100) / sam;
        
        textarea.setText(String.format("Dust volume: %.2f" +
                                "\nPeople: %.2f" +
                                "\nHealthy population: %.2f" +
                                "\nSick population: %.2f" +
                                "\nPercentage of the population: %.2f",
                                dataPM, pp, ppG, ppS, ppAvg));
        
        
         }
     
       }
    }
}
    
    

    
    // ลดค่า PM2.5 ของทุกปุ่มลง 50% ฝนธรรมชาติ
    private void rainFTam() {
        
        for (int i = 0; i < row; i++) {
            String[] dataAtRow = dataMap.get(i);
            if (dataAtRow != null && dataAtRow.length > 0) {
                try {
                    int dataPMh = Integer.parseInt(dataAtRow[0]);
                    dataPMh = Math.max(0, dataPMh / 2); // ป้องกันค่า PM2.5 เป็นลบ
                    dataMap.put(i, new String[]{Integer.toString(dataPMh)});
                } catch (NumberFormatException ex) {
                    // ... (จัดการกรณีเกิดข้อผิดพลาด)
                }
            }
        }
        COLORBUTTON();
    }

    private void rainfantam() {
        if(selectedButtonIndex != 0){
        int tn21l =Math.max(0,selectedButtonIndex-21);
        int tn20l =Math.max(0,selectedButtonIndex-20);
        int tn19l =Math.max(0,selectedButtonIndex-19);
        int tn_1 =Math.max(0,selectedButtonIndex-1);
        int tn1b =Math.max(0,selectedButtonIndex+1);
        int tn21b =Math.max(0,selectedButtonIndex+21);
        int tn20b =Math.max(0,selectedButtonIndex+20);
        int tn19b =Math.max(0,selectedButtonIndex+19);
        //System.out.println("tn21l"+tn21l+"  tn20l"+tn20l+"  tn19l"+tn19l+"  tn_1"+tn_1+"  tn1b"+tn1b+"  tn21b"+tn21b+"  tn20b"+tn20b+"  tn19b"+tn19b);
        
            String[] dataAtRow1 = dataMap.get(selectedButtonIndex);
            String[] dataAtRow2 = dataMap.get(tn21l);
            String[] dataAtRow3 = dataMap.get(tn20l);
            String[] dataAtRow4 = dataMap.get(tn19l);
            String[] dataAtRow5 = dataMap.get(tn_1);
            String[] dataAtRow6 = dataMap.get(tn1b);
            String[] dataAtRow7 = dataMap.get(tn21b);
            String[] dataAtRow8 = dataMap.get(tn20b);
            String[] dataAtRow9 = dataMap.get(tn19b);
            
    
                try {
                    int dataPM0 = Integer.parseInt(dataAtRow1[0]);
                    int dataPM00 = (int) Math.max(0, dataPM0 / 2);

                
                    int dataPM1 = Integer.parseInt(dataAtRow2[0]);
                    int dataPM2 = Integer.parseInt(dataAtRow3[0]);
                    int dataPM3 = Integer.parseInt(dataAtRow4[0]);
                    int dataPM4 = Integer.parseInt(dataAtRow5[0]);
                    int dataPM5 = Integer.parseInt(dataAtRow6[0]);
                    int dataPM6 = Integer.parseInt(dataAtRow7[0]);
                    int dataPM7 = Integer.parseInt(dataAtRow8[0]);
                    int dataPM8 = Integer.parseInt(dataAtRow9[0]);
                    int   dataPM11 = (int)Math.max(0, dataPM1-(dataPM1 *0.3));
                    int  dataPM22 = (int) Math.max(0,dataPM2-(dataPM2 *0.3));
                    int  dataPM33 = (int) Math.max(0,dataPM3-(dataPM3 *0.3));
                    int  dataPM44 = (int) Math.max(0,dataPM4-(dataPM4 *0.3));
                    int  dataPM55 = (int) Math.max(0,dataPM5-(dataPM5 *0.3));
                    int  dataPM66 = (int) Math.max(0,dataPM6-(dataPM6 *0.3));
                    int  dataPM77 = (int) Math.max(0,dataPM7-(dataPM7 *0.3));
                    int  dataPM88 = (int) Math.max(0,dataPM8-(dataPM8 *0.3));

                    dataMap.put(selectedButtonIndex, new String[]{Integer.toString(dataPM00)});
                    dataMap.put(tn21l, new String[]{Integer.toString(dataPM11)});
                    dataMap.put(tn20l, new String[]{Integer.toString(dataPM22)});
                    dataMap.put(tn19l, new String[]{Integer.toString(dataPM33)});
                    dataMap.put(tn_1, new String[]{Integer.toString(dataPM44)});
                    dataMap.put(tn1b, new String[]{Integer.toString(dataPM55)});
                    dataMap.put(tn21b, new String[]{Integer.toString(dataPM66)});
                    dataMap.put(tn20b, new String[]{Integer.toString(dataPM77)});
                    dataMap.put(tn19b, new String[]{Integer.toString(dataPM88)});
                } catch (NumberFormatException ex) {
                    // ... (จัดการกรณีเกิดข้อผิดพลาด)
                }
            }
            else {textarea.setText("Please select a button first.");}
            
        
        
            COLORBUTTONt();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void COLORBUTTON() {
    for (int i = 0; i < row; i++) {
        JButton buttoni = buttonList.get(i ); // นำปุ่มจาก buttonList
        String[] dataAtRow = dataMap.get(i);
        

        if (dataAtRow != null && dataAtRow.length > 0) {
            try {
                dataPM = Integer.parseInt(dataAtRow[0]);
            } catch (NumberFormatException ex) {
                dataPM = 0; // กำหนดค่าเริ่มต้นหรือค่าที่ต้องการเมื่อไม่สามารถแปลงได้
            }
        }

        if (dataPM >= 0 && dataPM <= 50) {
            buttoni.setBackground(new Color(48, 165, 7)); // สีเขียว
        } else if (dataPM >= 51 && dataPM <= 100) {
            buttoni.setBackground(new Color(231, 231, 21)); // สีเหลือง
        } else if (dataPM >= 101 && dataPM <= 150) {
            buttoni.setBackground(new Color(254, 151, 3)); // สีส้ม
        } else if (dataPM >= 151 && dataPM <= 250) {
            buttoni.setBackground(new Color(241, 31, 9)); // สีแดง
        } else {
            buttoni.setBackground(Color.GRAY); // สีเทาเป็นค่าเริ่มต้น
        }
        }
        
    }
    private void COLORBUTTONt() {
        // ตรวจสอบว่ามีข้อมูลใน dataMap หรือไม่
        if (dataMap != null) {
            // ลูปผ่านทุกปุ่มใน buttonList และ dataMap
            for (int i = 0; i < buttonList.size(); i++) {
                JButton button = buttonList.get(i);
                String[] dataAtRow = dataMap.get(i);
    
                if (dataAtRow != null && dataAtRow.length > 0) {
                    try {
                        dataPM = Integer.parseInt(dataAtRow[0]);
    
                        // เปลี่ยนสีของปุ่มตามค่า PM2.5
                        if (dataPM >= 0 && dataPM <= 50) {
                            button.setBackground(new Color(48, 165, 7)); // สีเขียว
                        } else if (dataPM >= 51 && dataPM <= 100) {
                            button.setBackground(new Color(231, 231, 21)); // สีเหลือง
                        } else if (dataPM >= 101 && dataPM <= 150) {
                            button.setBackground(new Color(254, 151, 3)); // สีส้ม
                        } else if (dataPM >= 151 && dataPM <= 250) {
                            button.setBackground(new Color(241, 31, 9)); // สีแดง
                        } else {
                            button.setBackground(Color.GRAY); // สีเทาเป็นค่าเริ่มต้น
                        }
                    } catch (NumberFormatException ex) {
                        // จัดการกรณีเกิดข้อผิดพลาด
                        button.setBackground(Color.GRAY); // สีเทาเป็นค่าเริ่มต้นหากเกิดข้อผิดพลาด
                    }
                } else {
                    button.setBackground(Color.GRAY); // สีเทาเป็นค่าเริ่มต้นหากไม่มีข้อมูล
                }
            }
        }
    }
    
}

    


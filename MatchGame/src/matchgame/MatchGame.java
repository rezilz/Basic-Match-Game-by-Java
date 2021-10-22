package matchgame;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

import javax.swing.*;


public class MatchGame extends JFrame implements ActionListener {
       String[] messageStrings = {"4*4 Mode","6*6 Mode"}; ////เป็นการกำหนดจำนวนเเละ Mode ของตัวเกมว่ามีให้เลือกถึงสาม Mode
       JComboBox cmbMessageList = new JComboBox (messageStrings); ////ทำการสร้าง ComboBox ขึ้น
       JLabel lblText = new JLabel(); ////เพื่อเเสดงตัวอักษรต่างๆ เมื่อเราทำการปรับเปลี่ยนค่าของ ComboBox 
       
	JPanel p;
	JFrame f;
        JLabel score1, score2,winner;
        
	MyButton[][] buttons;
        MyButton button1, button2,button3,button4; ////มีการเพิ่ม button 3 เเละ 4 ขึ้นเนื่องจากจะมีการใช้เป็นการจับคู่เป็น 4 คู่เเทน

        ImageIcon icon, defaultIcon;   // icon used for face-up image (BUGs) defaultIcon used for face-down image (PUCCA)
        BufferedImage image;
        
                
        int state = 0;               // 0=initial , 1=select 1 picture, 2=select 2 
        int matchScore=0, missedScore=0;
        boolean matched = false;    // keep previous state of player's card matching.
        int width=900,height=1000; ////กำหนดขนาดของความกว้างเเละความยาวของจอเอง เพื่อให้เรียกใช้ง่ายๆ
        int heightmenu=100;
        public int w,b; ////กำหนดตัวเเปรเพื่อใช้ในการเปลี่ยนด่าน จะอธิบายเพิ่มเติมด้านล่าง
        
        
        

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread
		//to create application and display its GUI
                 java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true); ////ตอนนี้เราจะไม่เลือกใช้ MatchGame เเบบเก่าเเล้วเราจะไปใช้ในเมนเมนูเเทน 
            }
        });		
	}
       
          public MatchGame() {
            setLayout(new FlowLayout());
            setSize(width,heightmenu); 
            setTitle("Pairing the same Bugs 4-4 || Main Menu");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            getContentPane().setBackground(new Color(224, 187, 228));
            
            cmbMessageList.setSelectedIndex(1); ////จะเป็นการเเสดง Mode ตอนกดเข้าเมนเมนูครั้งเเรกซึ่งจะปรับได้สามค่าคือ 0 1 2  ซึ่งจะเป็นเเบบ 4*4 5*5 เเละ 6*6 ตามลำดับ
            cmbMessageList.addActionListener(this);
            add(cmbMessageList);
            add(lblText);
            ////โดยรวมเเล้วเป็นการปรับเเต่งลักษณะภายนอกต่างๆของเมนเมนูต่างๆ เช่น สี การเเสดงผล เป็นต้น
        }

	public void generateCards(JPanel panel) {

                int [] cards = new int[9];   // Keep the number of image usage. Where the index of the array is smaller than the number used as the image name. 
                                              // if 1.png was used as an IconImage of and single button, the cards[0] will be set as 1 
                                              // Later, after 1.png was selected to be another button's IconImage, thus the cards[0] will be set as 2
                                              // And then 1.png file cannot be used as the other buttons. 
                                              
                                              ////ตรงนี้เราจะเปลี่ยนเป็น 9 เเทนเนื่องจากเราเปลี่ยนเกมเป็นจับคู่ 4 คู่เเทน ซึ่งจะใช้เเค่ 9 รูปเท่านั้น
                int x;
		buttons = new MyButton[6][6]; 
                Random rand = new Random();
                
                for(int i=0; i<9; i++){  ////ตรงนี้เราจะเปลี่ยนเป็น 9 เเทนเนื่องจากเราเปลี่ยนเกมเป็นจับคู่ 4 คู่เเทน ซึ่งจะใช้เเค่ 9 รูปเท่านั้น
                    cards[i]=0;   // initial all array's member to be 0
                }
                ////อธิบายเรื่องตัวเเปร w เป็นการกำหนดค่าขึ้นมาเพื่อทำให้เราสามารถเรียกใช้ด่านตามที่เราต้องการได้ ซึ่งหากเป็นด่าน 5*5 จะต้องมีค่า j<5 จึงทำให้ w=1 ซึ่งการกำหนดค่าของ w จะอยู่ด้านล่าง
		for (int i= 0; i< 5-w; i++) { 
                    for(int j=0; j<6-w; j++){
                        
                       do {
                           x = rand.nextInt(b);
                           
                           if(cards[x]<4) 
                           {
                              cards[x]++;
                              break;
                           }
                          
                       }while(cards[x]>=4); ////เพราะฉะนั้นเลข 2 ทั้งหมดใน while จะเปลี่ยนเป็น 4 เเทนเพื่อจับ 4 คู่เเทน
                       
                       //***** a loop to find a random integer x within a range of 0 to numberOfImage - 1 
                       //***** and check if it is available (the image usage times is less than 2)than use it 
                       //***** and increase the usage time of the image by one.
                       
                       addButton(panel,i,j,x+1);                       
                    }   
                }
                x = 0;
                for(int j=0; j<6-w; j++){  // This separated loop could solve the slow initialization problem of the last row buttons caused by the random process.

                   while(cards[x]>=4) 
                       x++;
                   //***** scan the cards[] array to find the image that was used less than 2 times and then use it.
                        cards[x]++;
                   addButton(panel,5,j,x+1);
                  
                   
                }

        }
        
        public void addButton(JPanel panel,int i, int j,int val){
                       
                        String iconFile =  "bug/" + val + ".png";
                        image = loadImage(iconFile);
                        ImageIcon bIcon = new ImageIcon(image);
                        
                        
                        //*****  create a new MyButton object and store it at buttons[i][j]
                        /*..............................*/
                        buttons[i][j] = new MyButton();
                        
                        //*****  call a method of MyButton to initial its icon images and value
                        /*..............................*/
                        buttons[i][j].set(defaultIcon, bIcon, val);
                        
			buttons[i][j].addActionListener(this);
			panel.add(buttons[i][j]);
        }
        
        
	//  Creates the JFrame and its UI components.
	
	public  void makeGUI() {	

                        String defaultIconFile =  "bug/pucca.png"; ////change to others
                        image = loadImage(defaultIconFile);
                        
                        defaultIcon = new ImageIcon(image); //T^T
                        
		JFrame frame = new JFrame("Pairing the same Bugs 4-4");
                frame.setIconImage(image);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new JPanel(new GridLayout(7-w,6-w));
		p.setPreferredSize(new Dimension(200, 200));
                

              
		generateCards(p);    
                score1 = new JLabel("  Matched Score: 0  ");
                score2 = new JLabel("  Missed Score: 0  ");
                winner = new JLabel();
                
                p.add(score1);
                p.add(score2);
                p.add(winner);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(p,BorderLayout.CENTER);
                
		// Display the window.

                //***** resize the contained component proportionally to its preferenced size or current adjusted size (by player at the runtime.
		frame.setSize(width,height);
                frame.setResizable(false);
                
                //***** make the application window visible
                frame.setVisible(true);
                
                }
        
        
        public BufferedImage loadImage(String path){
            BufferedImage image = null;
                            
                try {
			image = ImageIO.read(getClass().getResource(path));

		} catch (IOException e) {
			e.printStackTrace();
		}
                //***** return the result
                return image;
        }

	public void actionPerformed(ActionEvent e) {
		int r,c;
                ////ในนี้เราจะไม่ใช้เเล้วไปดูที่ MainMenu.java เเทน
                if(e.getSource()== cmbMessageList){
                    JComboBox cb = (JComboBox)e.getSource();
                    String msg = (String)cb.getSelectedItem();
                    switch (msg){ ////ใน ณ ที่นี้จะเป็นการเลือก Mode ซึ่งหากเราเลือก Mode ใด เราก็จะเข้าไปเล่นเกมทันทีไม่ต้องรอกดปุ่มใด ๆ
                        case "4*4 Mode":
                            w = 2;
                            b = 4; ////ในค่าของ b ในเเต่ละเคสหมายถึงขอบเขตของคะเเนนที่เมื่อถึงเเล้วจะชนะ ซึ่งการชนะของ 4*4 จะต้องจับคู่ 8 คู่นั้นคือ 8 คะเเนนนั้นเอง
                            makeGUI();
                            setVisible(false);  
                            break;
                                                
                        case "6*6 Mode": 
                            w = 0;
                            b = 9;
                            makeGUI();
                            setVisible(false);
                            break;
                            
                        default: lblText.setText("Whoops. We seem have an error :("); ////อันนี้เป็นกรณีเผื่อที่มีการเลือกที่ไม่ถูกต้อง เราจะปริ้น text ออกมาข้างๆตัว Combo
                        
                    } 
                    
                }
               
                 // reset the state and flip the card to the back-face (PUCCA) 
                if (state == 4){ ////ซึ่งหากเราจับคู่เเบบ 4 อันเเล้ว เราจึงจำเป็นต้องให้ stage มี 4 อันเเทน             
                    state = 0;
                    if (matched == false) {
                        button1.resetIcon();
                        button2.resetIcon();
                        button3.resetIcon();
                        button4.resetIcon();
                        ////จะทำการรีไอคอนที่กดทั้งหมด
                    }

                }
                
                for(r=0;r<6;r++){
                    for(c=0;c<6;c++){
                        if (e.getSource()== buttons[r][c])
                        {

                            if (state < 1)      
                            {
                                state++;
                                button1 =  buttons[r][c];
                                button1.setIcon();
                            }
                            if (state == 1)   
                            {
                       
                                button2 =  buttons[r][c];
                                button2.setIcon();
                                if(buttons[r][c] == button1)
                                     return;
                                if(buttons[r][c] != button1) 
                                {  
                                button2 =  buttons[r][c];
                                button2.setIcon();
                                state++;
                                }
                                
                                System.out.println("Matched Score : " + matchScore + "       Missed Score : " + missedScore); ////เเสดงคะเเนนใน Console

                                //***** update scores shown in JLabel
                                score1.setText("Matched Score = " + String.valueOf(matchScore)); ////เเสดงจำนวนคู่ที่เราจับได้ในเกม
                                score2.setText("Missed Score = " + String.valueOf(missedScore)); ////เเสดงจำนวนคู่ที่เราจับไม่ได้ในเกม
                            }
                            if (state == 2)      
                            {
                                
                                button3 =  buttons[r][c];
                                button3.setIcon();
                                ////ในกรณีล่างๆ คือเป้นกรณีไว้เช็คว่าปุ่มต่างๆ เหมือนกันหรือไม่
                                if(buttons[r][c] == button1 || buttons[r][c] == button2)
                                     return;
                                
                                if(buttons[r][c] != button1 && buttons[r][c] != button2 ) 
                                {
                                
                                button3 =  buttons[r][c];
                                button3.setIcon();
                                state++;
                                
                                }
                                
                                System.out.println("Matched Score : " + matchScore + "       Missed Score : " + missedScore);

                                score1.setText("Matched Score = " + String.valueOf(matchScore));
                                score2.setText("Missed Score = " + String.valueOf(missedScore));
                            }
                            if (state == 3)
                            {
                                
                                button4 =  buttons[r][c];
                                button4.setIcon();
                                if(buttons[r][c] == button1 || buttons[r][c] == button2 || buttons[r][c] == button3)
                                     return;
                                if(buttons[r][c] != button1 && buttons[r][c] != button2 && buttons[r][c] != button3) 
                                {
                                
                                button4 =  buttons[r][c];
                                button4.setIcon();
                                state++;
                                }
                                if ( button1.value() == button2.value() && button1.value() == button3.value() && button1.value() == button4.value())
                                {
                                    matchScore++; ////หากค่าของปุ่มเท่ากันจะได้ว่าได้คะเเนน 1 คะเเนน
                                    
                                    if (matchScore == b) {
                                        System.out.println("You are the winner!!!!");
                                        winner.setText("You are the WINNER!!!");
                                    }
                                    button1.setBackground(Color.gray);
                                    button2.setBackground(Color.gray);
                                    button3.setBackground(Color.gray);
                                    button4.setBackground(Color.gray);
                                    ////เมื่อกดคู่ได้เเล้วให้รูปเปลี่ยนเป็นสีเทา
                                    
                                    button1.setEnabled(false);
                                    button2.setEnabled(false);
                                    button3.setEnabled(false);
                                    button4.setEnabled(false);
                                    ////ไม่สามารถกดได้
                                    
                                    matched = true;
                                }
                                else {
                                    missedScore++;
                                    matched = false;
                                }
                                System.out.println("Matched Score : " + matchScore + "       Missed Score : " + missedScore);

                                score1.setText("Matched Score = " + String.valueOf(matchScore));
                                score2.setText("Missed Score = " + String.valueOf(missedScore));
                            }
                            
                        }
                        
                    }
                }
        }}


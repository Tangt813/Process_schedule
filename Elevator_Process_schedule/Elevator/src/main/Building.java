package main;

import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import component.Mybuttons;
import component.Floor;
import component.Elevator;

public class Building extends JFrame{
    public static boolean whichFloorIsWaitUp[] = { false, false, false, false, false, false, false, false, false, false,
                                                    false, false, false, false, false, false, false, false, false,false };//20

    public static boolean whichFloorIsWaitDown[] = { false, false, false, false, false, false, false, false, false, false,
                                                    false, false, false, false, false, false, false, false, false,false };//20
    Elevator elevators[]=new Elevator[5];
    public static Mybuttons upbtn[]=new Mybuttons[20];
    public static Mybuttons downbtn[]=new Mybuttons[20];
    int ChooseAlgorithm(int want,int upordown)
    {
        int way[]={0,0,0,0,0};//用来死锁时，计算最适合的电梯
        int CloseEle=0;//最接近的电梯
        int posgap=21;//最近的位置差
        for (int i = 0; i < 5; i++) {
            if(elevators[i].islegal(want,upordown))
            {
                if(Math.abs(elevators[i].pos-want)<posgap)
                {
                    posgap=Math.abs(elevators[i].pos-want);
                    CloseEle=i;
                }
            }
            else
            {
                way[i]=Math.abs(elevators[i].pos-elevators[i].getfarestpos())+Math.abs(elevators[i].getfarestpos()-want);
            }
        }
        if(posgap==21)//出现死锁
        {
            int minwayi=0;
            for (int i = 0; i < 5; i++) {
                if(way[i]<=way[minwayi])
                {
                    minwayi=i;
                }
            }
            CloseEle=minwayi;
        }
        return CloseEle;
    }
    public Building()
    {
        setLayout(null);
        setTitle("电梯调度");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10,10,1300,750);
        setResizable(false);
        //五部电梯
        for (int i = 0; i < 5; i++) {
            Elevator elevator1=new Elevator(i+1);
            add(elevator1);
            elevators[i]=elevator1;
        }



        int x=60,y=35;
        //upbuttons
        for (int i = 1; i < 20; i++) {
            Mybuttons button=new Mybuttons();
            upbtn[i]=button;
            button.num=20-i;//num不减一，19楼就是19
            button.btn.setLocation(x,y+i*30);
            button.btn.setText("▲");
            button.btn.setBackground(new Color(64,224,208));
            button.btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    whichFloorIsWaitUp[button.num-1] = true;
                    button.btn.setBackground(Color.YELLOW);
                    button.btn.setOpaque(true);
                    button.btn.setEnabled(false);
                    int choice = ChooseAlgorithm(button.num , 0);
                    elevators[choice].toWhichFloor[button.num-1] = true;
                }
            });
            add(upbtn[i].btn);
        }
        //downbuttons
        x=120;
        y=35;
        for (int i = 0; i < 19; i++) {
            Mybuttons button=new Mybuttons();
            downbtn[i]=button;
            downbtn[i].num=20-i;
            downbtn[i].btn.setLocation(x,y+i*30);
            downbtn[i].btn.setText("▼");
            downbtn[i].btn.setBackground(new Color(64,224,208));
            button.btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    button.btn.setEnabled(false);
                    whichFloorIsWaitDown[button.num-1] = true;
                   // System.out.println(button.num);
                    button.btn.setBackground(Color.YELLOW);
                    int choice = ChooseAlgorithm(button.num, 1);//down
                    elevators[choice].toWhichFloor[button.num-1] = true;
                }
            });
            add(downbtn[i].btn);
        }

        //background floor
        add(new Floor().floor);
        for (int i = 0; i < 5 ; i++) {
            new Thread(elevators[i]).start();
        }


    }
    public static void main(String[] args) {
        Building mybuilding=new Building();
        mybuilding.setVisible(true);
    }
}

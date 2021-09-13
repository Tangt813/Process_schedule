package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.Building;
public class Elevator extends JPanel implements Runnable{
    public boolean toWhichFloor[]={false, false, false, false, false, false, false, false, false, false,
                                    false, false, false, false, false, false, false, false, false,false};
    Mybuttons allbtn[]=new Mybuttons[20];//1到20楼
    public int pos;
    public  int ith;
    JButton Close;
    JButton Open;
    JButton alarm;
    JPanel light;
    JLabel door;
    JLabel floorlight;
    JLabel elevator;
    Font font;
    boolean isopen=false;
    boolean isup;
    boolean isdown;
    boolean isrun;
    boolean isalarm;
    public int getfarestpos()
    {
        int farest=0;
        int farestpos=0;
        for (int i = 0; i < 20; i++) {
            if(Math.abs(i-this.pos)>farest&&toWhichFloor[i])
            {
                farest=Math.abs(i-this.pos);
                farestpos=i;
            }
        }
        return farestpos;
    }
    public boolean islegal(int want,int upordown)//0为up，1为down
    {
        if(!this.isalarm &&((upordown==0&&this.pos<want&&isup&&!isopen&&!isdown)||(upordown==1&&pos>want&&isdown&&!isopen&&!isup||isup&&isdown&&!isrun&&!isopen)))
        {
            return true;
        }
        else
            return false;
    }
    public Elevator (int th)
    {
        ith=th;
        this.pos=1;
        isopen=false;
        isup=true;
        isdown=true;
        isrun=false;
        isalarm=false;
        setOpaque(false);
        setLayout(null);
        setBounds(ith*180,0,200,700);

        light=new JPanel();
        add(light);
        light.setBounds(80,0,100,700);
        light.setBackground(new Color( 225,255,255));
        light.setLayout(null);
        //楼层
        floorlight=new JLabel("  0 1  ");
        floorlight.setBounds(10,0,80,25);
        floorlight.setBackground(Color.black);
        floorlight.setForeground(Color.red);
        floorlight.setOpaque(true);
        font=new Font("宋体",Font.BOLD,20);
        floorlight.setFont(font);
        light.add(floorlight);

        //楼层按钮
        int x=15,y=30;
        for (int i = 0; i < 20; i++) {
            Mybuttons button=new Mybuttons();
            allbtn[i]=button;
            allbtn[i].num=20-i;
            allbtn[i].btn.setSize(70,25);
            allbtn[i].btn.setBackground(new Color(215, 224, 248));
            allbtn[i].btn.setText(""+(20-i));
            allbtn[i].btn.setLocation(x,y+(i*30));
            int finalI = i;
            allbtn[i].btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("finalI="+finalI);
                    if(pos== allbtn[finalI].num)
                    {
                        toWhichFloor[ allbtn[finalI].num-1]=true;
                      //  System.out.println("towhichfloor["+( allbtn[finalI].num-1)+"]="+toWhichFloor[ allbtn[finalI].num-1]);
                        return;
                    }
                    button.btn.setOpaque(true);
                    toWhichFloor[button.num-1]=true;

                    button.btn.setBackground(Color.yellow);
                    button.btn.setEnabled(false);
                }
            });

            light.add(button.btn);

        }

        //电梯
        elevator=new JLabel();
        door=new JLabel();
        elevator.setLocation(20,600);
        elevator.setSize(30,30);
        door.setIcon(new ImageIcon(this.getClass().getResource("/img/door.png")));

        door.setSize(26,30);
        door.setLocation(2,0);
        elevator.add(door);
        elevator.setOpaque(true);
        elevator.setBackground(Color.green);
        add(elevator);




        //open,close,alarm
        Open=new JButton();
        Close=new JButton();
        alarm=new JButton();
        Open.setSize(35,30);
        Open.setIcon(new ImageIcon(this.getClass().getResource("/img/open.png")));
        Open.setLocation(10,630);
        Open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!isalarm)
                {
                    isopen=true;
                }
            }
        });
        Close.setSize(35,30);
        Close.setIcon(new ImageIcon(this.getClass().getResource("/img/close.png")));
        Close.setLocation(55,630);
        Close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isopen&&!isalarm)
                    Closedoor();
            }

        });
        alarm.setIcon(new ImageIcon(this.getClass().getResource("/img/alarm.png")));
        alarm.setLocation(25,670);
        alarm.setSize(50,30);
        alarm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // alarm.setIcon(new ImageIcon(this.getClass().getResource("/img/alarm.png")));;
                alarm.setEnabled(false);
                isalarm=true;
                isrun=false;
                light.setBackground(Color.red);
                for (int i = 0; i < 20; i++) {
                    allbtn[i].btn.setEnabled(false);
                    allbtn[i].btn.setBackground(Color.red);
                }
            }
        });
        light.add(Open);
        light.add(Close);
        light.add(alarm);



    }

    @Override
    public void run() {
        while(true)
        {
            if(isalarm)
            {
                alarming();
                break;
            }
            if(isopen)
            {
                Opendoor();
                try {// wait for a second
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Closedoor();
            }
            HereWego();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void moveToFloor(int want)
    {
        if(isalarm||isopen)
            return;
        if(this.pos==want)//在原地
        {
            if(isalarm||isopen)
                return;
            isrun=false;
            isup=true;
            isdown=true;
            toWhichFloor[this.pos-1]=false;
            if(this.pos!=20&&!Building.upbtn[20-pos].btn.isEnabled())
            {
                Building.upbtn[20-pos].btn.setEnabled(true);
            }
            else if(this.pos!=1&&!Building.downbtn[20-pos].btn.isEnabled())
            {
                Building.downbtn[20-pos].btn.setEnabled(true);
            }
            return;
        }
        while(this.pos<want)//往上走
        {
            if(isalarm||isopen)
                return;
            isrun=true;
            isdown=false;
            isup=true;
            int gap=want-this.pos;

            for (int i = 1; i <=gap &&!isalarm; i++) {
                for (int j = 1; j <= 10; j++) {
                    elevator.setLocation(20,600-(this.pos-1)*30-3*j);
                    try {// wait for a second
                        Thread.sleep(30);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                this.pos++;
                if(this.pos<10)
                {
                    floorlight.setText("  0 "+this.pos+"  ");
                }
                else if(this.pos>=10&&this.pos<20)
                {
                    floorlight.setText("  1 "+(this.pos%10)+"  ");
                }
                else
                {
                    floorlight.setText("  2 "+(this.pos%10)+"  ");
                }
                if(toWhichFloor[pos-1]||isopen)
                {
                    return;
                }

            }
            toWhichFloor[this.pos-1]=false;
            return;
        }
        while(this.pos>want)//往下走
        {
            if(isalarm||isopen)
                return;
            isrun=true;
            isdown=true;
            isup=false;
            int gap=this.pos-want;
            for (int i = 0; i <gap&&!isalarm ; i++) {
                for (int j = 1; j <= 10; j++) {
                    elevator.setLocation(20,600-(this.pos-1)*30+3*j);
                    try {// wait for a second
                        Thread.sleep(30);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                }
                this.pos--;
                if(this.pos<10)
                {
                    floorlight.setText("  0 "+this.pos+"  ");
                }
                else if(this.pos>=10&&this.pos<20)
                {
                    floorlight.setText("  1 "+(this.pos%10)+"  ");
                }
                else
                {
                    floorlight.setText("  2 "+(this.pos%10)+"  ");
                }
            }
            toWhichFloor[this.pos-1]=false;
            return;
            }

    }
    void Arrive()
    {
        if(isalarm||isopen)
            return;
        if(Building.whichFloorIsWaitUp[this.pos-1]&&!isopen)
        {
            toWhichFloor[pos-1]=false;
            Building.whichFloorIsWaitUp[this.pos-1]=false;
            Building.upbtn[20-this.pos].btn.setEnabled(true);
            Building.upbtn[20-this.pos].btn.setBackground(new Color(64,224,208));
            ////这里看看这部电梯还需不需要动下去
            boolean flag=false;
            for (int i = this.pos; i <20 ; i++) {
                if (toWhichFloor[i])
                    flag=true;
            }
            if(flag)
            {
                isrun=true;
                isup=true;
                isdown=false;
            }
            else
            {
                isrun=false;
                isup=true;
                isdown=true;
            }
            ///////////////////////
        }

        else if(Building.whichFloorIsWaitDown[this.pos-1]&&!isopen)
        {
            toWhichFloor[this.pos-1]=false;
            Building.whichFloorIsWaitDown[this.pos-1]=false;
            Building.downbtn[20-this.pos].btn.setEnabled(true);
            Building.downbtn[20-this.pos].btn.setBackground(new Color(64,224,208));
            Building.downbtn[20-this.pos].btn.setOpaque(true);
            boolean flag=false;
            for (int i = 0; i < this.pos; i++) {
                if (toWhichFloor[i])
                    flag=true;
            }
            if(flag)
            {
                isrun=true;
                isup=false;
                isdown=true;
            }
            else
            {
                isrun=false;
                isup=true;
                isdown=true;
            }
        }

        if(!this.allbtn[20-pos].btn.isEnabled()&&!isopen)
        {

            allbtn[20-pos].btn.setOpaque(true);
            allbtn[20-pos].btn.setEnabled(true);
            allbtn[20-pos].btn.setBackground(new Color(215, 224, 248));
            toWhichFloor[pos-1]=false;
            ////这里看看这部电梯还需不需要动下去
            if(isup&&!isdown)
            {
                boolean flag=false;
                for (int i = this.pos; i <20 ; i++) {
                    if (toWhichFloor[i])
                        flag=true;
                }
                if(flag)
                {
                    isrun=true;
                    isup=true;
                    isdown=false;
                }
                else
                {
                    isrun=false;
                    isup=true;
                    isdown=true;
                }
            }
            else if(!isup&&isdown)
            {
                boolean flag=false;
                for (int i = 0; i < this.pos; i++) {
                    if (toWhichFloor[i])
                        flag=true;
                }
                if(flag)
                {
                    isrun=true;
                    isup=false;
                    isdown=true;
                }
                else
                {
                    isrun=false;
                    isup=true;
                    isdown=true;
                }
            }
            else if(isup&&isdown)
            {
                isrun=false;
                isup=true;
                isdown=true;
            }
        }

    }

    void Opendoor()
    {
        if(isalarm)
            return;
        isopen=true;
        elevator.setOpaque(true);
        elevator.setBackground(Color.red);

    }
    void Closedoor()
    {
        isopen=false;
        elevator.setOpaque(true);
        elevator.setBackground(Color.green);
    }
    void HereWego()
    {
        while(!isalarm&&!isopen)
        {
            Open.setEnabled(true);
            Close.setEnabled(true);
            for (int i = 0; i < 20; i++) {

                if (toWhichFloor[i]) {
                    moveToFloor(i+1);
                    Arrive();
                    Opendoor();
                    try {// wait for a second
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    Closedoor();
                    try {// wait for a second
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
    void alarming()
    {
        isrun=false;
        for (int i = 0; i < 20; i++) {
            allbtn[i].btn.setEnabled(false);
            allbtn[i].btn.setBackground(Color.red);
        }
        elevator.setOpaque(true);
        elevator.setBackground(Color.red);
        Open.setEnabled(false);
        Close.setEnabled(false);
        alarm.setEnabled(false);
        if(isup)
        {
            for (int i = pos; i <20 ; i++) {
                if(toWhichFloor[i])
                {
                    Building.whichFloorIsWaitUp[i]=false;
                    Building.upbtn[20-i-1].btn.setEnabled(true);
                    Building.upbtn[20-i-1].btn.setBackground(new Color(64,224,208));

                }
            }
        }
        else if(isdown)
        {
            for (int i = pos; i >0 ; i--) {
                if(toWhichFloor[i])
                {
                    Building.whichFloorIsWaitUp[i]=false;
                    Building.downbtn[20-i-1].btn.setEnabled(true);
                    Building.downbtn[20-i-1].btn.setBackground(new Color(64,224,208));
                }
            }
        }
        isup=false;
        isdown=false;

    }
}

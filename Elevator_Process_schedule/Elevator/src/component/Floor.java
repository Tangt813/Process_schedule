package component;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
public class Floor extends JPanel{
    public JPanel floor;
    public JLabel jl;
    public Floor()
    {
        floor=new JPanel();
        floor.setVisible(true);
        floor.setLayout(null);
        floor.setBounds(0,0,1500,650);
        Font font=new Font("宋体",Font.PLAIN,18);
        for (int i = 0; i < 20; i++) {
            jl=new JLabel(" ");
            jl.setFont(font);
            jl.setBounds(0, 30+i * 30, 1500, 30);
            jl.setText("第"+(20-i)+"楼");
            jl.setVisible(true);
            jl.setOpaque(true);
            if(i%2==0)
            {
                jl.setBackground(new Color(230,230,250));
            }
            else
            {
                jl.setBackground(new Color(240,248,255));
            }
            floor.add(jl);
        }

    }
}

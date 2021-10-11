package src.Snake;

import javax.imageio.ImageIO;
import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;



public class Snake extends JPanel implements ActionListener{
    // images
    Image egg;
    Image body;
    Image head;

    // body length
    private final int BODY_SIZE=10;
    
    // all bodys
    private final int ALL_BODY= 14000;           // 400*350=140000/ 10= 14000

    // Board size
    private final int BOARD_X_SIZE=400;
    private final int BOARD_Y_SIZE=350;

    // location  snake
    private final int[] snake_x= new int[ALL_BODY];
    private final int[] snake_y= new int[ALL_BODY];

    // location of egg
    private int egg_x;
    private int egg_y;

    // random Position
    private final int RANDOM_POSITION= 29;

    // fisrt length
    private int sLenght;

    // timer 
    Timer timer;
    // speed
    int speed=70;

    // direction
    private boolean left=false;
    private boolean right= true;
    private boolean up= false;
    private boolean down= false;


    // Are you out or in Game 
    private boolean inGame= true;


    // score 
    public int scrore=0;
    JLabel score;

    // jframe
    static JFrame frame;

    // jpanel
    JPanel panel;

    // replay button
    JButton repaly;

    // buttons for speed 
    JButton x1, x2, x3;

    Snake(){

        setBounds(20, 50, 400, 350);
        setLayout(null);

        setBackground(Color.BLACK);
        

        addKeyListener(new Key());
        setFocusable(true);

        showImages();
        moveSnakeFirst();


        frame= new JFrame();
        frame.setSize(460, 480);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(164, 195, 108));
        frame.add(this);

        panel= new JPanel();
        panel.setBounds(20, 10, 400, 50);
        panel.setBackground(Color.gray);
        // panel.setLayout(null);
        frame.add(panel);


        score= new JLabel();
        score.setText("Score: "+ scrore);
        score.setFont(new Font("Modak", Font.BOLD, 16));
        panel.add(score);

        repaly= new JButton("Replay");
        repaly.setBounds(180, 410, 100, 20);
        setBackground(repaly);
        repaly.addActionListener(this);
        frame.add(repaly);

    }

    void setBackground(JButton button){
        button.setBackground(Color.black);
        button.setForeground(Color.white);
    }

    void showImages(){

        try {
            BufferedImage i= ImageIO.read(new File("B://SnakeGame//Icons//egg.png"));
            ImageIcon icon= new ImageIcon(i);
            egg= icon.getImage();


            BufferedImage i2= ImageIO.read(new File("B://SnakeGame//Icons//head.png"));
            ImageIcon icon2= new ImageIcon(i2);
            head= icon2.getImage();

            BufferedImage i3= ImageIO.read(new File("B://SnakeGame//Icons//body.png"));
            ImageIcon icon3= new ImageIcon(i3);
            body= icon3.getImage();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }



    void moveSnakeFirst(){
        sLenght= 3;
        for(int a=0;a<=sLenght;a++){
            snake_x[a]= 50 - a*BODY_SIZE;
            snake_y[a]= 50;
        }

        locateEgg();
        timer = new Timer(speed, this);
        timer.start();
    }

    void locateEgg(){
        // for x
        int a= (int) (Math.random() * RANDOM_POSITION);
        egg_x= (a* BODY_SIZE);
        // for y
        a= (int) (Math.random() * RANDOM_POSITION);
        egg_y= (a* BODY_SIZE);
    }

    // this method will check wether snake eat egg or not
    void checkEgg(){
        if((snake_x[0]== egg_x) && (snake_y[0]== egg_y)){
            scrore++;
            sLenght++;
            score.setText("Score: "+ scrore);
            locateEgg();
        }
    }

    void checkCollision(){

        for(int a=sLenght; a>0;a--){
            if((a>4)  &&  (snake_x[0] == snake_x[a])  && (snake_y[0] == snake_y[a])){
                inGame= false;
            }
        }

        if(snake_x[0] > BOARD_X_SIZE){
            inGame=false;
        }

        if(snake_y[0] > BOARD_Y_SIZE){
            inGame= false;
        }

        if(snake_x[0] < 0){
            inGame= false;
        }

        if(snake_y[0] < 0){
            inGame=false;
        }

        if(!inGame){
            timer.stop();
        }
    }

    void moveSnake(){

        for(int a=sLenght;a>0;a--){
            snake_x[a] = snake_x[a -1];
            snake_y[a] = snake_y[a -1];   
        }

        if(left){
            snake_x[0] -= BODY_SIZE;
        }
        if(right){
            snake_x[0] += BODY_SIZE;
        }

        if(up){
            snake_y[0] -= BODY_SIZE;
        }
        if(down){
            snake_y[0] += BODY_SIZE;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if(inGame){
            checkEgg();
            checkCollision();
            moveSnake();
        }

        repaint();

        if(e.getSource()== repaly){
            frame.setVisible(false);
            new Snake();
            frame.setVisible(true);
        }

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        draw(g);
    }

    public void draw(Graphics g){
        if (inGame){
            g.drawImage(egg, egg_x, egg_y, this);
            for (int a=0;a<sLenght; a++){
                if (a==0){
                    g.drawImage(head, snake_x[a], snake_y[a], this);
                }else {
                    g.drawImage(body, snake_x[a], snake_y[a], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        }else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg="GAME OVER";
        Font font= new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrics= getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (400-metrics.stringWidth(msg))/ 2, 350/2);
    }

    public static void main(String[] args) {
        new Snake();
        frame.setVisible(true);
    }


    private class Key extends KeyAdapter{

        public void keyPressed(KeyEvent e) {
            int keycode= e.getKeyCode();

            if(keycode== KeyEvent.VK_LEFT && (!right)){
                left= true;
                up= false;
                down= false;
            }
            if(keycode== KeyEvent.VK_UP && (!down)){
                up= true;
                right= false;
                left= false;
            }
            if(keycode== KeyEvent.VK_DOWN && (!up)){
                down= true;
                right= false;
                left= false;
            }
            if(keycode== KeyEvent.VK_RIGHT  && (!left)){
                right= true;
                up= false;
                down= false;
            }

        }
    }
}
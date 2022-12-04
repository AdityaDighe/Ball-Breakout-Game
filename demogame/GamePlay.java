package demogame;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Font;

public class GamePlay extends JPanel implements ActionListener, KeyListener{
    private boolean play=false;
    private int score=0;
    private int totalBrick=21;
    private Timer timer;
    private int delay=8;
    private int ballposX=120;
    private int ballposY=350;
    private int ballXdir=-1;
    private int ballYdir=-2;
    private int playerX=350;
    private MapGenerator map;

    public GamePlay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        timer=new Timer(delay,this);
        timer.start();

        map= new MapGenerator(3,7);
    }

    public void paint(Graphics g) {
        //black canvas
        g.setColor(Color.black);
        g.fillRect(0, 0, 700, 600);
        
        //border
        g.setColor(Color.yellow);
        g.fillRect(0,0,700,3);
        g.fillRect(0,3,3,597);
        g.fillRect(683,3,3,597);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        //bricks
        map.draw((Graphics2D)g);

        //ball
        g.setColor(Color.red);
        g.fillOval(ballposX, ballposY, 20, 20);

        //score
        g.setColor(Color.green);
        g.setFont(new Font("serif",Font.BOLD,20));
        g.drawString("Score : "+score, 550, 30);

        /*//intruction
        if(ballposX==20 && ballposY==20) {
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press LEFT or RIGHT key",170,300);
        }*/

        //gameover
        if(ballposY>570) {
            play=false;
            ballXdir=0;
            ballYdir=0;

            g.setColor(Color.green);
            g.setFont(new Font("serif",Font.BOLD,50));
            g.drawString("GameOver Score : "+score,100,300);

            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press ENTER to RESTART",150,350);
        }

        //PlayerWon
        if(totalBrick<=0) {
                play=false;
                ballXdir=0;
                ballYdir=0;
    
                g.setColor(Color.green);
                g.setFont(new Font("serif",Font.BOLD,30));
                g.drawString("YOU WON Score : "+score,175,300);
    
                g.setFont(new Font("serif",Font.BOLD,30));
                g.drawString("Press ENTER to RESTART",150,350);
        
    }

    }

    private void moveLeft() {
        play=true;
        playerX-=20;
    }

    private void moveRight() {
        play=true;
        playerX+=20;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            if(playerX<=3) {
                playerX=3;
            }
            else {
                moveLeft();
            }   
        }

        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            if(playerX>=583) {
                playerX=583;
            }
            else {
                moveRight();
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_ENTER) {
            if(!play) {
                score=0;
                totalBrick=21;
                ballposX=120;
                ballposY=350;
                ballXdir=-1;
                ballYdir=-2;
                playerX=320;

                map=new MapGenerator(3, 7);
            }
        }

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(play) {
            if(ballposX<=0){
                ballXdir=-ballXdir;
            }

            if(ballposX>=663){
                ballXdir=-ballXdir;
            }

            if(ballposY<=0){
                ballYdir=-ballYdir;
            }

            Rectangle ballRect=new Rectangle(ballposX,ballposY,20,20);
            Rectangle paddleRect=new Rectangle(playerX,550,100,8);

            if(ballRect.intersects(paddleRect)) {
                ballYdir=-ballYdir;
            }

            A:for(int i=0;i<map.map.length;i++) {
                for(int j=0;j<map.map[0].length;j++) {
                    if(map.map[i][j]>0) {
                        int width=map.brickWidth;
                        int height=map.brickHeight;
                        int brickXpos=80+j*width;
                        int brickYpos=50+i*height;

                        Rectangle brickRect=new Rectangle(brickXpos,brickYpos,width,height);
                        if(ballRect.intersects(brickRect)) {

                            map.setBrick(0,i,j);
                            totalBrick--;
                            score+=5;
                            
                            if(ballposX+19 <= brickXpos || ballposX+1 >= brickXpos+width) {
                                ballXdir=-ballXdir;  
                            }
                            else {
                                ballYdir=-ballYdir;
                            }

                            break A;
                        }  
                    }
                }
            }



            ballposX+=ballXdir;
            ballposY+=ballYdir;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}

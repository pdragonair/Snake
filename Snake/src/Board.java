/**
 * Created by pranav.khorana on 8/7/2018.
 */



// make enemy rotten apples

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;



public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int [ALL_DOTS];
    private final int y[] = new int [ALL_DOTS];

    private int dots;

    private int apple_x;
    private int apple_y;

    private int rotten_x;
    private int rotten_y;

    private int rotten_x2;
    private int rotten_y2;

    private int rotten_x3;
    private int rotten_y3;

    private int rotten_x4;
    private int rotten_y4;

    private int rotten_x5;
    private int rotten_y5;


    private int reset_x = 100;
    private int reset_y = 175;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image reset;

    private Image rotten;
    private Image rotten2;
    private Image rotten3;
    private Image rotten4;
    private Image rotten5;

    private ArrayList<Image> dangers =  new ArrayList<Image>();
    private ArrayList<Integer> locations_x =  new ArrayList<>();
    private ArrayList<Integer> locations_y =  new ArrayList<>();



    public Board(){
        timer = new Timer(DELAY, this);

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());

        setBackground(Color.black);
        setFocusable(true);
        setDoubleBuffered(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        loadRottens();
        initGame();
    }




    private void loadImages() {


        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();

        ImageIcon fin = new ImageIcon("src/resources/reset.png");
        reset = fin.getImage();

        ImageIcon iir = new ImageIcon("src/resources/rotten.png");
        rotten = iir.getImage();

        ImageIcon iir2 = new ImageIcon("src/resources/rotten2.png");
        rotten2 = iir2.getImage();
        ImageIcon iir3 = new ImageIcon("src/resources/rotten3.png");
        rotten3 = iir3.getImage();
        ImageIcon iir4 = new ImageIcon("src/resources/rotten4.png");
        rotten4 = iir4.getImage();
        ImageIcon iir5 = new ImageIcon("src/resources/rotten5.png");
        rotten5 = iir5.getImage();

        dangers.add(rotten);
        dangers.add(rotten2);
        dangers.add(rotten3);
        dangers.add(rotten4);
        dangers.add(rotten5);
    }

    private void loadRottens() {
        locations_x.add(rotten_x);
        locations_y.add(rotten_y);
        locations_x.add(rotten_x2);
        locations_y.add(rotten_y2);
        locations_x.add(rotten_x3);
        locations_y.add(rotten_y3);
        locations_x.add(rotten_x4);
        locations_y.add(rotten_y4);
        locations_x.add(rotten_x5);
        locations_y.add(rotten_y5);
    }





    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++){
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();
        locateRotten();

        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g){

        if (inGame) {

            for(int i = 0; i < 5; i++){
                g.drawImage(dangers.get(i), locations_x.get(i), locations_y.get(i), this);
            }

//            g.drawImage(rotten, rotten_x, rotten_y, this);

            g.drawImage(apple, apple_x, apple_y, this);


            for (int z = 0; z < dots; z++){
                if (z==0){
                    g.drawImage(head, x[z], y[z], this);
                }
                else{
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {


            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg))/2,
                 B_HEIGHT / 2);

        g.drawImage(reset, reset_x, reset_y, this);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e){

                double minx = reset_x;
                double miny = reset_y;
                double maxx = minx + reset.getWidth(null);
                double maxy = miny + reset.getHeight(null);


                System.out.println(minx);
                System.out.println(maxx);

                double mousex = MouseInfo.getPointerInfo().getLocation().getX() - 533;
                double mousey = MouseInfo.getPointerInfo().getLocation().getY() - 225;
                System.out.println(mousex);

                if ((mousex >= minx && mousex <= maxx) && (mousey >= miny && mousey <= maxy)) {
                    inGame = true;

                    initBoard();
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                    leftDirection = false;
                }
                else{
                    System.out.println("not working");
                }
            }
        });

    }

    private void checkApple() {

        if((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }


    private void checkRotten() {
        for(int i = 0; i<5; i++){
            if((x[0] == locations_x.get(i)) && (y[0] == locations_y.get(i))) {

                dots--;
                locateRotten();
            }
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--){
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];

        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {


        for(int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (dots == 0)
            inGame = false;

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0){
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }

    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r*DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));

    }


    private void locateRotten() {

        for (int i = 0; i<5; i++){
            int r = (int) (Math.random() * RAND_POS);
            locations_x.set(i, r*DOT_SIZE);
        }

        for (int i = 0; i<5; i++){
            int r = (int) (Math.random() * RAND_POS);
            locations_y.set(i, r*DOT_SIZE);
        }

    }




    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkRotten();
            checkCollision();
            move();
        }


        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }












}

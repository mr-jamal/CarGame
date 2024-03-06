import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JFrame implements KeyListener, ActionListener {
    private final int WIDTH = 500;
    private final int HEIGHT = 500;
    private final int CAR_WIDTH = 50;
    private final int CAR_HEIGHT = 100;
    private final int ROAD_WIDTH = 300;
    private final int OBSTACLE_WIDTH = 50;
    private final int OBSTACLE_HEIGHT = 50;

    private int carX = WIDTH / 2 - CAR_WIDTH / 2;
    private int carY = HEIGHT - CAR_HEIGHT - 50;
    private int roadY = 0;

    private Timer timer;
    private boolean gameOver = false;
    private int score = 0;
    private List<Rectangle> obstacles = new ArrayList<>();
    private Random random = new Random();

    public Main() {
        setTitle("Advanced Car Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        timer = new Timer(10, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        g.fillRect((WIDTH - ROAD_WIDTH) / 2, 0, ROAD_WIDTH, HEIGHT);

        g.setColor(Color.RED);
        g.fillRect(carX, carY, CAR_WIDTH, CAR_HEIGHT);

        for (Rectangle obstacle : obstacles) {
            g.setColor(Color.BLUE);
            g.fillRect(obstacle.x, obstacle.y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            roadY += 1;
            if (roadY >= HEIGHT) {
                roadY = 0;
                score++;
            }
            updateObstacles();
            checkCollision();
            repaint();
        }
    }

    private void updateObstacles() {
        if (random.nextInt(100) < 2) {
            int obstacleX = (WIDTH - ROAD_WIDTH) / 2 + random.nextInt(ROAD_WIDTH - OBSTACLE_WIDTH);
            Rectangle obstacle = new Rectangle(obstacleX, -OBSTACLE_HEIGHT, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
            obstacles.add(obstacle);
        }
        for (int i = 0; i < obstacles.size(); i++) {
            Rectangle obstacle = obstacles.get(i);
            obstacle.y += 2;
            if (obstacle.y > HEIGHT) {
                obstacles.remove(i);
                i--;
            }
        }
    }

    private void checkCollision() {
        Rectangle car = new Rectangle(carX, carY, CAR_WIDTH, CAR_HEIGHT);
        for (Rectangle obstacle : obstacles) {
            if (car.intersects(obstacle)) {
                gameOver = true;
                timer.stop();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if (carX > (WIDTH - ROAD_WIDTH) / 2)
                carX -= 5;
        }
        if (key == KeyEvent.VK_RIGHT) {
            if (carX < (WIDTH - ROAD_WIDTH) / 2 + ROAD_WIDTH - CAR_WIDTH)
                carX += 5;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main game = new Main();
            game.setVisible(true);
        });
    }
}

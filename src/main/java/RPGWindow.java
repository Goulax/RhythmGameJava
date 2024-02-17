import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RPGWindow extends JFrame {
    private static final int PLAYER_HEALTH = 100;
    private static final int ENEMY_HEALTH = 50;

    private int playerHealth = PLAYER_HEALTH;
    private int enemyHealth = ENEMY_HEALTH;

    private JLabel playerHealthLabel;
    private JLabel enemyHealthLabel;
    private ImageRenderer imageRenderer;

    public RPGWindow() {
        super("Simple RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        playerHealthLabel = new JLabel("Jugador: " + playerHealth);
        enemyHealthLabel = new JLabel("Enemigo: " + enemyHealth);
        topPanel.add(playerHealthLabel);
        topPanel.add(enemyHealthLabel);
        add(topPanel, BorderLayout.NORTH);

        imageRenderer = new ImageRenderer();
        add(imageRenderer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        JButton attackButton = new JButton("Atacar");
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attack();
            }
        });
        bottomPanel.add(attackButton);

        JButton healButton = new JButton("Curar");
        healButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                heal();
            }
        });
        bottomPanel.add(healButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void attack() {
        int playerDamage = (int) (Math.random() * 10) + 1;
        int enemyDamage = (int) (Math.random() * 5) + 1;
        enemyHealth -= playerDamage;
        playerHealth -= enemyDamage;
        if (enemyHealth < 0) {
            enemyHealth = 0;
        }
        if (playerHealth < 0) {
            playerHealth = 0;
        }
        updateHealthLabels();
        checkGameOver();
    }

    private void heal() {
        int healing = (int) (Math.random() * 15) + 1;
        playerHealth = Math.min(PLAYER_HEALTH, playerHealth + healing);
        updateHealthLabels();
    }

    private void updateHealthLabels() {
        playerHealthLabel.setText("Jugador: " + playerHealth);
        enemyHealthLabel.setText("Enemigo: " + enemyHealth);
    }

    private void checkGameOver() {
        if (playerHealth <= 0) {
            JOptionPane.showMessageDialog(this, "¡Has sido derrotado! Fin del juego.");
            System.exit(0);
        } else if (enemyHealth <= 0) {
            JOptionPane.showMessageDialog(this, "¡Has derrotado al enemigo! Fin del juego.");
            System.exit(0);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RPGWindow();
            }
        });
    }
}
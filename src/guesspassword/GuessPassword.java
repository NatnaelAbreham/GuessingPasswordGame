package guesspassword;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GuessPassword extends JFrame {

    JButton[][] button = new JButton[8][4];
    JButton[] go = new JButton[8];
    JLabel[][] label = new JLabel[8][4];
    JPanel[] panel = new JPanel[8];
    JPanel p1, pleft, pright, plrholder;
    JButton colorcontrol;
    JProgressBar pbar;
    Timer timer;
    int second = 100, counter = 0, resetcounter;
    int[] choose = new int[4];

    GuessPassword() {

        colorcontrol = new JButton();
        colorcontrol.setBackground(Color.LIGHT_GRAY);
        colorcontrol.getBackground();

        pbar = new JProgressBar();
        pbar.setMinimum(0);
        pbar.setMaximum(100);
        pbar.setStringPainted(false);
        pbar.setForeground(Color.BLUE);

        JLabel l1 = new DisplayTime();
        l1.setForeground(Color.BLUE);

        get4RandomNumber();

        p1 = new JPanel(new GridLayout(8, 5));
        plrholder = new JPanel(new GridLayout(1, 2));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                button[i][j] = new JButton("-");
                label[i][j] = new JLabel("");
                go[i] = new JButton("GO");

                if (i == 0) {
                    button[i][j].setBackground(Color.BLACK);
                } else {
                    button[i][j].setBackground(Color.LIGHT_GRAY);
                    go[i].setVisible(false);
                }

                button[i][j].setFont(new Font("Serif", 2, 23));
                label[i][j].setFont(new Font("Serif", 2, 23));
                button[i][j].setForeground(Color.WHITE);
                p1.add(button[i][j]);

            }

            go[i].setFont(new Font("Serif", 2, 23));
            go[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            p1.add(go[i]);
        }

        pleft = new JPanel(new GridLayout(4, 1));
        pright = new JPanel(new GridLayout(4, 1));

        pleft.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        pright.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));

        for (int i = 0; i < 4; i++) {
            panel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel[i].add(new JLabel("" + (i + 1)));
            pleft.add(panel[i]);

        }

        for (int i = 4; i < 8; i++) {
            panel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel[i].add(new JLabel("" + (i + 1)));
            pright.add(panel[i]);
        }

        //add empty label to the panel
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                panel[i].add(label[i][j]);
            }
        }

        plrholder.setBorder(new TitledBorder("Guessed score"));
        plrholder.setBackground(Color.WHITE);

        plrholder.add(pleft);
        plrholder.add(pright);

        add(pbar, BorderLayout.NORTH);
        add(p1, BorderLayout.CENTER);
        add(plrholder, BorderLayout.SOUTH);

        ButtonListener listener = new ButtonListener();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                button[i][j].addActionListener(listener);
            }
            go[i].addActionListener(listener);
        }

    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 8; i++) {
                counter = 0;
                if (e.getSource() == go[i]) {

                    for (int m = 0; m < 4; m++) {
                        //if statement check whether the button number is repeated
                        if (m == 1 && (button[i][m - 1].getText().equals(button[i][m].getText()))) {
                            continue;
                        } else if (m == 2 && (button[i][m - 1].getText().equals(button[i][m].getText()) || button[i][0].getText().equals(button[i][m].getText()))) {
                            continue;
                        } else if (m == 3 && (button[i][m - 1].getText().equals(button[i][m].getText()) || button[i][0].getText().equals(button[i][m].getText()) || button[i][1].getText().equals(button[i][m].getText()))) {
                            continue;

                        }
                        for (int n = 0; n < 4; n++) {

                            if (button[i][m].getText().equals(String.valueOf(choose[n]))) {
                                if (m == n) {
                                    label[i][m].setForeground(Color.BLUE);
                                    label[i][m].setText("O");
                                    break;
                                } else {
                                    label[i][m].setText("O");
                                    break;
                                }
                            }
                        }
                    }
                    for (int k = 0; k < 8; k++) {
                        for (int y = 0; y < 4; y++) {
                            if (k == (i + 1)) {
                                button[k][y].setBackground(Color.BLACK);
                            } else {
                                button[k][y].setBackground(Color.LIGHT_GRAY);
                            }

                        }
                        if (k == (i + 1)) {
                            go[k].setVisible(true);
                        }
                    }
                    go[i].setVisible(false);
                    for (int m = 0; m < 4; m++) {
                        for (int n = 0; n < 4; n++) {
                            if (button[i][m].getText().equals(String.valueOf(choose[n]))) {
                                if (m == n) {
                                    counter++;
                                    if (counter == 4) {
                                        timer.stop();
                                        int highscore = storeScore(100 - second);

                                        if (highscore == (100 - second)) {
                                            JOptionPane.showMessageDialog(null, "HighScored in " + highscore + " seconds \nYou got in " + (100 - second) + " seconds\nThe Secret Code is <<" + choose[0] + choose[1] + choose[2] + choose[3] + ">>\nnew record !!");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "HighScore in " + highscore + " seconds\nYou got in " + (100 - second) + " seconds\nThe Secret Code is <<" + choose[0] + choose[1] + choose[2] + choose[3] + ">> ");
                                        }

                                        int yes = JOptionPane.showConfirmDialog(null, "Do you went to Continue");

                                        switch (yes) {
                                            case JOptionPane.YES_OPTION:
                                                resetGame();
                                                break;
                                            case JOptionPane.NO_OPTION:
                                                timer.stop();
                                                System.exit(0);
                                                break;
                                            case JOptionPane.CANCEL_OPTION:
                                                timer.stop();
                                                break;
                                            default:
                                                break;
                                        }

                                    }

                                }
                            }
                        }
                    }
                    if ((i + 1) == 8) {
                        timer.stop();
                        JOptionPane.showMessageDialog(null, "You lost !!" + "  \nThe Secret Code is <<" + choose[0] + choose[1] + choose[2] + choose[3] + ">>");

                        int yes = JOptionPane.showConfirmDialog(null, "Do you went to Continue");

                        switch (yes) {
                            case JOptionPane.YES_OPTION:
                                resetGame();
                                break;
                            case JOptionPane.NO_OPTION:
                                timer.stop();
                                break;
                            case JOptionPane.CANCEL_OPTION:
                                timer.stop();
                                break;
                            default:
                                break;
                        }
                    }
                }
                for (int j = 0; j < 4; j++) {
                    if (e.getSource() == button[i][j]) {
                        if (button[i][j].getBackground() == colorcontrol.getBackground()) {
                            return;
                        } else {
                            String s = button[i][j].getText();

                            switch (s) {
                                case "-":
                                    button[i][j].setText("0");
                                    break;
                                case "0":
                                    button[i][j].setText("1");
                                    break;
                                case "1":
                                    button[i][j].setText("2");
                                    break;
                                case "2":
                                    button[i][j].setText("3");
                                    break;
                                case "3":
                                    button[i][j].setText("4");
                                    break;
                                case "4":
                                    button[i][j].setText("5");
                                    break;
                                case "5":
                                    button[i][j].setText("6");
                                    break;
                                case "6":
                                    button[i][j].setText("7");
                                    break;
                                case "7":
                                    button[i][j].setText("8");
                                    break;
                                case "8":
                                    button[i][j].setText("9");
                                    break;
                                case "9":
                                    button[i][j].setText("-");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                }

            }
        }
    }

    class DisplayTime extends JLabel {

        DisplayTime() {
            timer = new Timer(1000, new TimerListener());
            second = 100;
            timer.start();

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            /*font metrics and dimension class are used to dispaly
            second in label you can ignore it if you went
             */
            FontMetrics fontmetrics = g.getFontMetrics();

            int stringwidth = fontmetrics.stringWidth(String.valueOf(second));
            int stringascent = fontmetrics.getAscent();

            int xcoordinate = getWidth() / 2 - stringwidth / 2;
            int ycoordinate = getHeight() / 2 + stringascent / 2;

            g.drawString(String.valueOf(second), xcoordinate, ycoordinate);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }

        class TimerListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                second--;

                if (second >= 0) {
                    pbar.setValue(second);
                    repaint();
                } else {

                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Game Over!\n The scret code is " + choose[0] + choose[1] + choose[2] + choose[3]);
                    int yes = JOptionPane.showConfirmDialog(null, "Do you went to Continue");

                    switch (yes) {
                        case JOptionPane.YES_OPTION:
                            resetGame();
                            break;
                        case JOptionPane.NO_OPTION:
                            timer.stop();
                            System.exit(0);
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            timer.stop();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        GuessPassword frame = new GuessPassword();

        frame.setSize(500, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //generating 4 Random numbers without repeat  
    private void get4RandomNumber() {

        for (int i = 0; i < 4; i++) {
            choose[i] = (int) (Math.random() * 10);
        }

        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    choose[i] = (int) (Math.random() * 10);
                    break;
                case 1:
                    if (choose[0] == choose[1]) {
                        while (true) {
                            choose[i] = (int) (Math.random() * 10);
                            if (choose[0] != choose[1]) {
                                break;
                            }
                        }
                    }
                    break;
                case 2:
                    if (choose[0] == choose[2] || choose[1] == choose[2]) {
                        while (true) {
                            choose[i] = (int) (Math.random() * 10);
                            if (choose[0] != choose[2] && choose[1] != choose[2]) {
                                break;
                            }
                        }
                    }
                    break;
                case 3:
                    if (choose[0] == choose[3] || choose[1] == choose[3] || choose[2] == choose[3]) {
                        while (true) {
                            choose[i] = (int) (Math.random() * 10);
                            if (choose[0] != choose[3] && choose[1] != choose[3] && choose[2] != choose[3]) {
                                break;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

        }

    }

    private void resetGame() {
        counter = 0;
        second = 101;
        get4RandomNumber();
        resetcounter++;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                button[i][j].setText("-");
                label[i][j].setText("");
                if (i == 0) {
                    button[i][j].setBackground(Color.BLACK);
                    go[i].setVisible(true);
                } else {
                    button[i][j].setBackground(Color.LIGHT_GRAY);
                    go[i].setVisible(false);
                }
            }
        }
        timer.start();
    }

    private int storeScore(int s) {
        File file = new File("score.txt");
        int previousscore = 0;

        if (file.exists()) {
            try {
                DataInputStream inputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                previousscore = inputstream.readInt();

                if (previousscore > s) {
                    previousscore = s;

                    DataOutputStream outstream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

                    outstream.writeInt(s);

                    outstream.close();
                    inputstream.close();

                    return previousscore;
                }
            } catch (IOException ex) {

            }
        } else {
            try {
                DataOutputStream outputstream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                outputstream.writeInt(s);
                outputstream.close();
                return s;
            } catch (IOException ex) {

            }
        }

        return previousscore;
    }
}

package TicTakToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameWindow extends JFrame {
    private static final int WIN_HEIGHT = 555;
    private static final int WIN_WIDTH = 507;
    private static final int WIN_POS_X = 800;
    private static final int WIN_POS_Y = 300;
    private static StartNewGameWindow startNewGameWindow;
    private static Map field;


    GameWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(WIN_POS_X, WIN_POS_Y, WIN_WIDTH, WIN_HEIGHT);
        setTitle("Tic Tac Toe");
        setResizable(false);

        // 15 создаем наше новое окно и указываем ему координаты первого окна с помощью "this"
        startNewGameWindow = new StartNewGameWindow(this);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        JButton btnNewGame = new JButton("Start new game");
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGameWindow.setVisible(true);
            }
        });
        bottomPanel.add(btnNewGame);
        JButton btnExit = new JButton("Exit game");
        // 17.1 создаем обработчик событий
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // 9.1 - добавляем кнопку в панель
        bottomPanel.add(btnExit);

        // 14 добавляем поле в наше окно
        field = new Map();
        add(field, BorderLayout.CENTER);

        // 9.2 добавляем панель к нашему окну
        add(bottomPanel, BorderLayout.SOUTH);

//        JLabel label = new JLabel("Score");
//        add(label, BorderLayout.NORTH);
        // 3 делаем окно видимым
        setVisible(true);
    }
    // 17 старт игры будет происходить из дополнительного окна, а этот метод в основом окне, связываем два этих окна (лучше нарисовать картинку)
    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength) {
        field.startNewGame(mode, fieldSizeX, fieldSizeY, winLength);
    }
}

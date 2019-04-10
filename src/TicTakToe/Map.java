package TicTakToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Map extends JPanel {

    Scoore scoore = new Scoore();
    int h;
    int a;

    int[] coordinates = new int[2];
    public static final int MODE_H_V_A = 0;
    public static final int MODE_H_V_H = 1;
    //Scoore scoore = new Scoore();

    // 24.1 чтобы заполнить поле
    int[][] field;
    int fieldSizeX;
    int fieldSizeY;
    int winLeght;
    // 25 высота и ширина каждоый ячейки
    int cellheight;
    int cellWidth;

    // 27 если ничего не нарисовано
    boolean isInitialized = false;

    private static final String MSG_DRAW = "Ничья!";
    private static final String MSG_HUMAN_WIN = "Победил игрок!";
    private static final String MSG_AI_WIN = "Победил компьютер!";
    private final Font font = new Font("Times new roman", Font.BOLD, 48);
    private int stateGameOver;
    private boolean gameOver = false;
    private static final int STATE_DRAW = 0;
    private static final int STATE_HUMAN_WIN = 1;
    private static final int STATE_AI_WIN = 2;

    // 10 создаем конструктор и задаем цвет поля
    Map() {
        setBackground(Color.orange);
        // 30 создаем слушателя шелчка мышки
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
    }

    // 31 создаем метод который определяем куда челкнули
    void update(MouseEvent e) {
        // пиксели делим на ширину и высоту
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellheight;

        System.out.println("x: " + cellX + " y: " + cellY);
        if (field[cellY][cellX] == 0) {
            cross(cellX, cellY, getGraphics());
        }// repaint();
    }

    // 24 метод для рисования нашего поля в целом
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    // 11 создаем метод который говорит о типе игры, размеры поля, и выиграшная длина
    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength) {
        // заглушка
//        System.out.println("mode = " + mode +
//                " fsX = " + fieldSizeX +
//                " fsy = " + fieldSizeY +
//                " winlen = " + winLength);
        //25 запоняем поля при старте новой игры
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLeght = winLength;
        field = new int[fieldSizeY][fieldSizeX];
        initField();


        isInitialized = true;
        // 28 говорим панели перерисоваться
        repaint();
    }


    // 24.1 метод для рисование
    void render(Graphics g) {
        if (!isInitialized) return;

        int panelWidth = getWidth();
        int panelHeigt = getHeight();
        // узнаем кол-во ечеек
        cellheight = panelHeigt / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;

        // 26 отрисовываем по Y (горизонтальные полоски)
        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellheight;
            g.drawLine(0, y, panelWidth, y);
        }

        // 29 отрисовываем по X (вертикальные полоски)
        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, panelHeigt);
        }
    }

    void initField() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = 0;
            }
        }
    }

    void cross(int cellX, int cellY, Graphics graphics) {
        field[cellY][cellX] = 1;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(new Color(0, 0, 255));
        graphics2D.setStroke(new BasicStroke((7.0f)));
        graphics2D.drawLine(cellX * cellWidth + cellWidth / 7, cellY * cellheight + cellheight / 7, (cellX + 1) * cellWidth - cellWidth / 7, (cellY + 1) * cellheight - cellheight / 7);
        graphics2D.drawLine(cellX * cellWidth + cellWidth / 7, (cellY + 1) * cellheight - cellheight / 7, (cellX + 1) * cellWidth - cellWidth / 7, cellY * cellheight + cellheight / 7);

        if (checkWin(1)) {
            initField();
            repaint();
            h++;
            scoore.setTitle("Вы выиграли! SkyNet уничтожен Player win: " + h + ",   AI win: " + a);
            showMessageGameOver(graphics, STATE_HUMAN_WIN);


        } else if (isFieldFull()) {
            scoore.setTitle(" Ничья баланс сил восстановлен" + "Player win: " + h + ",   AI win: " + a);
            initField();
            repaint();
            showMessageGameOver(graphics, STATE_DRAW);
        } else aiStep();
    }

    void circle(int cellY, int cellX, Graphics graphics) {
        field[cellY][cellX] = 2;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(new Color(255, 0, 0));
        graphics2D.setStroke(new BasicStroke((7.0f)));
        graphics2D.drawOval(cellX * cellWidth + (cellWidth / 10), cellY * cellheight + (cellheight / 10), cellWidth - (cellWidth / 5), cellheight - (cellheight / 5));
        if (checkWin(2)) {
            initField();
            repaint();
            a++;
            showMessageGameOver(graphics, STATE_AI_WIN);
            scoore.setTitle("Ужас!!! SkyNet выиграл! Player win: " + h + ",   AI win: " + a);
        }
        if (isFieldFull()) {
            scoore.setTitle(" Ничья баланс сил восстановлен" + "Player win: " + h + ",   AI win: " + a);
            showMessageGameOver(graphics, STATE_DRAW);
            initField();
            repaint();

        }
    }

    boolean horizontal(int sign, boolean flag, int winLeght) {
        for (int i = 0; i < fieldSizeY; i++) {
            int score = 0;
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == sign) {
                    if (j > 0 && field[i][j - 1] == 0) {
                        coordinates[0] = i;
                        coordinates[1] = j - 1;
                    }
                    score++;
                    if (score == winLeght) {
                        if (j < fieldSizeX - 1 && field[i][j + 1] == 0) {
                            coordinates[0] = i;
                            coordinates[1] = j + 1;
                            if (!flag) {
                                return true;
                            }
                        } else {
                            if (coordinates[0] > -1) {
                                if (!flag) {
                                    return true;
                                }
                            }
                        }
                        if (flag) {
                            return true;
                        }
                    }
                } else {
                    score = 0;
                    coordinates[0] = -2;
                }
            }
        }
        return false;
    }

    boolean vertical(int sign, boolean flag, int winLeght) {
        for (int j = 0; j < fieldSizeX; j++) {
            int score = 0;
            coordinates[0] = -2;
            for (int i = 0; i < fieldSizeY; i++) {
                if (field[i][j] == sign) {
                    if (i > 0 && field[i - 1][j] == 0) {
                        coordinates[0] = i - 1;
                        coordinates[1] = j;
                    }
                    score++;
                    if (score == winLeght) {
                        if (i < fieldSizeY - 1 && field[i + 1][j] == 0) {
                            coordinates[0] = i + 1;
                            coordinates[1] = j;
                            if (!flag) {
                                return true;
                            }
                        } else {
                            if (coordinates[0] > -1) {
                                if (!flag) {
                                    return true;
                                }
                            }
                        }
                        if (flag) {
                            return true;
                        }
                    }
                } else {
                    score = 0;
                    coordinates[0] = -2;
                }
            }
        }
        return false;
    }

    boolean basicDiagonal(int sign, boolean flag, int winLeght) {
        for (int i = 0; i < fieldSizeY - winLeght + 1; i++) {
            for (int j = 0; j < fieldSizeX - winLeght + 1; j++) {
                coordinates[0] = -2;
                int score = 0;
                if (field[i][j] == sign) {
                    if (i > 0 && j > 0 && field[i - 1][j - 1] == 0) {
                        coordinates[0] = i - 1;
                        coordinates[1] = j - 1;
                    }
                    int x = j + 1;
                    int y = i + 1;
                    score++;
                    while (x < fieldSizeX && y < fieldSizeY) {
                        if (field[y][x] == sign) {
                            score++;
                            if (score == winLeght) {
                                if (y < fieldSizeY - 1 && x < fieldSizeX - 1 && field[y + 1][x + 1] == 0) {
                                    coordinates[0] = y + 1;
                                    coordinates[1] = x + 1;
                                    if (!flag) {
                                        return true;
                                    }
                                } else {
                                    if (coordinates[0] > -1) {
                                        if (!flag) {
                                            return true;
                                        }
                                    }
                                }
                                if (flag) {
                                    return true;
                                }
                            }
                        } else {
                            score = 0;
                            coordinates[0] = -2;
                        }
                        x++;
                        y++;
                    }
                }
            }
        }
        return false;
    }

    boolean backgroundDiagonal(int sign, boolean flag, int winLeght) {
        for (int i = winLeght - 1; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX - winLeght + 1; j++) {
                coordinates[0] = -2;
                if (field[i][j] == sign) {
                    if (i < fieldSizeY - 1 && j > 0 && field[i + 1][j - 1] == 0) {
                        coordinates[0] = i + 1;
                        coordinates[1] = j - 1;
                    }
                    int x = j + 1;
                    int y = i - 1;
                    int score = 1;
                    while (x < fieldSizeX && y > -1) {
                        if (field[y][x] == sign) {
                            score++;
                            if (score == winLeght) {
                                if (x < fieldSizeX - 1 && y > 0 && field[y - 1][x + 1] == 0) {
                                    coordinates[0] = y - 1;
                                    coordinates[1] = x + 1;
                                    if (!flag) {
                                        return true;
                                    }
                                } else {
                                    if (coordinates[0] > -1) {
                                        if (!flag) {
                                            return true;
                                        }
                                    }
                                }
                                if (flag) {
                                    return true;
                                }
                            }
                        } else {
                            score = 0;
                            coordinates[0] = -2;
                        }
                        x++;
                        y--;
                    }
                }
            }
        }
        return false;
    }

    boolean checkWin(int sign) {
        if (horizontal(sign, true, winLeght)) return true;
        if (vertical(sign, true, winLeght)) return true;
        if (basicDiagonal(sign, true, winLeght)) return true;
        if (backgroundDiagonal(sign, true, winLeght)) return true;

        return false;
    }

    void aiStep() {
        int x = winLeght - 1;
        for (int i = x; i > 0; i--) {
            if (basicDiagonal(2, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            if (backgroundDiagonal(2, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            if (horizontal(2, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            if (vertical(2, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            if (basicDiagonal(1, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            if (backgroundDiagonal(1, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            if (horizontal(1, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            if (vertical(1, false, x)) {
                circle(coordinates[0], coordinates[1], getGraphics());
                break;
            }
            x--;
        }
    }

    boolean isFieldFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    void showMessageGameOver(Graphics g, int sw){
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(font);
        switch (sw){
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_HUMAN_WIN:
                g.drawString(MSG_HUMAN_WIN, 70, getHeight() / 2);
                break;
            case STATE_AI_WIN:
                g.drawString(MSG_AI_WIN, 20, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("unexpected Gameover state: " + stateGameOver);
        }
    }

}

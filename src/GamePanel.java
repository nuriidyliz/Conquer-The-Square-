import com.sun.xml.internal.ws.api.ha.StickyFeature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.util.ArrayList;


public class GamePanel extends JPanel implements MouseListener, Runnable {

    Box[][] boxes;
    Thread gameThread;
    final Preferences preferencesPointer;
    int gameSize;


    public GamePanel(Preferences preferences) {
        this.preferencesPointer = preferences;


        gameSize = preferencesPointer.gameMode;
        preferencesPointer.boxSize = preferencesPointer.canvasHeight / gameSize;

        setLayout(null);
        setLocation(20, 20);
        setBackground(preferencesPointer.background);
        addMouseListener(this);


        boxes = new Box[preferencesPointer.canvasHeight / preferencesPointer.boxSize][preferencesPointer.canvasHeight / preferencesPointer.boxSize];
        for (int i = 0; i < preferencesPointer.canvasHeight / preferencesPointer.boxSize; i++) {
            for (int j = 0; j < preferencesPointer.canvasWidth / preferencesPointer.boxSize; j++) {
                boxes[j][i] = new Box(i, j, preferencesPointer);
            }
        }

        prepareBoard();
        initializeFirstBoxes();

        gameThread = new Thread(this);
        gameThread.start();

    }


    @Override
    public void paint(Graphics g) {
        //Painting the boxes according to draw method in Box class.
        super.paint(g);
        for (Box[] row : boxes) {
            for (Box box : row) {
                box.draw(g);
            }
        }
    }

    private void initializeFirstBoxes() {
        switch (gameSize) {
            case 10:
                switch (preferencesPointer.players) {
                    case 2:
                        preferencesPointer.gameMoves.add("0-0-b");
                        preferencesPointer.gameMoves.add("9-9-r");
                        preferencesPointer.greenPlayer = false;
                        preferencesPointer.orangePlayer = false;
                        break;
                    case 4:
                        preferencesPointer.gameMoves.add("0-0-b");
                        preferencesPointer.gameMoves.add("9-0-g");
                        preferencesPointer.gameMoves.add("0-9-o");
                        preferencesPointer.gameMoves.add("9-9-r");
                        break;
                }
                break;
            case 20:
                switch (preferencesPointer.players) {
                    case 2:
                        preferencesPointer.gameMoves.add("0-0-b");
                        preferencesPointer.gameMoves.add("19-19-r");
                        break;
                    case 4:
                        preferencesPointer.gameMoves.add("0-0-b");
                        preferencesPointer.gameMoves.add("19-0-g");
                        preferencesPointer.gameMoves.add("0-19-o");
                        preferencesPointer.gameMoves.add("19-19-r");
                        break;
                }
                break;
            case 30:
                switch (preferencesPointer.players) {
                    case 2:
                        preferencesPointer.gameMoves.add("0-0-b");
                        preferencesPointer.gameMoves.add("29-29-r");
                        break;
                    case 4:
                        preferencesPointer.gameMoves.add("0-0-b");
                        preferencesPointer.gameMoves.add("29-0-g");
                        preferencesPointer.gameMoves.add("0-29-o");
                        preferencesPointer.gameMoves.add("29-29-r");
                        break;
                }
        }
    }

    private boolean isCheckedBefore(int x, int y) {

        for (int i = 0; i < preferencesPointer.gameMoves.size(); i++) {
            try {
                String[] coordinates = preferencesPointer.gameMoves.get(i).split("-");
                int finalX = Integer.parseInt(coordinates[0]);
                int finalY = Integer.parseInt(coordinates[1]);

                if (finalX == x && finalY == y) {
                    return true;
                }
            } catch (Exception e) {

            }
        }

        return false;
    }


    private void writeMoves(int x, int y, String color) {
        preferencesPointer.gameMoves.add(x + "-" + y + "-" + color);
    }

    private boolean checkBoxesAround(int x, int y, String color) {

        for (int i = 0; i < preferencesPointer.gameMoves.size(); i++) {
            try {
                String[] coordinates = preferencesPointer.gameMoves.get(i).split("-");
                int finalX = Integer.parseInt(coordinates[0]);
                int finalY = Integer.parseInt(coordinates[1]);
                String colorName = coordinates[2];

                if (finalX == x + 1 && finalY == y || finalX == x - 1 && finalY == y || finalX == x && finalY == y + 1 || finalX == x && finalY == y - 1) {
                    if (colorName.equals(color)) {
                        return true;
                    }
                }
            } catch (Exception e) {

            }
        }

        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int coordinateX = e.getX();
        int coordinateY = e.getY();
        String color = "b";


        switch (preferencesPointer.turn) {
            case 1:
                color = "b";
                break;
            case 2:
                color = "g";
                break;
            case 3:
                color = "o";
                break;
            case 4:
                color = "r";
                break;
        }

        hakCalmayalimLutfen();

        int finalX = coordinateX / preferencesPointer.boxSize;
        int finalY = coordinateY / preferencesPointer.boxSize;

        if (preferencesPointer.dieCount == 0) {
            JOptionPane.showMessageDialog(null, "Please roll the die first!!", "Warning!!", JOptionPane.ERROR_MESSAGE);

        } else if (isCheckedBefore(finalX, finalY)) {
            JOptionPane.showMessageDialog(null, "This box has been checked before!!", "Warning!!", JOptionPane.ERROR_MESSAGE);

        } else if (!checkBoxesAround(finalX, finalY, color)) {

            JOptionPane.showMessageDialog(null, "You cannot check this box!!", "Warning!!", JOptionPane.ERROR_MESSAGE);

        } else {
            if (preferencesPointer.bluePlayer && !preferencesPointer.greenPlayer && !preferencesPointer.orangePlayer && !preferencesPointer.redPlayer ||
                    !preferencesPointer.bluePlayer && preferencesPointer.greenPlayer && !preferencesPointer.orangePlayer && !preferencesPointer.redPlayer ||
                    !preferencesPointer.bluePlayer && !preferencesPointer.greenPlayer && preferencesPointer.orangePlayer && !preferencesPointer.redPlayer ||
                    !preferencesPointer.bluePlayer && !preferencesPointer.greenPlayer && !preferencesPointer.orangePlayer && preferencesPointer.redPlayer) {


                if (preferencesPointer.bluePlayer && !preferencesPointer.greenPlayer && !preferencesPointer.orangePlayer && !preferencesPointer.redPlayer) {
                    preferencesPointer.blueScore += ((preferencesPointer.gameMode * preferencesPointer.gameMode) - (preferencesPointer.blueScore + preferencesPointer.greenScore + preferencesPointer.orangeScore + preferencesPointer.redScore));
                    preferencesPointer.turn = -1;

                } else if (!preferencesPointer.bluePlayer && preferencesPointer.greenPlayer && !preferencesPointer.orangePlayer && !preferencesPointer.redPlayer) {
                    preferencesPointer.greenScore += ((preferencesPointer.gameMode * preferencesPointer.gameMode) - (preferencesPointer.blueScore + preferencesPointer.greenScore + preferencesPointer.orangeScore + preferencesPointer.redScore));
                    preferencesPointer.turn = -2;

                } else if (!preferencesPointer.bluePlayer && !preferencesPointer.greenPlayer && preferencesPointer.orangePlayer && !preferencesPointer.redPlayer) {
                    preferencesPointer.orangeScore += ((preferencesPointer.gameMode * preferencesPointer.gameMode) - (preferencesPointer.blueScore + preferencesPointer.greenScore + preferencesPointer.orangeScore + preferencesPointer.redScore));
                    preferencesPointer.turn = -3;

                } else if (!preferencesPointer.bluePlayer && !preferencesPointer.greenPlayer && !preferencesPointer.orangePlayer && preferencesPointer.redPlayer) {
                    preferencesPointer.redScore += ((preferencesPointer.gameMode * preferencesPointer.gameMode) - (preferencesPointer.blueScore + preferencesPointer.greenScore + preferencesPointer.orangeScore + preferencesPointer.redScore));
                    preferencesPointer.turn = -4;

                }


                preferencesPointer.dieCount = -1;

                JOptionPane.showMessageDialog(null, "Blue Score: " + preferencesPointer.blueScore +
                        "\nGreen Score: " + preferencesPointer.greenScore +
                        "\nOrange Score: " + preferencesPointer.orangeScore +
                        "\nRed Score: " + preferencesPointer.redScore, "Game Over!!", JOptionPane.INFORMATION_MESSAGE);

            } else if (preferencesPointer.bluePlayer && preferencesPointer.greenPlayer && preferencesPointer.orangePlayer && preferencesPointer.redPlayer) {
                switch (preferencesPointer.turn) {
                    case 1:
                        // preferencesPointer.movesPanelColor=preferencesPointer.blue;
                        boxes[finalY][finalX].color = preferencesPointer.blue;
                        repaint();
                        writeMoves(finalX, finalY, "b");
                        preferencesPointer.blueScore++;
                        break;
                    case 2:
                        //preferencesPointer.movesPanelColor=preferencesPointer.green;
                        boxes[finalY][finalX].color = preferencesPointer.green;
                        repaint();
                        writeMoves(finalX, finalY, "g");
                        preferencesPointer.greenScore++;
                        break;
                    case 3:
                        //preferencesPointer.movesPanelColor=preferencesPointer.orange;
                        boxes[finalY][finalX].color = preferencesPointer.orange;
                        repaint();
                        writeMoves(finalX, finalY, "o");
                        preferencesPointer.orangeScore++;
                        break;
                    case 4:
                        // preferencesPointer.movesPanelColor=preferencesPointer.red;
                        boxes[finalY][finalX].color = preferencesPointer.red;
                        repaint();
                        writeMoves(finalX, finalY, "r");
                        preferencesPointer.redScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;
                }
                if (preferencesPointer.turn >= 5) {
                    preferencesPointer.turn = 1;
                }

            } else if (preferencesPointer.bluePlayer && preferencesPointer.greenPlayer && preferencesPointer.orangePlayer) {
                switch (preferencesPointer.turn) {
                    case 1:
                        // preferencesPointer.movesPanelColor=preferencesPointer.blue;
                        boxes[finalY][finalX].color = preferencesPointer.blue;
                        repaint();
                        writeMoves(finalX, finalY, "b");
                        preferencesPointer.blueScore++;
                        break;
                    case 2:
                        //preferencesPointer.movesPanelColor=preferencesPointer.green;
                        boxes[finalY][finalX].color = preferencesPointer.green;
                        repaint();
                        writeMoves(finalX, finalY, "g");
                        preferencesPointer.greenScore++;
                        break;
                    case 3:
                        //preferencesPointer.movesPanelColor=preferencesPointer.orange;
                        boxes[finalY][finalX].color = preferencesPointer.orange;
                        repaint();
                        writeMoves(finalX, finalY, "o");
                        preferencesPointer.orangeScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;
                }
                if (preferencesPointer.turn >= 4) {
                    preferencesPointer.turn = 1;
                }

            } else if (preferencesPointer.bluePlayer && preferencesPointer.greenPlayer && preferencesPointer.redPlayer) {
                switch (preferencesPointer.turn) {
                    case 1:
                        // preferencesPointer.movesPanelColor=preferencesPointer.blue;
                        boxes[finalY][finalX].color = preferencesPointer.blue;
                        repaint();
                        writeMoves(finalX, finalY, "b");
                        preferencesPointer.blueScore++;
                        break;
                    case 2:
                        //preferencesPointer.movesPanelColor=preferencesPointer.green;
                        boxes[finalY][finalX].color = preferencesPointer.green;
                        repaint();
                        writeMoves(finalX, finalY, "g");
                        preferencesPointer.greenScore++;
                        break;
                    case 4:
                        // preferencesPointer.movesPanelColor=preferencesPointer.red;
                        boxes[finalY][finalX].color = preferencesPointer.red;
                        repaint();
                        writeMoves(finalX, finalY, "r");
                        preferencesPointer.redScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;
                }
                if (preferencesPointer.turn == 3)
                    preferencesPointer.turn++;

                if (preferencesPointer.turn >= 5) {
                    preferencesPointer.turn = 1;
                }
            } else if (preferencesPointer.bluePlayer && preferencesPointer.redPlayer && preferencesPointer.orangePlayer) {
                switch (preferencesPointer.turn) {
                    case 1:
                        // preferencesPointer.movesPanelColor=preferencesPointer.blue;
                        boxes[finalY][finalX].color = preferencesPointer.blue;
                        repaint();
                        writeMoves(finalX, finalY, "b");
                        preferencesPointer.blueScore++;
                        break;
                    case 3:
                        //preferencesPointer.movesPanelColor=preferencesPointer.orange;
                        boxes[finalY][finalX].color = preferencesPointer.orange;
                        repaint();
                        writeMoves(finalX, finalY, "o");
                        preferencesPointer.orangeScore++;
                        break;
                    case 4:
                        // preferencesPointer.movesPanelColor=preferencesPointer.red;
                        boxes[finalY][finalX].color = preferencesPointer.red;
                        repaint();
                        writeMoves(finalX, finalY, "r");
                        preferencesPointer.redScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;
                }
                if (preferencesPointer.turn == 2)
                    preferencesPointer.turn++;
                if (preferencesPointer.turn >= 5) {
                    preferencesPointer.turn = 1;
                }
            } else if (preferencesPointer.greenPlayer && preferencesPointer.redPlayer && preferencesPointer.orangePlayer) {
                switch (preferencesPointer.turn) {
                    case 2:
                        //preferencesPointer.movesPanelColor=preferencesPointer.green;
                        boxes[finalY][finalX].color = preferencesPointer.green;
                        repaint();
                        writeMoves(finalX, finalY, "g");
                        preferencesPointer.greenScore++;
                        break;
                    case 3:
                        //preferencesPointer.movesPanelColor=preferencesPointer.orange;
                        boxes[finalY][finalX].color = preferencesPointer.orange;
                        repaint();
                        writeMoves(finalX, finalY, "o");
                        preferencesPointer.orangeScore++;
                        break;
                    case 4:
                        // preferencesPointer.movesPanelColor=preferencesPointer.red;
                        boxes[finalY][finalX].color = preferencesPointer.red;
                        repaint();
                        writeMoves(finalX, finalY, "r");
                        preferencesPointer.redScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;
                }
                if (preferencesPointer.turn >= 5) {
                    preferencesPointer.turn = 2;
                }
            } else if (preferencesPointer.bluePlayer && preferencesPointer.greenPlayer) {
                switch (preferencesPointer.turn) {
                    case 1:
                        // preferencesPointer.movesPanelColor=preferencesPointer.blue;
                        boxes[finalY][finalX].color = preferencesPointer.blue;
                        repaint();
                        writeMoves(finalX, finalY, "b");
                        preferencesPointer.blueScore++;
                        break;
                    case 2:
                        //preferencesPointer.movesPanelColor=preferencesPointer.green;
                        boxes[finalY][finalX].color = preferencesPointer.green;
                        repaint();
                        writeMoves(finalX, finalY, "g");
                        preferencesPointer.greenScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;
                }
                if (preferencesPointer.turn >= 3) {
                    preferencesPointer.turn = 1;
                }
            } else if (preferencesPointer.bluePlayer && preferencesPointer.orangePlayer) {
                switch (preferencesPointer.turn) {
                    case 1:
                        // preferencesPointer.movesPanelColor=preferencesPointer.blue;
                        boxes[finalY][finalX].color = preferencesPointer.blue;
                        repaint();
                        writeMoves(finalX, finalY, "b");
                        preferencesPointer.blueScore++;
                        break;
                    case 3:
                        //preferencesPointer.movesPanelColor=preferencesPointer.orange;
                        boxes[finalY][finalX].color = preferencesPointer.orange;
                        repaint();
                        writeMoves(finalX, finalY, "o");
                        preferencesPointer.orangeScore++;
                        break;

                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;
                }
                if (preferencesPointer.turn == 2)
                    preferencesPointer.turn++;
                if (preferencesPointer.turn >= 4) {
                    preferencesPointer.turn = 1;
                }
            } else if (preferencesPointer.bluePlayer && preferencesPointer.redPlayer) {

                switch (preferencesPointer.turn) {
                    case 1:
                        // preferencesPointer.movesPanelColor=preferencesPointer.blue;
                        boxes[finalY][finalX].color = preferencesPointer.blue;
                        repaint();
                        writeMoves(finalX, finalY, "b");
                        preferencesPointer.blueScore++;
                        break;
                    case 4:
                        // preferencesPointer.movesPanelColor=preferencesPointer.red;
                        boxes[finalY][finalX].color = preferencesPointer.red;
                        repaint();
                        writeMoves(finalX, finalY, "r");
                        preferencesPointer.redScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;

                }
                if (preferencesPointer.turn == 2) {
                    preferencesPointer.turn = 4;
                }
                if (preferencesPointer.turn == 3) {
                    preferencesPointer.turn = 4;
                }
                if (preferencesPointer.turn >= 5) {
                    preferencesPointer.turn = 1;
                }
            } else if (preferencesPointer.orangePlayer && preferencesPointer.redPlayer) {
                switch (preferencesPointer.turn) {
                    case 3:
                        //preferencesPointer.movesPanelColor=preferencesPointer.orange;
                        boxes[finalY][finalX].color = preferencesPointer.orange;
                        repaint();
                        writeMoves(finalX, finalY, "o");
                        preferencesPointer.orangeScore++;
                        break;
                    case 4:
                        // preferencesPointer.movesPanelColor=preferencesPointer.red;
                        boxes[finalY][finalX].color = preferencesPointer.red;
                        repaint();
                        writeMoves(finalX, finalY, "r");
                        preferencesPointer.redScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;

                }
                if (preferencesPointer.turn == 1)
                    preferencesPointer.turn = 3;
                if (preferencesPointer.turn >= 5) {
                    preferencesPointer.turn = 3;
                }
            } else if (preferencesPointer.greenPlayer && preferencesPointer.redPlayer) {
                switch (preferencesPointer.turn) {

                    case 2:
                        //preferencesPointer.movesPanelColor=preferencesPointer.green;
                        boxes[finalY][finalX].color = preferencesPointer.green;
                        repaint();
                        writeMoves(finalX, finalY, "g");
                        preferencesPointer.greenScore++;
                        break;

                    case 4:
                        // preferencesPointer.movesPanelColor=preferencesPointer.red;
                        boxes[finalY][finalX].color = preferencesPointer.red;
                        repaint();
                        writeMoves(finalX, finalY, "r");
                        preferencesPointer.redScore++;
                        break;
                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;

                }
                if (preferencesPointer.turn == 1)
                    preferencesPointer.turn++;
                if (preferencesPointer.turn == 3)
                    preferencesPointer.turn++;
                if (preferencesPointer.turn >= 5) {
                    preferencesPointer.turn = 2;
                }
            } else if (preferencesPointer.greenPlayer && preferencesPointer.orangePlayer) {
                switch (preferencesPointer.turn) {

                    case 2:
                        //preferencesPointer.movesPanelColor=preferencesPointer.green;
                        boxes[finalY][finalX].color = preferencesPointer.green;
                        repaint();
                        writeMoves(finalX, finalY, "g");
                        preferencesPointer.greenScore++;
                        break;
                    case 3:
                        //preferencesPointer.movesPanelColor=preferencesPointer.orange;
                        boxes[finalY][finalX].color = preferencesPointer.orange;
                        repaint();
                        writeMoves(finalX, finalY, "o");
                        preferencesPointer.orangeScore++;
                        break;

                }
                repaint();


                preferencesPointer.dieCount--;

                if (preferencesPointer.dieCount == 0) {
                    preferencesPointer.turn++;

                }

                if (preferencesPointer.turn == 1)
                    preferencesPointer.turn++;

                if (preferencesPointer.turn >= 4) {
                    preferencesPointer.turn = 2;
                }
            }
            olenCiksinKasiyor();
        }


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void prepareBoard() {
        switch (gameSize) {
            case 10:
                switch (preferencesPointer.players) {
                    case 2:
                        boxes[0][0].color = preferencesPointer.blue;
                        boxes[9][9].color = preferencesPointer.red;
                        repaint();
                        break;
                    case 4:
                        boxes[0][0].color = preferencesPointer.blue;
                        boxes[0][9].color = preferencesPointer.green;
                        boxes[9][0].color = preferencesPointer.orange;
                        boxes[9][9].color = preferencesPointer.red;
                        repaint();
                        break;
                }
                break;
            case 20:
                switch (preferencesPointer.players) {
                    case 2:
                        boxes[0][0].color = preferencesPointer.blue;
                        boxes[19][19].color = preferencesPointer.red;
                        repaint();
                        break;
                    case 4:
                        boxes[0][0].color = preferencesPointer.blue;
                        boxes[0][19].color = preferencesPointer.green;
                        boxes[19][0].color = preferencesPointer.orange;
                        boxes[19][19].color = preferencesPointer.red;
                        repaint();
                        break;
                }
                break;

            case 30:
                switch (preferencesPointer.players) {
                    case 2:
                        boxes[0][0].color = preferencesPointer.blue;
                        boxes[29][29].color = preferencesPointer.red;
                        repaint();
                        break;
                    case 4:
                        boxes[0][0].color = preferencesPointer.blue;
                        boxes[0][29].color = preferencesPointer.green;
                        boxes[29][0].color = preferencesPointer.orange;
                        boxes[29][29].color = preferencesPointer.red;
                        repaint();
                        break;

                }
                break;
        }

    }

    @Override
    public void run() {
        while (true) {
            hakCalmayalimLutfen();
            try {
                //Time between filling or emptying a box according to Anim.txt file.
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void olenCiksinKasiyor() {
        ArrayList<String> blueBoxes = new ArrayList<>();
        ArrayList<String> greenBoxes = new ArrayList<>();
        ArrayList<String> orangeBoxes = new ArrayList<>();
        ArrayList<String> redBoxes = new ArrayList<>();

        boolean blueOlmus = true;
        boolean greenOlmus = true;
        boolean orangeOlmus = true;
        boolean redOlmus = true;


        for (int i = 0; i < preferencesPointer.gameMoves.size(); i++) {
            try {

                String[] theBox = preferencesPointer.gameMoves.get(i).split("-");
                int finalX = Integer.parseInt(theBox[0]);
                int finalY = Integer.parseInt(theBox[1]);
                String colorName = theBox[2];


                if (colorName.matches("b")) {
                    blueBoxes.add(finalX + "-" + finalY);
                } else if (colorName.matches("g")) {
                    greenBoxes.add(finalX + "-" + finalY);
                } else if (colorName.matches("o")) {
                    orangeBoxes.add(finalX + "-" + finalY);
                } else if (colorName.matches("r")) {
                    redBoxes.add(finalX + "-" + finalY);
                }

            } catch (Exception e) {

            }


        }


        for (int i = 0; i < blueBoxes.size(); i++) {
            try {
                String[] theBox = blueBoxes.get(i).split("-");
                int theX = Integer.parseInt(theBox[0]);
                int theY = Integer.parseInt(theBox[1]);

                String top = theX + "-" + (theY - 1);
                String down = theX + "-" + (theY + 1);
                String right = (theX + 1) + "-" + theY;
                String left = (theX - 1) + "-" + theY;

                if ((!blueBoxes.contains(top) && !greenBoxes.contains(top) && !orangeBoxes.contains(top) && !redBoxes.contains(top)) && theY != 0) {
                    blueOlmus = false;

                    break;
                }
                if ((!blueBoxes.contains(right) && !greenBoxes.contains(right) && !orangeBoxes.contains(right) && !redBoxes.contains(right)) && theX != (gameSize - 1)) {
                    blueOlmus = false;

                    break;
                }
                if ((!blueBoxes.contains(left) && !greenBoxes.contains(left) && !orangeBoxes.contains(left) && !redBoxes.contains(left)) && (theX != 0)) {

                    blueOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(down) && !greenBoxes.contains(down) && !orangeBoxes.contains(down) && !redBoxes.contains(down)) && theY != (gameSize - 1)) {

                    blueOlmus = false;
                    break;
                }
            } catch (Exception e) {

            }
        }

        for (int i = 0; i < greenBoxes.size(); i++) {
            try {
                String[] theBox = greenBoxes.get(i).split("-");
                int theX = Integer.parseInt(theBox[0]);
                int theY = Integer.parseInt(theBox[1]);

                String top = theX + "-" + Math.abs(theY - 1);
                String down = theX + "-" + (theY + 1);
                String right = (theX + 1) + "-" + theY;
                String left = Math.abs(theX - 1) + "-" + theY;

                if ((!blueBoxes.contains(top) && !greenBoxes.contains(top) && !orangeBoxes.contains(top) && !redBoxes.contains(top)) && theY != 0) {
                    greenOlmus = false;

                    break;
                }
                if ((!blueBoxes.contains(right) && !greenBoxes.contains(right) && !orangeBoxes.contains(right) && !redBoxes.contains(right)) && theX != (gameSize - 1)) {
                    greenOlmus = false;

                    break;
                }
                if ((!blueBoxes.contains(left) && !greenBoxes.contains(left) && !orangeBoxes.contains(left) && !redBoxes.contains(left)) && (theX != 0)) {

                    greenOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(down) && !greenBoxes.contains(down) && !orangeBoxes.contains(down) && !redBoxes.contains(down)) && theY != (gameSize - 1)) {

                    greenOlmus = false;
                    break;
                }
            } catch (Exception e) {

            }
        }


        for (int i = 0; i < orangeBoxes.size(); i++) {
            try {
                String[] theBox = orangeBoxes.get(i).split("-");
                int theX = Integer.parseInt(theBox[0]);
                int theY = Integer.parseInt(theBox[1]);

                String top = theX + "-" + Math.abs(theY - 1);
                String down = theX + "-" + (theY + 1);
                String right = (theX + 1) + "-" + theY;
                String left = Math.abs(theX - 1) + "-" + theY;


                if ((!blueBoxes.contains(top) && !greenBoxes.contains(top) && !orangeBoxes.contains(top) && !redBoxes.contains(top)) && theY != 0) {
                    orangeOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(right) && !greenBoxes.contains(right) && !orangeBoxes.contains(right) && !redBoxes.contains(right)) && theX != (gameSize - 1)) {
                    orangeOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(left) && !greenBoxes.contains(left) && !orangeBoxes.contains(left) && !redBoxes.contains(left)) && (theX != 0)) {
                    orangeOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(down) && !greenBoxes.contains(down) && !orangeBoxes.contains(down) && !redBoxes.contains(down)) && theY != (gameSize - 1)) {
                    orangeOlmus = false;
                    break;
                }
            } catch (Exception e) {
            }
        }


        for (int i = 0; i < redBoxes.size(); i++) {
            try {
                String[] theBox = redBoxes.get(i).split("-");
                int theX = Integer.parseInt(theBox[0]);
                int theY = Integer.parseInt(theBox[1]);

                String top = theX + "-" + Math.abs(theY - 1);
                String down = theX + "-" + (theY + 1);
                String right = (theX + 1) + "-" + theY;
                String left = Math.abs(theX - 1) + "-" + theY;


                if ((!blueBoxes.contains(top) && !greenBoxes.contains(top) && !orangeBoxes.contains(top) && !redBoxes.contains(top)) && theY != 0) {
                    redOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(right) && !greenBoxes.contains(right) && !orangeBoxes.contains(right) && !redBoxes.contains(right)) && theX != (gameSize - 1)) {
                    redOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(left) && !greenBoxes.contains(left) && !orangeBoxes.contains(left) && !redBoxes.contains(left)) && (theX != 0)) {
                    redOlmus = false;
                    break;
                }
                if ((!blueBoxes.contains(down) && !greenBoxes.contains(down) && !orangeBoxes.contains(down) && !redBoxes.contains(down)) && theY != (gameSize - 1)) {
                    redOlmus = false;
                    break;
                }
            } catch (Exception e) {

            }
        }


        if (preferencesPointer.bluePlayer && blueOlmus && preferencesPointer.turn == 1) {
            preferencesPointer.bluePlayer = false;
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;
        } else if (preferencesPointer.bluePlayer && blueOlmus) {
            preferencesPointer.bluePlayer = false;

        }

        if (preferencesPointer.greenPlayer && greenOlmus && preferencesPointer.turn == 2) {
            preferencesPointer.greenPlayer = false;
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;
        } else if (preferencesPointer.greenPlayer && greenOlmus) {
            preferencesPointer.greenPlayer = false;

        }

        if (preferencesPointer.orangePlayer && orangeOlmus && preferencesPointer.turn == 3) {
            preferencesPointer.orangePlayer = false;
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;
        } else if (preferencesPointer.orangePlayer && orangeOlmus) {
            preferencesPointer.orangePlayer = false;

        }

        if (preferencesPointer.redPlayer && redOlmus && preferencesPointer.turn == 4) {
            preferencesPointer.redPlayer = false;
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;
        } else if (preferencesPointer.redPlayer && redOlmus) {
            preferencesPointer.redPlayer = false;

        }


    }

    public void hakCalmayalimLutfen() {
        if (!preferencesPointer.bluePlayer && preferencesPointer.turn == 1) {
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;

        }

        if (!preferencesPointer.greenPlayer && preferencesPointer.turn == 2) {
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;
        }

        if (!preferencesPointer.orangePlayer && preferencesPointer.turn == 3) {
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;
        }

        if (!preferencesPointer.redPlayer && preferencesPointer.turn == 4) {
            preferencesPointer.dieCount = 0;
            preferencesPointer.turn++;
        }

    }
}

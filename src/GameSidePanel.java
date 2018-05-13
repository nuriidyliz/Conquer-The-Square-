import com.sun.deploy.uitoolkit.impl.awt.AWTPluginEmbeddedFrameWindow;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameSidePanel extends JPanel implements Runnable {
    //MainFrame mainFrame;

    public MovesPanel movesPanel;
    public ScoresPanel scoresPanel;
    public ButtonsPanel buttonsPanel;
    Thread refreshThread;

    final Preferences preferencesPointerGSPanel;
    final GamePanel gamePanelPointerGSPanel;

    public GameSidePanel(MainFrame _mainFrame, Preferences preferences, GamePanel _gamePanel) {

        gamePanelPointerGSPanel = _gamePanel;
        preferencesPointerGSPanel = preferences;
        movesPanel = new MovesPanel(preferences);
        buttonsPanel = new ButtonsPanel(_mainFrame);
        scoresPanel = new ScoresPanel(preferences);


        setBackground(preferencesPointerGSPanel.background);
        setLayout(null);
        movesPanel.setBounds(150, 50, 200, 200);
        scoresPanel.setBounds(190, 300, 140, 140);
        buttonsPanel.setBounds(135, 500, 250, 100);
        add(movesPanel);
        add(scoresPanel);
        add(buttonsPanel);

        movesPanel.setVisible(true);
        scoresPanel.setVisible(true);
        buttonsPanel.setVisible(true);

        refreshThread = new Thread(this);
        refreshThread.start();
    }

    @Override
    public void run() {

        while (true) {

           // movesPanel.movesLabel.repaint();
            scoresPanel.scoreBlue.setText(String.valueOf(preferencesPointerGSPanel.blueScore));
            scoresPanel.scoreGreen.setText(String.valueOf(preferencesPointerGSPanel.greenScore));
            scoresPanel.scoreOrange.setText(String.valueOf(preferencesPointerGSPanel.orangeScore));
            scoresPanel.scoreRed.setText(String.valueOf(preferencesPointerGSPanel.redScore));


            String colorName = "b";
            switch (preferencesPointerGSPanel.turn) {
                case 1:
                    colorName = "b";
                    break;
                case 2:
                    colorName = "g";
                    break;
                case 3:
                    colorName = "o";
                    break;
                case 4:
                    colorName = "r";
                    break;
                default:

            }
                if(preferencesPointerGSPanel.dieCount==0){
                    movesPanel.movesLabel.setIcon(new ImageIcon("Files/Die/" + colorName + "R.png"));
                }else if (preferencesPointerGSPanel.turn == -1||preferencesPointerGSPanel.turn == -2||preferencesPointerGSPanel.turn == -3||preferencesPointerGSPanel.turn == -4){
                    switch (preferencesPointerGSPanel.turn){
                        case -1:
                            movesPanel.movesLabel.setIcon(new ImageIcon("Files/Die/blueWin.gif"));
                            break;
                        case -2:
                            movesPanel.movesLabel.setIcon(new ImageIcon("Files/Die/greenWin.gif"));
                            break;
                        case -3:
                            movesPanel.movesLabel.setIcon(new ImageIcon("Files/Die/orangeWin.gif"));
                            break;
                        case -4:
                            movesPanel.movesLabel.setIcon(new ImageIcon("Files/Die/redWin.gif"));
                            break;

                    }
                }
                else {
                    movesPanel.movesLabel.setIcon(new ImageIcon("Files/Die/" + colorName + preferencesPointerGSPanel.dieCount +".png"));
                }



            try {
                //Time between filling or emptying a box according to Anim.txt file.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    class MovesPanel extends JPanel {


        JLabel movesBanner = new JLabel(preferencesPointerGSPanel.movesBanner);
        JLabel movesLabel = new JLabel(preferencesPointerGSPanel.die);
        JButton dieButton = new JButton(preferencesPointerGSPanel.rollButton);
        Die die = new Die(preferencesPointerGSPanel);


        final Preferences preferencesPointer;

        MovesPanel(Preferences preferences) {

            this.preferencesPointer = preferences;

            setLayout(null);
            setBackground(preferencesPointerGSPanel.background);

            //movesTitle.setBounds(0, 0, 200, 50);
            movesBanner.setBounds(35, 0, 200, 50);
            movesLabel.setBounds(37,52,128,128);
            movesLabel.setOpaque(false);
            movesLabel.setBackground(preferencesPointerGSPanel.background);
            dieButton.setBounds(20, 180, 160, 38);
            dieButton.setOpaque(false);
            dieButton.setBorderPainted(false);
            dieButton.setRolloverIcon(preferencesPointerGSPanel.rollButtonRollover);


            dieButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (preferencesPointerGSPanel.dieCount == 0) {
                        die.rollDie(preferencesPointer);


                        switch (preferencesPointerGSPanel.turn) {
                            case 1:
                                movesLabel.setIcon(new ImageIcon("Files/Die/b"+preferences.dieCount+".png"));
                                break;
                            case 2:
                                movesLabel.setIcon(new ImageIcon("Files/Die/g"+preferences.dieCount+".png"));
                                break;
                            case 3:
                                movesLabel.setIcon(new ImageIcon("Files/Die/o"+preferences.dieCount+".png"));
                                break;
                            case 4:
                                movesLabel.setIcon(new ImageIcon("Files/Die/r"+preferences.dieCount+".png"));
                                break;
                            case -1:
                                movesLabel.setIcon(new ImageIcon("Files/Die/blueWin.gif"));
                                break;
                            case -2:
                                movesLabel.setIcon(new ImageIcon("Files/Die/greenWin.gif"));
                                break;
                            case -3:
                                movesLabel.setIcon(new ImageIcon("Files/Die/orangeWin.gif"));
                                break;
                            case -4:
                                movesLabel.setIcon(new ImageIcon("Files/Die/redWin.gif"));
                                break;
                        }


                    }else{
                        JOptionPane.showMessageDialog(null, "Please wait until turn ends!!", "Warning!!", JOptionPane.ERROR_MESSAGE);

                    }
                }
            });


            //add(movesTitle);
            add(movesBanner);
            add(movesLabel);
            add(dieButton);
        }

    }

    class ScoresPanel extends JPanel {
        JLabel scoreBlue = new JLabel();
        JLabel scoreGreen = new JLabel();
        JLabel scoreOrange = new JLabel();
        JLabel scoreRed = new JLabel();

        final Preferences preferencesPointer;

        ScoresPanel(Preferences _preferences) {
            this.preferencesPointer = _preferences;

            setLayout(new GridLayout(2, 2));

            switch (preferencesPointerGSPanel.players){
                case 2:
                    preferencesPointer.blueScore= 1;
                    preferencesPointer.redScore= 1;
                    break;
                case 4:
                    preferencesPointer.blueScore= 1;
                    preferencesPointer.greenScore= 1;
                    preferencesPointer.orangeScore= 1;
                    preferencesPointer.redScore= 1;
                    break;

            }

            scoreBlue.setBackground(preferencesPointer.blue);
            scoreGreen.setBackground(preferencesPointer.green);
            scoreOrange.setBackground(preferencesPointer.orange);
            scoreRed.setBackground(preferencesPointer.red);
            scoreBlue.setOpaque(true);
            scoreGreen.setOpaque(true);
            scoreOrange.setOpaque(true);
            scoreRed.setOpaque(true);

            scoreBlue.setText(String.valueOf(preferencesPointer.blueScore));
            scoreBlue.setHorizontalAlignment(SwingConstants.CENTER);
            scoreBlue.setFont(preferencesPointer.scoresFont);
            scoreBlue.setForeground(preferencesPointer.white);

            scoreGreen.setText(String.valueOf(preferencesPointer.greenScore));
            scoreGreen.setHorizontalAlignment(SwingConstants.CENTER);
            scoreGreen.setFont(preferencesPointer.scoresFont);
            scoreGreen.setForeground(preferencesPointer.white);

            scoreOrange.setText(String.valueOf(preferencesPointer.orangeScore));
            scoreOrange.setHorizontalAlignment(SwingConstants.CENTER);
            scoreOrange.setFont(preferencesPointer.scoresFont);
            scoreOrange.setForeground(preferencesPointer.white);

            scoreRed.setText(String.valueOf(preferencesPointer.redScore));
            scoreRed.setHorizontalAlignment(SwingConstants.CENTER);
            scoreRed.setFont(preferencesPointer.scoresFont);
            scoreRed.setForeground(preferencesPointer.white);


            add(scoreBlue);
            add(scoreGreen);
            add(scoreOrange);
            add(scoreRed);


        }
    }

    class ButtonsPanel extends JPanel {


        //  MainFrame mainFrame;

        final MainFrame mainFramePointer;


        ButtonsPanel(MainFrame mainFrame) {
            this.mainFramePointer = mainFrame;
            setLayout(null);

            setBackground(preferencesPointerGSPanel.background);


            JButton backButton = new JButton(preferencesPointerGSPanel.backToTheMainMenu);
            backButton.setBounds(2,12,250,73);
            backButton.setBorderPainted(false);
            backButton.setRolloverIcon(preferencesPointerGSPanel.backToTheMainMenuRoll);
            backButton.setOpaque(false);


            backButton.setVisible(true);


            add(backButton);


            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    mainFramePointer.setMainMenu();
                    preferencesPointerGSPanel.turn = 1;
                    preferencesPointerGSPanel.dieCount = 0;
                    preferencesPointerGSPanel.gameMoves.clear();
                    preferencesPointerGSPanel.blueScore = 0;
                    preferencesPointerGSPanel.greenScore = 0;
                    preferencesPointerGSPanel.orangeScore = 0;
                    preferencesPointerGSPanel.redScore = 0;
                    preferencesPointerGSPanel.bluePlayer = true;
                    preferencesPointerGSPanel.greenPlayer = true;
                    preferencesPointerGSPanel.orangePlayer = true;
                    preferencesPointerGSPanel.redPlayer = true;
                    preferencesPointerGSPanel.boxSize = 20;

                }
            });


        }
    }
}

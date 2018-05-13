import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

//These classes are for the side menu/Menu panel.

public class MenuPanel extends JPanel implements ActionListener {

    //Defining variables.

    private JLabel title ;
    private JPanel bannerCTS = new JPanel();
    private JPanel optionsMenu;
    private StartMenu startMenu;

//    MainFrame mainFrame;

    final Preferences preferencesPointerMenuPanel;

    public MenuPanel(MainFrame _mainFrame, Preferences preferences) {
        this.preferencesPointerMenuPanel = preferences;
        //Defining menuPanel and adding features.
        setLayout(new GridLayout(3, 1, 0, 0));
        setBackground(preferencesPointerMenuPanel.background);

        title = new JLabel(preferences.banner);
        startMenu = new StartMenu(_mainFrame, preferencesPointerMenuPanel);
        optionsMenu = new OptionsMenu(preferencesPointerMenuPanel);

        add(bannerCTS);
        add(optionsMenu);
        add(startMenu);

        bannerCTS.setBackground(preferencesPointerMenuPanel.background);
        bannerCTS.add(title);


    }


    class OptionsMenu extends JPanel {



        final Preferences preferencesPointer;

        OptionsMenu(Preferences preferences) {

            this.preferencesPointer = preferences;

            setLayout(new GridLayout(1, 2, 0, 0));
            setBackground(preferencesPointer.background);

            JPanel players = new JPanel();
            JPanel gameMode = new JPanel();

            add(players);
            add(gameMode);

            players.setBackground(preferencesPointer.background);
            players.setOpaque(false);
            players.setLayout(new GridLayout(4, 1, 0, 0));
            JLabel playersTitle = new JLabel(new ImageIcon("Files/players.png"));


            //Adding and configuring RadioButtons.
            JRadioButton player2 = new JRadioButton(preferencesPointer.twoPlayers); //2 players
            JRadioButton player4 = new JRadioButton(preferencesPointer.fourPlayers); //4 players.
            player2.setHorizontalAlignment(JRadioButton.CENTER);
            player4.setHorizontalAlignment(JRadioButton.CENTER);
            player2.setOpaque(false);
            player4.setOpaque(false);

            ButtonGroup btgPlayer = new ButtonGroup();

            btgPlayer.add(player2);
            btgPlayer.add(player4);

            players.add(playersTitle);
            players.add(player2);
            players.add(player4);

            //Listeners.
            ItemListener rdBtnListener1 = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    AbstractButton radio = (AbstractButton) e.getSource();
                    int state = e.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        radio.setIcon(preferencesPointer.twoPlayersSelected);
                        preferencesPointer.players = 2;
                        preferencesPointer.greenPlayer = false;
                        preferencesPointer.orangePlayer = false;


                    } else if (state == ItemEvent.DESELECTED)
                        radio.setIcon(preferencesPointer.twoPlayers);
                }
            };
            ItemListener rdBtnListener3 = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    AbstractButton radio = (AbstractButton) e.getSource();
                    int state = e.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        radio.setIcon(preferencesPointer.fourPlayersSelected);
                        preferencesPointer.players = 4;


                    } else if (state == ItemEvent.DESELECTED)
                        radio.setIcon(preferencesPointer.fourPlayers);
                }
            };

            player2.addItemListener(rdBtnListener1);
            player4.addItemListener(rdBtnListener3);


            gameMode.setBackground(preferencesPointer.background);
            gameMode.setOpaque(false);
            gameMode.setLayout(new GridLayout(4, 1, 0, 0));
            JLabel gameModeTitle = new JLabel(new ImageIcon("Files/gameMode.png"));

            //Adding and configuring RadioButtons.
            JRadioButton tenToTen = new JRadioButton(preferencesPointer.tenIcon); //10*10 game mode.
            JRadioButton twentyToTwenty = new JRadioButton(preferencesPointer.twentyIcon); //20*20 game mode.
            JRadioButton thirtyToThirty = new JRadioButton(preferencesPointer.thirtyIcon); //30*30 game mode.
            tenToTen.setHorizontalAlignment(JRadioButton.CENTER);
            twentyToTwenty.setHorizontalAlignment(JRadioButton.CENTER);
            thirtyToThirty.setHorizontalAlignment(JRadioButton.CENTER);
            tenToTen.setOpaque(false);
            twentyToTwenty.setOpaque(false);
            thirtyToThirty.setOpaque(false);

            ButtonGroup btgGameMode = new ButtonGroup();

            btgGameMode.add(tenToTen);
            btgGameMode.add(twentyToTwenty);
            btgGameMode.add(thirtyToThirty);

            gameMode.add(gameModeTitle);
            gameMode.add(tenToTen);
            gameMode.add(twentyToTwenty);
            gameMode.add(thirtyToThirty);

            //Listeners

            ItemListener rdBtnListener4 = new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    AbstractButton radio = (AbstractButton) e.getSource();
                    int state = e.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        radio.setIcon(preferencesPointer.tenSelectedIcon);
                        preferencesPointer.gameMode = 10;



                    } else if (state == ItemEvent.DESELECTED) {
                        radio.setIcon(preferencesPointer.tenIcon);
                    }
                }
            };
            ItemListener rdBtnListener5 = new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    AbstractButton radio = (AbstractButton) e.getSource();
                    int state = e.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        radio.setIcon(preferencesPointer.twentySelectedIcon);
                        preferencesPointer.gameMode = 20;


                    } else if (state == ItemEvent.DESELECTED) {
                        radio.setIcon(preferencesPointer.twentyIcon);
                    }
                }
            };
            ItemListener rdBtnListener6 = new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    AbstractButton radio = (AbstractButton) e.getSource();
                    int state = e.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        radio.setIcon(preferencesPointer.thirtySelectedIcon);
                        preferencesPointer.gameMode = 30;



                    } else if (state == ItemEvent.DESELECTED) {
                        radio.setIcon(preferencesPointer.thirtyIcon);
                    }
                }
            };

            tenToTen.addItemListener(rdBtnListener4);
            twentyToTwenty.addItemListener(rdBtnListener5);
            thirtyToThirty.addItemListener(rdBtnListener6);


        }

    }

    class StartMenu extends JPanel {
        //Panel for Start and How To buttons

        final MainFrame mainFramePointer;
        final Preferences preferencesPointerStartMenu;

        StartMenu(MainFrame mainFrame, Preferences preferences) {
            this.mainFramePointer = mainFrame;
            this.preferencesPointerStartMenu = preferences;


            setBackground(preferencesPointerStartMenu.background);
            setLayout(null);

            JButton startButton = new JButton(preferencesPointerStartMenu.startIcon);
            startButton.setBorderPainted(false);
            startButton.setRolloverIcon(preferencesPointerStartMenu.startIconRollover);
            startButton.setBounds(100, 30, 125, 125);

            JButton howToPlayButton = new JButton(preferencesPointerStartMenu.howToIcon);
            howToPlayButton.setBorderPainted(false);
            howToPlayButton.setRolloverIcon(preferencesPointerStartMenu.howToIconRollover);
            howToPlayButton.setBounds(316, 30, 300, 125);


            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    if (preferencesPointerStartMenu.players == 0) {
                        JOptionPane.showMessageDialog(null, "Please select player!", "Warning!!", JOptionPane.ERROR_MESSAGE);

                    } else if (preferencesPointerStartMenu.gameMode == 0) {
                        JOptionPane.showMessageDialog(null, "Please select game mode!", "Warning!!", JOptionPane.ERROR_MESSAGE);
                    } else {

                        mainFramePointer.setGameScreen(mainFramePointer);
                    }
                }
            });

            howToPlayButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainFramePointer.setHowToScreen();
                }
            });


            add(startButton);
            add(howToPlayButton);


        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}
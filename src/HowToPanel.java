
import org.omg.CORBA.MARSHAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class HowToPanel extends JPanel {

    public static JPanel tipOne;
    public static JPanel tipTwo;
    public static JPanel tipThree;
    public static JPanel tipFour;
    Preferences preferencesPointerHTPanel;
    MainFrame mainFramePointerHTPanel;





    public HowToPanel(Preferences _preferences, MainFrame _mainFrame) {
        preferencesPointerHTPanel=_preferences;
        mainFramePointerHTPanel=_mainFrame;
        JPanel howToButtonsPanel = new howToButtonsPanel(mainFramePointerHTPanel,preferencesPointerHTPanel);

        tipOne = new TipPanel(1,preferencesPointerHTPanel);
        tipTwo = new TipPanel(2,preferencesPointerHTPanel);
        tipThree = new TipPanel(3,preferencesPointerHTPanel);
        tipFour = new TipPanel(4,preferencesPointerHTPanel);
        setVisible(false);

        tipThree.setVisible(false);
        tipFour.setVisible(false);

        setLayout(null);
        setBackground(_preferences.background);
        setSize(1300, 700);
        JLabel howToBanner = new JLabel(preferencesPointerHTPanel.bannerHowTo);
        howToBanner.setBounds(200, 20, 900, 100);
        add(howToBanner);
        add(tipOne);
        add(tipTwo);
        add(tipThree);
        add(tipFour);

        tipOne.setBounds(100, 130, 555, 430);
        tipTwo.setBounds(655, 130, 555, 430);
        tipThree.setBounds(100, 130, 555, 430);
        tipFour.setBounds(655, 130, 555, 430);
        //add(tip2);

        add(howToButtonsPanel);
        howToButtonsPanel.setBounds(100, 575, 1165, 120);

    }


}

class howToButtonsPanel extends JPanel {

    MainFrame mainFramePointerBP;
    Preferences preferencesPointerBP;

    JButton back = new JButton(new ImageIcon("Files/backToMenu.png"));
    JButton previous = new JButton(new ImageIcon("Files/previous.png"));
    JButton next = new JButton(new ImageIcon("Files/next.png"));


    public howToButtonsPanel(MainFrame _mainframe,Preferences _preferences) {
        mainFramePointerBP = _mainframe;
        preferencesPointerBP = _preferences;
        setLayout(null);
        setBackground(preferencesPointerBP.background);

        add(back);
        add(previous);
        add(next);

        back.setRolloverIcon(new ImageIcon("Files/backToMenuRollover.png"));
        previous.setRolloverIcon(new ImageIcon("Files/previousRollover.png"));
        next.setRolloverIcon(new ImageIcon("Files/nextRollover.png"));
        back.setBorderPainted(false);
        previous.setBorderPainted(false);
        next.setBorderPainted(false);

        back.setBounds(0, 0, 325, 90);
        previous.setBounds(388, 0, 325, 90);
        next.setBounds(776, 0, 325, 90);

        back.addActionListener(howToListeners);
        previous.addActionListener(howToListeners);
        next.addActionListener(howToListeners);
    }

    ActionListener howToListeners = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == back) {
                mainFramePointerBP.setMainMenuHowTo();

            } else if (e.getSource() == previous) {
                HowToPanel.tipOne.setVisible(true);
                HowToPanel.tipTwo.setVisible(true);
                HowToPanel.tipThree.setVisible(false);
                HowToPanel.tipFour.setVisible(false);
            } else {
                HowToPanel.tipOne.setVisible(false);
                HowToPanel.tipTwo.setVisible(false);
                HowToPanel.tipThree.setVisible(true);
                HowToPanel.tipFour.setVisible(true);

            }
        }
    };
}


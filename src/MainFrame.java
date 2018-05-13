import com.apple.eawt.Application;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {


    private Preferences preferences = new Preferences();
    public JPanel animationPanel = new AnimationPanel(preferences);
    public JPanel menuPanel = new MenuPanel(this, preferences);
    public GamePanel gamePanel;
    public JPanel gameSidePanel;
    public JPanel howToPanel = new HowToPanel(preferences, this);

    public MainFrame() {


        //Configuring and adding dock icon for macOS.
    Application app = Application.getApplication();
        Image iconImg = Toolkit.getDefaultToolkit().getImage("Files/icon.png");
       app.setDockIconImage(iconImg);

        //Setting features of main frame

        getContentPane().setBackground(preferences.background);
        setIconImage(new ImageIcon("Files/icon.png").getImage());
        setTitle("Conquer The Square");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1300, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        animationPanel.setBounds(10, 40, preferences.canvasWidth, preferences.canvasHeight);
        menuPanel.setBounds(preferences.canvasWidth + 20, 30, 680, 700);


        add(animationPanel);
        add(menuPanel);
        add(howToPanel);
        howToPanel.setVisible(false);



    }

    public static void main(String[] args) {

        JFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);

    }

    public void setGameScreen(MainFrame mainFrame) {

        animationPanel.setVisible(false);
        menuPanel.setVisible(false);
        howToPanel.setVisible(false);
        gamePanel = new GamePanel(preferences);
        gamePanel.setBounds(50, 40, preferences.canvasWidth, preferences.canvasHeight);

        gameSidePanel = new GameSidePanel(mainFrame, preferences, gamePanel);
        gameSidePanel.setBounds(preferences.canvasWidth + 100, 30, 680, 700);
        mainFrame.add(gamePanel);
        mainFrame.add(gameSidePanel);
        gameSidePanel.setVisible(true);

    }

    public void setMainMenu() {
        animationPanel.setVisible(true);
        menuPanel.setVisible(true);
        gamePanel.setVisible(false);
        gameSidePanel.setVisible(false);
    }

    public void setHowToScreen() {
        howToPanel.setVisible(true);
        animationPanel.setVisible(false);
        menuPanel.setVisible(false);
    }
    public void setMainMenuHowTo(){
        animationPanel.setVisible(true);
        menuPanel.setVisible(true);
        howToPanel.setVisible(false);
    }


}
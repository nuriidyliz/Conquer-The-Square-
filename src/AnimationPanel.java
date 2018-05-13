
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class AnimationPanel extends JPanel implements Runnable {

    //Defining variables.
    Box[][] boxes;
    Thread gameThread;
    ArrayList<String> anim;

    final Preferences preferencesPointer;
    public AnimationPanel(Preferences preferences) {
        this.preferencesPointer = preferences;

        //setting features of animation panel.
        setLayout(null);
        setLocation(20, 20);
        setBackground(preferencesPointer.background);

        //Defining an arrayList for animation codes.
        anim = new ArrayList<>();


        //Adding the boxes to boxes array
        boxes = new Box[preferencesPointer.canvasHeight / preferencesPointer.boxHeight][preferencesPointer.canvasHeight / preferencesPointer.boxWidth];
        for (int i = 0; i < preferencesPointer.canvasHeight / preferencesPointer.boxHeight; i++) {
            for (int j = 0; j < preferencesPointer.canvasWidth / preferencesPointer.boxWidth; j++) {
                boxes[j][i] = new Box(i, j,preferencesPointer);
            }
        }

        //Defining first colors for corners for animation.
        boxes[0][0].color = preferencesPointer.blue;
        boxes[0][29].color = preferencesPointer.green;
        boxes[29][0].color = preferencesPointer.orange;
        boxes[29][29].color = preferencesPointer.red;

        //Calling the method to start and run the animation.
        addSteps();


        gameThread = new Thread(this);
        gameThread.start();

    }


    public void addSteps() {
        //Defining the anim.txt file

        Scanner scan = null;
        try {
            scan = new Scanner(preferencesPointer.animationCodes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Reading the Anim.txt file
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.length() > 0) {
                anim.add(line);
            }
        }

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

    //Method to analyze the animation order. They are divided with dashes.
    //First number is x value, second value is y value and the letter is for the color.
    //'b' stands for Blue, 'g' stands for Green, 'r' stands for Red and 'o' stands for Orange.

    public void parseString(String code) {
        try {
            String[] coordinates = code.split("-");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            Color color = Color.WHITE;
            String colorName = coordinates[2];
            if (colorName.equals("b")) {
                color = preferencesPointer.blue;
            } else if (colorName.equals("g")) {
                color = preferencesPointer.green;
            } else if (colorName.equals("r")) {
                color = preferencesPointer.red;
            } else if (colorName.equals("o")) {
                color = preferencesPointer.orange;
            }
            boxes[y][x].color = color;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to clear the boxes for animation.
    //Animation firstly fills the boxes and than empties them to be able to fill them again.
    //This is necessary for animation to be in aloop.
    public void emptyBoxes(String code) {
        try {
            String[] coordinates = code.split("-");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            boxes[y][x].color = Color.white;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int index = 0;
    private boolean isForward = true;

    @Override
    public void run() {

        while (true) {

            //Filling the animation.
            if (isForward) {
                parseString(anim.get(index));
                if (index < anim.size() - 1)
                    index++;
                repaint();
                if (index == anim.size() - 1) {
                    isForward = false;
                }

            } else {
                //Emptying the animation canvas, to fill again in a loop.
                emptyBoxes(anim.get(index));

                if (index > 0)
                    index--;
                repaint();
                if (index == 0) {
                    isForward = true;
                }
            }


            try {
                //Time between filling or emptying a box according to Anim.txt file.
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

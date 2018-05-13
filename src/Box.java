import java.awt.*;
import java.util.AbstractCollection;


public class Box {

    public int x;
    public int y;
    public Color color;
    final Preferences preferencesPointer;

    public Box(int x, int y,Preferences preferences) {
        this.preferencesPointer = preferences;
        this.x = x;
        this.y = y;
        color = preferencesPointer.white;
    }

    //Draw method, this method firstly draws the lines as rectangle,
    //and then fills them with white, to be able to color them in the animation.
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x * preferencesPointer.boxSize, y * preferencesPointer.boxSize, preferencesPointer.boxSize, preferencesPointer.boxSize);
        g.setColor(preferencesPointer.black);
        g.setFont(preferencesPointer.boxFont);
        g.drawRect(x * preferencesPointer.boxSize, y * preferencesPointer.boxSize, preferencesPointer.boxSize, preferencesPointer.boxSize);
        // g.drawString(x+","+y,x*AnimationPanel.BOX_WIDTH,(y+1)* AnimationPanel.BOX_HEIGHT);
    }
}

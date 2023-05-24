package ClassProject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CircleButton extends JButton {
    int diameter = 100;
    public CircleButton(int diameter){
        this.diameter = diameter;
        this.setBorder(new RoundBorder(diameter));
    }
    public CircleButton(){
        this.setBorder(new RoundBorder(diameter));
    }

    private static class RoundBorder implements Border {
        private int diameter;
        RoundBorder(int diameter){

            this.diameter = diameter;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.fillRoundRect(x, y, width-1,height -1, diameter, diameter);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.diameter + 1, this.diameter +1,  this.diameter + 2, this.diameter + 2);
        }

        @Override
        public boolean isBorderOpaque() {

            return true;
        }
    }
}

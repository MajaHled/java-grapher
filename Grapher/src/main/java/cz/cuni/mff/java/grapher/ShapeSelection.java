package cz.cuni.mff.java.grapher;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * A custom {@link JPanel} subclass implementing a shape picker.
 * The available shapes are {@code CIRCLE}, {@code SQUARE}, {@code TRIANGLE} and {@code OCTAGON} (from {@link ShapeSelection.Shape}).
 * The selection panels are arranged in a 2x2 grid.
 * The class also handles the drawing of the shapes in static methods.
 */
public class ShapeSelection extends JPanel {
    private final MyButton[] shapePanels;
    private final Shape[] shapes;
    private int chosenIndex;
    private final GraphSettings settings;
    private final Color borderColor;

    /**
     * Constructs a new {@link ShapeSelection} connected to the given {@link GraphSettings} instance.
     * @param settings a {@link GraphSettings} instance to write the selected shape into
     */
    public ShapeSelection(GraphSettings settings) {
        this.settings = settings;
        borderColor = Color.DARK_GRAY;

        this.setLayout(new GridLayout(2, 2));
        shapePanels = new MyButton[4];

        shapes = Shape.values();

        for (int i = 0; i < 4; i++) {
            shapePanels[i] = new MyButton(shapes[i]);
            shapePanels[i].setPreferredSize(new Dimension(45, 45));
            shapePanels[i].setBorder(new LineBorder(borderColor));
            int finalI = i;
            shapePanels[i].addActionListener(e -> setChosenIndex(finalI));
            this.add(shapePanels[i]);
        }
        this.repaint();
        setChosenIndex(0);
    }

    /**
     * {@code enum} of all the possible shapes that can be selected with {@link ShapeSelection}.
     */
    public enum Shape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        OCTAGON
    }

    /**
     * Utility {@link JButton} subclass, used as the shape panels in the picker.
     */
    private static class MyButton extends JButton {
        private final Shape shape;

        public MyButton(Shape shape) {
            this.shape = shape;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            drawShape(shape, (Graphics2D) g.create(), getWidth()/2,getHeight()/2, getWidth()-10, getHeight()-10);
        }
    }

    /**
     * Draw a filled shape with the given parameters.
     * The given {@link Graphics} instance should come with the desired color preselected.
     * @param shape the {@link ShapeSelection.Shape} that will be drawn
     * @param g the {@link Graphics} instance to draw on
     * @param x the x coordinate of the center
     * @param y the y coordinate of the center
     * @param width the width of the shape
     * @param height the height of the shape
     */
    public static void fillShape(Shape shape, Graphics g, int x, int y, int width, int height) {
        int X = x - width / 2;
        int Y = y - height / 2;
        switch (shape) {
            case CIRCLE -> g.fillOval(x - width / 2, y - height / 2, width, height);
            case SQUARE -> g.fillRect(x - width / 2, y - height / 2, width, height);
            case TRIANGLE ->
                    g.fillPolygon(new int[]{X + width / 2, X, X + width}, new int[]{Y, Y + height, Y + height}, 3);
            case OCTAGON -> {
                int[] Xs = new int[]{X, X + width / 3, X + 2 * width / 3, X + width};
                int[] Ys = new int[]{Y, Y + height / 3, Y + 2 * height / 3, Y + height};
                g.fillPolygon(new int[]{Xs[1], Xs[2], Xs[3], Xs[3], Xs[2], Xs[1], Xs[0], Xs[0]},
                        new int[]{Ys[0], Ys[0], Ys[1], Ys[2], Ys[3], Ys[3], Ys[2], Ys[1]},
                        8);
            }
        }
    }

    /**
     * Draw a shape outline with the given parameters.
     * The given {@link Graphics2D} instance should come with the desired color and stroke style preselected.
     * @param shape the {@link ShapeSelection.Shape} that will be drawn
     * @param g the {@link Graphics2D} instance to draw on
     * @param x the x coordinate of the center
     * @param y the y coordinate of the center
     * @param width the width of the shape
     * @param height the height of the shape
     */
    public static void drawShape(Shape shape, Graphics2D g, int x, int y, int width, int height) {
        int X = x - width / 2;
        int Y = y - height / 2;
        switch (shape) {
            case CIRCLE -> g.drawOval(x - width / 2, y - height / 2, width, height);
            case SQUARE -> g.drawRect(x - width / 2, y - height / 2, width, height);
            case TRIANGLE ->
                    g.drawPolygon(new int[]{X + width / 2, X, X + width}, new int[]{Y, Y + height, Y + height}, 3);
            case OCTAGON -> {
                int[] Xs = new int[]{X, X + width / 3, X + 2 * width / 3, X + width};
                int[] Ys = new int[]{Y, Y + height / 3, Y + 2 * height / 3, Y + height};
                g.drawPolygon(new int[]{Xs[1], Xs[2], Xs[3], Xs[3], Xs[2], Xs[1], Xs[0], Xs[0]},
                        new int[]{Ys[0], Ys[0], Ys[1], Ys[2], Ys[3], Ys[3], Ys[2], Ys[1]},
                        8);
            }
        }
    }

    /**
     * Select a new shape.
     * @param shape shape to be selected
     */
    public void select(Shape shape) {
        for (int i = 0; i < 4; i++) {
            if (shapes[i] == shape) {
                setChosenIndex(i);
                break;
            }
        }
    }

    /**
     * Select the {@code index}th shape panel.
     * @param index index of panel to select
     */
    private void setChosenIndex(int index) {
        if (index >= 4)
            return;
        shapePanels[chosenIndex].setBorder(new LineBorder(borderColor));
        chosenIndex = index;
        settings.setShapeNoPanelSelect(shapes[index]);
        shapePanels[index].setBorder(new LineBorder(borderColor, 3));
        this.repaint();
    }
}

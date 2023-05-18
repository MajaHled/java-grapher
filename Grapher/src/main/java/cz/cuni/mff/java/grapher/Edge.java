package cz.cuni.mff.java.grapher;

import java.awt.*;

/**
 * Represents an edge of a {@link PaintedGraph}.
 * Contains data about its appearance, references to the nodes that it connects and can draw itself.
 */
public class Edge {
    /** Node that is one endpoint of the edge. */
    private final Node start, end;
    /** Color of the edge. */
    private Color color;
    /** Is the edge selected? (affects appearance) */
    private boolean selected;

    /**
     * Constructs {@link Edge} with the given parameters.
     * @param start starting {@link Node}
     * @param end end {@link Node}
     * @param color {@link Color} of the edge
     */
    public Edge(Node start, Node end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.selected = false;
    }

    /**
     * Draws the edge. Selected edge will be dashed.
     * @param g {@link Graphics} instance to draw on
     */
    public void drawSelf(Graphics g) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g.create();
        if (selected) {
            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0, new float[]{9}, 0);
            g2.setStroke(dashed);
        } else {
            g2.setStroke(new BasicStroke(3));
        }
        g2.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * Determines whether the given point intersects with the edge.
     * @param x x coordinate of point
     * @param y y coordinate of point
     * @return {@code true} if inside, {@code false} if not
     */
    public boolean isInside(int x, int y) {
        float px = end.getX() - start.getX();
        float py = end.getY() - start.getY();
        float temp = (px*px) + (py*py);
        float u = ((x - start.getX()) * px + (y - start.getY()) * py) / (temp);
        if(u>1) u = 1;
        else if (u<0) u=0;
        float X = start.getX() + u * px;
        float Y = start.getY() + u * py;
        float dx = X - x;
        float dy = Y - y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist <= 3;
    }

    /**
     * Select the edge.
     */
    public void select() {
        selected = true;
    }
    /**
     * Deselect the edge.
     */
    public void deselect() {
        selected = false;
    }


    // Getters and Setters
    /**
     * Gets the {@link Node} where the edge starts.
     * @return start {@link Node}
     */
    public Node getStart() {
        return start;
    }
    /**
     * Gets the {@link Node} where the edge ends.
     * @return end {@link Node}
     */
    public Node getEnd() {
        return end;
    }
    /**
     * Gets the color of the edge.
     * @return {@link Color}
     */
    public Color getColor() {
        return color;
    }
    /**
     * Sets the color of the edge.
     * @param color {@link Color}
     */
    public void setColor(Color color) {
        this.color = color;
    }
}


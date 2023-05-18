package cz.cuni.mff.java.grapher;

import java.awt.*;

/**
 * Represents a partial edge of a {@link PaintedGraph}.
 * This is useful for edges that are currently being drawn/edited (dragged by the user).
 * Contains data about its appearance, end coordinates, reference to the starting node and can draw itself.
 */
public class PartialEdge {
    /** Starting {@link Node} of the partial edge. */
    private final Node start;
    /** Coordinate of the endpoint of the edge. */
    private int endX, endY;
    /** Color of the edge. */
    protected Color color;

    /**
     * Constructs {@link PartialEdge} with the given parameters.
     * @param start starting {@link Node}
     * @param endX x coordinate of the end point
     * @param endY y coordinate of the end point
     * @param color {@link Color} of the edge
     */
    public PartialEdge(Node start, int endX, int endY, Color color) {
        this.start = start;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
    }

    /**
     * Draws the edge. Selected edge will be dashed.
     * @param g {@link Graphics} instance to draw on
     */
    public void drawSelf(Graphics g) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(start.getX(), start.getY(), endX, endY);
    }

    /**
     * Returns an {@link Edge} which is created by connecting the {@code PartialEdge} to the given {@link Node}.
     * @param endNode {@code Node} where the new edge will end
     * @return the new {@code Edge}
     */
    public Edge toFullEdge(Node endNode) {
        return new Edge(start, endNode, color);
    }

    /**
     * Repositions the free end of the edge.
     * @param endX new x coordinate of the end point
     * @param endY new y coordinate of the end point
     */
    public void setEnd(int endX, int endY) {
        this.endX = endX;
        this.endY = endY;
    }

    // Getters and Setters
    /**
     * Gets the x coordinate of where the edge ends.
     * @return {@code int} x
     */
    public int getEndX() {
        return endX;
    }
    /**
     * Gets the y coordinate of where the edge ends.
     * @return {@code int} y
     */
    public int getEndY() {
        return endY;
    }
    /**
     * Gets the {@link Node} where the edge starts.
     * @return start {@link Node}
     */
    public Node getStart() {
        return start;
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

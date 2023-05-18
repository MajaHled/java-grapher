package cz.cuni.mff.java.grapher;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a node of a {@link PaintedGraph}.
 * Contains data about its appearance and can draw itself.
 */
public class Node {
    /** Coordinate of the center of the node. */
    private int x, y;
    /** Size of the node. */
    private int width, height;
    /** Color of the node. */
    private Color color;
    /** Shape of the node. */
    private ShapeSelection.Shape shape;
    /** Edges for which this node is the start or end node. */
    private Set<Edge> edges;
    /** Is the node selected? (affects appearance) */
    private boolean selected;

    /**
     * Constructs {@link Node} with the given parameters.
     * @param x x coordinate of center
     * @param y y coordinate of center
     * @param width width of node
     * @param height height of node
     * @param color {@link Color} of node
     * @param shape {@link ShapeSelection.Shape} of node
     */
    public Node(int x, int y, int width, int height, Color color, ShapeSelection.Shape shape) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.shape = shape;
        this.edges = new HashSet<>();
        this.selected = false;
    }

    /**
     * Draws the node. Selected node will have a thick outline.
     * @param g {@link Graphics} instance to draw on
     */
    public void drawSelf(Graphics g) {
        g.setColor(color);
        ShapeSelection.fillShape(shape, g, x, y, width, height);
        if (selected) {
            if (color == Color.BLACK) g.setColor(Color.GRAY);
            else g.setColor(Color.BLACK);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            ShapeSelection.drawShape(shape, g2, x, y, width, height);
        }
    }

    /**
     * Adds edge to node (so that it can be deleted when the node is deleted).
     * @param e {@link Edge} to add
     */
    public void addEdge(Edge e) {
        edges.add(e);
    }
    /**
     * Removes edge from node.
     * @param e {@link Edge} to remove
     */
    public void deleteEdge(Edge e) {
        edges.remove(e);
    }

    /**
     * Change the node's position.
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void reposition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Determines whether the given point intersects with the node.
     * @param x x coordinate of point
     * @param y y coordinate of point
     * @return {@code true} if inside, {@code false} if not
     */
    public boolean isInside(int x, int y) {
        return x >= this.x - width/2 && x <= this.x + width/2 && y >= this.y - width/2 && y <= this.y + width/2;
    }

    /**
     * Select the node.
     */
    public void select() {
        selected = true;
    }

    /**
     * Deselect the node.
     */
    public void deselect() {
        selected = false;
    }

    // Getters and Setters
    /**
     * Gets {@link Node#edges}.
     * @return edges
     */
    public Set<Edge> getEdges() {
        return edges;
    }
    /**
     * Gets the x coordinate of the center of the node.
     * @return {@code int} x
     */
    public int getX() {
        return x;
    }
    /**
     * Gets the y coordinate of the center of the node.
     * @return {@code int} y
     */
    public int getY() {
        return y;
    }
    /**
     * Gets the color of the node.
     * @return {@link Color}
     */
    public Color getColor() {
        return color;
    }
    /**
     * Sets the color of the node.
     * @param color {@link Color} to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
    /**
     * Gets the shape of the node.
     * @return {@link ShapeSelection.Shape}
     */
    public ShapeSelection.Shape getShape() {
        return shape;
    }
    /**
     * Sets the shape of the node.
     * @param shape {@link ShapeSelection.Shape} to set
     */
    public void setShape(ShapeSelection.Shape shape) {
        this.shape = shape;
    }
    /**
     * Gets the width of the node.
     * @return {@code int} width
     */
    public int getWidth() {
        return width;
    }
    /**
     * Gets the height of the node.
     * @return {@code int} height
     */
    public int getHeight() {
        return height;
    }
    /**
     * Sets the width of the node.
     * @param width {@code int} width
     */
    public void setWidth(int width) {
        this.width = width;
    }
    /**
     * Sets the height of the node.
     * @param height {@code int} height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}

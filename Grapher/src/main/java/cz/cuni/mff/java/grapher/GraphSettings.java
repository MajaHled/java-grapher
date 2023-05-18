package cz.cuni.mff.java.grapher;

import java.awt.*;

/**
 * Keeps the settings for a {@link GraphDisplay} instance, which it is linked to.
 * Also handles the changing of properties of selected elements of the {@code GraphDisplay}
 * as the settings are changed. May also update selections on {@link RecentColors} and {@link ShapeSelection}
 * instances as they are selected.
 */
public class GraphSettings {
    private Color color;
    private ShapeSelection.Shape shape;
    private Action action;
    private GraphDisplay display;
    private RecentColors colorPane;
    private ShapeSelection shapeSelection;

    /**
     * {@code enum} of all the possible actions that can be set with {@link GraphSettings}.
     */
    public enum Action {
        CREATE_NODE,
        EDIT_NODE,
        CREATE_EDGE,
        EDIT_EDGE
    }

    /**
     * Constructs a new {@link GraphSettings} instance.
     * @param color initial {@link Color}
     * @param shape initial {@link ShapeSelection.Shape}
     * @param action initial {@link Action}
     */
    public GraphSettings(Color color, ShapeSelection.Shape shape, Action action) {
        this.shape = shape;
        this.color = color;
        this.action = action;
    }

    /**
     * Connects a {@link GraphDisplay} instance to the {@code GraphSettings}.
     * Should always be called before the {@code GraphSettings} instance is used.
     * @param display {@link GraphDisplay} instance to connect
     */
    public void setDisplay(GraphDisplay display) {
        this.display = display;
    }
    /**
     * Connects a {@link RecentColors} instance to the {@code GraphSettings}.
     * Calling this method allows the {@code GraphSettings} to update the color selection as the settings are changed.
     * @param colorPane {@link RecentColors} instance to connect
     */
    public void setColorPane(RecentColors colorPane) {
        this.colorPane = colorPane;
    }
    /**
     * Connects a {@link ShapeSelection} instance to the {@code GraphSettings}.
     * Calling this method allows the {@code GraphSettings} to update the shape selection as the settings are changed.
     * @param shapeSelection {@link ShapeSelection} instance to connect
     */
    public void setShapeSelection(ShapeSelection shapeSelection) {
        this.shapeSelection = shapeSelection;
    }

    /**
     * Get the currently selected color.
     * @return {@link Color}
     */
    public Color getColor() {
        return color;
    }
    /**
     * Select color in the settings and update the linked {@link RecentColors} instance.
     * <b>IMPORTANT</b>: if updating the settings from the linked {@link RecentColors} instance, never use this method,
     * use {@link GraphSettings#setColorNoPanelSelect(Color)} instead.
     * @param color {@link Color} to select
     */
    public void setColor(Color color) {
        this.color = color;
        if (colorPane != null)
            colorPane.addColor(color);
        if (display != null)
            display.recolorSelected(color);
    }
    /**
     * Select color in the settings without triggering the {@link RecentColors} selection update.
     * <b>IMPORTANT</b>: if updating the settings from the linked {@link RecentColors} instance, always use this method
     * and never {@link GraphSettings#setColor(Color)} to avoid a cycle.
     * @param color {@link Color} to select
     */
    public void setColorNoPanelSelect(Color color) {
        this.color = color;
        if (display != null)
            display.recolorSelected(color);
    }

    /**
     * Get the currently selected shape.
     * @return {@link ShapeSelection.Shape}
     */
    public ShapeSelection.Shape getShape() {
        return shape;
    }

    /**
     * Select shape in the settings and update the linked {@link ShapeSelection} instance.
     * <b>IMPORTANT</b>: if updating the settings from the linked {@link ShapeSelection} instance, never use this method,
     * use {@link GraphSettings#setShapeNoPanelSelect(ShapeSelection.Shape)} instead.
     * @param shape {@link ShapeSelection.Shape} to select
     */
    public void setShape(ShapeSelection.Shape shape) {
        this.shape = shape;
        if (shapeSelection != null)
            shapeSelection.select(shape);
        if (display != null)
            display.reshapeSelected(shape);
    }
    /**
     * Select color in the settings without triggering the {@link ShapeSelection} selection update.
     * <b>IMPORTANT</b>: if updating the settings from the linked {@link ShapeSelection} instance, always use this method
     * and never {@link GraphSettings#setShapeNoPanelSelect(ShapeSelection.Shape)} to avoid a cycle.
     * @param shape {@link ShapeSelection.Shape} to select
     */
    public void setShapeNoPanelSelect(ShapeSelection.Shape shape) {
        this.shape = shape;
        if (display != null)
            display.reshapeSelected(shape);
    }

    /**
     * Get the currently selected action.
     * @return {@link Action}
     */
    public Action getAction() {
        return action;
    }
    /**
     * Select action in the settings.
     * @param action {@link Action} to select
     */
    public void setAction(Action action) {
        this.action = action;
        if (display != null) {
            if (action != Action.EDIT_EDGE)
                display.deselectEdge();
            if (action != Action.EDIT_NODE)
                display.deselectNode();
        }
    }
}

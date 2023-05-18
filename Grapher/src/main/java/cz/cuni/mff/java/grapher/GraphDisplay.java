package cz.cuni.mff.java.grapher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

/**
 * A custom {@link javax.swing.JPanel} subclass which handles displaying and editing graphs.
 * Is always linked to a single {@link PaintedGraph} instance, which it displays.
 * Sends editing instructions to the graph based on user input.
 */
public final class GraphDisplay extends JPanel {
    private final PaintedGraph graph;
    private final MouseHandler mh;
    private Node selectedNode;
    private Edge selectedEdge;

    private JScrollPane scrollPane;

    /**
     * Constructs a new {@code GraphDisplay}
     * @param settings {@code GraphSettings} instance to connect to the display.
     */
    public GraphDisplay(GraphSettings settings) {
        graph = new PaintedGraph();
        mh = new MouseHandler(graph, settings);
        settings.setDisplay(this);
        selectedNode = null;
        selectedEdge = null;

        this.addMouseListener(mh);
        this.addMouseMotionListener(mh);

        this.setBorder(BorderFactory.createTitledBorder("Drawing Area"));
    }

    /**
     * Sets a reference to the containing {@link JScrollPane}.
     * If a {@link GraphDisplay} instance is displayed within a {@link JScrollPane},
     * this method should always be called in order to facilitate proper scroll behavior
     * (i.e. so that scroll bars are displayed whenever a part of the graph is outside the visible area).
     * @param scrollPane {@code JScrollPane} instance to link
     */
    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    /**
     * Paints the {@code GraphDisplay}. Invokes standart {@link JPanel} painting,
     * plus paints the contained {@link PaintedGraph}.
     * Also, handles the component's preferred size and scroll behavior.
     * @param g the {@code Graphics} object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graph.drawSelf(g);

        var newPreferredSize = graph.getMinSize(new Dimension(700, 650));
        this.setPreferredSize(newPreferredSize);
        if (this.scrollPane != null) {
            scrollPane.setPreferredSize(newPreferredSize);
        }
    }

    /**
     * Loads a graph to display from a file, creating a new {@link PaintedGraph} instance and connecting it to itself.
     * @see PaintedGraph#loadFromFile(File) PaintedGraph's loading method for expected format
     * @param inputFile file with the graph definition
     * @return return code - 0 on success, 1 on failure
     */
    public int openGraph(File inputFile) {
        int ret = graph.loadFromFile(inputFile);
        repaint();
        return ret;
    }

    /**
     * Saves the displayed {@link PaintedGraph} to a file.
     * @see PaintedGraph#saveFile(File)  PaintedGraph's saving method for output format
     * @param outputFile file to which the graph will be saved
     * @return return code - 0 on success, 1 on failure
     */
    public int saveGraph(File outputFile) {
        return graph.saveFile(outputFile);
    }

    // Edit Methods
    /**
     * Selects the given {@link Node}.
     * @param node {@link Node} to select
     */
    private void setSelectedNode(Node node) {
        if (selectedNode != null) selectedNode.deselect();
        if (selectedEdge != null) deselectEdge();
        selectedNode = node;
        selectedNode.select();
        repaint();
    }
    /**
     * Selects the given {@link Edge}.
     * @param edge {@link Edge} to select
     */
    private void setSelectedEdge(Edge edge) {
        if (selectedEdge != null) selectedEdge.deselect();
        if (selectedNode != null) deselectNode();
        selectedEdge = edge;
        selectedEdge.select();
        repaint();
    }
    /**
     * Deselect selected node (if a node is selected).
     */
    public void deselectNode() {
        if (selectedNode != null) selectedNode.deselect();
        selectedNode = null;
        repaint();
    }
    /**
     * Deselect selected edge (if an edge is selected).
     */
    public void deselectEdge() {
        if (selectedEdge != null) selectedEdge.deselect();
        selectedEdge = null;
        repaint();
    }

    /**
     * Change the color of the selected element ({@link Node} or {@link Edge}).
     * @param color {@link Color} to change to
     */
    public void recolorSelected(Color color) {
        if (selectedNode != null)
            selectedNode.setColor(color);
        if (selectedEdge != null)
            selectedEdge.setColor(color);
        repaint();
    }
    /**
     * Change the shape of the selected {@link Node} (if a node is selected).
     * @param shape {@link ShapeSelection.Shape} to change to
     */
    public void reshapeSelected(ShapeSelection.Shape shape) {
        if (selectedNode != null)
            selectedNode.setShape(shape);
        repaint();
    }

    /**
     * Delete the selected element ({@link Node} or {@link Edge}).
     */
    public void deleteSelected() {
        if (selectedNode != null) {
            graph.deleteNode(selectedNode);
            deselectNode();
        }
        if (selectedEdge != null) {
            graph.deleteEdge(selectedEdge);
            deselectEdge();
        }
        repaint();
    }

    /**
     * Class to handle the mouse inputs to the {@code GraphDisplay}.
     */
    private class MouseHandler implements MouseListener, MouseMotionListener {
        private final PaintedGraph graph;
        private final GraphSettings settings;

        public MouseHandler(PaintedGraph graph, GraphSettings settings) {
            this.graph = graph;
            this.settings = settings;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (settings.getAction() == GraphSettings.Action.CREATE_NODE) {
                graph.addNode(new Node(e.getX(), e.getY(), 30, 30, settings.getColor(), settings.getShape()));
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (settings.getAction() == GraphSettings.Action.EDIT_NODE) {
                var node = graph.getNodeAt(e.getX(), e.getY());
                if (node != null) {
                    setSelectedNode(node);
                    settings.setColor(node.getColor());
                    settings.setShape(node.getShape());
                } else deselectNode();
            } else if (settings.getAction() == GraphSettings.Action.CREATE_EDGE) {
                var node = graph.getNodeAt(e.getX(), e.getY());
                if (node != null) {
                    graph.setHeldEdge(new PartialEdge(node, e.getX(), e.getY(), settings.getColor()));
                }
            } else if (settings.getAction() == GraphSettings.Action.EDIT_EDGE) {
                var edge = graph.getEdgeAt(e.getX(), e.getY());
                if (edge != null) {
                    setSelectedEdge(edge);
                    settings.setColor(edge.getColor());
                } else deselectEdge();
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (settings.getAction() == GraphSettings.Action.CREATE_EDGE) {
                if (!graph.finalizeHeldEdge()) {
                    graph.dropHeldEdge();
                }
            }
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (settings.getAction() == GraphSettings.Action.EDIT_NODE) {
                if (selectedNode != null) {
                    Dimension dim;
                    if (scrollPane != null) {
                        dim = scrollPane.getSize();
                        dim.width -= scrollPane.getVerticalScrollBar().getSize().width;
                        dim.height -= scrollPane.getHorizontalScrollBar().getSize().height;
                    }
                    else
                        dim = getSize();

                    int x = Math.min(Math.max(selectedNode.getWidth()/2, e.getX()), dim.width - selectedNode.getWidth()/2);
                    int y = Math.min(Math.max(selectedNode.getHeight(), e.getY()), dim.height) - selectedNode.getHeight()/2;
                    selectedNode.reposition(x, y);
                }
            } else if (settings.getAction() == GraphSettings.Action.CREATE_EDGE) {
                if (graph.hasHeldEdge()) {
                    Dimension dim;
                    if (scrollPane != null) {
                        dim = scrollPane.getSize();
                        dim.width -= scrollPane.getVerticalScrollBar().getSize().width;
                        dim.height -= scrollPane.getHorizontalScrollBar().getSize().height;
                    }
                    else
                        dim = getSize();

                    int x = Math.min(Math.max(0, e.getX()), dim.width);
                    int y = Math.min(Math.max(0, e.getY()), dim.height);
                    graph.moveHeldEdgeEnd(x, y);
                }
            }
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

}

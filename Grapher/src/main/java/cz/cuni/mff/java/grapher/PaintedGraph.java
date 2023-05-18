package cz.cuni.mff.java.grapher;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a graph to be drawn and edited.
 * Apart from the graph itself, this contains also all data necessary to draw, like positions and colors.
 * Can draw itself on a {@link java.awt.Graphics} instance.
 * Allows to access nodes and edges at specific positions.
 */
public class PaintedGraph {
    /**
     * Set of all the nodes in the graph.
     */
    protected Set<Node> nodes;
    /**
     * Set of all the edges in the graph.
     */
    protected Set<Edge> edges;
    /**
     * Currently held, incomplete edge. Only has a beginning node. Used for an edge which is mid-being added.
     */
    protected PartialEdge heldEdge;

    /**
     * Constructs and empty {@code PaintedGraph}.
     */
    public PaintedGraph() {
        nodes = new HashSet<>();
        edges = new HashSet<>();
        heldEdge = null;
    }

    /**
     * Loads the graph from a file.
     * The expected format is an XML document with the a {@code <root>} element
     * containing one {@code <nodes>} and one {@code <edges>} element,
     * which contain any number of {@code <node>} and {@code <edge>} elements respectively.
     * {@code <node>} specifies a node, containing {@code <x>}, {@code <y>}, {@code <width>}, {@code <height>} and {@code <color>} elements.
     * {@code <edge>} specifies an edge, containing {@code <start>}, {@code <end>} and {@code <color>}.
     * {@code <start>} and {@code <end>} are integers specifying the index of the starting resp. ending node of the edge by index in the nodes list
     * {@code <color>} is a hex number preceded by # (e.g. #ffffff).
     * @param inputFile  file with the graph definition
     * @return return code - 0 on success, 1 on failure
     */
    public int loadFromFile(File inputFile) {
        var builder = new SAXBuilder();
        try {

            var doc = builder.build(new FileInputStream(inputFile));
            var d = doc.getRootElement();

            var nodes = d.getChild("nodes");
            var nodeList = new ArrayList<Node>();
            for (var n : nodes.getDescendants(new ElementFilter("node"))) {
                int x = Integer.parseInt(n.getChildText("x"));
                int y = Integer.parseInt(n.getChildText("y"));
                int width = Integer.parseInt(n.getChildText("width"));
                int height = Integer.parseInt(n.getChildText("height"));
                int leftSide = x - width;
                int topSide = y - height;
                if (leftSide < 0 || topSide < 0) {
                    System.err.println("Nodes in negative values.");
                    System.err.println("left side: " + leftSide + ", top side: " + topSide);
                    return 2;
                }
                var node = new Node(x, y, width, height,
                                    Color.decode(n.getChildText("color")),
                                    ShapeSelection.Shape.valueOf(n.getChildText("shape"))
                                   );
                nodeList.add(node);
            }

            this.nodes = new HashSet<>();
            for (var n : nodeList)
                this.addNode(n);

            var edges = d.getChild("edges");
            this.edges = new HashSet<>();
            for (var e : edges.getDescendants(new ElementFilter("edge"))) {
                this.addEdge(new Edge(nodeList.get(Integer.parseInt(e.getChildText("start"))),
                                      nodeList.get(Integer.parseInt(e.getChildText("end"))),
                                      Color.decode(e.getChildText("color"))
                                     ));
            }

            return 0;

        } catch (Exception e) {
            this.edges = new HashSet<>();
            this.nodes = new HashSet<>();
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Saves the graph to a file.
     * The output format is an XML document with the a {@code <root>} element
     * containing one {@code <nodes>} and one {@code <edges>} element,
     * which contain any number of {@code <node>} and {@code <edge>} elements respectively.
     * {@code <node>} specifies a node, containing {@code <x>}, {@code <y>}, {@code <width>}, {@code <height>} and {@code <color>} elements.
     * {@code <edge>} specifies an edge, containing {@code <start>}, {@code <end>} and {@code <color>}.
     * {@code <start>} and {@code <end>} are integers specifying the index of the starting resp. ending node of the edge by index in the nodes list
     * {@code <color>} is a hex number preceded by # (e.g. #ffffff).
     * @param outputFile  file to which the graph will be saved
     * @return return code - 0 on success, 1 on failure
     */
    public int saveFile(File outputFile) {
        var root = new Element("root");
        var nodes = new Element("nodes");
        var edges = new Element("edges");
        root.addContent(nodes);
        root.addContent(edges);

        var nodeList = new ArrayList<>(this.nodes);
        for (var n : nodeList) {
            var nodeElement = new Element("node");
            nodeElement.addContent(new Element("x").addContent(String.valueOf(n.getX())));
            nodeElement.addContent(new Element("y").addContent(String.valueOf(n.getY())));
            nodeElement.addContent(new Element("width").addContent(String.valueOf(n.getWidth())));
            nodeElement.addContent(new Element("height").addContent(String.valueOf(n.getHeight())));
            nodeElement.addContent(new Element("color").addContent(String.format("#%06x", n.getColor().getRGB() & 0xffffff)));
            nodeElement.addContent(new Element("shape").addContent(n.getShape().toString()));
            nodes.addContent(nodeElement);
        }

        for (var e : this.edges) {
            var edgeElement = new Element("edge");
            edgeElement.addContent(new Element("start").addContent(String.valueOf(nodeList.indexOf(e.getStart()))));
            edgeElement.addContent(new Element("end").addContent(String.valueOf(nodeList.indexOf(e.getEnd()))));
            edgeElement.addContent(new Element("color").addContent(String.format("#%06x", e.getColor().getRGB() & 0xffffff)));
            edges.addContent(edgeElement);
        }

        var doc = new Document(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        try {
            xmlOutputter.output(doc, new FileWriter(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    /**
     * Draws the graph (i.e. invokes the {@code drawSelf} methods of all the containing edges, held edge if present and nodes).
     * @param g the {@code Graphics} object to draw on
     */
    public void drawSelf(Graphics g) {
        for (var e : edges) {
            e.drawSelf(g);
        }
        if (heldEdge != null) {
            heldEdge.drawSelf(g);
        }
        for (var n : nodes) {
            n.drawSelf(g);
        }
    }

    /**
     * Adds new node to the graph.
     * @param node node to be added
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Removes given node from graph. Deletes also all corresponding edges.
     * @param node node to remove
     */
    public void deleteNode(Node node) {
        for (var edge : node.getEdges()) {
            deleteEdge(edge);
        }
        nodes.remove(node);
    }

    /**
     * Adds new edge to the graph.
     * @param edge edge to be added
     */
    public void addEdge(Edge edge) {
        edges.removeIf(existingEdge -> edge.getStart().equals(existingEdge.getStart()) && edge.getEnd().equals(existingEdge.getEnd()));
        edges.removeIf(existingEdge -> edge.getStart().equals(existingEdge.getEnd()) && edge.getEnd().equals(existingEdge.getStart()));

        edges.add(edge);
        edge.getStart().addEdge(edge);
        edge.getEnd().addEdge(edge);
    }
    /**
     * Removes given edge from graph.
     * @param edge edge to remove
     */
    public void deleteEdge(Edge edge) {
        edge.getStart().deleteEdge(edge);
        edge.getEnd().deleteEdge(edge);
        edges.remove(edge);
    }

    /**
     * Returns the minimal size of a canvas the graph should be displayed on.
     * This is defined as the smallest size that can fit the whole graph and is greater than or equal to the given limit.
     * This method is useful for scroll behavior of a containing canvas.
     * @param limit the lower limit on the minimal size
     * @return minimal size
     */
    public Dimension getMinSize(Dimension limit) {
        int rightSide = 0;
        int bottomSide = 0;

        for (var n : nodes) {
            rightSide = Math.max(rightSide, n.getX() + n.getWidth());
            bottomSide = Math.max(bottomSide, n.getY() + n.getHeight());
        }

        return new Dimension(Math.max(limit.width, rightSide), Math.max(limit.height, bottomSide));
    }

    /**
     * Fetches the top-most node which intersect the given coordinates.
     * @param x x coordinate
     * @param y y coordinate
     * @return found node
     */
    public Node getNodeAt(int x, int y) {
        Node result = null;
        for (var node : nodes) {
            if (node.isInside(x, y))
                result = node;
        }
        return result;
    }
    /**
     * Fetches the top-most edge which intersect the given coordinates.
     * @param x x coordinate
     * @param y y coordinate
     * @return found edge
     */
    public Edge getEdgeAt(int x, int y) {
        Edge result = null;
        for (var edge : edges) {
            if (edge.isInside(x, y))
                result = edge;
        }
        return result;
    }

    // held edge operations
    /**
     * Set a new {@link PaintedGraph#heldEdge}.
     * @param heldEdge {@code PartialEdge} to add
     */
    public void setHeldEdge(PartialEdge heldEdge) {
        this.heldEdge = heldEdge;
    }
    /**
     * Move the {@link PaintedGraph#heldEdge} to given coordinates.
     * @param x x coordinate
     * @param y y coordinate
     */
    public void moveHeldEdgeEnd(int x, int y) {
        heldEdge.setEnd(x, y);
    }
    /**
     * Drop the {@link PaintedGraph#heldEdge}.
     */
    public void dropHeldEdge() {
        heldEdge = null;
    }

    /**
     * Convert the {@link PaintedGraph#heldEdge} to a full edge and add it (if possible).
     * The end node is selected based on the {@code PartialEdge} end coordinates.
     * @return {@code true} if successful, {@code false} if unsuccessful
     */
    public boolean finalizeHeldEdge() {
        if (heldEdge == null)
            return true;
        var node = getNodeAt(heldEdge.getEndX(), heldEdge.getEndY());
        if (node != null) {
            if (!node.equals(heldEdge.getStart()))
                addEdge(heldEdge.toFullEdge(node));
            heldEdge = null;
            return true;
        } else
            return false;
    }

    /**
     * Does the graph have an active {@link PaintedGraph#heldEdge}?
     * @return {@code true} if present, {@code false} if not
     */
    public boolean hasHeldEdge() {
        return heldEdge!=null;
    }
}

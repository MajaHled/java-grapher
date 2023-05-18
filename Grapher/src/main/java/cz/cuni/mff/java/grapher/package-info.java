/**
 * This package implements a simple graph editing GUI tool.
 * <br><br>
 * A full application is implemented in the {@link cz.cuni.mff.java.grapher.Grapher} class.
 * The building blocks of the package can also be extended and/or added to another GUI app.
 * <br><br>
 * The central class of the app is the {@link cz.cuni.mff.java.grapher.Grapher} class.
 * The {@link cz.cuni.mff.java.grapher.Grapher#main(java.lang.String[])} method is the entry point.
 * It creates a GUI frame with a control panel and a canvas.
 * <br><br>
 * The drawing itself as well as user input for graph editing is handled by the {@link cz.cuni.mff.java.grapher.GraphDisplay} class.
 * The graph is stored in a {@link cz.cuni.mff.java.grapher.PaintedGraph} instance, which contains sets of
 * {@link cz.cuni.mff.java.grapher.Node} and {@link cz.cuni.mff.java.grapher.Edge} instances that the graph is made up of
 * (and optionally a {@link cz.cuni.mff.java.grapher.PartialEdge} instance to represent an edge that is being created
 * and the end of which is dragged by the user). The graph can also be saved to and loaded from a file (*.grph extension is used).
 * <br><br>
 * The {@link cz.cuni.mff.java.grapher.Node} and {@link cz.cuni.mff.java.grapher.Edge} objects
 * hold data about the node, reps. edge's properties and can draw themselves.
 * <br><br>
 * The control panel includes to custom panels: {@link cz.cuni.mff.java.grapher.RecentColors} and {@link cz.cuni.mff.java.grapher.ShapeSelection}.
 * The flow of information between the control panel and the {@code GraphDisplay} is handeled by {@link cz.cuni.mff.java.grapher.GraphSettings}.
 *
 * @author Marie Hledíková
 * @version 1.0
 */

package cz.cuni.mff.java.grapher;
package cz.cuni.mff.java.grapher;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.*;

/**
 * Main widow of the Grapher application.
 * A simple GUI including a menu panel with controls and a {@link GraphDisplay} for graph drawing.
 */
public class Grapher {
    private static final JFrame frame = new JFrame("Grapher");;
    private static final GraphSettings settings = new GraphSettings(Color.BLUE, ShapeSelection.Shape.CIRCLE, GraphSettings.Action.CREATE_NODE);
    private static final GraphDisplay canvas = new GraphDisplay(settings);
    private static final JScrollPane canvasScroller = new JScrollPane(canvas);

    // File Dialogs
    private static final JFileChooser openGraphDialog = new JFileChooser();
    private static final JFileChooser saveGraphDialog = new JFileChooser();

    // Mode Switch Buttons
    private static final JPanel editRadioButtons = new JPanel();
    private static final ButtonGroup modeButtonGroup = new ButtonGroup();
    private static final JRadioButton createNodeButton = new JRadioButton("Create Node", true);
    private static final JRadioButton editNodeButton = new JRadioButton("Edit Node");
    private static final JRadioButton createEdgeButton = new JRadioButton("Create Edge");
    private static final JRadioButton editEdgeButton = new JRadioButton("Edit Edge");

    // Shape Selection
    private static final JPanel shapePanel = new JPanel();

    // Color Selection
    private static final JColorChooser colorChooser = new JColorChooser(Color.BLUE);
    private static final JPanel colorPanel = new JPanel();
    private static final JButton colorDialogButton = new JButton("Add Color");

    // Edit Actions
    private static final JPanel editActionButtons = new JPanel();
    private static final JButton deleteButton = new JButton("Delete");

    // File Actions
    private static final JPanel fileActionsButtons = new JPanel();
    private static final JButton openButton = new JButton("Open...");
    private static final JButton saveButton = new JButton("Save");
    private static final JButton saveAsButton = new JButton("Save as...");

    private static File saveFile = null;

    private static void createColorSelection() {
        var initCols = new ArrayList<Color>();
        initCols.add(Color.BLUE);
        initCols.add(Color.BLACK);
        var rc = new RecentColors(initCols, frame.getBackground(), settings);
        rc.setPreferredSize(new Dimension(90, 90));

        colorPanel.setLayout(new GridBagLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder("Color Selection"));

        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 0, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        colorPanel.add(rc, gbc);
        settings.setColorPane(rc);

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 1;
        colorPanel.add(colorDialogButton, gbc);
        colorDialogButton.addActionListener(e -> {
            var newColor = JColorChooser.showDialog(colorChooser, "Choose color", colorChooser.getColor());
            if (newColor != null)
                rc.addColor(newColor);
        });
    }

    private static void createShapeSelection() {
        var sSel = new ShapeSelection(settings);
        shapePanel.setBorder(BorderFactory.createTitledBorder("Shape Selection"));
        shapePanel.add(sSel);
        settings.setShapeSelection(sSel);
    }

    private static void createActionSelection() {
        editRadioButtons.setLayout(new BoxLayout(editRadioButtons, BoxLayout.Y_AXIS));

        modeButtonGroup.add(createNodeButton);
        editRadioButtons.add(createNodeButton);
        createNodeButton.addActionListener(e -> settings.setAction(GraphSettings.Action.CREATE_NODE));
        modeButtonGroup.add(createEdgeButton);
        editRadioButtons.add(createEdgeButton);
        createEdgeButton.addActionListener(e -> settings.setAction(GraphSettings.Action.CREATE_EDGE));
        modeButtonGroup.add(editNodeButton);
        editRadioButtons.add(editNodeButton);
        editNodeButton.addActionListener(e -> settings.setAction(GraphSettings.Action.EDIT_NODE));
        modeButtonGroup.add(editEdgeButton);
        editRadioButtons.add(editEdgeButton);
        editEdgeButton.addActionListener(e -> settings.setAction(GraphSettings.Action.EDIT_EDGE));

        editRadioButtons.setBorder(BorderFactory.createTitledBorder("Edit Options"));
    }

    private static void createEditActionButtons() {
        editActionButtons.setLayout(new GridBagLayout());


        deleteButton.setPreferredSize(new Dimension(90, deleteButton.getPreferredSize().height));
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        editActionButtons.add(deleteButton, gbc);
        deleteButton.addActionListener(e -> canvas.deleteSelected());

        editActionButtons.setBorder(BorderFactory.createTitledBorder("Edit Actions"));
    }

    private static void createFileButtons() {
        fileActionsButtons.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints();
        openButton.setPreferredSize(new Dimension(90, openButton.getPreferredSize().height));
        gbc.insets = new Insets(5, 5, 0, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        fileActionsButtons.add(openButton, gbc);
        openButton.addActionListener(e -> {
            if (openGraphDialog.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                var chosenFile = openGraphDialog.getSelectedFile();
                if (chosenFile != null) {
                    if (canvas.openGraph(chosenFile) == 0)
                        saveFile = chosenFile;
                }
            }
        });

        saveButton.setPreferredSize(new Dimension(90, saveButton.getPreferredSize().height));
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.gridx = 0;
        gbc.gridy = 1;
        fileActionsButtons.add(saveButton, gbc);
        saveButton.addActionListener(e -> {
            if (saveFile != null) {
                canvas.saveGraph(saveFile);
            } else if (saveGraphDialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                var chosenFile = saveGraphDialog.getSelectedFile();
                if (chosenFile != null) {
                    if(!chosenFile.getName().endsWith(".grph"))
                        chosenFile = new File(chosenFile.getPath() + ".grph");
                    saveFile = chosenFile;
                    canvas.saveGraph(saveFile);
                }
            }
        });

        saveAsButton.setPreferredSize(new Dimension(90, saveAsButton.getPreferredSize().height));
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 2;
        fileActionsButtons.add(saveAsButton, gbc);
        saveAsButton.addActionListener(e -> {
            if (saveGraphDialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                var chosenFile = saveGraphDialog.getSelectedFile();
                if (chosenFile != null) {
                    if(!chosenFile.getName().endsWith(".grph"))
                        chosenFile = new File(chosenFile.getPath() + ".grph");
                    saveFile = chosenFile;
                    canvas.saveGraph(saveFile);
                }
            }
        });

        fileActionsButtons.setBorder(BorderFactory.createTitledBorder("File Actions"));
    }

    private static void setupFileDialogs() {
        openGraphDialog.setDialogTitle("Choose Graph");
        openGraphDialog.setAcceptAllFileFilterUsed(false);
        openGraphDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openGraphDialog.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "Grapher Files (*.grph)";
            }
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".grph");
            }
        });

        saveGraphDialog.setDialogTitle("Choose Location");
        saveGraphDialog.setAcceptAllFileFilterUsed(false);
        saveGraphDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        saveGraphDialog.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "Grapher Files (*.grph)";
            }
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".grph");
            }
        });
    }


    /**
     * Creates all the GUI elements and puts them on a {@link JFrame}.
     */
    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setupFileDialogs();

        createColorSelection();
        createShapeSelection();
        createActionSelection();
        createEditActionButtons();
        createFileButtons();

        var leftMenuPanel = new JPanel();
        leftMenuPanel.add(colorPanel);
        leftMenuPanel.add(shapePanel);
        leftMenuPanel.add(editRadioButtons);
        leftMenuPanel.add(editActionButtons);
        leftMenuPanel.add(fileActionsButtons);
        leftMenuPanel.setPreferredSize(new Dimension(120,590));


        leftMenuPanel.setAutoscrolls(true);
        var leftMenuScroller = new JScrollPane(leftMenuPanel);
        leftMenuScroller.setPreferredSize(new Dimension(140, 590));
        leftMenuScroller.setBorder(BorderFactory.createEmptyBorder());
        frame.add(leftMenuScroller, BorderLayout.LINE_START);

        canvas.setPreferredSize(new Dimension(700, 650));
        canvas.setAutoscrolls(true);
        canvasScroller.setPreferredSize(new Dimension( 700,650));
        canvasScroller.setBorder(BorderFactory.createEmptyBorder());
        canvas.setScrollPane(canvasScroller);
        frame.add(canvasScroller, BorderLayout.CENTER);

        frame.pack();
        frame.setSize(870, 700);
        frame.setVisible(true);
    }

    /**
     * Application entry point. Launches the Grapher window application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Grapher::createAndShowGUI);
    }
}

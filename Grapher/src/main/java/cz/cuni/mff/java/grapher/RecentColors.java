package cz.cuni.mff.java.grapher;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom {@link JPanel} subclass implementing a color picker, where colors are ordered by most recently selected.
 * The max capacity is 9 and the color panels are arranged in a 3x3 grid.
 */
public class RecentColors extends JPanel {
    private final java.util.List<Color> colors;
    private final JButton[] colorPanels;
    private int chosenIndex;
    private final GraphSettings settings;
    private final Color disabledColor, borderColor;

    /**
     * Constructs a new {@link RecentColors} instance.
     * @param initialColors a list of colors to add to the picker initially (may be empty)
     * @param disabledColor the background color of unused color tiles
     * @param settings a {@link GraphSettings} instance to write the selected color into
     */
    public RecentColors(List<Color> initialColors, Color disabledColor, GraphSettings settings) {
        this.settings = settings;
        this.disabledColor = disabledColor;
        borderColor = Color.DARK_GRAY;
        if (initialColors.size() > 9) {
            initialColors.subList(9, initialColors.size()).clear();
        }
        colors = new ArrayList<>(initialColors);
        this.setLayout(new GridLayout(3, 3));
        colorPanels = new JButton[9];
        for (int i = 0; i < 9; i++) {
            colorPanels[i] = new JButton();
            colorPanels[i].setPreferredSize(new Dimension(30, 30));
            colorPanels[i].setBorder(new LineBorder(borderColor));
            int finalI = i;
            colorPanels[i].addActionListener(e -> setChosenIndex(finalI));
            this.add(colorPanels[i]);
        }
        refresh();
        setChosenIndex(0);
    }

    /**
     * Add new color (if not present yet) and select it.
     * @param color color to add
     */
    public void addColor(Color color) {
        if (colors.contains(color))
            colors.remove(color);
        else if (colors.size() > 8)
            colors.remove(8);
        colors.add(0, color);
        setChosenIndex(0);
        refresh();
    }

    /**
     * Resets the colors of the panels based on the current color list and repaints the component.
     */
    private void refresh() {
        for (int i = 0; i < colors.size(); i++) {
            colorPanels[i].setBackground(colors.get(i));
            colorPanels[i].setEnabled(true);
        }
        for (int i = colors.size(); i < 9; i++) {
            colorPanels[i].setBackground(disabledColor);
            colorPanels[i].setEnabled(false);
        }
        this.repaint();
    }

    /**
     * Select the {@code index}th color panel.
     * @param index index of panel to select
     */
    private void setChosenIndex(int index) {
        if (index >= colors.size())
            return;
        colorPanels[chosenIndex].setBorder(new LineBorder(borderColor));
        chosenIndex = index;
        settings.setColorNoPanelSelect(colors.get(chosenIndex));
        colorPanels[index].setBorder(new LineBorder(borderColor, 3));
        refresh();
    }

}

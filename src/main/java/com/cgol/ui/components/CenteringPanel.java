package com.cgol.ui.components;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

public class CenteringPanel extends JPanel implements Scrollable {

    private final JComponent content;

    public CenteringPanel(JComponent content) {
        super(new GridBagLayout());
        this.content = content;
        add(content);
        setOpaque(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return content.getPreferredSize();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        Container parent = getParent();
        return !(parent instanceof JViewport)
            || content.getPreferredSize().width <= parent.getWidth();
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        Container parent = getParent();
        return !(parent instanceof JViewport)
            || content.getPreferredSize().height <= parent.getHeight();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 24;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 96;
    }
}

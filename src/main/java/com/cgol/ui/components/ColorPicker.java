package com.cgol.ui.components;

import com.cgol.config.AppTheme;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ColorPicker extends JPanel {

    public interface Listener {
        void colorChanged(Color color);
    }

    private final String name;
    private final JPanel swatch;
    private final List<Listener> listeners = new ArrayList<>();
    private Color color;

    public ColorPicker(String name, Color initial) {
        this.name = name;
        this.color = initial;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        swatch = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape shape = AppTheme.roundedRectangle(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 4f);
                g2.setColor(color);
                g2.fill(shape);
                g2.setColor(AppTheme.BORDER);
                g2.draw(shape);
                g2.dispose();
            }
        };
        swatch.setOpaque(false);
        swatch.setPreferredSize(new Dimension(18, 18));
        swatch.setMaximumSize(new Dimension(18, 18));
        swatch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        swatch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openChooser();
            }
        });

        JLabel label = new JLabel(name);
        label.setFont(AppTheme.FONT);
        label.setForeground(AppTheme.TEXT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        add(swatch);
        add(label);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color == null || color.equals(this.color)) {
            return;
        }
        this.color = color;
        swatch.repaint();
        for (Listener listener : listeners) {
            listener.colorChanged(color);
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    private void openChooser() {
        Color selected = JColorChooser.showDialog(this, "Choose " + name, color);
        if (selected != null) {
            setColor(selected);
        }
    }
}

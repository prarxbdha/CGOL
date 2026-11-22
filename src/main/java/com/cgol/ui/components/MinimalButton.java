package com.cgol.ui.components;

import com.cgol.config.AppTheme;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;

public class MinimalButton extends JButton {

    public enum Style {
        PRIMARY,
        NEUTRAL
    }

    public MinimalButton(String text, Style style) {
        super(text);
        setFont(AppTheme.FONT_BOLD);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
        setUI(new MinimalButtonUI(style));
    }

    private static class MinimalButtonUI extends BasicButtonUI {

        private final Style style;

        MinimalButtonUI(Style style) {
            this.style = style;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean enabled = button.isEnabled();
            boolean pressed = button.getModel().isPressed();
            boolean rollover = button.getModel().isRollover();

            Color background;
            Color outline = null;
            Color textColor;

            if (!enabled) {
                background = AppTheme.APP_BACKGROUND;
                outline = AppTheme.BORDER;
                textColor = AppTheme.MUTED_TEXT;
            } else if (style == Style.PRIMARY) {
                background = pressed ? AppTheme.TEXT.darker()
                    : rollover ? AppTheme.TEXT.brighter() : AppTheme.TEXT;
                textColor = AppTheme.CANVAS_BACKGROUND;
            } else {
                background = pressed ? AppTheme.DEAD_CELL
                    : rollover ? AppTheme.APP_BACKGROUND : AppTheme.CANVAS_BACKGROUND;
                outline = AppTheme.BORDER;
                textColor = AppTheme.TEXT;
            }

            Shape shape = AppTheme.roundedRectangle(0.5f, 0.5f, c.getWidth() - 1f, c.getHeight() - 1f, 8f);
            g2.setColor(background);
            g2.fill(shape);
            if (outline != null) {
                g2.setColor(outline);
                g2.draw(shape);
            }
            if (button.isFocusOwner()) {
                g2.setColor(AppTheme.ACCENT);
                g2.draw(shape);
            }

            String text = button.getText();
            if (text != null && !text.isEmpty()) {
                FontMetrics fm = g2.getFontMetrics(button.getFont());
                Insets insets = button.getInsets();
                int available = c.getWidth() - insets.left - insets.right;
                int x = insets.left + (available - fm.stringWidth(text)) / 2;
                int contentHeight = c.getHeight() - insets.top - insets.bottom;
                int y = insets.top + (contentHeight - fm.getHeight()) / 2 + fm.getAscent();
                g2.setColor(textColor);
                g2.drawString(text, x, y);
            }

            g2.dispose();
        }
    }
}

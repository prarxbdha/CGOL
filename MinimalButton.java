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
        setFont(Theme.FONT_BOLD);
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
                background = Theme.APP_BACKGROUND;
                outline = Theme.BORDER;
                textColor = Theme.MUTED_TEXT;
            } else if (style == Style.PRIMARY) {
                background = pressed ? Theme.TEXT.darker()
                    : rollover ? Theme.TEXT.brighter() : Theme.TEXT;
                textColor = Theme.CANVAS_BACKGROUND;
            } else {
                background = pressed ? Theme.DEAD_CELL
                    : rollover ? Theme.APP_BACKGROUND : Theme.CANVAS_BACKGROUND;
                outline = Theme.BORDER;
                textColor = Theme.TEXT;
            }

            Shape shape = Theme.roundedRectangle(0.5f, 0.5f, c.getWidth() - 1f, c.getHeight() - 1f, 8f);
            g2.setColor(background);
            g2.fill(shape);
            if (outline != null) {
                g2.setColor(outline);
                g2.draw(shape);
            }
            if (button.isFocusOwner()) {
                g2.setColor(Theme.ACCENT);
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

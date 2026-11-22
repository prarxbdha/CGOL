import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

public class MinimalScrollBarUI extends BasicScrollBarUI {

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return hiddenButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return hiddenButton();
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(28, 28);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(c.getBackground());
        g2.fill(bounds);
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle bounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape thumb = Theme.roundedRectangle(bounds.x + 3f, bounds.y + 3f,
            bounds.width - 6f, bounds.height - 6f, 5f);
        g2.setColor(isDragging ? Theme.MUTED_TEXT : Theme.BORDER);
        g2.fill(thumb);
        g2.dispose();
    }

    private static JButton hiddenButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}

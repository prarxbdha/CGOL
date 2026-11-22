import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.text.NumberFormatter;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ParameterControl extends JPanel {

    public interface Listener {
        void valueChanged(int value);
    }

    private final int min;
    private final int max;
    private final JSlider slider;
    private final JFormattedTextField input;
    private final List<Listener> listeners = new ArrayList<>();
    private int value;
    private boolean updating;

    public ParameterControl(String name, int initial, int min, int max) {
        this.min = min;
        this.max = max;
        this.value = initial;

        setLayout(new GridBagLayout());
        setOpaque(false);

        JLabel label = new JLabel(name);
        label.setFont(Theme.FONT);
        label.setForeground(Theme.TEXT);

        NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
        formatter.setAllowsInvalid(true);
        input = new JFormattedTextField(formatter);
        input.setValue(initial);
        input.setFont(Theme.FONT);
        input.setForeground(Theme.TEXT);
        input.setBackground(Theme.CANVAS_BACKGROUND);
        input.setHorizontalAlignment(JFormattedTextField.CENTER);
        input.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(3, 4, 3, 4)));
        input.setPreferredSize(new Dimension(58, 26));
        input.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

        slider = new JSlider(min, max, initial);
        slider.setUI(new MinimalSliderUI(slider));
        slider.setOpaque(false);
        slider.setFocusable(false);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setPreferredSize(new Dimension(10, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 7, 8);
        add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 7, 0);
        add(input, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(slider, gbc);

        slider.addChangeListener((ChangeEvent e) -> {
            if (!updating) {
                setValue(slider.getValue());
            }
        });
        input.addActionListener(e -> commitInput());
        input.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                commitInput();
            }
        });
    }

    public int getValue() {
        return value;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void setValue(int newValue) {
        int clamped = Math.max(min, Math.min(max, newValue));

        if (slider.getValue() != clamped) {
            updating = true;
            slider.setValue(clamped);
            updating = false;
        }
        if (!Integer.valueOf(clamped).equals(input.getValue())) {
            updating = true;
            input.setValue(clamped);
            updating = false;
        }
        if (clamped == value) {
            return;
        }
        value = clamped;
        for (Listener listener : listeners) {
            listener.valueChanged(clamped);
        }
    }

    private void commitInput() {
        if (updating) {
            return;
        }
        try {
            input.commitEdit();
        } catch (ParseException ignored) {
            input.setValue(value);
        }

        Object edited = input.getValue();
        int parsed;
        if (edited instanceof Number) {
            parsed = ((Number) edited).intValue();
        } else {
            try {
                parsed = Integer.parseInt(input.getText().trim());
            } catch (NumberFormatException ignored) {
                parsed = value;
            }
        }
        setValue(parsed);
    }

    private class MinimalSliderUI extends BasicSliderUI {

        MinimalSliderUI(JSlider component) {
            super(component);
        }

        @Override
        protected Dimension getThumbSize() {
            return new Dimension(14, 14);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle track = trackRect;
            int thickness = 4;
            int y = track.y + (track.height - thickness) / 2;
            Shape base = Theme.roundedRectangle(track.x, y, track.width, thickness, thickness / 2f);
            g2.setColor(Theme.BORDER);
            g2.fill(base);

            double progress = (slider.getValue() - min) / (double) (max - min);
            int filled = (int) Math.round(track.width * progress);
            if (filled > 0) {
                Shape active = Theme.roundedRectangle(track.x, y, filled, thickness, thickness / 2f);
                g2.setColor(Theme.ACCENT);
                g2.fill(active);
            }
            g2.dispose();
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle bounds = thumbRect;
            float diameter = Math.min(bounds.width, bounds.height);
            float x = bounds.x + (bounds.width - diameter) / 2f;
            float y = bounds.y + (bounds.height - diameter) / 2f;
            Ellipse2D thumb = new Ellipse2D.Float(x, y, diameter, diameter);

            g2.setColor(Theme.CANVAS_BACKGROUND);
            g2.fill(thumb);
            g2.setColor(Theme.ACCENT);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(thumb);
            g2.dispose();
        }

        @Override
        public void paintFocus(Graphics g) {
        }
    }
}

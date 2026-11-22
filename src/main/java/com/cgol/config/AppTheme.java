package com.cgol.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public final class AppTheme {

    public static final Color APP_BACKGROUND = color("#F7F7F5");
    public static final Color CANVAS_BACKGROUND = color("#FFFFFF");
    public static final Color TEXT = color("#252B25");
    public static final Color MUTED_TEXT = color("#7A817A");
    public static final Color ACCENT = color("#4D967D");
    public static final Color DEAD_CELL = color("#EDEFEA");
    public static final Color BORDER = color("#E2E5DF");

    public static final Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font FONT_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    public static final Font FONT_SMALL = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    public static final Font FONT_TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 13);

    private AppTheme() {
    }

    public static Color color(String hex) {
        return Color.decode(hex);
    }

    public static Shape roundedRectangle(float x, float y, float width, float height, float radius) {
        return new RoundRectangle2D.Float(x, y, width, height, radius, radius);
    }

    public static Shape squircle(float x, float y, float size) {
        float r = size / 2f;
        float k = r * 0.9f;

        Path2D path = new Path2D.Float();
        path.moveTo(x + r, y);
        path.curveTo(x + r + k, y, x + size, y + r - k, x + size, y + r);
        path.curveTo(x + size, y + r + k, x + r + k, y + size, x + r, y + size);
        path.curveTo(x + r - k, y + size, x, y + r + k, x, y + r);
        path.curveTo(x, y + r - k, x + r - k, y, x + r, y);
        path.closePath();
        return path;
    }
}

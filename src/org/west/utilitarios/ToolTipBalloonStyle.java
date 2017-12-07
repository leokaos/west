package org.west.utilitarios;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import net.java.balloontip.styles.BalloonTipStyle;

public class ToolTipBalloonStyle extends BalloonTipStyle{

    private final Color borderColor;
    private final Color fillColor;

    public ToolTipBalloonStyle(Color fillColor, Color borderColor) {
        this.borderColor = borderColor;
        this.fillColor = fillColor;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        if (this.flipY) {
            return new Insets(this.verticalOffset + 1, 1, 1, 1);
        }
        return new Insets(1, 1, this.verticalOffset + 1, 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        width--;
        height--;
        int yBottom;
        int yTop;

        if (this.flipY) {
            yTop = y + this.verticalOffset;
            yBottom = y + height;
        } else {
            yTop = y;
            yBottom = y + height - this.verticalOffset;
        }

        g2d.setPaint(this.fillColor);
        g2d.fillRect(x, yTop, width, yBottom);
        g2d.setPaint(this.borderColor);
        g2d.drawRect(x, yTop, width, yBottom);
    }
}
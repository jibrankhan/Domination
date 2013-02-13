package net.yura.swingme.core;

import java.util.Calendar;
import net.yura.mobile.gui.Font;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.plaf.Style;

public class AnalogClock extends Component {

    private double diam = 0.38;
    private double LineLengthSeconds = 0.90;
    private double LineLengthMinutes = 0.75;
    private double LineLengthHour = 0.50;
    private double LineLengthTicks = 0.08;
    private double TextPositionRelativeR = 1.22;

    Font Font;
    Calendar _now;
    boolean drawNumbers;
    boolean drawSecondhand;
    boolean drawTicks;

    public AnalogClock() {
    }

    private int pointX(double minute, double radius, int _circleCenterX) {
        double angle = minute * Math.PI / 30.0;
        return (int) ((double) _circleCenterX + radius * Math.sin(angle));
    }

    private int pointY(double minute, double radius, int oy) {
        double angle = minute * Math.PI / 30.0;
        return (int) ((double) oy - radius * Math.cos(angle));
    }

    public void paintComponent(Graphics2D g) {

        int Width = getWidth();
        int Height = getHeight();

        int size = Math.min(Width, Height);
        int _raduis = (int) (diam * (double) size);
        int _circleCenterX = size / 2;
        int _circleCenterY = size / 2;

        
        g.setColor( getForeground() );

        // draw circle
        g.drawArc(_circleCenterX - _raduis, _circleCenterY - _raduis, _raduis * 2, _raduis * 2, 0, 360);

        // set text's font
        g.setFont(Font);
        int textH = Font.getHeight();

        // draw ticks & digits
        int textW;
        for (int hour = 1; hour <= 12; hour++) {
            double angle = hour * 60.0 / 12.0;
            if (drawTicks) {
                g.drawLine(
                        pointX(angle, _raduis * (1 - LineLengthTicks), _circleCenterX),
                        pointY(angle, _raduis * (1 - LineLengthTicks), _circleCenterY),
                        pointX(angle, _raduis, _circleCenterX),
                        pointY(angle, _raduis, _circleCenterY));
            }
            if (drawNumbers) {
                // texts
                textW = Font.getWidth("" + hour);
                g.drawString("" + hour,
                        (int) pointX(angle, _raduis * TextPositionRelativeR, _circleCenterX) - textW / 2,
                        (int) pointY(angle, _raduis * TextPositionRelativeR, _circleCenterY) - textH / 2
                        );
            }
        }
        
        double hour = _now.get(Calendar.HOUR) * 60.0 / 12.0;
        double minute = _now.get(Calendar.MINUTE);
        double second = _now.get(Calendar.SECOND);

        // draw hour line
        g.drawLine(_circleCenterX, _circleCenterY,
                pointX(hour + (double) minute / 12.0, _raduis * LineLengthHour, _circleCenterX),
                pointY(hour + (double) minute / 12.0, _raduis * LineLengthHour, _circleCenterY));

        // draw minutes line
        g.drawLine(_circleCenterX, _circleCenterY,
                pointX(minute + second / 60.0, _raduis * LineLengthMinutes, _circleCenterX),
                pointY(minute + second / 60.0, _raduis * LineLengthMinutes, _circleCenterY));

        if (drawSecondhand) {
            // draw seconds line
            g.drawLine(_circleCenterX, _circleCenterY,
                    pointX((double) second, _raduis * LineLengthSeconds, _circleCenterX),
                    pointY((double) second, _raduis * LineLengthSeconds, _circleCenterY));
        }
    }

    public void updateUI() {
        super.updateUI();
        Font = theme.getFont(Style.ALL);
    }

    protected String getDefaultName() {
        return "AnalogClock";
    }

    protected void workoutMinimumSize() {
        width=10;
        height=10;
    }

    public void setTime(Calendar time) {
        _now = time;
    }

}

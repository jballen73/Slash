package projects.jballen.slash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import static projects.jballen.slash.Constants.BASE_ARROW_HEIGHT;
import static projects.jballen.slash.Constants.BASE_ARROW_WIDTH;
import static projects.jballen.slash.Constants.TOP_ARROW_EXTENSION;
import static projects.jballen.slash.Constants.TOP_ARROW_HEIGHT;

/**
 * TODO: document your custom view class.
 */
public class GameArrow extends View {
    private int color = Color.BLUE;
    private int directionValue = 8;
    private FlingType direction;
    private Paint arrowPaint;
    public GameArrow(Context context) {
        super(context);
        init(null, 0);
    }

    public GameArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameArrow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameArrow, defStyle, 0);

        color = a.getColor(
                R.styleable.GameArrow_color,
                color);
        directionValue = a.getInt(R.styleable.GameArrow_arrowDirection, directionValue);
        direction = FlingType.getTypeFromIndex(directionValue);


        a.recycle();
        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setColor(color);
        arrowPaint.setStyle(Paint.Style.FILL);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom - (int)BASE_ARROW_HEIGHT - (int)TOP_ARROW_HEIGHT;
        canvas.save();
        canvas.rotate(direction.getRotateAngle(), contentWidth/2, (contentHeight + BASE_ARROW_HEIGHT + TOP_ARROW_HEIGHT)/2);
        // Draw the arrow.
        arrowPaint.setColor(color);
        Path arrowPath = new Path();
        arrowPath.moveTo((contentWidth)/2, (contentHeight)/2);
        arrowPath.rLineTo(BASE_ARROW_WIDTH/2, BASE_ARROW_HEIGHT);
        arrowPath.rLineTo(TOP_ARROW_EXTENSION, 0);
        arrowPath.rLineTo(-(BASE_ARROW_WIDTH/2 + TOP_ARROW_EXTENSION), TOP_ARROW_HEIGHT);
        arrowPath.rLineTo(-(BASE_ARROW_WIDTH/2 + TOP_ARROW_EXTENSION), -TOP_ARROW_HEIGHT);
        arrowPath.rLineTo(TOP_ARROW_EXTENSION, 0);
        arrowPath.rLineTo(BASE_ARROW_WIDTH/2, -BASE_ARROW_HEIGHT);
        canvas.drawPath(arrowPath, arrowPaint);
        canvas.restore();


    }
    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return color;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param color The example color attribute value to use.
     */
    public void setColor(int color) {
        this.color = color;
        invalidateTextPaintAndMeasurements();
    }
    public void setDirection(int direction) {
        this.directionValue = direction;
        this.direction = FlingType.getTypeFromIndex(directionValue);
        invalidateTextPaintAndMeasurements();

    }
    public FlingType getDirection() {
        return direction;
    }





}

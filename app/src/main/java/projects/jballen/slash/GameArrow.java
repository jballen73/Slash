package projects.jballen.slash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import static projects.jballen.slash.Constants.BASE_ARROW_HEIGHT_PERCENT;
import static projects.jballen.slash.Constants.BASE_ARROW_WIDTH_PERCENT;

public class GameArrow extends View {
    private int color = Color.BLUE;
    private int borderColor = color;
    private int directionValue = 8;
    private int alpha = 255;
    private FlingType direction;
    private Paint arrowPaint;
    Path arrowPath;
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

        arrowPath =  new Path();
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = (getWidth() - paddingLeft - paddingRight);
        int contentHeight = (getHeight() - paddingTop - paddingBottom);
        canvas.save();
        canvas.rotate(direction.getRotateAngle(), contentWidth/2, (contentHeight)/2);
        // Draw the arrow.
        arrowPaint.setColor(color);
        arrowPaint.setAlpha(alpha);
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPath.moveTo(contentWidth / 2, 0);
        arrowPath.rLineTo(-(contentWidth * BASE_ARROW_WIDTH_PERCENT)/2, contentHeight * BASE_ARROW_HEIGHT_PERCENT);
        arrowPath.rLineTo(-(contentWidth * (BASE_ARROW_WIDTH_PERCENT)/(2.5f)), 0);
        arrowPath.rLineTo((contentWidth * (BASE_ARROW_WIDTH_PERCENT)/(2.5f)) + (contentWidth * BASE_ARROW_WIDTH_PERCENT)/2,
                contentHeight * (1-BASE_ARROW_HEIGHT_PERCENT));
        arrowPath.rLineTo((contentWidth * (BASE_ARROW_WIDTH_PERCENT)/(2.5f)) + (contentWidth * BASE_ARROW_WIDTH_PERCENT)/2,
                -contentHeight * (1-BASE_ARROW_HEIGHT_PERCENT));
        arrowPath.rLineTo(-(contentWidth * (BASE_ARROW_WIDTH_PERCENT)/(2.5f)), 0);
        arrowPath.rLineTo(-(contentWidth * BASE_ARROW_WIDTH_PERCENT)/2, -contentHeight * BASE_ARROW_HEIGHT_PERCENT);
        canvas.drawPath(arrowPath, arrowPaint);
        arrowPaint.setColor(borderColor);
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(10);
        arrowPaint.setAlpha(alpha/2);
        canvas.drawPath(arrowPath, arrowPaint);
        canvas.restore();


    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size;
        int width = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        size = (width > height) ? height : width;
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingBottom() + getPaddingTop());
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec), measureDimension(desiredHeight, heightMeasureSpec));
    }
    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize((measureSpec));
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    */
    /**
     * Sets the arrow's color attribute value. In the example view, this color
     * is the font color.
     *
     * @param color The example color attribute value to use.
     */
    public void setColor(int color) {
        this.color = color;
        this.borderColor = color;
        invalidateTextPaintAndMeasurements();
    }
    public void setDirection(int direction) {
        this.directionValue = direction;
        this.direction = FlingType.getTypeFromIndex(directionValue);
        invalidateTextPaintAndMeasurements();

    }
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidateTextPaintAndMeasurements();
    }
    public void setBorderColor(int newBorderColor) {
        this.borderColor = newBorderColor;
    }






}

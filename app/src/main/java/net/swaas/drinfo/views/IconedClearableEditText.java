package net.swaas.drinfo.views;

/**
 * Created by SwaaS on 7/3/2015.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import net.swaas.drinfo.R;
import net.swaas.drinfo.utils.Strings;
import net.swaas.drinfo.utils.TextWatcherAdapter;

/**
 * To change clear icon, set
 *
 * <pre>
 * android:drawableRight="@drawable/custom_icon"
 * </pre>
 */
public class IconedClearableEditText extends EditText implements OnTouchListener, OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

    public interface Listener {
        void didClearText();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private Drawable mPrefixDrawable = null;
    private Listener listener;

    public IconedClearableEditText(Context context) {
        super(context);
        init();
    }

    public IconedClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconedClearableEditText, 0, 0);
        Drawable base = a.getDrawable(R.styleable.IconedClearableEditText_prefixDrawable);
        if (base != null) {
            mPrefixDrawable = base;
        }
        init();
    }

    public IconedClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconedClearableEditText, defStyle, 0);
        Drawable base = a.getDrawable(R.styleable.IconedClearableEditText_prefixDrawable);
        if (base != null) {
            mPrefixDrawable = base;
        }
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD.getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(Strings.isNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            setClearIconVisible(Strings.isNotEmpty(text));
        }
    }

    public void setPrefixDrawable(Drawable drawable) {
        mPrefixDrawable = drawable;
        setCompoundDrawablesWithIntrinsicBounds(mPrefixDrawable, null, null, null);
    }

    private void init() {
        if (mPrefixDrawable != null) {
            getCompoundDrawables()[0] = mPrefixDrawable;
            mPrefixDrawable.setBounds(0, 0, mPrefixDrawable.getIntrinsicWidth(), mPrefixDrawable.getIntrinsicHeight());
        }
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black_18dp);
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    protected void setClearIconVisible(boolean visible) {
        boolean wasVisible = (getCompoundDrawables()[2] != null);
        if (visible != wasVisible) {
            Drawable x = visible ? xD : null;
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }
}
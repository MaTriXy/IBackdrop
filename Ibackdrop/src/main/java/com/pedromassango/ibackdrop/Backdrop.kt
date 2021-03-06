package com.pedromassango.ibackdrop

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat

class Backdrop(context: Context, attributeSet: AttributeSet): FrameLayout( context, attributeSet) {

    private lateinit var toolbar: Toolbar
    private var openIcon: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_drop_open, null)
    private var closeIcon: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_drop_close, null)

    init {

        val customProperties = context.obtainStyledAttributes(attributeSet, R.styleable.Backdrop)

        val moIcon: Drawable? = customProperties.getDrawable(R.styleable.Backdrop_openIcon)
        val mcIcon: Drawable? = customProperties.getDrawable(R.styleable.Backdrop_closeIcon)
        moIcon?.let { openIcon = moIcon }
        mcIcon?.let { closeIcon = mcIcon }

        customProperties.recycle()
    }

    /**
     * Set the toolbar to use with this backdrop.
     */
    fun withToolbar(toolbar: Toolbar): Backdrop {
        this.toolbar = toolbar
        // update open icon
        this.toolbar.navigationIcon = openIcon
        return this
    }

    fun build() {

        // click listener to open/close the sheet
        val navIconClickListener = NavigationIconClickListener(context,
                backView = getBackView(),
                sheet =  getFrontView(),
                interpolator = LinearInterpolator(),
                openIcon = openIcon,
                closeIcon = closeIcon
        )

        // throw if toolbar not set
        checkNotNull(toolbar) {
            IllegalStateException("Toolbar must not be null")
        }

        // on toolbar navigation click, handle it
        toolbar.setNavigationOnClickListener(navIconClickListener)
    }

    /**
     * Here whe check if there is more than two child views.
     * If true, we throw an exception in runtime.
     * @throws IllegalArgumentException if there is more than two child views.
     *
     * And change the front view background color.
     */
    override fun onFinishInflate() {
        super.onFinishInflate()

        // if there is more than two views, crash the execution
        if(childCount > 2){
            throw IllegalArgumentException(" ${this.javaClass.simpleName} Must contain only two child!")
        }

        getFrontView().background = ResourcesCompat.getDrawable(resources, R.drawable.backdrop_background, null)
    }

    /**
     * Function to return the back view.
     * @return the first view in this layout.
     */
    private fun getBackView(): View = getChildAt(0)

    /**
     * Function to return the backdrop view.
     * @return the second view in this layout.
     */
    private fun getFrontView(): View = getChildAt(1)

}
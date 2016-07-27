package com.panching.train.obj;

import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by panching on 16/4/21.
 */
public class ImageItemObj
{
    public TextView imageView;

    public ImageItemObj() {
        super();
    }
    public ImageItemObj(TextView imageView) {
        this.imageView = imageView;
    }

    public TextView getImageView() {

        return imageView;
    }

    public void setImageView(TextView imageView) {
        this.imageView = imageView;
    }
}

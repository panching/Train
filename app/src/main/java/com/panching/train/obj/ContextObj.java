package com.panching.train.obj;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by panching on 16/5/4.
 */
public class ContextObj implements Serializable{
   private Context context;

    public ContextObj() {
    }

    public ContextObj(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

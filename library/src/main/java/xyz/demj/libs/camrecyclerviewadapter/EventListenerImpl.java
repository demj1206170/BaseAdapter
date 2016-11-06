package xyz.demj.libs.camrecyclerviewadapter;

import android.support.annotation.Keep;
import android.view.View;

/**
 * Created by demj on 2016/10/15.
 */
@Keep
public class EventListenerImpl implements EventListener {
    @Override
    public void listenSetClickable(boolean clickable) {

    }

    @Override
    public void listenSetClickableInCAM(boolean clickableInCAM) {

    }

    @Override
    public void listenSetCanTriggerCAM(boolean canTriggerCAM) {

    }

    @Override
    public void listenSetLongClickable(boolean longClickable) {

    }

    @Override
    public void listenAddHandleClickView(View view) {

    }

    @Override
    public boolean listenClick(View view) {
        return false;
    }

    @Override
    public boolean listenLongClick(View view) {
        return false;
    }
}

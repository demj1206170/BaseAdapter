package xyz.demj.libs.camrecyclerviewadapter;

import android.support.annotation.Keep;
import android.view.View;

/**
 * Created by demj on 2016/10/15.
 */
@Keep
public interface EventListener {

    /**
     * let listener know the adapter  clickable state is changed.
     *
     * @param clickable
     */
    void listenSetClickable(boolean clickable);

    /**
     * let listener know the adapter clickable in CAM state is changed.
     *
     * @param clickableInCAM
     */
    void listenSetClickableInCAM(boolean clickableInCAM);

    /**
     * let listener know the adapter can trigger CAM state is changed.
     *
     * @param canTriggerCAM
     */
    void listenSetCanTriggerCAM(boolean canTriggerCAM);

    /**
     * let listener know the adapter long clickable state is changed.
     *
     * @param longClickable
     */
    void listenSetLongClickable(boolean longClickable);

    /**
     * let listener know what view will handle click alone.
     *
     * @param view the view will handle click event alone.
     */
    void listenAddHandleClickView(View view);

    /**
     * let listener handle click before adapter handle click event.
     *
     * @param view the clicked view.
     * @return true has handle click event,adapter will not handle again.False adapter will handle the event.
     */
    boolean listenClick(View view);

    /**
     * let listener handle long click before adapter handle click event.
     *
     * @param view the long clicked view.
     * @return true has handle long click event,adapter will not handle again.False adapter will handle the event.
     */
    boolean listenLongClick(View view);

}

package xyz.demj.libs.camrecyclerviewadapter;

import android.support.annotation.Keep;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by demj on 2016/10/15.
 */
@Keep
public abstract class BaseRecyclerViewHolder<E> extends RecyclerView.ViewHolder {
    BaseRecyclerViewAdapter<E> mAdapter;
    private EventListener mEventListener = sEventListener;
    private boolean isClickable = true;
    private boolean isLongClickable = true;

    public void setCanTriggerCAM(boolean pCanTriggerCAM) {
        isCanTriggerCAM = pCanTriggerCAM;
    }

    public void setLongClickable(boolean pLongClickable) {
        isLongClickable = pLongClickable;
    }

    public void setClickable(boolean pClickable) {
        isClickable = pClickable;
    }

    public void setEventListener(EventListener pEventListener) {
        if (pEventListener != null)
            mEventListener = pEventListener;
        else mEventListener = sEventListener;
    }

    private boolean isCanTriggerCAM = true;

    private static EventListener sEventListener = new EventListenerImpl();


    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(mOnClickListener);
        itemView.setOnLongClickListener(mOnLongClickListener);
    }

    protected abstract void bindViewHolder(E element, BaseRecyclerViewAdapter<E> pAdapter, int position);

    @Keep
    public interface ViewHolderFactory<E> {
        BaseRecyclerViewHolder<E> createViewHolder(BaseRecyclerViewAdapter<E> pAdapter, ViewGroup parent, int viewType);
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isClickable)
                return;
            mEventListener.listenClick(v);
            //  return;
            int position = getAdapterPosition();
            internalPerformClick(v, position);
        }
    };

    private void internalPerformClick(View pV, int pPosition) {
        if (pPosition < 0 || pPosition > mAdapter.getItemCount()) {
            return;
        }
        if (mAdapter.canHandleClick(pV)) {
            if (mAdapter.mOnHandleClickListener != null) {
                mAdapter.mOnHandleClickListener.handleClick(pV, pPosition);
            }
            return;
        }
        if (mAdapter.isInCAM) {
            mAdapter.toggleSelection(pPosition);
        } else {
            if (mAdapter.mOnItemClickListener != null) {
                mAdapter.mOnItemClickListener.onClick(pV, pPosition);
            }
        }

    }

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (!isClickable || !isLongClickable || !isCanTriggerCAM)
                return false;

            mEventListener.listenLongClick(v);
            int position = getAdapterPosition();
            internalPerformLongClick(v, position);
            return true;
        }
    };

    private void internalPerformLongClick(View pV, int pPosition) {
        if (pPosition < 0 || pPosition > mAdapter.getItemCount()) {
            return;
        }
        if (!mAdapter.isInCAM) {
            if (mAdapter.mOnItemLongClickListener != null)
                mAdapter.mOnItemLongClickListener.onLongClick(pV, pPosition);
        }

    }
}

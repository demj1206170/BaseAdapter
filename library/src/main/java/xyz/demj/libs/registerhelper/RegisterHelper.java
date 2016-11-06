package xyz.demj.libs.registerhelper;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by demj on 2016/10/19.
 */
@Keep
public class RegisterHelper<T, D> {


    private static final RH sRH = new RH();
    private final RegisterCallback<T, D> mRegisterCallback;
    private final List<T> mTList = new ArrayList<>();

    public RegisterHelper(RegisterCallback<T, D> pRegisterCallback) {
        if (pRegisterCallback == null)
            throw new IllegalStateException("RegisterCallback cannot be null");
        mRegisterCallback = pRegisterCallback;
    }

    public void reigster(T pT) {
        if (pT == null)
            throw new IllegalStateException("the register stuff cannot be null");
        synchronized (mTList) {
            if (mTList.contains(pT)) {
                throw new IllegalStateException("this stuff already registered");
            }
            mTList.add(pT);
        }
    }

    public void unregister(T pT) {
        if (pT == null)
            throw new IllegalStateException("the unregister stuff cannot be null");
        synchronized (mTList) {
            if (!mTList.contains(pT)) {
                throw new IllegalStateException("this stuff hasn't registered yet");
            }
            mTList.remove(pT);
        }
    }

    public void notifyRegister(D pD) {
        if (mRegisterCallback.isCallbackInMainThread()) {
            Message msg = Message.obtain();
            Wrapper wrapper = new Wrapper();
            wrapper.mRegisterHelper = this;
            wrapper.mD = pD;
            msg.obj = wrapper;
            sRH.sendMessage(msg);
        } else {
            call(pD);
        }
    }

    private void call(D pD) {
        synchronized (mTList) {
            if (mRegisterCallback.isAllCallback()) {
                mRegisterCallback.call(Collections.unmodifiableList(mTList), pD);
            } else {
                int count = mTList.size();
                int index = -1;
                for (T t : mTList) {
                    mRegisterCallback.call(t, pD, ++index, count);
                }
            }
        }
    }

    public interface RegisterCallback<T, D> {
        boolean isAllCallback();

        boolean isCallbackInMainThread();

        void call(List<T> registerStuffs, D pData);

        void call(T registerSutff, D pData, int position, int count);
    }

    public static abstract class RegisterCallbackAdapter<T, D> implements RegisterHelper.RegisterCallback<T, D> {


        @Override
        public boolean isCallbackInMainThread() {
            return true;
        }

        @Override
        public void call(List<T> registerStuffs, D pData) {

        }

        @Override
        public void call(T registerSutff, D pData, int position, int count) {

        }
    }

    private static class RH extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Wrapper wrapper = (Wrapper) msg.obj;
            wrapper.mRegisterHelper.call(wrapper.mD);
        }
    }

    private static class Wrapper {
        RegisterHelper mRegisterHelper;
        Object mD;
    }

    public static <T, D> RegisterHelper<T, D> get(RegisterCallback<T, D> pRegisterCallback) {
        return new RegisterHelper<>(pRegisterCallback);
    }

}

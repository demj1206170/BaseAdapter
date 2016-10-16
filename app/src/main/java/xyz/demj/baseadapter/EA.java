package xyz.demj.baseadapter;

import xyz.demj.library.camrecyclerviewadapter.ConvertAdapter;

public  class EA<E> implements ConvertAdapter.To<E> {

            E mE;

            public EA(E e) {
                mE = e;
            }

            @Override
            public E to() {
                return mE;
            }
        }


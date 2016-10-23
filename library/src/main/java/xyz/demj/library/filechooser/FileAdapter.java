package xyz.demj.library.filechooser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import xyz.demj.library.R;
import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewAdapter;
import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewHolder;
import xyz.demj.library.camrecyclerviewadapter.MultiSelectionRecyclerViewAdapter;

/**
 * Created by demj on 2016/10/19.
 */

public class FileAdapter extends MultiSelectionRecyclerViewAdapter<File> {

    public FileAdapter() {
        super(sFactory);
        // setIsMultiSelection(false);
    }

    @Override
    protected void doAfterCreateViewHolder(BaseRecyclerViewHolder<File> pCraetedViewHolder) {
        super.doAfterCreateViewHolder(pCraetedViewHolder);
        FileViewHolder fileViewHolder = (FileViewHolder) pCraetedViewHolder;
        fileViewHolder.mOnPositionCheckChangedListener = mPositionCheckChangedListener;
    }

    private static BaseRecyclerViewHolder.ViewHolderFactory<File> sFactory = new BaseRecyclerViewHolder.ViewHolderFactory<File>() {
        @Override
        public BaseRecyclerViewHolder<File> createViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_file_chooser_item, parent, false);
            FileViewHolder fileViewHolder = new FileViewHolder(view);
            return fileViewHolder;
        }
    };

    private FileViewHolder.OnPositionCheckChangedListener mPositionCheckChangedListener = new FileViewHolder.OnPositionCheckChangedListener() {
        @Override
        public void onChanged(int position, boolean isChecked) {
            setItemSelection(position, isChecked);
        }
    };


    private static class FileViewHolder extends BaseRecyclerViewHolder<File> {
        TextView mFileNameTV;
        ImageView mFileIconIV;
        TextView mFileSizeTV;
        TextView mFileDateTV;
        CheckBox mCheckBox;

        interface OnPositionCheckChangedListener {
            void onChanged(int position, boolean isChecked);
        }

        private OnPositionCheckChangedListener mOnPositionCheckChangedListener;

        public FileViewHolder(View itemView) {
            super(itemView);
            mFileNameTV = (TextView) itemView.findViewById(R.id.file_name);
            mFileIconIV = (ImageView) itemView.findViewById(R.id.file_icon);
            mFileSizeTV = (TextView) itemView.findViewById(R.id.file_size);
            mFileDateTV = (TextView) itemView.findViewById(R.id.file_date);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.check);
            //  mCheckBox.setOnCheckedChangeListener(mListener);
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPositionCheckChangedListener.onChanged(getAdapterPosition(), mCheckBox.isChecked());
                }
            });
        }

        private CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOnPositionCheckChangedListener.onChanged(getAdapterPosition(), isChecked);
            }
        };

        @Override
        protected void bindViewHolder(File element, BaseRecyclerViewAdapter<File> pAdapter, int position) {

            boolean isSelected = pAdapter.isItemSelected(position);
            itemView.setSelected(isSelected);
            mFileNameTV.setText(element.getName());
            if (element.isFile())
                mFileIconIV.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
            else mFileIconIV.setImageResource(R.drawable.ic_folder_black_24dp);
            mFileSizeTV.setText(FileUtil.formatFileSize(element));
            mFileDateTV.setText(FileUtil.formatFileDate(element));
            mCheckBox.setChecked(isSelected);
        }
    }

}

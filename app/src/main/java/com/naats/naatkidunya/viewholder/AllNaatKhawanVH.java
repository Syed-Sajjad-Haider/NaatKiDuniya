package com.naats.naatkidunya.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.naats.naatkidunya.R;
import com.squareup.picasso.Picasso;

public class AllNaatKhawanVH extends RecyclerView.ViewHolder {
    View mView;
    Typeface customTypeOne;

    public AllNaatKhawanVH(View itemView) {
        super(itemView);

        mView = itemView;
        customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Pak Nastaleeq (Beta Release).ttf");
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }

    public void setDetails(Context ctx, String title, String image) {
        //Views

        TextView mTitleTv = mView.findViewById(R.id.rTitleTv);
        ImageView mImageIv = mView.findViewById(R.id.rImageView);
        mTitleTv.setText(title);
        if (mTitleTv != null) {
            mTitleTv.setTypeface(customTypeOne);
        }
        Picasso.get().load(image).into(mImageIv);
    }

    private AllNaatKhawanVH.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(AllNaatKhawanVH.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
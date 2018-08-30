package a13126.example.com.photoselector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public class PictureSelectorAdapter extends RecyclerView.Adapter<PictureSelectorAdapter.ViewHolder> {
    private static final String TAG = "PictureSelectorAdapter";
    private List<LocalMedia> photolist;
    private Context context;
    private LayoutInflater inflater;

    //定义常量表示加载不同布局
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOT = 2;

    public PictureSelectorAdapter(List<LocalMedia> photolist, Context context) {
        this.photolist = photolist;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setPhotolist(List<LocalMedia> photolist) {
        this.photolist = photolist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    /**
     * 给添加图片的Item添加点击事件
     */
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setmOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 点击照片预览图片
     */
    public interface OnPhotoItemClickListener{
        void onItemClick(View view,int position);
    }

    private OnPhotoItemClickListener photoItemClickLitener;

    public void setPhotoItemClickLitener(OnPhotoItemClickListener photoItemClickLitener) {
        this.photoItemClickLitener = photoItemClickLitener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            LocalMedia media = photolist.get(position);
            holder.tv_addphoto.setVisibility(View.GONE);
            Glide.with(context).load(media.getPath()).into(holder.iv_photo);
            //点击删除按钮删除图片
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photolist.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.iv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    photoItemClickLitener.onItemClick(holder.iv_photo,pos);
                }
            });
        } else if (getItemViewType(position) == TYPE_FOOT) {
            holder.iv_delete.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (photolist.size() < 9) {
            return photolist.size() + 1;
        } else {
            return photolist.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == photolist.size()) {
            return TYPE_FOOT;
        } else {
            return TYPE_NORMAL;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_photo;
        ImageView iv_delete;
        TextView tv_addphoto;

        private ViewHolder(View itemView) {
            super(itemView);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            tv_addphoto = itemView.findViewById(R.id.tv_add);
        }
    }

}

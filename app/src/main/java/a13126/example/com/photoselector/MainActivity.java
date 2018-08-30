package a13126.example.com.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView rlv_photos;

    private List<LocalMedia> selectList = new ArrayList<>();

    private Button button;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    PictureSelectorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(MainActivity.this);
        button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 自由配置选项
                Log.d(TAG, "onClick: ");

            }
        });
        rlv_photos = findViewById(R.id.rlv_photos);
        rlv_photos.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PictureSelectorAdapter(selectList, this);
        rlv_photos.setAdapter(adapter);
        adapter.setmOnItemClickLitener(new PictureSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                        .maxSelectNum(9)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        //.previewVideo()// 是否可预览视频 true or false
                        // .enablePreviewAudio() // 是否可播放音频 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(1f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        // .enableCrop(true)// 是否裁剪 true or false
                        // .compress(true)// 是否压缩 true or false
                        // .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        //.withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                        .isGif(true)// 是否显示gif图片 true or false
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //       .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        //.circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        //.showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        //.showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        //.openClickSound()// 是否开启点击声音 true or false
                        .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        //.cropCompressQuality()// 裁剪压缩质量 默认90 int
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //   .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                        // .rotateEnabled() // 裁剪是否可旋转图片 true or false
                        //   .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        // .videoQuality()// 视频录制质量 0 or 1 int
                        //     .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                        //    .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                        //  .recordVideoSecond()//视频秒数录制 默认60s int
                        .isDragFrame(false)// 是否可拖动裁剪框(固定)
                        .forResult(1);//结果回调onActivityResult code
            }
        });

        adapter.setPhotoItemClickLitener(new PictureSelectorAdapter.OnPhotoItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PictureSelector.create(MainActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // 图片选择结果回调
            selectList = PictureSelector.obtainMultipleResult(data);
            adapter.setPhotolist(selectList);
            adapter.notifyDataSetChanged();
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
            for (LocalMedia media : selectList) {
                Log.i("图片-----》", media.getPath() + "\n");

            }
            PictureFileUtils.deleteCacheDirFile(MainActivity.this);
        } else {
            System.out.println("failed");
        }

    }
}


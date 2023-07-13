package com.frstudio.bilibilivideomanagerpro
//
//
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import com.shuyu.gsyvideoplayer.GSYVideoManager
//import com.shuyu.gsyvideoplayer.utils.OrientationUtils
//import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
//
//
///**
// * 横屏不旋转的 Demo
// */
//class SimplePlayer : ComponentActivity() {
//    var videoPlayer: StandardGSYVideoPlayer? = null
//    var orientationUtils: OrientationUtils? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//
//        }
//        init()
//    }
//
//    private fun init() {
//        videoPlayer = findViewById(R.id.video_player) as StandardGSYVideoPlayer?
//        val source1 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
//        videoPlayer.setUp(source1, true, "测试视频")
//
//        //增加封面
//        val imageView = ImageView(this)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        imageView.setImageResource(R.mipmap.xxx1)
//        videoPlayer.setThumbImageView(imageView)
//        //增加title
//        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE)
//        //设置返回键
//        videoPlayer.getBackButton().setVisibility(View.VISIBLE)
//        //设置旋转
//        orientationUtils = OrientationUtils(this, videoPlayer)
//        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
//        videoPlayer.getFullscreenButton()
//            .setOnClickListener(View.OnClickListener { // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
//                // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
//                //orientationUtils.resolveByClick();
//                finish()
//            })
//        //是否可以滑动调整
//        videoPlayer.setIsTouchWiget(true)
//        //设置返回按键功能
//        videoPlayer.getBackButton().setOnClickListener(View.OnClickListener { onBackPressed() })
//
//
//        ///不需要屏幕旋转
//        videoPlayer.setNeedOrientationUtils(false)
//        videoPlayer.startPlayLogic()
//    }
//
//    protected fun onPause() {
//        super.onPause()
//        videoPlayer.onVideoPause()
//    }
//
//    protected fun onResume() {
//        super.onResume()
//        videoPlayer.onVideoResume()
//    }
//
//    protected fun onDestroy() {
//        super.onDestroy()
//        GSYVideoManager.releaseAllVideos()
//        if (orientationUtils != null) orientationUtils.releaseListener()
//    }
//
//    fun onBackPressed() {
/////       不需要回归竖屏
////        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
////            videoPlayer.getFullscreenButton().performClick();
////            return;
////        }
//        //释放所有
//        videoPlayer.setVideoAllCallBack(null)
//        super.onBackPressed()
//    }
//}
//
//class VideoPlayerActivity: ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//
//        }
//    }
//}
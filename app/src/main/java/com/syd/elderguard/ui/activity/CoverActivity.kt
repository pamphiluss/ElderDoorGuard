package com.syd.elderguard.ui.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.syd.elderguard.BaseApplication
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.ui.adapter.CoverAdapter
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dvider.CoverLine
import com.syd.elderguard.utils.CropUtils
import com.syd.elderguard.utils.FileUtil
import com.syd.elderguard.utils.Utils
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_cover.*
import kotlinx.android.synthetic.main.header_home.*
import java.io.File


class CoverActivity : BaseActivity(), View.OnClickListener {

    private lateinit var coverAdapter: CoverAdapter
    private val REQUEST_CODE_ALBUM = 2
    private val REQUEST_CODE_CROUP_PHOTO = 3
    private var uri: Uri? = null
    private lateinit var file: File

    override fun getLayoutId() = R.layout.activity_cover

    override fun getToolbarTitleId() = R.string.title_cover_setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //预览标题
        txvHomeTitle.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        imvHomeTitleArrow.setColorFilter(Color.WHITE)
        txvCustomCover.setOnClickListener(this)

        file = File(FileUtil.getCachePath(this), "${System.currentTimeMillis()}_cover.jpg")
        uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            FileProvider.getUriForFile(BaseApplication.inst,"$packageName.fileprovider", file)
        }

        coverAdapter = CoverAdapter()
        rvCoverList.addItemDecoration(CoverLine(this))
        rvCoverList.layoutManager = GridLayoutManager(this, 3)
        rvCoverList.adapter = coverAdapter
        coverAdapter.setNewData(mutableListOf("0", "1", "2", "3", "4", "5"))

        //显示预览图
        val coverIndex = MMKV.defaultMMKV().decodeString(Constant.SP_COVER_INDEX, "5")

        coverAdapter.updateCoverSelect(coverIndex.toInt())
        updateCoverPreview(coverIndex)
        coverAdapter.setOnItemClickListener { _, _, position ->
            MMKV.defaultMMKV().encode(Constant.SP_COVER_INDEX, position.toString())
            coverAdapter.updateCoverSelect(position)
            updateCoverPreview(position.toString())
        }
    }

    override fun onClick(p0: View?) {
        p0!!.let {
            when(p0.id) {
                R.id.txvCustomCover-> {
                    startCustomCover()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != -1) {
            return
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null) {
            val newUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.parse("file:///" + CropUtils.getPath(this, data.data))
            } else {
                data.data
            }

            if (newUri != null) {
                startPhotoZoom(newUri)
            } else {
                showLongToast("没有得到相册图片")
            }
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            uploadAvatarFromPhoto()
        }
    }

    private fun uploadAvatarFromPhoto() {
        val path = file.path
        Glide.with(this).load(path).into(imvHomeThemeImage)
        MMKV.defaultMMKV().encode(Constant.SP_COVER_CUSTOM_PATH, path)
        MMKV.defaultMMKV().encode(Constant.SP_COVER_INDEX, "-1")
        coverAdapter.updateCoverSelect(-1)
        updateCoverPreview("-1")
        showLongToast("设置成功")
    }

    private fun startPhotoZoom(uri: Uri?) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra("crop", "true") // crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1) // 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1) // x:y=1:1
        intent.putExtra("output", Uri.fromFile(file))
        intent.putExtra("outputFormat", "JPEG") // 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO)
    }

    /**
     * 更新预览
     */
    private fun updateCoverPreview(coverIndex: String) {
        if (coverIndex == "-1") {
            val path: String = MMKV.defaultMMKV().decodeString(Constant.SP_COVER_CUSTOM_PATH)
            Glide.with(this).load(path).into(imvHomeThemeImage)
            return
        }
        var drawable = Utils.getDrawableResource(this, "ic_header_theme_${coverIndex}")
        imvHomeThemeImage.setImageResource(drawable)
    }

    /**
     * 自定义
     */
    private fun startCustomCover() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM)
    }
}
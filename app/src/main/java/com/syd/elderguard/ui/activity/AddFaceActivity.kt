package com.syd.elderguard.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.syd.elderguard.BaseApplication
import com.syd.elderguard.R
import com.syd.elderguard.model.FaceBmob
import com.syd.elderguard.model.User
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dialog.ActionSheetDialog
import com.syd.elderguard.ui.viewmodel.AddAccountViewModel
import com.syd.elderguard.ui.viewmodel.AddFaceViewModel
import com.syd.elderguard.utils.CropUtils
import com.syd.elderguard.utils.FileUtil
import kotlinx.android.synthetic.main.activity_add_face.*
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File

class AddFaceActivity : BaseActivity(), View.OnClickListener {
    private val REQUEST_CODE_TAKE_PHOTO = 1
    private val REQUEST_CODE_ALBUM = 2
    private val REQUEST_CODE_CROUP_PHOTO = 3

    private lateinit var name: String
    private var sex = 0
    private lateinit var file: File
    private var uri: Uri? = null
    private lateinit var addFaceViewModel: AddFaceViewModel
    private var CurrenFaceBmob: FaceBmob = FaceBmob()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        friend_imvHeader.setOnClickListener(this)
        friend_name.setOnClickListener(this)
        friend_txvRelationshipName.setOnClickListener(this)
        friend_txvPartInDate.setOnClickListener(this)
        friend_sex.setOnClickListener(this)
        friend_edtRemark.setOnClickListener(this)
        init()

        AddFaceViewModel.toastLiveData.observe(this, androidx.lifecycle.Observer {
            showLongToast(it)
            if("添加成功" == it || "删除成功" == it) {
                finish()
            }
        })

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_face
    }

    override fun showToolbar(): Boolean {
        return true
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_add_face_out
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .transparentNavigationBar()
                .navigationBarDarkIcon(true)
                .init();
    }


    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.friend_imvHeader -> {
                    showHeaderDialog()
                }

            }
        }
    }

    private fun showHeaderDialog() {
        ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(
                        "从相册选择",
                        ActionSheetDialog.SheetItemColor.Blue
                ) { uploadAvatarFromAlbumRequest() }
                .addSheetItem(
                        "相机拍照",
                        ActionSheetDialog.SheetItemColor.Blue
                ) { uploadAvatarFromPhotoRequest() }
                .show()
    }

    private fun uploadAvatarFromPhoto() {
        val path = file.path
        val cover = FileUtil.getSmallBitmap(this, path)
        Glide.with(this).load(cover).into(imvHeader!!)
        val bmobFile = BmobFile(file)
        bmobFile.uploadblock(object : UploadFileListener() {
            override fun done(e: BmobException?) {
                if (e == null) {
                    val user = User.getCurrentUser(User::class.java)
                    user.avatar = bmobFile
                    user.update(object : UpdateListener() {
                        override fun done(e: BmobException?) {
                            if (e == null) {
                                showLongToast("更新成功")
                            }
                        }
                    })
                } else {
                    showLongToast("上传文件失败" + e.errorCode.toString())
                }
            }

            override fun onProgress(value: Int) {
                // 返回的上传进度（百分比）
            }
        })
    }

    private fun uploadAvatarFromPhotoRequest() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO)
    }

    private fun uploadAvatarFromAlbumRequest() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != -1) {
            return
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null) {
            //val newUri: Uri?
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
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri)
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            uploadAvatarFromPhoto()
        }
    }

    fun startPhotoZoom(uri: Uri?) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra("crop", "true") // crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1) // 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1) // x:y=1:1
        //        intent.putExtra("outputX", 400);//图片输出大小
//        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(file))
        intent.putExtra("outputFormat", "JPEG") // 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO)
    }

    private fun init() {
        file = File(FileUtil.getCachePath(this), "user-avatar.jpg")
        uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            FileProvider.getUriForFile(BaseApplication.inst, "$packageName.fileprovider", file)
        }
        if (CurrenFaceBmob.photo != null) {
            val bmobFile: BmobFile = CurrenFaceBmob.photo
            Glide.with(this).load(bmobFile.fileUrl).into(friend_imvHeader!!)
        }

    }
}
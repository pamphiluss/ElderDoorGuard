package com.syd.elderguard.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.jeremyliao.liveeventbus.LiveEventBus
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.User
import com.syd.elderguard.ui.activity.AboutUsActivity
import com.syd.elderguard.ui.activity.CoverActivity
import com.syd.elderguard.ui.activity.TodoListActivity
import com.syd.elderguard.ui.base.BaseFragment
import com.syd.elderguard.ui.viewmodel.MineViewModel
import com.syd.elderguard.utils.toLogin
import com.syd.elderguard.utils.toUserInfoAct
import com.syd.elderguard.works.SyncDataScheduler
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.koin.android.viewmodel.ext.android.getViewModel


class MineFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mineViewModel: MineViewModel
    private var unUploadCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = getString(R.string.title_mine)

        txvAboutUs.setOnClickListener(this)
        txvContactUs.setOnClickListener(this)
        relUserInfo.setOnClickListener(this)
        txvTodo.setOnClickListener(this)
        rlSyncData.setOnClickListener(this)
        txvCoverSetting.setOnClickListener(this)

        mineViewModel = getViewModel<MineViewModel>()
    }

    override fun onResume() {
        super.onResume()
        mineViewModel.checkUnUploadCounter()
        checkUserInfo()
        dataObsever()
    }

    private fun checkUserInfo() {
        if (User.isLogin()) {
            val user = User.getCurrentUser(User::class.java)
            if (user.avatar == null) {
                imvAvatar.setImageResource(R.drawable.ic_avatar_default)
            } else {
                Glide.with(this).load(user.avatar.fileUrl).into(imvAvatar)
            }
        } else {
            imvAvatar.setImageResource(R.drawable.ic_unlogin_avatar)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.txvAboutUs-> {
                startActivity(Intent(activity, AboutUsActivity::class.java))
            }

            R.id.txvContactUs-> {
                showConactDevloperDialog()
            }

            R.id.relUserInfo-> {
                toUserInfo()
            }
            R.id.txvTodo-> {
                startActivity(Intent(activity, TodoListActivity::class.java))
            }
            R.id.rlSyncData-> {
                syncData()
            }

            R.id.txvCoverSetting-> {
                startActivity(Intent(activity, CoverActivity::class.java))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun syncData() {
        if (!UserManager.isLogin()) {
            activity?.let { toLogin(it) }
            return
        }
        if (unUploadCount == 0) {
            showLongToast("暂无同步数据")
            return
        }

        if (SyncDataScheduler.uploadSyscState>0) {
            showLongToast("数据同步中")
            return
        }

        activity?.let {
            MaterialDialog(it).show {
                title(R.string.dialog_title_sync_data)
                message(R.string.dialog_message_sync_data)
                positiveButton(R.string.lable_ok) {
                    this@MineFragment.syncDataCloud.playAnimation()
                    SyncDataScheduler.refreshSyncOneTimeWork(viewLifecycleOwner)
                }
                negativeButton(R.string.lable_cancle)
            }
        }
    }

    private fun toUserInfo() {
        if (UserManager.isLogin()) {
            activity?.let { toUserInfoAct(it) }
        } else {
            activity?.let { toLogin(it) }
        }
    }

    private fun showConactDevloperDialog() {
        activity?.let {
            MaterialDialog(it).show {
                title(R.string.dialog_title_contact_us)
                message(R.string.dialog_message_contact_us)
            }
        }
    }

    private fun dataObsever() {
        //待同步数
        mineViewModel.counterLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            unUploadCount = it
            if (it>0) {
                syncDataCloud.visibility = View.VISIBLE
            } else {
                syncDataCloud.visibility = View.GONE
            }
        })
        //同步中剩余任务
        LiveEventBus.get(Constant.EVENT_SYNC_UPLOAD_LESS_COUNTER, Int::class.java)
            .observe(this, Observer<Int> {
                if (it==0) {
                    showLongToast("同步完成")
                    this@MineFragment.syncDataCloud.pauseAnimation()
                    this@MineFragment.syncDataCloud.visibility = View.GONE
                }
            })
    }
}

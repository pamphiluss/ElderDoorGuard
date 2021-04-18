package com.syd.elderguard.ui.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.syd.elderguard.BaseApplication
import com.syd.elderguard.R
import com.syd.elderguard.extensions.toPx
import com.syd.elderguard.utils.Utils

class CoverAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_cover) {

    private var select: Int = 0
    private var coverWidth = 0
    private var coverHeight = 0

    init {
        val screenWidth = BaseApplication.inst.resources.displayMetrics.widthPixels
        coverWidth = (screenWidth - 4 * 10.toPx())/3;
        coverHeight = 70.toPx()
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        val position = helper.adapterPosition

        val cbCoverCheck = helper.getView<CheckBox>(R.id.cbCoverCheck)
        if (position == select) {
            cbCoverCheck.visibility = View.VISIBLE
            cbCoverCheck.isChecked = true
        } else {
            cbCoverCheck.visibility = View.GONE
            cbCoverCheck.isChecked = false
        }

        val coverImageView = helper.getView<ImageView>(R.id.imvCoverImage)
        var params = coverImageView.layoutParams
        params.width = coverWidth
        params.height = coverHeight
        coverImageView.layoutParams = params
        coverImageView.setImageResource(
            Utils.getDrawableResource(BaseApplication.inst, "ic_header_theme_${item}"))
    }

    public fun updateCoverSelect(index: Int) {
        this.select = index
        notifyDataSetChanged()
    }

}
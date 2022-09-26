package com.hieuminh.chessclient.views.activitys

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.hieuminh.chessclient.common.enums.ChessManType
import com.hieuminh.chessclient.common.enums.PlayerType
import com.hieuminh.chessclient.databinding.ActivityMainBinding
import com.hieuminh.chessclient.models.Box
import com.hieuminh.chessclient.utils.ViewUtils
import com.hieuminh.chessclient.views.activitys.base.BaseActivity
import com.hieuminh.chessclient.views.adapters.BoxAdapter
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter
import kotlin.math.min

class MainActivity : BaseActivity<ActivityMainBinding>(), BaseAdapter.ItemEventListener<Box> {
    private lateinit var boxAdapter: BoxAdapter
    private lateinit var boxList: MutableList<Box>

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        initData()
        super.onCreate(savedInstanceState)
    }

    override fun onItemClick(item: Box, position: Int) {
        Log.d("HIEU_MINH", item.chessMan?.playerType?.name.toString())
    }

    override fun initListener() {
    }

    override fun initView() {
        val size = min(ViewUtils.getScreenWidth(this), ViewUtils.getScreenHeight(this)) - 50
        boxAdapter = BoxAdapter(size / 8)
        boxAdapter.updateData(boxList)
        boxAdapter.setItemListener(this)
        binding.rvBoxList.run {
            adapter = boxAdapter
            layoutManager = object : GridLayoutManager(context, 8, VERTICAL, true) {
                override fun canScrollVertically() = false

                override fun canScrollHorizontally() = false
            }
        }
    }

    private fun initData() {
        boxList = mutableListOf()
        for (y in 0..7) {
            for (x in 0..7) {
                val box = Box(x, y)
                val chessMain = when (y) {
                    0 -> ChessManType.getMainMap()[x]?.newInstance?.invoke()?.apply { playerType = PlayerType.PLAYER_FIRST }
                    1 -> ChessManType.PAWN?.newInstance?.invoke()?.apply { playerType = PlayerType.PLAYER_FIRST }
                    6 -> ChessManType.PAWN?.newInstance?.invoke()?.apply { playerType = PlayerType.PLAYER_SECOND }
                    7 -> ChessManType.getMainMap()[x]?.newInstance?.invoke()?.apply { playerType = PlayerType.PLAYER_SECOND }
                    else -> null
                }
                box.chessMan = chessMain
                boxList.add(box)
            }
        }
    }
}

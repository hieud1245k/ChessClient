package com.hieuminh.chessclient.views.activitys

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.hieuminh.chessclient.common.enums.ChessManType
import com.hieuminh.chessclient.common.enums.PlayerType
import com.hieuminh.chessclient.databinding.ActivityMainBinding
import com.hieuminh.chessclient.models.Bishop
import com.hieuminh.chessclient.models.Box
import com.hieuminh.chessclient.models.Castle
import com.hieuminh.chessclient.models.Pawn
import com.hieuminh.chessclient.utils.ViewUtils
import com.hieuminh.chessclient.views.activitys.base.BaseActivity
import com.hieuminh.chessclient.views.adapters.BoxAdapter
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter
import kotlin.math.min

class MainActivity : BaseActivity<ActivityMainBinding>(), BaseAdapter.ItemEventListener<Box> {
    private lateinit var boxAdapter: BoxAdapter
    private lateinit var boxList: MutableList<Box>
    private lateinit var boxMap: Map<Pair<Int, Int>, Box>

    private var currentBoxSelected: Box? = null
    private val moveBoxList = mutableListOf<Box>()
    private val killBoxList = mutableListOf<Box>()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        initData()
        super.onCreate(savedInstanceState)
    }

    override fun onItemClick(item: Box, position: Int) {
        Log.d("POSITION", position.toString())
        if (item.canKill || item.canMove) {
            item.chessMan = currentBoxSelected?.chessMan
            currentBoxSelected?.run {
                chessMan = null
                isClicked = false
                notifyChanged(boxAdapter)
            }
            currentBoxSelected = null
            killBoxList.forEach {
                it.canKill = false
                it.notifyChanged(boxAdapter)
            }
            moveBoxList.forEach {
                it.canMove = false
                it.notifyChanged(boxAdapter)
            }
            return
        }
        if (item.chessMan == null || currentBoxSelected == item || item.chessMan?.playerType == PlayerType.PLAYER_SECOND) {
            return
        }
        moveBoxList.forEach {
            it.canMove = false
            it.notifyChanged(boxAdapter)
        }
        killBoxList.forEach {
            it.canKill = false
            it.notifyChanged(boxAdapter)
        }
        moveBoxList.clear()
        killBoxList.clear()
        item.isClicked = true
        item.notifyChanged(boxAdapter)
        currentBoxSelected?.isClicked = false
        currentBoxSelected?.notifyChanged(boxAdapter)
        when (item.chessMan) {
            is Pawn -> onPawnClicked(item)
            is Castle -> onCastleClicked(item)
            is Bishop -> onBishopClicked(item)
        }
        moveBoxList.forEach {
            it.canMove = true
            it.notifyChanged(boxAdapter)
        }
        killBoxList.forEach {
            it.canKill = true
            it.notifyChanged(boxAdapter)
        }
        currentBoxSelected = item
    }

    private fun onPawnClicked(item: Box) {
        boxMap[Pair(item.x, item.y + 1)]?.let {
            if (it.chessMan == null) {
                moveBoxList.add(it)
            }
        }
        if (item.y == 1) {
            boxMap[Pair(item.x, item.y + 2)]?.let {
                if (it.chessMan == null) {
                    moveBoxList.add(it)
                }
            }
        }
        boxMap[Pair(item.x + 1, item.y + 1)]?.let {
            if (it.chessMan?.playerType == PlayerType.PLAYER_SECOND) {
                killBoxList.add(it)
            }
        }
        boxMap[Pair(item.x - 1, item.y + 1)]?.let {
            if (it.chessMan?.playerType == PlayerType.PLAYER_SECOND) {
                killBoxList.add(it)
            }
        }
    }

    private fun onCastleClicked(item: Box) {
        addActionList { Pair(item.x + it, item.y) }
        addActionList { Pair(item.x - it, item.y) }
        addActionList { Pair(item.x, item.y + it) }
        addActionList { Pair(item.x, item.y - it) }
    }

    private fun onBishopClicked(item: Box) {
        addActionList { Pair(item.x + it, item.y + it) }
        addActionList { Pair(item.x + it, item.y - it) }
        addActionList { Pair(item.x - it, item.y + it) }
        addActionList { Pair(item.x - it, item.y - it) }
    }

    private fun addActionList(getKey: (Int) -> Pair<Int, Int>) {
        for (i in 1..7) {
            val box = boxMap[getKey(i)] ?: break
            if (box.chessMan == null) {
                moveBoxList.add(box)
                continue
            }
            if (box.chessMan?.playerType == PlayerType.PLAYER_SECOND) {
                killBoxList.add(box)
            }
            break
        }
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
        boxMap = boxList.associateBy { Pair(it.x, it.y) }
    }
}

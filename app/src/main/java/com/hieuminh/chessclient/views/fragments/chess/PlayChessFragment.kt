package com.hieuminh.chessclient.views.fragments.chess

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.hieuminh.chessclient.common.enums.ChessManType
import com.hieuminh.chessclient.common.enums.PlayerType
import com.hieuminh.chessclient.databinding.FragmentPlayChessBinding
import com.hieuminh.chessclient.models.*
import com.hieuminh.chessclient.models.request.ChessRequest
import com.hieuminh.chessclient.utils.JsonUtils
import com.hieuminh.chessclient.utils.ViewUtils
import com.hieuminh.chessclient.views.adapters.BoxAdapter
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter
import com.hieuminh.chessclient.views.fragments.base.BaseFragment
import com.hieuminh.chessclient.views.fragments.dialogs.PawnPromotionFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.min

class PlayChessFragment : BaseFragment<FragmentPlayChessBinding>(), BaseAdapter.ItemEventListener<Box> {
    private lateinit var boxAdapter: BoxAdapter
    private lateinit var boxList: MutableList<Box>
    private lateinit var boxMap: Map<Pair<Int, Int>, Box>

    private var currentBoxSelected: Box? = null
    private val moveBoxList = mutableListOf<Box>()
    private val killBoxList = mutableListOf<Box>()

    private lateinit var room: Room
    private lateinit var name: String

    override fun getViewBinding() = FragmentPlayChessBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        initData()
        super.onCreate(savedInstanceState)
        val args = PlayChessFragmentArgs.fromBundle(requireArguments())
        room = args.room
        name = args.name
    }

    override fun onItemClick(item: Box, position: Int) {
        Log.d("POSITION", position.toString())
        if (item.canKill || item.canMove) {
            sendChessmanAction(item)
            item.chessMan = currentBoxSelected?.chessMan
            currentBoxSelected?.run {
                chessMan = null
                isClicked = false
                notifyChanged(boxAdapter)
            }
            currentBoxSelected = null
            resetActionList()
            if (item.y == 7 && item.chessMan is Pawn) {
                promotePawn(item)
            }
            return
        }
        if (item.chessMan == null || item.chessMan.isRival()) {
            return
        }
        resetActionList()
        onChessmanClicked(item)
    }

    private fun sendChessmanAction(item: Box) {
        val chessRequest = ChessRequest()
        chessRequest.from = currentBoxSelected?.copy()
        chessRequest.to = item.copy()
        chessRequest.playerName = room.getRivalPlayerName(name)
        val request = Gson().toJson(chessRequest)
        baseActivity?.subscribe { stompClient ->
            stompClient.send("/app/go-to-box", request).compose(applySchedulers()).subscribe {
                Toast.makeText(context, "Go to box success", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onChessmanClicked(item: Box) {
        currentBoxSelected?.isClicked = false
        currentBoxSelected?.notifyChanged(boxAdapter)
        if (currentBoxSelected == item) {
            currentBoxSelected = null
            return
        }
        item.isClicked = true
        item.notifyChanged(boxAdapter)
        when (item.chessMan) {
            is Pawn -> onPawnClicked(item)
            is Castle -> onCastleClicked(item)
            is Knight -> onKnightClicked(item)
            is Bishop -> onBishopClicked(item)
            is Queen -> onQueenClicked(item)
            is King -> onKingClicked(item)
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

    private fun promotePawn(box: Box) {
        val pawnPromotionFragment = PawnPromotionFragment()
        pawnPromotionFragment.setGeneralItemListener(object : BaseAdapter.ItemEventListener<ChessMan> {
            override fun onItemClick(item: ChessMan, position: Int) {
                box.chessMan = item
                box.notifyChanged(boxAdapter)
            }
        })
        pawnPromotionFragment.show(childFragmentManager, null)
    }

    private fun resetActionList() {
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
    }

    private fun onPawnClicked(item: Box) {
        fun addMoveList(key: Pair<Int, Int>) {
            boxMap[key]?.let {
                if (it.chessMan == null) {
                    moveBoxList.add(it)
                }
            }
        }

        fun addKillList(key: Pair<Int, Int>) {
            boxMap[key]?.let {
                if (it.chessMan.isRival()) {
                    killBoxList.add(it)
                }
            }
        }

        addMoveList(Pair(item.x, item.y + 1))
        if (item.y == 1 && moveBoxList.isNotEmpty()) {
            addMoveList(Pair(item.x, item.y + 2))
        }
        addKillList(Pair(item.x + 1, item.y + 1))
        addKillList(Pair(item.x - 1, item.y + 1))
    }

    private fun onCastleClicked(item: Box) {
        addActionList { Pair(item.x + it, item.y) }
        addActionList { Pair(item.x - it, item.y) }
        addActionList { Pair(item.x, item.y + it) }
        addActionList { Pair(item.x, item.y - it) }
    }

    private fun onKnightClicked(item: Box) {
        addActionList(Pair(item.x - 1, item.y + 2))
        addActionList(Pair(item.x - 1, item.y - 2))
        addActionList(Pair(item.x - 2, item.y + 1))
        addActionList(Pair(item.x - 2, item.y - 1))
        addActionList(Pair(item.x + 1, item.y + 2))
        addActionList(Pair(item.x + 1, item.y - 2))
        addActionList(Pair(item.x + 2, item.y + 1))
        addActionList(Pair(item.x + 2, item.y - 1))
    }

    private fun onBishopClicked(item: Box) {
        addActionList { Pair(item.x + it, item.y + it) }
        addActionList { Pair(item.x + it, item.y - it) }
        addActionList { Pair(item.x - it, item.y + it) }
        addActionList { Pair(item.x - it, item.y - it) }
    }

    private fun onQueenClicked(item: Box) {
        onCastleClicked(item)
        onBishopClicked(item)
    }

    private fun onKingClicked(item: Box) {
        addActionList(Pair(item.x, item.y + 1))
        addActionList(Pair(item.x, item.y - 1))
        addActionList(Pair(item.x + 1, item.y + 1))
        addActionList(Pair(item.x + 1, item.y))
        addActionList(Pair(item.x + 1, item.y - 1))
        addActionList(Pair(item.x - 1, item.y + 1))
        addActionList(Pair(item.x - 1, item.y))
        addActionList(Pair(item.x - 1, item.y - 1))
    }

    private fun addActionList(getKey: (Int) -> Pair<Int, Int>) {
        for (i in 1..7) {
            val box = boxMap[getKey(i)] ?: break
            if (box.chessMan == null) {
                moveBoxList.add(box)
                continue
            }
            if (box.chessMan.isRival()) {
                killBoxList.add(box)
            }
            break
        }
    }

    private fun addActionList(key: Pair<Int, Int>) {
        boxMap[key]?.let {
            if (it.chessMan == null) {
                moveBoxList.add(it)
            }
            if (it.chessMan.isRival()) {
                killBoxList.add(it)
            }
        }
    }

    override fun initListener() {
        baseActivity?.subscribe { stompClient ->
            stompClient.topic("/queue/go-to-box")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val chessRequest = JsonUtils.fromJson<ChessRequest>(it.payload) ?: return@subscribe
                    val name = name
                    if (!name.equals(chessRequest.playerName)) {
                        return@subscribe
                    }
                    val fromBox = boxMap[Pair(chessRequest.from?.x ?: 0, chessRequest.from?.y ?: 0)]
                    val toBox = boxMap[Pair(chessRequest.to?.x ?: 0, chessRequest.to?.y ?: 0)]

                    toBox?.chessMan = fromBox?.chessMan
                    fromBox?.chessMan = null
                    boxAdapter.notifyDataSetChanged()
                }, {
                    Toast.makeText(context, "Connect to /queue/go-to-box Failure!", Toast.LENGTH_LONG).show()
                })
        }
        baseActivity?.subscribe { stompClient ->
            stompClient.topic("/queue/join-room")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ stomMessage ->
                    JsonUtils.fromJson<Room>(stomMessage.payload)?.let {
                        room = it
                    }
                }, {
                    Toast.makeText(context, "Connect to /queue/go-to-box Failure!", Toast.LENGTH_LONG).show()
                })
        }
    }

    override fun initView() {
        binding.ivRoomId.text = room.roomTextId

        val size = min(ViewUtils.getScreenWidth(activity), ViewUtils.getScreenHeight(activity)) - 50
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
                    1 -> ChessManType.PAWN.newInstance.invoke().apply { playerType = PlayerType.PLAYER_FIRST }
                    6 -> ChessManType.PAWN.newInstance.invoke().apply { playerType = PlayerType.PLAYER_SECOND }
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

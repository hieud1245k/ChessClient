package com.hieuminh.chessclient.views.fragments.chess

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.common.enums.ChessManType
import com.hieuminh.chessclient.common.enums.PlayerType
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.FragmentPlayChessBinding
import com.hieuminh.chessclient.databinding.LayoutPlayerInfoBinding
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
    private var createRoom: Boolean = false
    private var currentChessRequest: ChessRequest? = null

    private var yourTurn = false

    override fun getViewBinding() = FragmentPlayChessBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        initData()
        super.onCreate(savedInstanceState)
        val args = PlayChessFragmentArgs.fromBundle(requireArguments())
        room = args.room
        name = args.name
        createRoom = args.createRoom
    }

    override fun onResume() {
        super.onResume()
        subscribe()
    }

    private fun subscribe() {
        subscribe { stompClient ->
            stompClient.topic("/queue/go-to-box/${room.id}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sub@{
                    val chessRequest = JsonUtils.fromJson<ChessRequest>(it.payload) ?: return@sub
                    val name = name
                    if (name != chessRequest.playerName) {
                        return@sub
                    }

                    currentChessRequest?.resetJump(boxAdapter)

                    val fromBox = boxMap[Pair(chessRequest.from?.x ?: 0, chessRequest.from?.y ?: 0)]
                    val toBox = boxMap[Pair(chessRequest.to?.x ?: 0, chessRequest.to?.y ?: 0)]
                    val oldChessMan = toBox?.chessMan

                    toBox?.run {
                        chessMan = fromBox?.chessMan
                        justJump = true
                        notifyChanged(boxAdapter)
                    }
                    fromBox?.run {
                        chessMan = null
                        justJump = true
                        notifyChanged(boxAdapter)
                    }

                    if (oldChessMan is King) {
                        showGameResult(false)
                        return@sub
                    }

                    currentChessRequest = ChessRequest().apply {
                        to = toBox
                        from = fromBox
                    }

                    yourTurn = true
                    updateProcess()
                }, {
                   toast( "Connect to /queue/go-to-box Failure!")
                })
        }
        subscribe { stompClient ->
            stompClient.topic("/queue/join-room/${room.id}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ stomMessage ->
                    JsonUtils.fromJson<Room>(stomMessage.payload)?.let {
                        binding.llStartGame.isVisible = room.playerFirstName == null || room.playerSecondName == null
                        if (
                            (it.playerFirstName == null || it.playerSecondName == null)
                            && room.playerFirstName != null
                            && room.playerSecondName != null
                        ) {
                            val rivalName = room.getRivalPlayerName(name)
                           toast( "$rivalName have left this room")
                            resetData()
                        }
                        room = it
                        updateRivalName(true)
                    }
                }, {
                   toast( "Connect to /queue/go-to-box Failure!")
                })
        }
        subscribe { stompClient ->
            stompClient.topic("/queue/start-game/${room.id}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ stompMessage ->
                    val firstPlayerName = stompMessage.payload
                    yourTurn = firstPlayerName == name
                    updateProcess()
                    binding.llStartGame.isVisible = false
                   toast( if (yourTurn) R.string.your_turn else R.string.please_waiting)
                }, {
                   toast( "Connect to /queue/go-to-box Failure!")
                })
        }
    }

    override fun onItemClick(item: Box, position: Int) {
        if (!yourTurn) {
            return
        }
        if (item.canKill || item.canMove) {
            sendChessmanAction(item)
            val oldChessMan = item.chessMan
            item.chessMan = currentBoxSelected?.chessMan
            item.justJump = true
            currentBoxSelected?.run {
                chessMan = null
                isClicked = false
                justJump = true
                notifyChanged(boxAdapter)
            }
            currentBoxSelected = null
            resetActionList()
            if (oldChessMan is King) {
                showGameResult(true)
                return
            }
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
        currentChessRequest?.resetJump(boxAdapter)

        val chessRequest = ChessRequest()
        chessRequest.from = currentBoxSelected?.copy()
        chessRequest.to = item.copy()
        chessRequest.playerName = room.getRivalPlayerName(name)
        chessRequest.roomId = room.id
        currentChessRequest = ChessRequest().apply {
            from = currentBoxSelected
            to = item
        }
        val request = Gson().toJson(chessRequest)
        subscribe { stompClient ->
            stompClient.send("/app/go-to-box", request).compose(applySchedulers()).subscribe {
                yourTurn = false
                updateProcess()
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

    private fun leave() {
        val roomRequest = room
        roomRequest.resetPlayerName(name)
        chessViewModel?.leaveRoom(roomRequest, {
           toast( "You left game!")
            view?.navController?.popBackStack()
        })
    }

    override fun initListener() {
        binding.btStartGame.setOnClickListener {
            subscribe { stompClient ->
                stompClient.send("/app/start-game", room.id.toString()).compose(applySchedulers()).subscribe {
                    Log.d("START_GAME", "Send start game message successful!")
                }
            }
        }
        binding.ivBack.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.notice)
                .setMessage(getString(R.string.are_you_sure_you_want_to_leave_this_room))
                .setPositiveButton(getString(R.string.leave)) { _, _ -> leave() }
                .setNegativeButton(R.string.cancel, null)
                .show()
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

        binding.layoutYourInfo.run {
            ivAvatar.setColorFilter(resources.getColor(R.color.colorOrange))
            tvName.text = name
        }

        binding.layoutRivalInfo.ivAvatar.setColorFilter(resources.getColor(R.color.black))
        updateRivalName()
    }

    private fun updateRivalName(rivalJoined: Boolean = false) {
        val rivalName = room.getRivalPlayerName(name)
        if (rivalName != null && rivalJoined) {
           toast( "$rivalName have joined this room")
        }
        binding.layoutRivalInfo.tvName.text = rivalName ?: resources.getString(R.string.waiting_for_the_next_player)
        binding.layoutYourInfo.tvStatus.setText(R.string.please_waiting)
        binding.layoutRivalInfo.tvStatus.setText(R.string.please_waiting)
    }

    private fun LayoutPlayerInfoBinding.updateProcess(yourTurn: Boolean) {
        tvStatus.setText(if (yourTurn) R.string.your_turn else R.string.please_waiting)
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

    private fun resetData() {
        initData()
        boxAdapter.updateData(boxList)
    }

    private fun showGameResult(win: Boolean) {
        AlertDialog.Builder(context)
            .setTitle(if (win) R.string.you_win else R.string.you_lose)
            .setMessage(if (win) "Congratulation!" else "Press \"Continue\" to start new game!")
            .setPositiveButton("Continue") { _, _ ->
                resetData()
                if (name.equals(room.playerFirstName)) {
                    binding.llStartGame.isVisible = true
                }
            }
            .setCancelable(false)
            .show()
        yourTurn = false
        updateProcess(true)
    }

    private fun updateProcess(isReset: Boolean = false) {
        binding.layoutYourInfo.updateProcess(yourTurn && !isReset)
        binding.layoutRivalInfo.updateProcess(!yourTurn && !isReset)
    }
}

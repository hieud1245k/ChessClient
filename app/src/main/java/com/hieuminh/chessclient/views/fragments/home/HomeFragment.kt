package com.hieuminh.chessclient.views.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.common.extensions.ContextExtensions.isLandscape
import com.hieuminh.chessclient.common.extensions.ContextExtensions.isTablet
import com.hieuminh.chessclient.common.extensions.StringExtensions.toLongSafe
import com.hieuminh.chessclient.common.extensions.ViewExtensions.hideKeyboard
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navigate
import com.hieuminh.chessclient.databinding.FragmentHomeBinding
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.views.adapters.RoomAdapter
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter
import com.hieuminh.chessclient.views.fragments.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(), BaseAdapter.ItemEventListener<Room> {
    private lateinit var name: String
    private lateinit var roomAdapter: RoomAdapter

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = HomeFragmentArgs.fromBundle(requireArguments())
            name = args.name
        }
    }

    override fun onResume() {
        super.onResume()
        chessViewModel?.fetchRoomList()
    }

    override fun onItemClick(item: Room, position: Int) {
        joinRoom(item.id ?: return, name)
    }

    override fun initListener() {
        binding.layoutHeader.btPlayNow.setOnClickListener {
            // TODO
        }
        binding.layoutHeader.btFindRoom.setOnClickListener {
            val roomId = binding.layoutHeader.etFindRoom.text.toString().toLongSafe()
            joinRoom(roomId, name)
        }
        binding.btCreateNewRoom.setOnClickListener { showConfirmCreateNewRoomDialog() }
        binding.sflRoomLisRefresh.setOnRefreshListener {
            binding.sflRoomLisRefresh.isRefreshing = false
            chessViewModel?.fetchRoomList()
        }
    }

    private fun observerLiveData() {
        chessViewModel?.roomListLiveData?.observe(this) { roomList ->
            roomAdapter.updateData(roomList.toMutableList())
        }
    }

    private fun joinRoom(roomId: Long, name: String) {
        chessViewModel?.joinRoom(roomId, name, { room ->
            goToPlayChess(room, false)
        }, {
            chessViewModel?.fetchRoomList()
            binding.layoutHeader.etFindRoom.run {
                text.clear()
                hideKeyboard()
            }
            AlertDialog.Builder(context)
                .setTitle(R.string.notice)
                .setMessage("Join room with id $roomId failed! please try again or create a new room!")
                .show()
        })
    }

    override fun initView() {
        observerLiveData()

        binding.layoutHeader.tvTitle.text = String.format(resources.getString(R.string.hello_s), name)

        roomAdapter = RoomAdapter()
        roomAdapter.registerAdapterDataObserver(roomDataObserver)
        val spanCount = when {
            context.isTablet && context.isLandscape -> 4
            context.isTablet -> 3
            context.isLandscape -> 2
            else -> 1
        }
        roomAdapter.setItemListener(this)
        binding.rlRoomList.setHasFixedSize(true)
        binding.rlRoomList.layoutManager = GridLayoutManager(context, spanCount)
        binding.rlRoomList.adapter = roomAdapter
    }

    private fun createNewRoom() {
        chessViewModel?.createNewRoom(name) { room ->
            goToPlayChess(room, true)
        }
    }

    private fun showConfirmCreateNewRoomDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.notice)
            .setMessage(getString(R.string.are_you_sure_you_want_to_create_new_room))
            .setPositiveButton(R.string.ok) { _, _ -> createNewRoom() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun goToPlayChess(room: Room, createRoom: Boolean) {
        view?.navigate(HomeFragmentDirections.actionHomeFragmentToPlayChessFragment(room, name, createRoom))
    }

    private val roomDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            binding.llNoRoomData.isVisible = roomAdapter.isEmptyData()
        }
    }
}

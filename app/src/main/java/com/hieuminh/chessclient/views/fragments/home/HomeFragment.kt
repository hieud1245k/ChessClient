package com.hieuminh.chessclient.views.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.common.extensions.ContextExtensions.isLandscape
import com.hieuminh.chessclient.common.extensions.ContextExtensions.isTablet
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.FragmentHomeBinding
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.viewmodels.ChessViewModel
import com.hieuminh.chessclient.views.adapters.RoomAdapter
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter
import com.hieuminh.chessclient.views.fragments.base.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding>(), BaseAdapter.ItemEventListener<Room> {
    private var name: String? = null
    private lateinit var chessViewModel: ChessViewModel
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
        getRoomList()
    }

    override fun onItemClick(item: Room, position: Int) {
        goToPlayChess(item)
    }

    override fun initListener() {
        binding.btPlayNow.setOnClickListener { }
        binding.btCreateNewRoom.setOnClickListener { showConfirmCreateNewRoomDialog() }
    }

    override fun initView() {
        chessViewModel = ViewModelProvider(this)[ChessViewModel::class.java]

        binding.tvTitle.text = String.format(resources.getString(R.string.hello_s), name)

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

    private fun getRoomList() {
        chessViewModel.fetchRoomList().observe(this) { roomList ->
            roomAdapter.updateData(roomList.toMutableList())
        }
    }

    private fun createNewRoom() {
        chessViewModel.createNewRoom().observe(this) { room ->
            goToPlayChess(room)
        }
    }

    private fun showConfirmCreateNewRoomDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.notice)
            .setMessage(getString(R.string.are_you_sure_you_want_to_create_new_room))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(R.string.ok) { _, _ -> createNewRoom() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun goToPlayChess(room: Room) {
        val action = HomeFragmentDirections.actionHomeFragmentToPlayChessFragment(room)
        view?.navController?.navigate(action)
    }

    private val roomDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            binding.llNoRoomData.isVisible = roomAdapter.isEmptyData()
        }
    }
}

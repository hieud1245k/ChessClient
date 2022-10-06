package com.hieuminh.chessclient.views.fragments.home

import androidx.recyclerview.widget.GridLayoutManager
import com.hieuminh.chessclient.common.extensions.ContextExtensions.isLandscape
import com.hieuminh.chessclient.common.extensions.ContextExtensions.isTablet
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.FragmentHomeBinding
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.views.adapters.RoomAdapter
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter
import com.hieuminh.chessclient.views.fragments.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(), BaseAdapter.ItemEventListener<Room> {
    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onItemClick(item: Room, position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToPlayChessFragment()
        view?.navController?.navigate(action)
    }

    override fun initListener() {
    }

    override fun initView() {
        val roomAdapter = RoomAdapter()
        roomAdapter.updateData(List(30) { Room() }.toMutableList())
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
}

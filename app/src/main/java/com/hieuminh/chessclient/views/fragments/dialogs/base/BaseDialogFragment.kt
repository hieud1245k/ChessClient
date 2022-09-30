package com.hieuminh.chessclient.views.fragments.dialogs.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.hieuminh.chessclient.interfaces.InitLayout

abstract class BaseDialogFragment<VBinding : ViewBinding> : DialogFragment(), InitLayout<VBinding> {
    protected lateinit var binding: VBinding
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return try {
            binding.root
        } catch (e: Exception) {
            e.printStackTrace()
            binding = getViewBinding()
            binding.root
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = getViewBinding()
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initView()
    }
}

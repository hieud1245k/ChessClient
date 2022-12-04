package com.hieuminh.chessclient.customs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import com.hieuminh.chessclient.common.constants.NumberConstants
import com.hieuminh.chessclient.customs.base.CustomLinerLayout
import com.hieuminh.chessclient.databinding.LayoutStartGameBinding

class StartGameCustomView(context: Context, attrs: AttributeSet) : CustomLinerLayout<LayoutStartGameBinding>(context, attrs) {
    private val levels = NumberConstants.levels.toList()

    val btStartGame: Button
        get() = binding.btStartGame

    val swSuggestions: SwitchCompat
        get() = binding.swSuggestion

    val spLevel: Spinner
        get() = binding.spLevel

    var isLevelVisible: Boolean
        set(value) {
            binding.llLevel.isVisible = value
        }
        get() = binding.llLevel.isVisible

    val level: Int
        get() = levels.getOrNull(binding.spLevel.selectedItemPosition) ?: 1

    override fun getViewBinding() = LayoutStartGameBinding.inflate(LayoutInflater.from(context), this, true)

    override fun initListener() {
        binding.swSuggestion.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                binding.llLevel.isVisible = isChecked
            }
        }
        binding.spLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.spLevel.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    override fun initView() {
        binding.spLevel.run {
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, levels)
            setSelection(1)
        }
    }
}

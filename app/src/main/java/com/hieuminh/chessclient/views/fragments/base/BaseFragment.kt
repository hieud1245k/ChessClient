package com.hieuminh.chessclient.views.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hieuminh.chessclient.interfaces.InitLayout
import com.hieuminh.chessclient.viewmodels.ChessViewModel
import com.hieuminh.chessclient.views.activities.ChessActivity
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.StompClient

abstract class BaseFragment<VBinding : ViewBinding> : Fragment(), InitLayout<VBinding> {
    private lateinit var subscribeList: MutableList<Disposable>

    protected val chessActivity: ChessActivity?
        get() = activity as? ChessActivity

    protected lateinit var binding: VBinding
        private set

    protected val chessViewModel: ChessViewModel?
        get() = chessActivity?.chessViewModel

    protected fun applySchedulers(): CompletableTransformer {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    protected fun subscribe(predicate: (StompClient) -> Disposable) {
        val disposable = chessActivity?.subscribe(predicate) ?: return
        subscribeList.add(disposable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeList = mutableListOf()
        initListener()
        initView()
    }

    override fun onDestroyView() {
        chessActivity?.clearListener(subscribeList)
        subscribeList.clear()
        super.onDestroyView()
    }

    protected fun toast(text: String) {
        chessActivity?.toast(text)
    }

    protected fun toast(id: Int) {
        toast(resources.getString(id))
    }
}

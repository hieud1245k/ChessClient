package com.hieuminh.chessclient.views.activities.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.hieuminh.chessclient.interfaces.InitLayout
import com.hieuminh.chessclient.viewmodels.ChessViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

abstract class BaseActivity<VBinding : ViewBinding> : AppCompatActivity(), InitLayout<VBinding> {
    private var stompClient: StompClient? = null

    private var compositeDisposable: CompositeDisposable? = null

    lateinit var binding: VBinding
        private set

    var chessViewModel: ChessViewModel? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initView()
        initListener()
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    fun clearListener(disposables: List<Disposable>) {
        disposables.forEach {
            compositeDisposable?.remove(it)
        }
    }

    fun connect(ipAddress: String, port: String, success: (StompClient) -> Unit) {
        if (stompClient?.isConnected == true) {
            success(stompClient!!)
            return
        }
        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://$ipAddress:$port/ws")
        this.stompClient = stompClient

        resetSubscriptions()

        val dispLifecycle = stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    null -> Unit
                    LifecycleEvent.Type.OPENED -> {
                        toast("Stomp connection opened")
                        chessViewModel = ViewModelProvider(this)[ChessViewModel::class.java]
                        success.invoke(stompClient)
                    }
                    LifecycleEvent.Type.ERROR -> {
                        toast("Stomp connection error")
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        toast("Stomp connection closed")
                        resetSubscriptions()
                        backToDestination()
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        toast("Stomp failed server heartbeat")
                    }
                }
            }

        compositeDisposable?.add(dispLifecycle)
        stompClient?.connect()
    }

    fun subscribe(disposable: Disposable) {
        compositeDisposable?.add(disposable)
    }

    fun subscribe(predicate: (StompClient) -> Disposable): Disposable? {
        val disposable = stompClient?.let { predicate.invoke(it) } ?: return null
        compositeDisposable?.add(disposable)
        return disposable
    }

    open fun backToDestination() = Unit
}

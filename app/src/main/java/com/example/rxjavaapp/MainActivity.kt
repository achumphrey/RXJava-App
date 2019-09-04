package com.example.rxjavaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var compositeDisposable = CompositeDisposable()
    lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numbersObservable: Observable<Int> = numbersObservable()
        val numbersObsever: Observer<Int> = numbersObserver()

        val animalsObserver = animalsObserver()
        val animalsObservable: Observable<String> = animalsObservable()


         tv_click_me.setOnClickListener {

            compositeDisposable.add(
            animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { animal -> animal.toLowerCase() }
                .subscribe{animalsObserver})

        }

       /* tv_click_me.setOnClickListener {

            numbersObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(numbersObsever)

        }*/
    }

    private fun animals(animal: String){

        Log.i(TAG, "animal: $animal")
    }

    private fun numbersObservable() = Observable.just(1, 2, 3, 4, 5)

    private fun animalsObservable() = Observable.just("Dog", "Cat", "Horse", "Bat", "Bee")

    private fun animalsObserver(): Observer<String>{

        return object : Observer<String>{

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

                compositeDisposable.add(d)

            }

            override fun onNext(t: String) {
                Log.i(TAG, "animal: $t")
            }

            override fun onError(e: Throwable) {

            }

            private fun randomObserver(): Observer<String>{

                return object : Observer<String>{

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                        compositeDisposable.add(d)



                    }

                    override fun onNext(t: String) {

                    }

                    override fun onError(e: Throwable) {

                    }
                }
            }

        }
    }

    private fun numbersObserver(): Observer<Int>{

        return object : Observer<Int>{
            override fun onComplete() {
                //called when all data have been emitted

                Log.i(TAG, "All item emitted")
            }

            override fun onSubscribe(d: Disposable) {
                // register to observe
                disposable = d
                Log.i(TAG, "onSubcribe")
            }

            override fun onNext(t: Int) {
              // next data to be emitted
                Log.i(TAG, " $t")
            }

            override fun onError(e: Throwable) {
               // for error messages
                Log.i(TAG, "something went wrong")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        compositeDisposable.clear()
    }

    companion object{
        const val TAG = "MainActivity"
    }
}

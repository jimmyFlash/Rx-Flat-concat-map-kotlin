package com.jimmy.rx_flat_concat_map.utilities.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by jamal.safwat on 3/2/2018.
 */

/**
 * Now we can add Disposable object to CompositeDisposable instance using += sign
 */

// operator defines a method that uses the += (plus assign) operator or other operator signb on extended class
operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}
package com.interview.mishi.pay.paymentapp.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.interview.mishi.pay.paymentapp.data.remote.CartRepositoryImpl
import com.interview.mishi.pay.paymentapp.data.remote.OrdersRepositoryImpl
import com.interview.mishi.pay.paymentapp.domain.repository.CartRepository
import com.interview.mishi.pay.paymentapp.domain.repository.OrdersRepository
import com.interview.mishi.pay.paymentapp.utils.CART
import com.interview.mishi.pay.paymentapp.utils.ORDERS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideCartRepository(): CartRepository {
        return CartRepositoryImpl(Firebase.firestore.collection(CART))
    }

    @Provides
    fun provideOrdersRepository(): OrdersRepository {
        return OrdersRepositoryImpl(
            Firebase.firestore.collection(ORDERS),
            Firebase.firestore.collection(CART)
        )
    }
}
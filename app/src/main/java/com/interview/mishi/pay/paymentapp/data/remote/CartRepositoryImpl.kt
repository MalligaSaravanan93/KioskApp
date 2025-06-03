package com.interview.mishi.pay.paymentapp.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.repository.CartRepository
import com.interview.mishi.pay.paymentapp.utils.AppError
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartReference: CollectionReference
) : CartRepository {
    override fun getCartItems(): Flow<ResponseState<List<CartItem>>> = callbackFlow {
        var productList = mutableListOf<CartItem>()
        val listener = cartReference.orderBy("updatedTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                try {
                    if (snapshots != null) {
                        if (snapshots.isEmpty) productList.clear()
                        for (doc in snapshots.documentChanges) {
                            val item = doc.document.toObject(CartItem::class.java)
                            when (doc.type) {
                                DocumentChange.Type.ADDED -> productList.add(item)
                                DocumentChange.Type.MODIFIED -> {
                                    val index = productList.indexOfFirst { it.id == item.id }
                                    if (index != -1) {
                                        productList[index] = item
                                    } else {
                                        productList.add(item) // Add if missing
                                    }
                                }

                                DocumentChange.Type.REMOVED -> {
                                    productList = mutableListOf()
                                }
                            }
                        }
                    }
                    trySend(ResponseState.Success(productList))
                } catch (e: Exception) {
                    e.printStackTrace()
                    trySend(ResponseState.Error())
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addCartItem(item: CartItem): Flow<ResponseState<Unit>> = flow {
        try {
            cartReference.document(item.id.toString()).set(item).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            emit(
                ResponseState.Error(
                    AppError.CustomError(
                        e.localizedMessage ?: "Error adding cart item"
                    )
                )
            )
        }
    }

    override suspend fun updateCartItem(item: CartItem): Flow<ResponseState<Unit>> = flow {
        try {
            cartReference.document(item.id.toString()).update(
                mapOf(
                    "quantity" to item.quantity,
                    "updatedTime" to FieldValue.serverTimestamp()
                )
            ).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            emit(
                ResponseState.Error(
                    AppError.CustomError(
                        e.localizedMessage ?: "Error updating cart item"
                    )
                )
            )
        }
    }
}
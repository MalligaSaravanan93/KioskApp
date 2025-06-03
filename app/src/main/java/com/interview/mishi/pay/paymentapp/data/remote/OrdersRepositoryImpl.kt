package com.interview.mishi.pay.paymentapp.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.domain.repository.OrdersRepository
import com.interview.mishi.pay.paymentapp.utils.AppError
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import com.interview.mishi.pay.paymentapp.utils.value
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val ordersReference: CollectionReference,
    private val cartReference: CollectionReference
) : OrdersRepository {
    override fun getOrders(): Flow<ResponseState<List<OrderItem>>> = callbackFlow {
        val ordersList = mutableListOf<OrderItem>()
        val listener = ordersReference.orderBy("createdTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                try {
                    if (snapshots != null) {
                        if (snapshots.isEmpty) ordersList.clear()
                        for (dc in snapshots.documentChanges) {
                            val item = dc.document.toObject(OrderItem::class.java)
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> ordersList.add(item)
                                DocumentChange.Type.MODIFIED -> {
                                    val index =
                                        ordersList.indexOfFirst { it.invoiceNo == item.invoiceNo }
                                    if (index != -1) {
                                        ordersList[index] = item
                                    } else {
                                        ordersList.add(item) // Add if missing
                                    }
                                }

                                DocumentChange.Type.REMOVED -> {
                                    ordersList.removeAll { it.invoiceNo == item.invoiceNo }
                                }
                            }
                        }
                    }
                    trySend(ResponseState.Success(ordersList))
                } catch (e: Exception) {
                    e.printStackTrace()
                    trySend(
                        ResponseState.Error(
                            AppError.CustomError(
                                e.localizedMessage ?: "Unknown Error"
                            )
                        )
                    )
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    //    override suspend fun createOrder(item: OrderItem): Flow<ResponseState<Unit>> = flow {
//        try {
//            ordersReference.document(item.invoiceNo.toString()).set(item).await()
//            item.itemsList?.forEach {
//                cartReference.document(it.id.value().toString()).delete().await()
//            }
//            emit(ResponseState.Success(Unit))
//        } catch (e: Exception) {
//            emit(ResponseState.Error())
//        }
//    }
    override suspend fun createOrder(item: OrderItem): Flow<ResponseState<Unit>> = flow {
        try {
            val batch = ordersReference.firestore.batch()

            // Add the order
            val orderDocRef = ordersReference.document(item.invoiceNo.toString())
            batch.set(orderDocRef, item)

            // Remove items from cart
            item.itemsList?.forEach {
                val cartDocRef = cartReference.document(it.id.value().toString())
                batch.delete(cartDocRef)
            }

            batch.commit().await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            emit(
                ResponseState.Error(
                    AppError.CustomError(
                        e.localizedMessage ?: "Error creating order"
                    )
                )
            )
        }
    }
}
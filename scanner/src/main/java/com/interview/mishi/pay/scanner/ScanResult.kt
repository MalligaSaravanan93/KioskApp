package com.interview.mishi.pay.scanner

sealed class ScanResult {
    data class Success(val product: Product): ScanResult()
    data class Error(val message: String): ScanResult()
}
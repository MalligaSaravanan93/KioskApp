package com.interview.mishi.pay.paymentapp.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object InvoiceUtils {

    // Generate a unique alphanumeric invoice number
    fun generateInvoiceNumber(prefix: String = "INV", suffix: String = ""): String {
        // Get the current timestamp
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))

        // Generate a random alphanumeric string
        val randomString = generateRandomAlphanumeric()

        // Combine prefix, timestamp, random string, and suffix
        return "$prefix-$timestamp-$randomString$suffix"
    }

    // Generate a random alphanumeric string of a given length
    private fun generateRandomAlphanumeric(length: Int = 6): String {
        val alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { alphanumeric[Random.nextInt(alphanumeric.length)] }
            .joinToString("")
    }
}
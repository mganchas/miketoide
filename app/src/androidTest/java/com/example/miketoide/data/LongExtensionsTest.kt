package com.example.miketoide.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.miketoide.data.extensions.toHumanDownloadSizeFormat
import com.example.miketoide.data.extensions.toHumanDownloadedTimesFormat
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LongExtensionsTest
{
    @Test
    fun toHumanDownloadedTimesFormat_receivesZero_returnsHyphen() {
        val dummyValue : Long = 0
        val convertedValue = dummyValue.toHumanDownloadedTimesFormat()
        Assert.assertEquals("-", convertedValue)
    }

    @Test
    fun toHumanDownloadedTimesFormat_receivesLessThanHundred_returnsLessThanHundred() {
        val dummyValue : Long = 50
        val convertedValue = dummyValue.toHumanDownloadedTimesFormat()
        Assert.assertEquals("< 100", convertedValue)
    }

    @Test
    fun toHumanDownloadedTimesFormat_receivesBetweenHundredAndThousand_returnsBetweenHundredAndThousand() {
        val dummyValue : Long = 500
        val convertedValue = dummyValue.toHumanDownloadedTimesFormat()
        Assert.assertEquals("100 - 1000", convertedValue)
    }

    @Test
    fun toHumanDownloadedTimesFormat_receivesBetweenThousandAndMillion_returnsK() {
        val dummyValue : Long = 500000
        val convertedValue = dummyValue.toHumanDownloadedTimesFormat()
        Assert.assertEquals("500 k", convertedValue)
    }

    @Test
    fun toHumanDownloadedTimesFormat_receivesMoreThanMillion_returnsM() {
        val dummyValue : Long = 5000000
        val convertedValue = dummyValue.toHumanDownloadedTimesFormat()
        Assert.assertEquals("5 M", convertedValue)
    }

    @Test
    fun toHumanDownloadSizeFormat_receivesOneByte_returnsB() {
        val dummyValue : Long = 1
        val convertedValue = dummyValue.toHumanDownloadSizeFormat()
        Assert.assertEquals("1 B", convertedValue)
    }

    @Test
    fun toHumanDownloadSizeFormat_receivesOneKilobyte_returnsK() {
        val dummyValue : Long = 1024
        val convertedValue = dummyValue.toHumanDownloadSizeFormat()
        Assert.assertEquals("1.0 KB", convertedValue)
    }

    @Test
    fun toHumanDownloadSizeFormat_receivesOneMegabyte_returnsM() {
        val dummyValue : Long = 1048576
        val convertedValue = dummyValue.toHumanDownloadSizeFormat()
        Assert.assertEquals("1.00 MB", convertedValue)
    }

    @Test
    fun toHumanDownloadSizeFormat_receivesOneGigabyte_returnsG() {
        val dummyValue : Long = 1073741824
        val convertedValue = dummyValue.toHumanDownloadSizeFormat()
        Assert.assertEquals("1.00 GB", convertedValue)
    }
}
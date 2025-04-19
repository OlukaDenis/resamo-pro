package com.dennytech.data.impl

import com.dennytech.data.utils.extractErrorBody
import com.dennytech.data.utils.resolveError
import com.dennytech.domain.models.Error
import com.dennytech.domain.models.ErrorBody
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UtilRepositoryImplTest {

    private lateinit var utilRepository: UtilRepositoryImpl

    @Before
    fun setup() {
        utilRepository = UtilRepositoryImpl()
    }

    @Test
    fun `getErrorBody should return resolved error message`() {
        // Given
        val throwable = Throwable()

        // When
        val result = utilRepository.getErrorBody(throwable)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.message).isEqualTo("Error occurred")
    }

    @Test
    fun `currentDate should return formatted date string`() {
        // Given
        val expectedFormat = "yyyy-MM-dd'T'hh:mm:ss"

        // When
        val result = utilRepository.currentDate()

        // Then
        val dateFormat = SimpleDateFormat(expectedFormat, Locale.getDefault())
        val parsedDate = dateFormat.parse(result)
        assertThat(parsedDate).isNotNull()
    }
} 
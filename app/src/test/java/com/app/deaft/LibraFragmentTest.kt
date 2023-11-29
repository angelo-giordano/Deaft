package com.app.deaft

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class LibraFragmentTest {

    private lateinit var libraFragment: LibraFragment
    private lateinit var mockDatabaseReference: DatabaseReference

    @Before
    fun setup() {
        libraFragment = LibraFragment()
        mockDatabaseReference = mock(DatabaseReference::class.java)

        // Certifique-se de que o fragmento use o mockDatabaseReference
        libraFragment.databaseReference = mockDatabaseReference

    }

    @Test
    fun testGetGifUrlById_NodeExists() {
        // Configurar o mock para simular a resposta de getGifUrlById
        `when`(mockDatabaseReference.child(anyString())).thenAnswer {
            val gifId = it.arguments[0] as String
            val mockGifNodeReference = mock(DatabaseReference::class.java)
            `when`(mockGifNodeReference.addValueEventListener(any())).thenAnswer {
                val listener = it.arguments[0] as ValueEventListener
                val mockDataSnapshot = mock(DataSnapshot::class.java)
                `when`(mockDataSnapshot.exists()).thenReturn(true)

                // Configurar valor para um gifId específico
                when (gifId) {
                    "oi" -> `when`(mockDataSnapshot.value).thenReturn("https://firebasestorage.googleapis.com/v0/b/deaft-864a2.appspot.com/o/gifs_libras%2Foi.gif?alt=media&token=a4479a6b-b535-4df9-ab34-5ed1ef84fc51")
                    // Adicionar outros casos conforme necessário
                }

                listener.onDataChange(mockDataSnapshot)
                null
            }
            mockGifNodeReference
        }

        // Chamar o método que utiliza getGifUrlById
        val gifUrlFuture = libraFragment.getGifUrlById("oi")

        // Obter o resultado (isso bloqueará até que o resultado esteja disponível)
        val result = gifUrlFuture.get()

        // Verificar se o resultado é o esperado
        assertEquals("https://firebasestorage.googleapis.com/v0/b/deaft-864a2.appspot.com/o/gifs_libras%2Foi.gif?alt=media&token=a4479a6b-b535-4df9-ab34-5ed1ef84fc51", result)
    }

    @Test
    fun testGetGifUrlById_NodeDoesNotExist() {
        // Configurar o mock para simular a resposta de getGifUrlById
        `when`(mockDatabaseReference.child(anyString())).thenAnswer {
            val mockGifNodeReference = mock(DatabaseReference::class.java)
            `when`(mockGifNodeReference.addValueEventListener(any())).thenAnswer {
                val listener = it.arguments[0] as ValueEventListener
                val mockDataSnapshot = mock(DataSnapshot::class.java)
                `when`(mockDataSnapshot.exists()).thenReturn(false)

                listener.onDataChange(mockDataSnapshot)
                null
            }
            mockGifNodeReference
        }

        // Chamar o método que utiliza getGifUrlById
        val gifUrlFuture = libraFragment.getGifUrlById("inexistente")

        // Obter o resultado (isso bloqueará até que o resultado esteja disponível)
        val result = gifUrlFuture.get()

        // Verificar se o resultado é nulo, indicando que o nó não existe no banco de dados
        assertNull(result)
    }
}
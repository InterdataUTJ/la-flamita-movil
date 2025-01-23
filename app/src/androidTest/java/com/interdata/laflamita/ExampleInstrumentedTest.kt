package com.interdata.laflamita

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.interdata.laflamita.controller.AuthController

import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var ctx: Context
    private lateinit var authController: AuthController
    private var timeout: Long = 10

    @Before
    fun setup() {
        ctx = InstrumentationRegistry.getInstrumentation().targetContext
        authController = AuthController(ctx)
    }

    @Test
    fun loginCorrecto() {
        val latch = CountDownLatch(1)
        var resultStatus = false

        authController.login("ismacortgtz@gmail.com", "0123456789") { success, _ ->
            resultStatus = success
            latch.countDown()
        }

        val completed = latch.await(timeout, TimeUnit.SECONDS)
        assertTrue(completed)
        assertTrue(resultStatus)
    }

    @Test
    fun loginIncorrecto1() {
        val latch = CountDownLatch(1)
        var resultStatus = false

        authController.login("alberto.falso@gmail.com", "0123456789") { success, _ ->
            resultStatus = success
            latch.countDown()
        }

        val completed = latch.await(timeout, TimeUnit.SECONDS)
        assertTrue(completed)
        assertFalse(resultStatus)
    }

    @Test
    fun loginIncorrecto2() {
        val latch = CountDownLatch(1)
        var resultStatus = false

        authController.login("email.falso@gmail.com", "ContraseñaRandom.@") { success, _ ->
            resultStatus = success
            latch.countDown()
        }

        val completed = latch.await(timeout, TimeUnit.SECONDS)
        assertTrue(completed)
        assertFalse(resultStatus)
    }

    @Test
    fun registrarCorrecto() {
        val latch = CountDownLatch(1)
        var resultStatus = false

        authController.singup("Alberto", "López", "alberto@gmail.com", "0123456789", "0123456789") { success, _ ->
            resultStatus = success
            latch.countDown()
        }

        val completed = latch.await(timeout, TimeUnit.SECONDS)
        assertTrue(completed)
        assertTrue(resultStatus)
    }

    @Test
    fun registrarDuplicado() {
        val latch = CountDownLatch(1)
        var resultStatus = false

        authController.singup("Alberto", "López", "alberto2@gmail.com", "0123456789", "0123456789") { success, _ ->
            resultStatus = success
            latch.countDown()
        }

        val completed = latch.await(timeout, TimeUnit.SECONDS)
        assertTrue(completed)
        assertFalse(resultStatus)
    }

    @Test
    fun registrarIncorrecto() {
        val latch = CountDownLatch(1)
        var resultStatus = false

        val nombre = "Alberto Este nombre es extremadamente pero super largo, tan largo que debe de fallar la validación"
        val clave = "01" // Minimo 8 caracteres

        authController.singup(nombre, "López", "caso.incorrecto@gmail.com", clave, "0123456789") { success, _ ->
            resultStatus = success
            latch.countDown()
        }

        val completed = latch.await(timeout, TimeUnit.SECONDS)
        assertTrue(completed)
        assertFalse(resultStatus)
    }




    @Test
    fun tokenGuardado() {
        val latch = CountDownLatch(2)
        var resultStatus = false

        authController.login("ismacortgtz@gmail.com", "0123456789") { success, _ ->
            latch.countDown()
            if (success) {
                authController.isLoggedIn { isLoggedIn ->
                    resultStatus = isLoggedIn
                    latch.countDown()
                }
            }
        }

        val completed = latch.await(timeout * 2, TimeUnit.SECONDS)
        assertTrue(completed)
        assertTrue(resultStatus)
    }
}
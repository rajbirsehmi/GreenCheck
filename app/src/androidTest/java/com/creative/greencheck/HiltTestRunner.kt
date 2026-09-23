package com.creative.greencheck

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        bypassHiddenApiRestrictions()
        super.onCreate(arguments)
    }

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    private fun bypassHiddenApiRestrictions() {
        try {
            val forName = Class::class.java.getDeclaredMethod("forName", String::class.java)
            val vmRuntimeClass = forName.invoke(null, "dalvik.system.VMRuntime") as Class<*>
            val getRuntime = vmRuntimeClass.getDeclaredMethod("getRuntime")
            val setHiddenApiExemptions = vmRuntimeClass.getDeclaredMethod(
                "setHiddenApiExemptions",
                Array<String>::class.java
            )
            val vmRuntime = getRuntime.invoke(null)
            setHiddenApiExemptions.invoke(vmRuntime, arrayOf("L"))
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

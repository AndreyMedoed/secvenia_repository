package com.example.networking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHost.navController.addOnDestinationChangedListener(destinationListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val destinationListener =
        NavController.OnDestinationChangedListener { _, destination, arguments ->
            supportActionBar?.title = prepareTitle(destination.label, arguments)
            supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
        }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestination = graph.startDestinationId
        return startDestination == destination.id
    }

    private fun prepareTitle(label: CharSequence?, arguments: Bundle?): String {

        // Этот кусок из гугл источника
        if (label == null) return ""
        val title = StringBuffer()
        val fillInPattern = Pattern.compile("\\{(.+?)\\}")
        val matcher = fillInPattern.matcher(label)
        while (matcher.find()) {
            val argName = matcher.group(1)
            if (arguments != null && arguments.containsKey(argName)) {
                matcher.appendReplacement(title, "")
                title.append(arguments[argName].toString())
            } else {
                throw IllegalArgumentException(
                    "Could not find $argName in $arguments to fill label $label"
                )
            }
        }
        matcher.appendTail(title)
        return title.toString()
    }

}
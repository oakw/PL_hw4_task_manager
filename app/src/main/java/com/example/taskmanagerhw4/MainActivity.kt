package com.example.taskmanagerhw4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerhw4.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
//    private lateinit var taskList: Array<TaskItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        // use arrayadapter and define an array
//        val arrayAdapter: ArrayAdapter<*>
//        val users = arrayOf(
//            "Virat Kohli", "Rohit Sharma", "Steve Smith",
//            "Kane Williamson", "Ross Taylor"
//        )
//        taskList = savedInstanceState?.getStringArrayList("TaskList")?.map { taskJson -> TaskItem.fromJson(taskJson) }
//            ?.toTypedArray() ?: arrayOf(
//            TaskItem("Sample Title 1", "A description of choice", LocalDate.now(), 2),
//            TaskItem("Sample Title 2", "Op", LocalDate.now().minusDays(3), 0),
//            TaskItem("Sample Title 3", "Do this task on your own pace and submit to the professor", LocalDate.now(), 1),
//        )

//        val intent = Intent(this@MainActivity, SecondFragment::class.java)
        // access the listView from xml file
//        var mListView = findViewById<ListView>(R.id.taskList)
//        arrayAdapter = ArrayAdapter(this,
//            R.layout.task_item, R.id.taskItemTitle, users)
//        mListView.adapter = arrayAdapter
//        val taskListStore = TaskListStore(context = this.baseContext)
//        val customAdapter = CustomAdapter(taskListStore.getAllTasks())
//
//        val recyclerView: RecyclerView = findViewById(R.id.taskList)
//        recyclerView.adapter = customAdapter


//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//        binding.fab.setOnClickListener { view ->
//            navController.navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }
    override fun onStart() {
        super.onStart()

    }

//    fun createNewTask(task: TaskItem) {
//        taskList += task
//    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        outState.putStringArrayList("TaskList", ArrayList(taskList.map { task -> task.toString() }))
//        super.onSaveInstanceState(outState)
//    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
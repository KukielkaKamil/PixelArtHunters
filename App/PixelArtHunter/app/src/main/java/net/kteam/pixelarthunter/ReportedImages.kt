package net.kteam.pixelarthunter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReportedImages : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reported_images)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerReportedView)
        recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = ReportedPoiAdapter(this)
            adapter.fetchDataAndInitializeAdapter()
            recyclerView.adapter = adapter
    }
}
package net.kteam.pixelarthunter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class PoiAdapter(context: Context, poiId: Int): RecyclerView.Adapter<MyViewHolder>() {

    private val apiHandler = ApiRequestQueue.getInstance(context)
    private val poiId = poiId
    private val loadingDialog = LoadinDialog(context)
    private val con = context
    private var size = 0
    private var artIds:ArrayList<Int> = ArrayList()
    private var jsonResponse: JSONArray = JSONArray()

    fun fetchDataAndInitializeAdapter() {
        val url = "${con.getString(R.string.url_base)}poi/$poiId/art"
        val poiImages = object : JsonArrayRequest(
            Method.GET, url, null, { response ->
                size = response.length()
                jsonResponse = response
                notifyDataSetChanged() // Notify the adapter that data has changed
            }, { error ->
                val message = if (error.hashCode() == 401) {
                    error.message.toString()
                } else {
                    error.toString()
                }
                Toast.makeText(con, "An error occurred: $message", Toast.LENGTH_LONG).show()
            }
        ) {override fun getHeaders(): MutableMap<String, String> {
            val token = apiHandler.getToken(con)
            val headers = HashMap<String, String>()
            headers["Authorization"] = "Bearer $token"
            return headers
        }}
        apiHandler.addToRequestQueue(poiImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        loadingDialog.showLoadingDialog()
        val layoutInflater = LayoutInflater.from(parent.context)
        val image = layoutInflater.inflate(R.layout.single_poi_image,parent,false)
        return MyViewHolder(image)
    }

    override fun getItemCount(): Int {
        return jsonResponse.length()
//        return 2
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position >= 0 && position < jsonResponse.length()) {
            val grid = holder.itemView.findViewById<DisplayGridView>(R.id.displaySingleImage)
            val authorText = holder.itemView.findViewById<TextView>(R.id.authorTextView)

            val responseImage = jsonResponse.getJSONObject(position)
            artIds.add(responseImage.getInt("id"))
            val image = responseImage.getString("image")
            val size = responseImage.getInt("size")
            val authorName = "Author: ${responseImage.getJSONObject("user").getString("name")}"
            authorText.text = authorName
            authorText.setOnClickListener {
                val userProfileIntent = Intent(con,UserProfileMenu::class.java)
                userProfileIntent.putExtra("id",responseImage.getJSONObject("user").getInt("id"))
                con.startActivity(userProfileIntent)
            }
            grid.setGridSize(size, size)
            grid.loadImage(Art.binStringToDecArray(image, size))

            if (position == itemCount - 1) {
                loadingDialog.hideLoadingDialog()
            }

            holder.itemView.findViewById<ImageButton>(R.id.okButton).setOnClickListener {
                react(position,1)
            }
            holder.itemView.findViewById<ImageButton>(R.id.noButton).setOnClickListener {
                react(position,0)
            }
        }

    }

    private fun react(position: Int, reaction:Int){
        val id = artIds[position]
        val att = JSONObject()
        att.put("liked",reaction)
        val url = "http://192.168.50.8:8000/art/$id/react"
        val reactRequest = object: JsonObjectRequest(
            Method.POST, url,att,{},{
                    error ->
                val message = if (error.hashCode() == 401) {
                    error.message.toString()
                } else {
                    error.toString()
                }
                Toast.makeText(con, "An error occurred: $message", Toast.LENGTH_LONG).show()
            }
        ){}
    }
}

class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
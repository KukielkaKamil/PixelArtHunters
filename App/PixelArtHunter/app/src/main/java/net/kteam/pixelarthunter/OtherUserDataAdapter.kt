package net.kteam.pixelarthunter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class OtherUserDataAdapter(context: Context,id: Int,usernameDP:TextView,descDP:TextView): RecyclerView.Adapter<MyViewHolder>() {

    private val apiHandler = ApiRequestQueue.getInstance(context)
    private lateinit var loadingDialog :LoadinDialog
    private val con = context
    private var size = 0
    var jsonResponse: JSONArray = JSONArray()
    var userJson: JSONObject = JSONObject()
    private var id = id
    var usernameDP = usernameDP
    val descDP = descDP

    fun fetchDataAndInitializeAdapter() {
        loadingDialog = LoadinDialog(con)
        val url = "${con.getString(R.string.url_base)}user/$id"
        val poiImages = object : JsonObjectRequest(
            Method.GET, url, null, { response ->
                userJson = response
                jsonResponse = response.getJSONArray("art")
                size = jsonResponse.length()

                usernameDP.text = userJson.getString("name")
        descDP.text = userJson.getString("description")

                notifyDataSetChanged() // Notify the adapter that data has changed
            }, { error ->
                loadingDialog.hideLoadingDialog()
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
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position >= 0 && position < jsonResponse.length()) {
            val grid = holder.itemView.findViewById<DisplayGridView>(R.id.displaySingleImage)
            val authorText = holder.itemView.findViewById<TextView>(R.id.authorTextView)
            authorText.text = ""
            val reactionBTS = holder.itemView.findViewById<LinearLayout>(R.id.reactionButtons)
            reactionBTS.visibility = View.GONE

            val responseImage = jsonResponse.getJSONObject(position)
            val image = responseImage.getString("image")
            val size = responseImage.getInt("size")
            grid.setGridSize(size, size)
            grid.loadImage(Art.binStringToDecArray(image, size))

            if (position == itemCount - 1) {
                loadingDialog.hideLoadingDialog()
            }

        }

    }

}
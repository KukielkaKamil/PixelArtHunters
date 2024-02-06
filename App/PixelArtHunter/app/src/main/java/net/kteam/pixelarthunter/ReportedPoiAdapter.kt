package net.kteam.pixelarthunter

import android.content.Context
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

class ReportedPoiAdapter(context: Context): RecyclerView.Adapter<MyViewHolder>() {

    private val apiHandler = ApiRequestQueue.getInstance(context)
    private val loadingDialog = LoadinDialog(context)
    private val con = context
    private var length = 0
    private var jsonResponse: JSONArray = JSONArray()

    fun fetchDataAndInitializeAdapter() {
        val url = "${con.getString(R.string.url_base)}art/reported"
        val poiImages = object: JsonArrayRequest(
            Method.GET,url,null,{response ->
                length = response.length()
                jsonResponse = response
                notifyDataSetChanged()

            },{error ->
                val message = if (error.hashCode() == 401){
                    error.message.toString()
                } else{
                    error.toString()
                }
                Toast.makeText(con, "An error occurred: $message", Toast.LENGTH_LONG).show()
            }
        ){}
        apiHandler.addToRequestQueue(poiImages)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val image = layoutInflater.inflate(R.layout.single_poi_image,parent,false)
        loadingDialog.showLoadingDialog()
        return MyViewHolder(image)
    }

    override fun getItemCount(): Int {
//        return apiHandler.poiList.size
        return length
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val grid = holder.itemView.findViewById<DisplayGridView>(R.id.displaySingleImage)
        val authorText = holder.itemView.findViewById<TextView>(R.id.authorTextView)
        val responseImage = jsonResponse.getJSONObject(position)
        val imageId = responseImage.getInt("id")
        val image = responseImage.getString("image")
        val size = responseImage.getInt("size")
        val authorName = "Author: ${responseImage.getJSONObject("user").getString("name")}"
        authorText.text = authorName
        grid.setGridSize(size,size)
        grid.loadImage(Art.binStringToDecArray(image,size))
        if(position >= itemCount-1){
            loadingDialog.hideLoadingDialog()
        }

        val acceptButton = holder.itemView.findViewById<ImageButton>(R.id.okButton)
        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.noButton)
        val unbanedImage = responseImage
        unbanedImage.put("reported",0)
        val acceptUrl = "${con.getString(R.string.url_base)}art/$imageId"
        val acceptRequest = object: JsonObjectRequest(
            Method.PATCH,acceptUrl,responseImage,
            {_ ->
                Toast.makeText(con, "Successful Registration", Toast.LENGTH_LONG).show()
            },{error ->
                val message = if (error.hashCode() == 401){
                    error.message.toString()
                } else{
                    error.toString()
                }
                Toast.makeText(con, "An error occurred: $message", Toast.LENGTH_LONG).show()
            }
        ){}

        acceptButton.setOnClickListener{
            apiHandler.addToRequestQueue(acceptRequest)
            deleteButton.visibility=View.GONE
            acceptButton.visibility=View.GONE
        }


        val deleteRequest = object: JsonObjectRequest(
            Method.DELETE,acceptUrl,responseImage,
            {_ ->
                Toast.makeText(con, "Successful Registration", Toast.LENGTH_LONG).show()
            },{error ->
                val message = if (error.hashCode() == 401){
                    error.message.toString()
                } else{
                    error.toString()
                }
                Toast.makeText(con, "An error occurred: $message", Toast.LENGTH_LONG).show()
            }
        ){}

        deleteButton.setOnClickListener{
            apiHandler.addToRequestQueue(deleteRequest)
            deleteButton.visibility=View.GONE
            acceptButton.visibility=View.GONE
        }
    }
}

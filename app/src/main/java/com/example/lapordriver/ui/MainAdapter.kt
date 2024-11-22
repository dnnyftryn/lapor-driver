package com.example.lapordriver.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lapordriver.App.Companion.context
import com.example.lapordriver.R
import com.example.lapordriver.api.responses.ListAllVehicleResponseItem
import com.example.lapordriver.databinding.ItemComplaintBinding
import com.example.lapordriver.utils.Helper

class MainAdapter(private val data : List<ListAllVehicleResponseItem?>?) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemComplaintBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(data : ListAllVehicleResponseItem) {
                val reportId = data.reportId
                val vehicleName = data.vehicleName
                val reportBy = data.createdBy
                val note = data.note
                val platNumber = data.vehicleLicenseNumber
                val status = data.reportStatus
                val url = data.photo

                val date = data.createdAt
                val dateFormatter = Helper.formatDate(date.toString())
                binding.tvDate.text = dateFormatter // Output: Kamis, 21 Nov - 14:39

                // Example of status checking
                if (status == "Tidak Terkirim") {
                    binding.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_red))
                    binding.tvStatus.text = "Tidak Terkirim"
                } else if (status == "Terkirim") {
                    binding.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_green))
                    binding.tvStatus.text = "Terkirim"
                }

                binding.tvReportId.text = reportId
                binding.tvVehicleName.text = vehicleName
                binding.tvReportedBy.text = reportBy
                binding.tvNotes.text = note
                binding.tvLicensePlate.text = platNumber
                binding.tvStatus.text = status

                Glide
                    .with(itemView.context)
                    .load(url)
                    .into(binding.ivNoteImage)
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemComplaintBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data!![position]!!)
    }

    override fun getItemCount(): Int = data!!.size
}
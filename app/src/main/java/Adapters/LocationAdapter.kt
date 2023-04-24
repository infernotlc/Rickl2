package Adapters

import DataClass.Location
import LocationViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickl.databinding.LocationViewHolderBinding

class LocationAdapter(
    private val onLocationClicked: OnLocationClickListener
) : RecyclerView.Adapter<LocationViewHolder>() {

    private val locations: MutableList<Location> = mutableListOf()
    private var selectedLocation: Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = LocationViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location, object : OnLocationClickListener {
            override fun onLocationClicked(location: Location) {
                setSelectedLocation(location)
                onLocationClicked(location)
            }
        })
    }

    override fun getItemCount(): Int = locations.size

    fun setItems(locations: List<Location>) {
        this.locations.clear()
        this.locations.addAll(locations)
        notifyDataSetChanged()
    }

    fun addItems(locations: List<Location>) {
        val oldSize = this.locations.size
        this.locations.addAll(locations)
        notifyItemRangeInserted(oldSize, locations.size)
    }

   public fun setSelectedLocation(location: Location) {
        selectedLocation = location
        notifyDataSetChanged()
    }

    fun getSelectedLocation(): Location? {
        return selectedLocation
    }
}

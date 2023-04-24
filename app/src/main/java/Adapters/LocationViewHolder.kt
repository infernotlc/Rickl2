
import Adapters.OnLocationClickListener
import DataClass.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickl.databinding.LocationViewHolderBinding

class LocationViewHolder(private val binding: LocationViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(location: Location, onLocationClicked: OnLocationClickListener) {
        binding.tvLocationName.text = location.name
        binding.tvLocationType.text = location.type
        binding.tvLocationDimension.text = location.dimension
        binding.root.setOnClickListener {
            onLocationClicked.onLocationClicked(location)
        }
    }

    companion object {
        fun create(parent: ViewGroup): LocationViewHolder {
            val binding =LocationViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return LocationViewHolder(binding)
        }
    }
}

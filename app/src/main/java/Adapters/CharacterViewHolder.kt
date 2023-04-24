package Adapters

import DataClass.Characters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickl.databinding.ItemCharacterBinding

class CharacterViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(character: Characters, onCharacterClicked: (Characters) -> Unit) {
        binding.tvName.text = character.name
        binding.tvGender.text = character.gender
        binding.tvSpecies.text = character.species
        binding.tvStatus.text = character.status

        Glide.with(itemView.context)
            .load(character.image)
            .into(binding.ivlCharacter)

        itemView.setOnClickListener {
            onCharacterClicked(character)
        }
    }

    companion object {
        fun create(parent: ViewGroup): CharacterViewHolder {
            val binding = ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CharacterViewHolder(binding)
        }
    }
}

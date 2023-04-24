package Fragments

import CharacterViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.rickl.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterDetailBinding
    private lateinit var characterViewModel: CharacterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val characterId = it.getInt(ARG_CHARACTER_ID)
            characterViewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)
            characterViewModel.getCharacter(characterId).observe(viewLifecycleOwner, Observer { character ->
                character?.let {
                    binding.tvDname.text = it.name
                    binding.tvDstatus.text = it.status
                    binding.tvDspecies.text = it.species
                    binding.tvDgender.text = it.gender

                    Glide.with(this)
                        .load(it.image)
                        .into(binding.ivCharacter)
                }
            })
        }
    }

    companion object {
        private const val ARG_CHARACTER_ID = "characterId"

        @JvmStatic
        fun newInstance(characterId: Int) =
            CharacterDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CHARACTER_ID, characterId)
                }
            }
    }
}

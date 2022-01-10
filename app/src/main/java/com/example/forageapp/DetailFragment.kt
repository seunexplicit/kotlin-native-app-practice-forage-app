package com.example.forageapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.forageapp.databinding.FragmentDetailBinding
import com.example.forageapp.viewmodel.ForageViewModel
import com.example.forageapp.viewmodel.ForageViewModelFactory
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private var _binding:FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args = navArgs<DetailFragmentArgs>()

    private val viewModel:ForageViewModel by activityViewModels {
        ForageViewModelFactory(
            (activity?.application as ForageApplication).database.forageDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forageDetailEditButton.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToAddFragment(args.value.forageId)
            findNavController().navigate(action)
        }
        preloadData(args.value.forageId)
    }

    fun preloadData(forageId:Int){
        viewModel.getOneForage(forageId).observe(viewLifecycleOwner){ forage ->
            binding.forageDetailName.text = forage.name
            binding.forageDetailNote.text = forage.note
            binding.forageDetailInseason.text = if(forage.isSeason) getText(R.string.in_season) else getText(R.string.in_season)
            binding.forageDetailLocation.text = forage.location
        }
    }

}
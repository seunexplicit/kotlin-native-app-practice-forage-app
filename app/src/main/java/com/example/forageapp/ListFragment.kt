package com.example.forageapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forageapp.adapter.ForageListAdapter
import com.example.forageapp.databinding.FragmentListBinding
import com.example.forageapp.viewmodel.ForageViewModel
import com.example.forageapp.viewmodel.ForageViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {


    private var _binding:FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel:ForageViewModel by activityViewModels{
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
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ForageListAdapter{
            navigateToDetail(it.id)
        }
        binding.apply {
            forageListRecycler.adapter = adapter
            forageListRecycler.layoutManager = LinearLayoutManager(requireContext())
            addFloatingButton.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToAddFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.allForage.observe(viewLifecycleOwner){ Forages ->
            Forages.let {
                adapter.submitList(it)
            }
        }
    }

    private fun navigateToDetail(forageId:Int){
        val action = ListFragmentDirections.actionListFragmentToDetailFragment(forageId)
        findNavController().navigate(action)
    }



}
package com.example.forageapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.forageapp.data.Forage
import com.example.forageapp.databinding.FragmentAddBinding
import com.example.forageapp.viewmodel.FetchingStatus
import com.example.forageapp.viewmodel.ForageViewModel
import com.example.forageapp.viewmodel.ForageViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AddFragment : Fragment() {

    private val viewModel:ForageViewModel by activityViewModels {
        ForageViewModelFactory(
            (activity?.application as ForageApplication).database.forageDao()
        )
    }

    private val args:AddFragmentArgs by navArgs()
    private lateinit var forage: Forage

    private var _binding:FragmentAddBinding?= null
    private val binding get() = _binding!!
    var progressLoader:View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            forageUpdateButtons.visibility = if(args.forageId>-1) View.VISIBLE else View.GONE
            addForageSubmitButton.visibility = if(args.forageId>-1) View.GONE else View.VISIBLE
            addForageDeleteButton.setOnClickListener {
                viewModel.deleteForage(forage)
                val action  = AddFragmentDirections.actionAddFragmentToListFragment()
                findNavController().navigate(action)
            }
            addForageSaveButton.setOnClickListener {
                updateForage(this@AddFragment)
            }
            addForageSubmitButton.setOnClickListener {
                addForage()
            }
        }
        progressLoader = activity?.findViewById<View>(R.id.loader)

        if(args.forageId>-1){
            preloadForage(args.forageId)
        }
    }

    private fun addForage(){
        if(validateForage()){
            viewModel.addState.observe(viewLifecycleOwner){
                if(it == FetchingStatus.FETCHING){
                    this.progressLoader?.apply {
                        visibility = View.VISIBLE
                    }
                }
                else if(it == FetchingStatus.IDLE){
                    this.progressLoader?.apply {
                        visibility = View.GONE
                    }
                    Toast.makeText(requireContext(), getText(R.string.created_successfully), Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(R.id.action_addFragment_to_listFragment)
                }
            }
            viewModel.setNewForageEntry(
                binding.addForageName.text.toString(),
                binding.addForageLocation.text.toString(),
                binding.addForageInseason.isChecked,
                binding.addForageNote.text.toString()
            )
        }
    }

    private fun validateForage():Boolean{
        return _validateForage(
            binding.addForageName.text.toString(),
            binding.addForageLocation.text.toString()
        )
    }

    private fun _validateForage(name:String, location:String):Boolean{
       if(name.isBlank()){
           binding.addForageName.error = getText(R.string.name_required_error)
           lifecycleScope.launch{
               resetErrorAsync()
           }
          return false
       }
       else if(location.isBlank()){
           binding.addForageLocation.error = getText(R.string.location_required_error)
           lifecycleScope.launch{
               resetErrorAsync()
           }
           return false
       }

        resetError()
        return true
    }

    private suspend fun resetErrorAsync(){
        delay(9000)
        resetError()

    }

    private fun resetError(){
        binding.addForageName.error = null
        binding.addForageLocation.error = null
    }

    private fun preloadForage(forageId:Int){
        viewModel.getOneForage(forageId).observe(viewLifecycleOwner){ forage ->
            binding.apply {
                addForageName.setText(forage.name)
                addForageLocation.setText(forage.location)
                addForageNote.setText(forage.note)
                addForageInseason.isChecked = forage.isSeason

            }

            this.forage = Forage(
                forage.id,
                forage.name,
                forage.location,
                forage.note,
                forage.isSeason
            )
        }
    }

    companion object {
        private fun updateForage(addFragment: AddFragment){
            if(addFragment.validateForage()){
                addFragment.viewModel.updateState.observe(addFragment.viewLifecycleOwner){
                    if(it == FetchingStatus.FETCHING){
                        addFragment.progressLoader?.apply {
                            visibility = View.VISIBLE
                        }
                    }
                    else if(it == FetchingStatus.IDLE){
                        Log.d("Add Fragment", addFragment.progressLoader.toString())
                        addFragment.progressLoader?.apply {
                            visibility = View.GONE
                        }
                        Toast.makeText(
                            addFragment.requireContext(),
                            addFragment.getText(R.string.updated_successfully), Toast.LENGTH_LONG)
                            .show()
                        val action = AddFragmentDirections.actionAddFragmentToDetailFragment(
                            addFragment.args.forageId)
                        addFragment.findNavController().navigate(action)
                    }
                }
                addFragment.viewModel.setForageEntryWithId(
                    addFragment.args.forageId,
                    addFragment.binding.addForageName.text.toString(),
                    addFragment.binding.addForageLocation.text.toString(),
                    addFragment.binding.addForageInseason.isChecked,
                    addFragment.binding.addForageNote.text.toString()
                )
            }
        }
    }

}
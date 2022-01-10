package com.example.forageapp.viewmodel

import androidx.lifecycle.*
import com.example.forageapp.data.Forage
import com.example.forageapp.data.ForageDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

enum class FetchingStatus {
    FETCHING, IDLE
}

class ForageViewModel(private val forageDao: ForageDao):ViewModel() {

    private val _updateState:MutableLiveData<FetchingStatus> = MutableLiveData(FetchingStatus.IDLE)
    val updateState:LiveData<FetchingStatus>  = _updateState

    private val _addState:MutableLiveData<FetchingStatus> = MutableLiveData(FetchingStatus.IDLE)
    val addState:LiveData<FetchingStatus>  = _addState

    val allForage:LiveData<List<Forage>> = forageDao.getAllForages().asLiveData()

    fun getOneForage(forageId:Int):LiveData<Forage> = forageDao.getForage(forageId).asLiveData()

    private fun addForage(forage:Forage){
        _addState.value = FetchingStatus.FETCHING
        viewModelScope.launch {
            forageDao.insert(forage)
            _addState.value = FetchingStatus.IDLE
        }
    }

    private fun updateForage(forage:Forage){
        _updateState.value = FetchingStatus.FETCHING
        viewModelScope.launch {
            forageDao.update(forage)
            _updateState.value = FetchingStatus.IDLE
        }
    }

    fun deleteForage(forage: Forage) = viewModelScope.launch {
        forageDao.delete(forage)
    }

    fun setNewForageEntry(name:String, location:String, isSeason:Boolean, note:String){
        val forage = Forage(
            name = name,
            location = location,
            note = note,
            isSeason = isSeason
        )
        addForage(forage)
    }

    fun setForageEntryWithId(id:Int, name:String, location:String, isSeason:Boolean, note:String){
        val forage = Forage(
            id,
            name,
            location,
            note,
            isSeason
        )
        updateForage(forage)
    }


}

class ForageViewModelFactory(val forageDao: ForageDao):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ForageViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ForageViewModel(forageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}
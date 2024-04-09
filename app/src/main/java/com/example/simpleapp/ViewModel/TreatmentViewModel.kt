package com.example.simpleapp.ViewModel

import com.example.simpleapp.model.Treatment

class TreatmentViewModel {
    companion object {
        fun addBlankObjectatTop(treatments: List<Treatment>): List<Treatment> {

            val listString = listOf<String>("")
            val blankTreatment = Treatment("", "", "", "", "", 0.0, 0.0, 0.0, 0.0, "", listString)
            val treatmentList = listOf<Treatment>(blankTreatment)
            return treatmentList + treatments

        }
    }
}
package Adapters

import DataClass.Location

interface OnLocationClickListener {
    fun onLocationClicked(location: Location)
}
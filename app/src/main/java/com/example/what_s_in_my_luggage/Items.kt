package com.example.what_s_in_my_luggage

import com.google.firebase.storage.StorageReference

class Items(var image: StorageReference, val name: String, var x: Float = 0f, var y: Float = 0f)
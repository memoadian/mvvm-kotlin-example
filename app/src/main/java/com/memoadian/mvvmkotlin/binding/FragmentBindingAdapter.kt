package com.memoadian.mvvmkotlin.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import javax.inject.Inject

class FragmentBindingAdapter
@Inject constructor(val fragment: Fragment){
    @BindingAdapter("imageUrl")
    fun bindImage (iv: ImageView, url: String) {
        Glide.with(fragment).load(url).into(iv)
    }
}
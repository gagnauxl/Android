/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.eldogo

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.android.eldogo.databinding.RecyclerItemDogModelBinding


class DogListFragment : Fragment() {

  private lateinit var imageResIds: IntArray
  private lateinit var names: Array<String>
  private lateinit var descriptions: Array<String>
  private lateinit var urls: Array<String>
  private lateinit var listener: OnDogSelected


  companion object {

    fun newInstance(): DogListFragment {
      return DogListFragment()
    }
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context is OnDogSelected) {
      listener = context
    } else {
      throw ClassCastException(
        context.toString() + " must implement OnDogSelected.")
    }


    if (context != null) {
      // Get dog names and descriptions.
      val resources = context.resources
      names = resources.getStringArray(R.array.names)
      descriptions = resources.getStringArray(R.array.descriptions)
      urls = resources.getStringArray(R.array.urls)

      // Get dog images.
      val typedArray = resources.obtainTypedArray(R.array.images)
      val imageCount = names.size
      imageResIds = IntArray(imageCount)
      for (i in 0 until imageCount) {
        imageResIds[i] = typedArray.getResourceId(i, 0)
      }
      typedArray.recycle()
    }
  }

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val view: View = inflater.inflate(R.layout.fragment_dog_list, container,
      false)
    val activity = activity as Context
    val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
    recyclerView.layoutManager = GridLayoutManager(activity, 2)
    recyclerView.adapter = DogListAdapter(activity)
    return view
  }

  internal inner class DogListAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
      val recyclerDogModelBinding =
          RecyclerItemDogModelBinding.inflate(layoutInflater, viewGroup, false)
      return ViewHolder(recyclerDogModelBinding.root, recyclerDogModelBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
      val dog = DogModel(imageResIds[position], names[position],
          descriptions[position], urls[position])
      viewHolder.setData(dog)
      viewHolder.itemView.setOnClickListener {
        listener.onDogSelected(dog)
      }

    }

    override fun getItemCount() = names.size
  }

  internal inner class ViewHolder constructor(itemView: View,
                                              private val recyclerItemDogListBinding:
                                              RecyclerItemDogModelBinding
  ) :
      RecyclerView.ViewHolder(itemView) {

    fun setData(dogModel: DogModel) {
      recyclerItemDogListBinding.dogModel = dogModel
    }
  }

  interface OnDogSelected {
    fun onDogSelected(dogModel: DogModel)
  }


}

package frog.company.restrauntapp.bottom_dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.R
import frog.company.restrauntapp.database.UtilsHall
import frog.company.restrauntapp.databinding.BottomDialogHomeBinding
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerSortHome
import frog.company.restrauntapp.model.Hall

class BottomDialogHomeSort(val listener : IListenerSortHome) : BottomSheetDialogFragment(), IListenerHall {

    private lateinit var _binding : BottomDialogHomeBinding
    private val binding get() = _binding

    private lateinit var arrayStatus : Array<String>
    private var posStatusId = 0

    private val arrayHall = arrayListOf(
        "Показать все"
    )
    private var posHallId = 0
    private lateinit var res : ArrayList<Hall>

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogHomeBinding.inflate(inflater, container, false)

        arrayStatus = requireActivity().resources.getStringArray(R.array.order_status)

        UtilsHall().onLoadHall(this)

        val arrayStatusAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayStatus)
        binding.cmbStatus.setAdapter(arrayStatusAdapter)
        arrayStatusAdapter.filter.filter(null)
        binding.cmbStatus.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                posStatusId = position-1
            }

        binding.cmbHall.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                posHallId = if(position == 0)
                    0
                else
                    res[position-1].hall_id
            }

        binding.btnFind.setOnClickListener {
            listener.onSortHome(posStatusId, posHallId)
            dismiss()
        }
        return binding.root
    }

    override fun onListHalls(result: ArrayList<Hall>?) {
        if(result != null){
            requireActivity().runOnUiThread {
                for(i in result)
                    arrayHall.add(i.hall_name)

                res = result

                val arrayHallAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, arrayHall)
                binding.cmbHall.setAdapter(arrayHallAdapter)
                arrayHallAdapter.filter.filter(null)
            }
        }
    }

    override fun onSelectHall(result: Hall?) {

    }
}
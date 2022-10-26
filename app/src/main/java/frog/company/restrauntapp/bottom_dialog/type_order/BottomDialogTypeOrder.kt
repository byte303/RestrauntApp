package frog.company.restrauntapp.bottom_dialog.type_order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.annotation.Nullable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.R
import frog.company.restrauntapp.database.UtilsHall
import frog.company.restrauntapp.databinding.BottomDialogHomeBinding
import frog.company.restrauntapp.databinding.BottomDialogTypeOrderBinding
import frog.company.restrauntapp.enum.EnumTypeOrder
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerSortHome
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.ui.order.AdapterOrder

class BottomDialogTypeOrder(val listener : IListenerTypeOrder) : BottomSheetDialogFragment(), IListenerTypeOrder {

    private lateinit var _binding : BottomDialogTypeOrderBinding
    private val binding get() = _binding

    private val arrayText = arrayListOf(
        "Обычный",
        "С собой",
        "Доставка"
    )

    private val arrayDrawable = arrayListOf(
        R.drawable.ic_type_default,
        R.drawable.ic_type_with_you_self,
        R.drawable.ic_type_delivery
    )

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogTypeOrderBinding.inflate(inflater, container, false)

        binding.listType.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.listType.adapter = AdapterTypeOrder(arrayText, arrayDrawable, this)
        return binding.root
    }

    override fun onResultType(result: Int) {
        val res = when(result){
            0 -> EnumTypeOrder.Default.num
            1 -> EnumTypeOrder.WithMySelf.num
            else -> EnumTypeOrder.Delivery.num
        }
        listener.onResultType(res)
        dismiss()
    }
}
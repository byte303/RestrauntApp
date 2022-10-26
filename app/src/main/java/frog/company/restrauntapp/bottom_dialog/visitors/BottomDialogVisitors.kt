package frog.company.restrauntapp.bottom_dialog.visitors

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
import frog.company.restrauntapp.databinding.BottomDialogVisitorsBinding
import frog.company.restrauntapp.enum.EnumTypeOrder
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerSortHome
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.ui.order.AdapterOrder

class BottomDialogVisitors(val listener : IListenerVisitors) : BottomSheetDialogFragment(), IListenerVisitors {

    private lateinit var _binding : BottomDialogVisitorsBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogVisitorsBinding.inflate(inflater, container, false)

        binding.listType.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.listType.adapter = AdapterVisitors(this)
        return binding.root
    }

    override fun onResultVisitors(result: Int) {
        listener.onResultVisitors(result)
        dismiss()
    }
}
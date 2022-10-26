package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.databinding.BottomDialogCountColumnsBinding
import frog.company.restrauntapp.databinding.BottomDialogUrlBinding
import frog.company.restrauntapp.model.Orientation
import io.paperdb.Paper

class BottomDialogCountColumns : BottomSheetDialogFragment() {

    private lateinit var _binding : BottomDialogCountColumnsBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogCountColumnsBinding.inflate(inflater, container, false)

        val count = Paper.book().read(UtilsDB.PAPER_COUNT, Orientation(2,4))!!
        binding.edtCountHor.setText(count.horizontal.toString())
        binding.edtCountVert.setText(count.vertical.toString())

        binding.btnSave.setOnClickListener {
            count.horizontal = binding.edtCountHor.text.toString().toInt()
            count.vertical = binding.edtCountVert.text.toString().toInt()
            Paper.book().write(UtilsDB.PAPER_COUNT, count)
            dismiss()
        }
        return binding.root
    }
}
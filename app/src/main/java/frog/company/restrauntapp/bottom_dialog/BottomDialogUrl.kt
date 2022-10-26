package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.databinding.BottomDialogUrlBinding
import io.paperdb.Paper

class BottomDialogUrl : BottomSheetDialogFragment() {

    private lateinit var _binding : BottomDialogUrlBinding
    private val binding get() = _binding


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogUrlBinding.inflate(inflater, container, false)

        val url = Paper.book().read(UtilsDB.URL, "")
        binding.edtUrl.setText(url)

        binding.btnSave.setOnClickListener {
            val urls = binding.edtUrl.text.toString()
            Paper.book().write(UtilsDB.URL, urls)
            dismiss()

        }
        return binding.root
    }
}
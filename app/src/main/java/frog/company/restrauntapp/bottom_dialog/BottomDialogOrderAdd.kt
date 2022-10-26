package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.databinding.BottomDialogAddBinding
import frog.company.restrauntapp.inter.IListenerProduct
import frog.company.restrauntapp.model.Product

class BottomDialogOrderAdd(var element : Product, var listener : IListenerProduct) : BottomSheetDialogFragment(){

    private lateinit var _binding : BottomDialogAddBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogAddBinding.inflate(inflater, container, false)

        binding.txtName.text = element.prod_name


        binding.imgAdd.setOnClickListener {
            element.prod_total++
            onUpdateText()
        }
        binding.imgMinus.setOnClickListener {
            if(element.prod_total > 0)
                element.prod_total--
            onUpdateText()
        }

        binding.btnDone.setOnClickListener {
            element.prod_comment = binding.edtComment.text.toString()

            listener.onProductCount(element.prod_total, element.prod_comment, element.prod_id)
            dismiss()
        }
        onUpdateText()
        return binding.root
    }

    private fun onUpdateText(){
        binding.txtCount.text = element.prod_total.toString()
    }
}
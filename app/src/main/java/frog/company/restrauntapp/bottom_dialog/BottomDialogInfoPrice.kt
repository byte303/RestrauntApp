package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.databinding.BottomDialogInfoPriceBinding

class BottomDialogInfoPrice(
    private val discount : String,
    private val waiter : String,
    private val hall : String,
    private val total : String) : BottomSheetDialogFragment() {

    private lateinit var _binding : BottomDialogInfoPriceBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogInfoPriceBinding.inflate(inflater, container, false)

        binding.txtDiscount.text = String.format("Скидка: $discount процентов")
        binding.txtWaiter.text = String.format("Оплата официанту: $waiter")
        binding.txtHall.text = String.format("Стоимость зала: $hall")
        binding.txtPrice.text = String.format("Общая стоимость: $total")

        return binding.root
    }
}
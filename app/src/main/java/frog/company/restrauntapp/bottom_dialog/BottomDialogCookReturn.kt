package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.database.UtilsDetails
import frog.company.restrauntapp.databinding.BottomDialogCookReturnBinding
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerResult
import frog.company.restrauntapp.model.OrderDetails

class BottomDialogCookReturn(
    val orderDetails : OrderDetails,
    val listener : IListenerResult
) : BottomSheetDialogFragment(), IListenerDetails {

    private lateinit var _binding : BottomDialogCookReturnBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogCookReturnBinding.inflate(inflater, container, false)

        binding.btnDone.setOnClickListener {
            orderDetails.details_status = EnumDetailsStatus.Return.num
            val array = arrayListOf(orderDetails)
            UtilsDetails().onUpdate(array, this)
        }
        return binding.root
    }

    override fun onBooleanResult(result: Int) {
        if(result == UtilsEnum.DETAILS_UPDATE){
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Блюдо успешно отправлено обратно на кухню!", Toast.LENGTH_SHORT).show()
                listener.onBooleanResult(true)
                dismiss()
            }
        }
    }

    override fun onIndexResult(result: Int) {
    }

    override fun onArrayDetails(result: ArrayList<OrderDetails>?) {
    }

    override fun onResultDetail(result: OrderDetails?) {
    }
}
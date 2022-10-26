package frog.company.restrauntapp.bottom_dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.database.UtilsDetails
import frog.company.restrauntapp.database.UtilsOrder
import frog.company.restrauntapp.databinding.BottomDialogStatusDoneBinding
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.inter.IListenerResult
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.OrderDetails
import frog.company.restrauntapp.model.User
import io.paperdb.Paper

class BottomDialogStatusDone(
    val details : Order,
    val listener : IListenerResult
) : BottomSheetDialogFragment(), IListenerOrder, IListenerDetails {

    private lateinit var _binding : BottomDialogStatusDoneBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogStatusDoneBinding.inflate(inflater, container, false)

        binding.btnDone.setOnClickListener {
            for(i in details.orderDetails!!)
                i.details_status = EnumDetailsStatus.Ready.num

            UtilsDetails().onUpdate(details.orderDetails!!, this)
        }
        return binding.root
    }

    override fun onListOrders(result: ArrayList<Order>?) {

    }

    override fun onOrder(result: Order?, index: Int) {

    }

    override fun onResultOrder(boolean: Boolean, result: Int) {
        if(boolean){
            if(result == UtilsEnum.ORDER_UPDATE){
                requireActivity().runOnUiThread {
                    Toast.makeText(requireActivity(), "Статус успешно изменён!", Toast.LENGTH_SHORT).show()
                    listener.onBooleanResult(true)
                    dismiss()
                }
            }
        }
    }

    override fun onBooleanResult(result: Int) {
        if(result == UtilsEnum.DETAILS_UPDATE){
            UtilsDetails().onLoadActive(details.order_id.toString(), this)
        }
    }

    override fun onIndexResult(result: Int) {

    }

    override fun onArrayDetails(result: ArrayList<OrderDetails>?) {
        if(result == null){
            details.order_status_cook = EnumDetailsStatus.Ready.num
            UtilsOrder().onUpdate(details, this)

            binding.btnAccept.visibility = View.GONE
            binding.btnDone.visibility = View.GONE
        }else{
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), "Статус успешно изменён!", Toast.LENGTH_SHORT).show()
                listener.onBooleanResult(true)
                dismiss()
            }
        }
    }

    override fun onResultDetail(result: OrderDetails?) {

    }
}
package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.database.UtilsDetails
import frog.company.restrauntapp.database.UtilsOrder
import frog.company.restrauntapp.database.UtilsTable
import frog.company.restrauntapp.databinding.BottomDialogCookCancelBinding
import frog.company.restrauntapp.databinding.BottomDialogCookReturnBinding
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.enum.EnumTablesStatus
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.inter.IListenerResult
import frog.company.restrauntapp.inter.IListenerTable
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.OrderDetails
import frog.company.restrauntapp.model.Table

class BottomDialogCookCancel(
    val order : Order,
    val listener : IListenerResult
) : BottomSheetDialogFragment(), IListenerOrder, IListenerTable {

    private lateinit var _binding : BottomDialogCookCancelBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogCookCancelBinding.inflate(inflater, container, false)

        binding.btnDone.setOnClickListener {
            UtilsOrder().onUpdateStatus(order.order_id, EnumOrderStatus.Cancel.num, this)
        }
        return binding.root
    }

    override fun onListOrders(result: ArrayList<Order>?) {

    }

    override fun onOrder(result: Order?, index: Int) {

    }

    override fun onResultOrder(boolean: Boolean, result: Int) {
        if(boolean){
            when(result){
                UtilsEnum.ORDER_FREE_TABLE ->{
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Заказ успешно отменен!", Toast.LENGTH_SHORT).show()
                        listener.onBooleanResult(true)
                        dismiss()
                    }
                }
                UtilsEnum.ORDER_UPDATE_STATUS ->{
                    UtilsOrder().onCheckTableFree(order.order_table, this)
                }
            }
        }else{
            if(result == UtilsEnum.ORDER_FREE_TABLE){
                order.table!!.table_status = EnumTablesStatus.Free.num
                UtilsTable().onUpdateTable(order.table!!, this)
            }
        }
    }

    override fun onListTables(result: ArrayList<Table>?) {

    }

    override fun onSelectTable(result: Table?) {

    }

    override fun onResultTable(boolean: Boolean) {
        if(boolean){
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Заказ успешно отменен!", Toast.LENGTH_SHORT).show()
                listener.onBooleanResult(true)
                dismiss()
            }
        }
    }
}
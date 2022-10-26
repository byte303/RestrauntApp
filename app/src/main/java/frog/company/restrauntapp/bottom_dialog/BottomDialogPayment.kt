package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.databinding.BottomDialogPaymentBinding
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.enum.EnumPayment
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Order

class BottomDialogPayment(
    private val payment : Boolean,
    private val myOrder : Order?,
    private val price : Double,
    val listener : IListenerOrder) : BottomSheetDialogFragment() {

    private lateinit var _binding : BottomDialogPaymentBinding
    private val binding get() = _binding
    private lateinit var order : Order

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogPaymentBinding.inflate(inflater, container, false)

        order = Order(
            0,
            0,
            "",
            "",
            0.0,
            0.0,
            0,
            0,
            0,
            0,
            0,
            "",
        0,
        0,
            0.0,)

        if(myOrder != null){
            binding.edtComment.setText(myOrder.order_comment)

            order.order_comment = myOrder.order_comment
            order.order_status = myOrder.order_status
            order.order_discount = myOrder.order_discount
            order.order_payment = myOrder.order_payment
        }

        if(payment){
            binding.txtTitle.text = "Оплата заказа"
            binding.txtPrice.text = String.format("Стоимость заказа: $price")
            binding.radioCard.isChecked = true
        }else{
            binding.txtTitle.text = "Отправка заказа"
            binding.txtPrice.visibility = View.GONE
            binding.linearRadio.visibility = View.GONE
            binding.edtDiscount.visibility = View.GONE
            binding.btnPayment.visibility = View.GONE
        }

        binding.btnDone.setOnClickListener {
            if(myOrder == null)
                order.order_status = EnumOrderStatus.NotPaid.num

            onPayment()
        }

        binding.btnPayment.setOnClickListener {
            order.order_status = EnumOrderStatus.Paid.num
            order.order_status_cook = EnumDetailsStatus.Ready.num
            onPayment()
        }

        binding.edtDiscount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != "")
                    if (s.toString().toDouble() > 100)
                        binding.edtDiscount.setText("100")
            }
        })
        return binding.root
    }

    private fun onPayment(){
        order.order_comment = binding.edtComment.text.toString()

        if(payment){
            if(binding.edtDiscount.text.toString() != "")
                order.order_discount = binding.edtDiscount.text.toString().toDouble()

            order.order_payment = if(binding.radioCard.isChecked) EnumPayment.Card.num else EnumPayment.Money.num
        }

        listener.onOrder(order, UtilsEnum.ORDER_PAYMENT)
        dismiss()
    }
}
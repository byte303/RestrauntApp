package frog.company.restrauntapp.bottom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.databinding.BottomDialogMenuOrderBinding
import frog.company.restrauntapp.enum.EnumUserRole
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerResult
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.User
import io.paperdb.Paper

class BottomDialogMenuOrder(
    val listener : IListenerResult,
    val ready : Boolean,
) : BottomSheetDialogFragment() {

    private lateinit var _binding : BottomDialogMenuOrderBinding
    private val binding get() = _binding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        _binding = BottomDialogMenuOrderBinding.inflate(inflater, container, false)

        val user = Paper.book().read(UtilsDB.USER, User())!!

        if(user.user_role == EnumUserRole.Admin.num ||
            user.user_role == EnumUserRole.Manager.num){

            binding.btnEdit.visibility = View.GONE
        }
        if(ready){
            binding.btnCancel.visibility = View.GONE
        }

        binding.btnEdit.setOnClickListener {
            listener.onIndexResult(UtilsEnum.MENU_EDIT)
            dismiss()
        }

        binding.btnInfo.setOnClickListener {
            listener.onIndexResult(UtilsEnum.MENU_INFO)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            listener.onIndexResult(UtilsEnum.MENU_CANCEL)
            dismiss()
        }
        return binding.root
    }
}
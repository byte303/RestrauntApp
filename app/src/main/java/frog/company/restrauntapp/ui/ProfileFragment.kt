package frog.company.restrauntapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import frog.company.restrauntapp.R
import frog.company.restrauntapp.bottom_dialog.BottomDialogCountColumns
import frog.company.restrauntapp.bottom_dialog.BottomDialogStatusDone
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.databinding.BottomDialogCountColumnsBinding
import frog.company.restrauntapp.databinding.FragmentProfileBinding
import frog.company.restrauntapp.enum.EnumUserRole
import frog.company.restrauntapp.helper.MyDialog
import frog.company.restrauntapp.model.User
import io.paperdb.Paper
import frog.company.restrauntapp.inter.IListenerDialog
import frog.company.restrauntapp.ui.home.HomeFragment

class ProfileFragment : Fragment(), IListenerDialog {

    private lateinit var _binding : FragmentProfileBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val user = Paper.book().read(UtilsDB.USER, User(1, "", "", 1))!!

        if(user.user_role == EnumUserRole.Cook.num){
            binding.btnMyWork.visibility = View.GONE
        }

        binding.txtLogin.text = user.user_name
        binding.txtId.text = String.format("ID: ${user.user_id}")

        binding.btnExit.setOnClickListener {
            MyDialog(requireContext()).onExitUser(this)
        }
        binding.btnMyWork.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.viewPager,
                HomeFragment()
            ).addToBackStack(null).commit()
        }
        binding.btnHelp.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.viewPager,
                HelpFragment()
            ).addToBackStack(null).commit()
        }
        binding.btnSetting.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.viewPager,
                ChangedFragment()
            ).addToBackStack(null).commit()
        }
        binding.btnCount.setOnClickListener{
            val dialogFragment = BottomDialogCountColumns()
            dialogFragment.show(
                requireActivity().supportFragmentManager,
                dialogFragment.tag
            )
        }
        return binding.root
    }

    override fun onResultDialog(result: Boolean, index: Int) {
        when(index){
            1->{
                if(result){
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }
}
package frog.company.restrauntapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import frog.company.restrauntapp.R
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.database.UtilsUser
import frog.company.restrauntapp.databinding.FragmentChangedBinding
import frog.company.restrauntapp.databinding.FragmentHelpBinding
import frog.company.restrauntapp.inter.IListenerUser
import frog.company.restrauntapp.model.User
import io.paperdb.Paper


class ChangedFragment : Fragment(), IListenerUser {

    private lateinit var _binding : FragmentChangedBinding
    private val binding get() = _binding

    private lateinit var myUser : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangedBinding.inflate(inflater, container, false)

        myUser = Paper.book().read(UtilsDB.USER, User(1, "","",0))!!

        binding.btnSave.setOnClickListener {
            requireActivity().runOnUiThread {
                val oldPass = binding.edtOLdPass.text.toString()
                val newPass = binding.edtNewPass.text.toString()

                if (myUser.user_password == oldPass) {
                    if (newPass.length == 6) {
                        UtilsUser().onSelectPassword(newPass, this)
                    } else
                        Toast.makeText(
                            requireContext(),
                            "Длина пароля должна составлять 6 цифр!",
                            Toast.LENGTH_SHORT
                        ).show()
                } else
                    Toast.makeText(
                        requireContext(),
                        "Старый пароль введён неверно!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().runOnUiThread {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        return binding.root
    }

    override fun onSelectUser(user: User?) {
        requireActivity().runOnUiThread {
            if (user == null) {
                val newPass = binding.edtNewPass.text.toString()

                myUser.user_password = newPass

                UtilsUser().onUpdateUser(myUser, this)
            } else
                Toast.makeText(requireContext(), "Данный пароль уже занят!", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onSelectAllUser(user: ArrayList<User>?) {

    }

    override fun onUserBooleanResult(boolean: Boolean) {
        requireActivity().runOnUiThread {

            Paper.book().write(UtilsDB.USER, myUser)

            Toast.makeText(requireContext(), "Пароль успешно изменен!", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
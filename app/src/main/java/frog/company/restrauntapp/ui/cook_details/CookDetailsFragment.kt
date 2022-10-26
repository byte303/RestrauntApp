package frog.company.restrauntapp.ui.cook_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import frog.company.restrauntapp.R
import frog.company.restrauntapp.bottom_dialog.BottomDialogCookReturn
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.database.UtilsDetails
import frog.company.restrauntapp.database.UtilsOrder
import frog.company.restrauntapp.databinding.FragmentCookDetailsBinding
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumUserRole
import frog.company.restrauntapp.helper.LoadingScreen
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.inter.IListenerResult
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.OrderDetails
import frog.company.restrauntapp.model.User
import frog.company.restrauntapp.ui.select_table.SelectTableFragment
import io.paperdb.Paper


class CookDetailsFragment : Fragment(), IListenerDetails, IListenerOrder, IListenerResult  {

    private var _binding : FragmentCookDetailsBinding? = null
    private val binding get() = _binding!!

    private var arrayDetails : ArrayList<OrderDetails> = ArrayList()
    private lateinit var order : Order
    private lateinit var user : User
    private var myKitchen = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCookDetailsBinding.inflate(inflater, container, false)

        Paper.init(requireContext())
        user = Paper.book().read(UtilsDB.USER, User(0, "","",0))!!

        val bundle = arguments

        if (bundle != null) {
            order = bundle.getSerializable(UtilsEnum.BUNDLE_MAINCOOK_COOK_DETAILS) as Order
            myKitchen = bundle.getInt(UtilsEnum.BUNDLE_KITCHEN)
        }

        if(user.user_role == EnumUserRole.Cook.num)
            UtilsDetails().onLoadOrderKitchen(
                order.order_id.toString(),
                myKitchen.toString(), this)
        else
            UtilsDetails().onLoadOrderId(order.order_id.toString(), this)

        LoadingScreen.displayLoadingWithText(requireContext(), false)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().runOnUiThread {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        if(user.user_role == EnumUserRole.Cook.num)
            binding.imgDone.visibility = View.VISIBLE
        else
            binding.imgDone.visibility = View.GONE

        binding.imgDone.setOnClickListener {
            for (i in arrayDetails)
                i.details_status = EnumDetailsStatus.Ready.num

            UtilsDetails().onUpdate(arrayDetails, this)
        }

        binding.btnChangeTable.setOnClickListener {
            val fragment = SelectTableFragment()
            val _bundle = Bundle()
            _bundle.putSerializable(UtilsEnum.BUNDLE_MAINCOOK_COOK_DETAILS, order)
            fragment.arguments = _bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.viewPager,
                fragment
            ).addToBackStack(null).commit()
        }
        return binding.root
    }

    override fun onBooleanResult(result: Boolean) {
        if(result){
            if(user.user_role == EnumUserRole.Cook.num)
                UtilsDetails().onLoadOrderKitchen(
                    order.order_id.toString(),
                    myKitchen.toString(), this)
            else
                UtilsDetails().onLoadOrderId(order.order_id.toString(), this)
        }
    }

    override fun onArrayDetails(result: ArrayList<OrderDetails>?) {
        if(result != null){
            requireActivity().runOnUiThread {
                arrayDetails = result

                binding.listDetails.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.listDetails.adapter = AdapterCookDetails(arrayDetails, this)
            }
        }
        LoadingScreen.hideLoading()
    }

    override fun onResultDetail(result: OrderDetails?) {
        if(result != null){
            if(user.user_role == EnumUserRole.Manager.num || user.user_role == EnumUserRole.Admin.num){
                if(result.details_status == EnumDetailsStatus.Ready.num){
                    requireActivity().runOnUiThread {
                        val dialogFragment = BottomDialogCookReturn(result, this)
                        dialogFragment.show(
                            requireActivity().supportFragmentManager,
                            dialogFragment.tag
                        )
                    }
                }
            }
        }
    }

    override fun onResultOrder(boolean: Boolean, result: Int) {
        if(boolean){
            if(user.user_role == EnumUserRole.Cook.num)
                UtilsDetails().onLoadOrderKitchen(
                    order.order_id.toString(),
                    myKitchen.toString(), this)
            else
                UtilsDetails().onLoadOrderId(order.order_id.toString(), this)
        }
    }
    ///////////////////
    ///////////////////
    ///////////////////

    override fun onListOrders(result: ArrayList<Order>?) {

    }

    override fun onOrder(result: Order?, index: Int) {

    }

    override fun onIndexResult(result: Int) {

    }

    override fun onBooleanResult(result: Int) {
        if(result == UtilsEnum.DETAILS_UPDATE){
            order.order_status_cook = EnumDetailsStatus.Ready.num
            UtilsOrder().onUpdate(order, this)

            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Данные успешно изменены!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
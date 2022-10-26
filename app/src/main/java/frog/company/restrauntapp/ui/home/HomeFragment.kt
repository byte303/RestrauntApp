package frog.company.restrauntapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import frog.company.restrauntapp.bottom_dialog.BottomDialogCookCancel
import frog.company.restrauntapp.bottom_dialog.BottomDialogHomeSort
import frog.company.restrauntapp.bottom_dialog.BottomDialogMenuOrder
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.database.UtilsOrder
import frog.company.restrauntapp.databinding.FragmentHomeBinding
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.enum.EnumUserRole
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.inter.IListenerResult
import frog.company.restrauntapp.inter.IListenerSortHome
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.User
import frog.company.restrauntapp.ui.cook_details.CookDetailsFragment
import frog.company.restrauntapp.ui.order.OrderActivity
import io.paperdb.Paper

class HomeFragment : Fragment(), IListenerOrder, IListenerSortHome, IListenerResult {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var user : User
    private var allArray : ArrayList<Order> = ArrayList()
    private lateinit var myOrder : Order

    private var adapterHome : AdapterHome? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        Paper.init(requireActivity())
        user = Paper.book().read(UtilsDB.USER, User())!!

        if(user.user_role == EnumUserRole.Waiter.num)
            UtilsOrder().onLoadOrderUser(user.user_id.toString(),this)
        else
            UtilsOrder().onLoadOrderAllWaiter(this)

        binding.listOrders.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.imgSort.setOnClickListener {
            requireActivity().runOnUiThread {
                val dialogFragment = BottomDialogHomeSort(this)
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    dialogFragment.tag
                )
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onListOrders(result: ArrayList<Order>?) {
        if(result != null){
            requireActivity().runOnUiThread {
                if(adapterHome == null){
                    allArray.addAll(result)
                    adapterHome = AdapterHome(allArray, this)
                    binding.listOrders.adapter = adapterHome
                }else{
                    allArray.clear()
                    adapterHome?.notifyDataSetChanged()

                    allArray.addAll(result)
                    adapterHome?.notifyDataSetChanged()
                }

            }
        }
    }

    override fun onOrder(result: Order?, index: Int) {
        if(result != null){
            Log.e("TestLog", result.order_id.toString())

            myOrder = result

            when (result.order_status) {
                EnumOrderStatus.Paid.num -> {

                }
                else ->{
                    requireActivity().runOnUiThread {
                        val ready = result.order_status_cook == EnumDetailsStatus.Ready.num
                        val dialogFragment = BottomDialogMenuOrder(this, ready)
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

    }

    override fun onSortHome(status: Int, hall: Int) {
        requireActivity().runOnUiThread {
            val result : ArrayList<Order> = ArrayList()
            Log.i("onSortHome", result.count().toString())

            for (i in allArray) {
                if ((i.order_status == status || status == -1) && (hall == 0 || i.table!!.hall!!.hall_id == hall))
                    result.add(i)
            }
            result.sortBy { it.order_status_cook }

            allArray.clear()
            adapterHome?.notifyDataSetChanged()

            allArray.addAll(result)
            adapterHome?.notifyDataSetChanged()
        }
    }

    override fun onBooleanResult(result: Boolean) {
        onUpdateAdapter()
    }

    override fun onIndexResult(result: Int) {
        when(result){
            UtilsEnum.MENU_EDIT ->{
                requireActivity().runOnUiThread {
                    val intent = Intent(requireActivity(), OrderActivity::class.java)
                    intent.putExtra(UtilsEnum.MY_ORDERS, myOrder)
                    intent.putExtra(UtilsEnum.SELECT_TABLE, myOrder.order_table)
                    intent.putExtra(UtilsEnum.SELECT_HALL, myOrder.table!!.hall)
                    startActivity(intent)
                }
            }UtilsEnum.MENU_INFO -> onOpenFragmentCookDetails()
            UtilsEnum.MENU_CANCEL ->{
                requireActivity().runOnUiThread {
                    val dialogFragment = BottomDialogCookCancel(myOrder, this)
                    dialogFragment.show(
                        requireActivity().supportFragmentManager,
                        dialogFragment.tag
                    )
                }
            }
        }
    }

    private fun onOpenFragmentCookDetails(){
        requireActivity().runOnUiThread {
            val fragment = CookDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable(UtilsEnum.BUNDLE_MAINCOOK_COOK_DETAILS, myOrder)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(
                frog.company.restrauntapp.R.id.viewPager,
                fragment
            ).addToBackStack(null).commit()
        }
    }

    override fun onResume() {
        super.onResume()
        onUpdateAdapter()
    }

    private fun onUpdateAdapter(){
        requireActivity().runOnUiThread {
            if(user.user_role == EnumUserRole.Waiter.num) {
                val user = Paper.book().read(UtilsDB.USER, User())!!
                UtilsOrder().onLoadOrderUser(user.user_id.toString(), this)
            }
            else
                UtilsOrder().onLoadOrderAllWaiter(this)
        }
    }
}
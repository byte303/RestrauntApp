package frog.company.restrauntapp.ui.main_cook

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import frog.company.restrauntapp.R
import frog.company.restrauntapp.bottom_dialog.BottomDialogStatusDone
import frog.company.restrauntapp.database.*
import frog.company.restrauntapp.databinding.FragmentMainCookBinding
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumUserRole
import frog.company.restrauntapp.helper.LoadingScreen
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.*
import frog.company.restrauntapp.model.*
import io.paperdb.Paper
import java.util.*


class MainCookFragment : Fragment(), IListenerDetails, IListenerResult, IListenerOrder, IListenerUser, IListenerTable {

    private var _binding : FragmentMainCookBinding? = null
    private val binding get() = _binding!!

    private var timer: Timer? = null
    private var tTask: TimerTask? = null
    private var interval: Long = 4000

    private var arrayCook : ArrayList<Order> = ArrayList()
    private var myKitchenId = -1
    private lateinit var user : User

    private var listUser : ArrayList<User> = ArrayList()
    private var indexUser = 0

    private var listTable : ArrayList<Table> = ArrayList()
    private var indexTable = 0

    private var adapterMain : AdapterMainCook? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCookBinding.inflate(inflater, container, false)

        user = Paper.book().read(UtilsDB.USER, User())!!
        myKitchenId = user.user_kitchen

        LoadingScreen.displayLoadingWithText(requireActivity(),  false)
        UtilsUser().onLoad(this)

        binding.toolbar.subtitle = user.kitchen!!.kitchen_name

        val count = Paper.book().read(UtilsDB.PAPER_COUNT, Orientation(2,4))!!

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.listCook.layoutManager =
                StaggeredGridLayoutManager(count.vertical, LinearLayoutManager.VERTICAL)
        else
            binding.listCook.layoutManager =
                StaggeredGridLayoutManager(count.horizontal, LinearLayoutManager.VERTICAL)

        timer = Timer()
        schedule()
        return binding.root
    }

    private fun schedule() {
        tTask?.cancel()
        if (interval > 0) {
            tTask = object : TimerTask() {
                override fun run() {
                    onUpdateDetails()
                }
            }
            timer?.schedule(tTask, 1000, interval)
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().runOnUiThread {
            timer?.cancel()
            tTask?.cancel()
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().runOnUiThread {
            timer?.cancel()
            tTask?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().runOnUiThread {
            timer?.cancel()
            tTask?.cancel()
        }
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        onUpdateDetails()
    }

    override fun onBooleanResult(result: Int) {

    }

    override fun onBooleanResult(result: Boolean) {
        onUpdateDetails()
    }

    override fun onIndexResult(result: Int) {

    }

    override fun onArrayDetails(result: ArrayList<OrderDetails>?) {
        if (result != null) {
            val res : ArrayList<Order> = ArrayList()
            var check = false

            for(i in result){
                for(q in res){
                    if(i.order == q){
                        check = true
                        break
                    }
                }
                if(!check) {
                    if(indexTable == 0 || listTable[indexTable-1].table_id == i.order!!.table!!.table_id)
                        if(indexUser == 0 || listUser[indexUser-1].user_id == i.order!!.user!!.user_id)
                            res.add(i.order!!)
                }
                check = false
            }
            Log.i("PROVERKA", "$indexTable || $indexUser || ${res.size}")

            for (q in res) {
                for(i in result) {
                    if(i.details_order == q.order_id){
                        if(q.orderDetails == null)
                            q.orderDetails = ArrayList()

                        q.orderDetails!!.add(i)
                    }
                }
                Log.i("PROVERKA CHECK COUNT", q.orderDetails!!.size.toString())
            }
            Log.i("PROVERKA CHECK", "res: ${res.size} || arrayCook: ${arrayCook.size}")

            if(arrayCook.size != res.size) {
                requireActivity().runOnUiThread {
                    if (adapterMain == null) {
                        arrayCook.addAll(res)

                        adapterMain = AdapterMainCook(requireActivity(),arrayCook, this)
                        binding.listCook.adapter = adapterMain
                    }else {
                        arrayCook.clear()
                        adapterMain?.notifyDataSetChanged()

                        if(res.size > 0) {
                            arrayCook.addAll(res)
                            arrayCook.sortByDescending { it.order_favorite }

                            adapterMain?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
        requireActivity().runOnUiThread { LoadingScreen.hideLoading() }

    }

    override fun onResultDetail(result: OrderDetails?) {

    }

    private fun onUpdateDetails(){
        requireActivity().runOnUiThread {
            val status = EnumDetailsStatus.NewOrder.num.toString()
            UtilsDetails().onLoadStatus(status,myKitchenId.toString(),this)
        }

    }

    override fun onListOrders(result: ArrayList<Order>?) {

    }

    override fun onOrder(result: Order?, index: Int) {
        if(result != null){
            when(index){
                UtilsEnum.ORDER_SELECT ->{
                    requireActivity().runOnUiThread {
                        val dialogFragment = BottomDialogStatusDone(result, this)
                        dialogFragment.show(
                            requireActivity().supportFragmentManager,
                            dialogFragment.tag
                        )
                    }
                    //onOpenFragmentCookDetails(result)
                }
                UtilsEnum.ORDER_FAVORITE ->{
                    result.order_favorite = if(result.order_favorite == 1) 0 else 1

                    UtilsOrder().onUpdateFavorite(result, this)
                }
            }
        }
    }

    override fun onResultOrder(boolean: Boolean, result: Int) {
        if(boolean){
            when(result){
                UtilsEnum.ORDER_UPDATE_FAVORITE ->{
                    onUpdateDetails()
                }
            }
        }
    }

    override fun onSelectUser(user: User?) {

    }

    override fun onSelectAllUser(user: ArrayList<User>?) {
        requireActivity().runOnUiThread {
            if (user != null) {
                val list = ArrayList<String>()
                list.add("Показать всех")

                for (i in user) {
                    if (i.user_role != EnumUserRole.Cook.num) {
                        list.add(i.user_name)
                        listUser.add(i)
                    }
                }

                val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.item_dropdown, list)
                binding.cmbUser.setAdapter(arrayAdapter)
                arrayAdapter.filter.filter(null)
                binding.cmbUser.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        indexUser = position
                        onUpdateDetails()
                        //onRefresh(true)
                    }

                UtilsTable().onLoad(this)

            } else
                LoadingScreen.hideLoading()
        }
    }

    override fun onUserBooleanResult(boolean: Boolean) {

    }

    override fun onListTables(result: ArrayList<Table>?) {
        requireActivity().runOnUiThread {
            if (result != null) {
                val list = ArrayList<String>()
                list.add("Показать всех")

                for (i in result) {
                    list.add(i.table_name)
                    listTable.add(i)
                }

                val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.item_dropdown, list)
                binding.cmbTable.setAdapter(arrayAdapter)
                arrayAdapter.filter.filter(null)
                binding.cmbTable.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        indexTable = position
                        onUpdateDetails()
                    }

                onUpdateDetails()

            } else
                LoadingScreen.hideLoading()
        }
    }

    override fun onSelectTable(result: Table?) {

    }

    override fun onResultTable(boolean: Boolean) {

    }
}
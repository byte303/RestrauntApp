package frog.company.restrauntapp.ui.select_table

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import frog.company.restrauntapp.R
import frog.company.restrauntapp.bottom_dialog.BottomDialogPayment
import frog.company.restrauntapp.bottom_dialog.type_order.BottomDialogTypeOrder
import frog.company.restrauntapp.bottom_dialog.type_order.IListenerTypeOrder
import frog.company.restrauntapp.bottom_dialog.visitors.BottomDialogVisitors
import frog.company.restrauntapp.bottom_dialog.visitors.IListenerVisitors
import frog.company.restrauntapp.database.UtilsHall
import frog.company.restrauntapp.database.UtilsOrder
import frog.company.restrauntapp.database.UtilsTable
import frog.company.restrauntapp.databinding.FragmentSelectTableBinding
import frog.company.restrauntapp.enum.EnumTypeOrder
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.inter.IListenerTable
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.Table
import frog.company.restrauntapp.ui.order.AdapterTab
import frog.company.restrauntapp.ui.order.OrderActivity

class SelectTableFragment : Fragment(),
    IListenerHall, IListenerTable, IListenerTypeOrder, IListenerVisitors, IListenerOrder{

    private var _binding : FragmentSelectTableBinding? = null
    private val binding get() = _binding!!

    private lateinit var myHall : Hall
    private var arrayTables : ArrayList<Table> = ArrayList()
    private lateinit var myTable : Table

    private var order : Order? = null

    private lateinit var myIntent : Intent
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectTableBinding.inflate(inflater, container, false)

        val bundle = arguments

        if (bundle != null) {
            order = bundle.getSerializable(UtilsEnum.BUNDLE_MAINCOOK_COOK_DETAILS) as Order
        }

        binding.listTable.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        UtilsTable().onLoad(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        UtilsTable().onLoad(this)
    }

    override fun onListHalls(result: ArrayList<Hall>?) {
        if(result != null){
            requireActivity().runOnUiThread {
                binding.listTab.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.listTab.adapter = AdapterHall(result, this)
            }
            onSortHall(result[0])
        }
    }

    override fun onSelectHall(result: Hall?) {
        if(result != null){
            onSortHall(result)
        }
    }

    private fun onSortHall(result: Hall){
        myHall = result

        val array = ArrayList<Table>()

        for(i in arrayTables)
            if(i.table_hall_id == result.hall_id)
                array.add(i)

        requireActivity().runOnUiThread {
            binding.toolbar.subtitle = "Стоимость зала: ${myHall.hall_price}"
            binding.listTable.adapter = AdapterTable(array, this)
        }
    }

    override fun onListTables(result: ArrayList<Table>?) {
        if(result != null){
            arrayTables = result

            UtilsHall().onLoadHall(this)
        }
    }

    override fun onSelectTable(result: Table?) {
        if(result != null){
            // Если мы создаем заказ, то выбираем столик
            if(order == null) {

                myTable = result
                requireActivity().runOnUiThread {
                    val dialogFragment = BottomDialogTypeOrder(this)
                    dialogFragment.show(
                        requireActivity().supportFragmentManager,
                        dialogFragment.tag
                    )
                }
            }else{
                // Если мы меняем заказ, то просто меняем столик
                val after = order!!.order_table
                order!!.order_table = result.table_id
                UtilsOrder().onUpdateTable(order!!, after,this)
            }
        }
    }

    override fun onResultTable(boolean: Boolean) {

    }

    override fun onResultType(result: Int) {
        myIntent = Intent(requireContext(), OrderActivity::class.java)

        myIntent.putExtra(UtilsEnum.MY_ORDERS, null as Order?)
        myIntent.putExtra(UtilsEnum.SELECT_TABLE, myTable.table_id)
        myIntent.putExtra(UtilsEnum.SELECT_HALL, myHall)
        myIntent.putExtra(UtilsEnum.SELECT_DELIVERY, result)

        if(EnumTypeOrder.Default.num == result){
            requireActivity().runOnUiThread {
                val dialogFragment = BottomDialogVisitors(this)
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    dialogFragment.tag
                )
            }
        }else
            startActivity(myIntent)
    }

    override fun onResultVisitors(result: Int) {
        // Установка кол-ва гостей
        startActivity(myIntent)
    }

    override fun onListOrders(result: ArrayList<Order>?) {

    }

    override fun onOrder(result: Order?, index: Int) {

    }

    override fun onResultOrder(boolean: Boolean, result: Int) {
        if(boolean){
            if(result == UtilsEnum.ORDER_UPDATE_TABLE){
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireActivity(),
                        "Вы успешно изменили столик",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }
}
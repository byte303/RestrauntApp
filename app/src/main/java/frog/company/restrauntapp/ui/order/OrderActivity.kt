package frog.company.restrauntapp.ui.order

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import frog.company.restrauntapp.*
import frog.company.restrauntapp.bottom_dialog.BottomDialogInfoPrice
import frog.company.restrauntapp.bottom_dialog.BottomDialogOrderAdd
import frog.company.restrauntapp.bottom_dialog.BottomDialogPayment
import frog.company.restrauntapp.database.*
import frog.company.restrauntapp.databinding.ActivityOrderBinding
import frog.company.restrauntapp.enum.*
import frog.company.restrauntapp.helper.MyDate
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.*
import frog.company.restrauntapp.model.*
import io.paperdb.Paper


class OrderActivity : AppCompatActivity(),
    IListenerCategory, IListenerProduct, IListenerOrder, IListenerDetails, IListenerTable {

    private lateinit var _binding : ActivityOrderBinding
    private val binding get() = _binding

    private var arrayProduct : ArrayList<Product> = ArrayList()
    private var arrayOrder : ArrayList<Product> = ArrayList()
    private var myOrderDetails : ArrayList<OrderDetails> = ArrayList()

    private lateinit var adapterOrder : AdapterOrder
    private lateinit var adapterProduct : AdapterProduct
    private var myOrder : Order? = null
    private lateinit var user : User

    private var totalPrice = 0.0
    private var myPayment = EnumPayment.Card.num
    private var myStatus = EnumOrderStatus.NotPaid.num
    private var myStatusCook = EnumDetailsStatus.NewOrder.num
    private var myDiscount = 0.0
    private var myWaiter = 0.0
    private var myComment = ""
    private var myDelivery = EnumTypeOrder.Default.num
    private lateinit var myHall : Hall
    private var checkUpdate = false
    private var tableId = 0
    private var checkOnBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(applicationContext)
        user = Paper.book().read(UtilsDB.USER, User(1, "", "", 1))!!

        myOrder = intent.getSerializableExtra(UtilsEnum.MY_ORDERS) as Order?
        tableId = intent.getIntExtra(UtilsEnum.SELECT_TABLE, 0)
        myHall = intent.getSerializableExtra(UtilsEnum.SELECT_HALL) as Hall
        myDelivery = intent.getIntExtra(UtilsEnum.SELECT_DELIVERY, EnumTypeOrder.Default.num)

        if(myOrder != null){
            myDelivery = myOrder!!.order_delivery
            myDiscount = myOrder!!.order_discount
            myComment = myOrder!!.order_comment
            myPayment = myOrder!!.order_payment
            myStatus = myOrder!!.order_status
            myStatusCook = myOrder!!.order_status_cook

            onUpdatePrice()
        }

        UtilsProduct().onLoad(this)

        binding.listProduct.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        binding.listItem.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        adapterOrder = AdapterOrder(arrayOrder, this)
        binding.listItem.adapter = adapterOrder

        binding.imgInfo.setOnClickListener {
            val dialogFragment = BottomDialogInfoPrice(
                myDiscount.toString(),
                myWaiter.toString(),
                myHall.hall_price.toString(),
                totalPrice.toString()
            )
            dialogFragment.show(
                supportFragmentManager,
                dialogFragment.tag
            )
        }

        binding.btnDone.setOnClickListener {
            if(user.user_role == EnumUserRole.Cashier.num){
                val dialogFragment = BottomDialogPayment(true, myOrder, totalPrice,this)
                dialogFragment.show(
                    supportFragmentManager,
                    dialogFragment.tag
                )
            }else{
                val dialogFragment = BottomDialogPayment(false, myOrder,totalPrice, this)
                dialogFragment.show(
                    supportFragmentManager,
                    dialogFragment.tag
                )
            }
        }
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            if(myOrder != null){
                binding.radioProduct!!.isChecked = false
                binding.radioMyOrder!!.isChecked = true

                animIn()
            }else{
                binding.radioProduct!!.isChecked = true
                binding.radioMyOrder!!.isChecked = false

                animOut()
            }


            binding.radioMyOrder!!.setOnCheckedChangeListener { _, b ->
                if (b) animIn()
            }

            binding.radioProduct!!.setOnCheckedChangeListener { _, b ->
                if (b) animOut()
            }
        }
        onUpdatePrice()
    }
    private fun animIn(){
        binding.constraintLayout.visibility = View.VISIBLE

        val animOut: Animation = AnimationUtils.loadAnimation(this, R.anim.transon)
        binding.constraintLayout.startAnimation(animOut)
    }

    private fun animOut(){

        val animOut: Animation = AnimationUtils.loadAnimation(this, R.anim.translate)
        binding.constraintLayout.startAnimation(animOut)

        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.constraintLayout.visibility = View.GONE
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun onCreateOrder(){
        if(myOrder == null){
            val order = Order(
                0,
                tableId,
                MyDate().onDateFormat(),
                MyDate().onDateFormat(),
                totalPrice,
                myDiscount,
                user.user_id,
                user.shift!!.shift_id,
                myPayment,
                myStatus,
                myDelivery,
                myComment,
                myStatusCook,
                0,
                myWaiter
            )
            UtilsOrder().onInsert(order,this)
        }
    }

    private fun onUpdateOrder(){
        if(myOrder != null){
            myOrder!!.order_table = tableId
            myOrder!!.order_close_date = MyDate().onDateFormat()
            myOrder!!.order_price = totalPrice
            myOrder!!.order_discount = myDiscount
            myOrder!!.order_shift = user.shift!!.shift_id
            myOrder!!.order_payment = myPayment
            myOrder!!.order_status = myStatus
            myOrder!!.order_status_cook = myStatusCook
            myOrder!!.order_delivery = myDelivery
            myOrder!!.order_comment = myComment
            myOrder!!.order_price_waiter = myWaiter
            UtilsOrder().onUpdate(myOrder!!,this)
        }
    }

    override fun onResultCategory(category: Category) {
        if(arrayProduct.size > 0){
            onSortCategory(category)
        }
    }

    private fun onSortCategory(category: Category){
        val array : ArrayList<Product> = ArrayList()

        for(i in arrayProduct)
            if(i.prod_category == category.category_id)
                array.add(i)

        runOnUiThread {
            adapterProduct = AdapterProduct(array, this@OrderActivity)
            binding.listProduct.adapter = adapterProduct
        }
    }

    override fun onResultCategoryAll(category: ArrayList<Category>?) {
        if(category != null){
            runOnUiThread {
                binding.listTab.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.listTab.adapter = AdapterTab(category, this@OrderActivity)
            }
            onSortCategory(category[0])

            if(myOrder != null){
                Log.e("TestLog", myOrder!!.order_id.toString())
                val id = myOrder!!.order_id.toString()
                UtilsDetails().onLoadOrderId(id, this)
            }
        }
    }

    override fun onArrayProducts(result: ArrayList<Product>?) {
        if(result != null){
            arrayProduct = result

            UtilsCategory().onLoadCategory(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSelectProduct(result: Product, boolean: Boolean) {
        if(!boolean){
            for ((index, i) in arrayOrder.withIndex()) {
                if (i.prod_id == result.prod_id && i.prod_status != EnumDetailsStatus.Ready.num) {
                    i.prod_total++
                    Log.i("prod_total", "prod_total: ${i.prod_total} AND price: ${i.prod_price}")
                    runOnUiThread {
                        adapterProduct.notifyDataSetChanged()
                        adapterOrder.notifyItemChanged(index)
                        onUpdatePrice()
                    }
                    return
                }
            }
            runOnUiThread {
                //result.prod_total++
                adapterProduct.notifyDataSetChanged()
                arrayOrder.add(Product(
                    result.prod_id,
                    result.prod_name,
                    result.prod_price,
                    result.prod_count,
                    result.prod_value,
                    result.prod_category,
                    result.prod_start_price,
                    result.prod_kitchen,
                    1,
                    result.prod_presence,
                    "",
                    EnumDetailsStatus.NewOrder.num
                ))
                adapterOrder.notifyItemInserted(arrayOrder.size - 1)
                onUpdatePrice()
            }
        }else{
            runOnUiThread {
                val dialogFragment = BottomDialogOrderAdd(result, this)
                dialogFragment.show(
                    supportFragmentManager,
                    dialogFragment.tag
                )
            }
        }
    }

    // Устанавливаем кол-во товара
    override fun onProductCount(count: Int, comment : String, id : Int) {
        for ((index, i) in arrayOrder.withIndex()) {
            if (i.prod_id == id && i.prod_status != EnumDetailsStatus.Ready.num) {
                i.prod_total = count
                i.prod_comment = comment
                runOnUiThread {
                    if (count != 0)
                        adapterOrder.notifyItemChanged(index)
                    else {
                        arrayOrder.removeAt(index)
                        adapterOrder.notifyItemRemoved(index)
                    }
                }
                onUpdatePrice()
                return
            }
        }
    }

    // Обновление цен
    private fun onUpdatePrice(){
        runOnUiThread {
            totalPrice = 0.0
            var price = 0.0
            // Узнаём стоимость всех блюд
            for(i in arrayOrder){
                Log.i("Price", "${i.prod_total} AND ${i.prod_price}")
                totalPrice += (i.prod_total * i.prod_price)
            }
            Log.i("prod_total", "arrayOrder size: ${arrayOrder.size}")
            Log.i("prod_total", "Total Price: $totalPrice")
            // Вычитаем скидку
            if (myDiscount != 0.0) {
                totalPrice -= (totalPrice / 100 * myDiscount)
                price = totalPrice
            }
            // Добавление стоимость зала
            totalPrice += myHall.hall_price

            // Оплата официанту
            val cashe = Paper.book().read(UtilsDB.CASHE, Cashe(
                0.0,0.0,0.0,0.0,0)
            )
            Log.e("CASHE", myDelivery.toString())
            if(myDelivery == EnumTypeOrder.Default.num){
                if(cashe != null){
                    myWaiter = if (cashe.cashe_type == EnumCasheType.Fix.num)
                        cashe.cashe_fix
                    else price / 100 * cashe.cashe_percent

                    Log.e("CASHE", "myWaiter $myWaiter")
                    Log.e("CASHE", "Percent ${cashe.cashe_percent}")
                }
            }
            totalPrice += myWaiter
            binding.txtPrice.text = String.format("Итого: $totalPrice")
        }
    }

    override fun onListOrders(result: ArrayList<Order>?) {

    }

    override fun onOrder(result: Order?, index: Int) {
        if(result != null){
            when(index) {
                UtilsEnum.ORDER_CREATE -> {
                    myOrder = result

                    UtilsDetails().onDeleteDetails(result.order_id.toString(), this)
                }
                UtilsEnum.ORDER_PAYMENT ->{
                    myPayment = result.order_payment
                    myStatus = result.order_status
                    myDiscount = result.order_discount
                    myComment = result.order_comment

                    onUpdatePrice()

                    if(result.order_status_cook == EnumDetailsStatus.Ready.num)
                        myStatusCook = result.order_status_cook
                    else{
                        for(i in arrayOrder) {
                            if (i.prod_status == EnumDetailsStatus.NewOrder.num) {
                                myStatusCook = if(myStatusCook == EnumDetailsStatus.Ready.num)
                                    EnumDetailsStatus.Edit.num
                                else
                                    EnumDetailsStatus.NewOrder.num
                                break
                            }
                        }
                    }
                    if(myOrder == null)
                        onCreateOrder()
                    else
                        onUpdateOrder()
                }
            }
        }else {
            runOnUiThread {
                Toast.makeText(applicationContext, "Произошла ошибка! (onOrder)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResultOrder(boolean: Boolean, result: Int) {
        if(boolean){
            if(result == UtilsEnum.ORDER_UPDATE){
                if(!checkUpdate)// А это обновление заказа в целом
                    UtilsDetails().onDeleteDetails(myOrder!!.order_id.toString(), this)
                else { // Если мы обновляем в конце с проверкой на новые блюда, которые нужно доготовить
                    runOnUiThread {
                        checkUpdate = false // Специальная переменная
                        Toast.makeText(
                            applicationContext,
                            "Операция выполнена успешно!",
                            Toast.LENGTH_SHORT
                        ).show()
                        checkOnBack = true
                        onBackPressed()
                    }
                }
            }
        }else {
            runOnUiThread {
                Toast.makeText(applicationContext, "Произошла ошибка! (onResultOrder)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBooleanResult(result: Int) {
        when(result) {
            UtilsEnum.DETAILS_DEFAULT -> {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Произошла ошибка! (DETAILS_DEFAULT)", Toast.LENGTH_SHORT).show()
                }
            }
            UtilsEnum.DETAILS_DELETE -> {
                val array = ArrayList<OrderDetails>()
                var details : OrderDetails
                // Здесь менять
                var status: Int
                for(i in arrayOrder){
                    //status = EnumDetailsStatus.NewOrder.num
/*
                    for(q in myOrderDetails){
                        if(i.prod_id == q.details_prod){
                            status = if (i.prod_total.toDouble() == q.details_count)
                                q.details_status
                            else {
                                if (q.details_id != 0)
                                    EnumDetailsStatus.Edit.num
                                else
                                    EnumDetailsStatus.NewOrder.num
                            }
                            break
                        }
                    }*/
                    details = OrderDetails(
                        0,
                        i.prod_id,
                        i.prod_total.toDouble(),
                        myOrder!!.order_id,
                        //status,
                        i.prod_status,
                        i.prod_comment)
                    array.add(details)
                }
                myOrderDetails = array
                UtilsDetails().onInsert(array,this)
            }

            UtilsEnum.DETAILS_INSERT -> {
                var table = Table(
                    tableId,
                    "",
                    0,
                    0,
                    EnumTablesStatus.Busy.num,
                    MyDate().onOnlyDateFormat(),
                    MyDate().onOnlyTimeFormat()
                )

                if(myOrder!!.order_status == EnumOrderStatus.Paid.num && myDelivery == EnumTypeOrder.Default.num){
                    table = Table(
                        tableId,
                        "",
                        0,
                        0,
                        EnumTablesStatus.Free.num,
                        MyDate().onOnlyDateFormat(),
                        MyDate().onOnlyTimeFormat()
                    )

                    UtilsTable().onUpdateTable(table, this)
                }

                UtilsTable().onUpdateTable(table, this)
            }
        }
    }

    override fun onIndexResult(result: Int) {

    }

    override fun onArrayDetails(result: ArrayList<OrderDetails>?) {
        if(result != null){
            Log.e("TestLog", result.size.toString())
            myOrderDetails = result
            runOnUiThread {
                for (i in result) {
                    for (q in arrayProduct) {
                        if (i.details_prod == q.prod_id) {
                            q.prod_total = i.details_count.toInt()
                            q.prod_status = i.details_status
                            arrayOrder.add(Product(
                                q.prod_id,
                                q.prod_name,
                                q.prod_price,
                                q.prod_count,
                                q.prod_value,
                                q.prod_category,
                                q.prod_start_price,
                                q.prod_kitchen,
                                q.prod_total,
                                q.prod_presence,
                                q.prod_comment,
                                q.prod_status
                            ))
                            adapterOrder.notifyItemInserted(arrayOrder.size - 1)
                            break
                        }
                    }
                }
            }
            Log.e("TestLog", arrayOrder.size.toString())
            onUpdatePrice()
        }
    }

    override fun onResultDetail(result: OrderDetails?) {

    }

    override fun onBackPressed() {
        if(checkOnBack){
            super.onBackPressed()
        }else {
            val deleteDialog = AlertDialog.Builder(this, R.style.MyDialogTheme)

            deleteDialog
                .setTitle("Выход")
                .setMessage("Вы действительно хотите закрыть данное окно?\nВсе несохраненные данные будут удалены!")
                .setCancelable(false)

            val del = deleteDialog.create()

            deleteDialog.setNegativeButton("Нет") { _, _ ->
                del.dismiss()
            }

            deleteDialog.setPositiveButton("Да") { _, _ ->
                del.dismiss()
                super.onBackPressed()
            }
            deleteDialog.show()
        }
    }

    override fun onListTables(result: ArrayList<Table>?) {

    }

    override fun onSelectTable(result: Table?) {

    }

    override fun onResultTable(boolean: Boolean) {
        runOnUiThread {
            Toast.makeText(applicationContext, "Операция выполнена успешно!", Toast.LENGTH_SHORT)
                .show()
            checkOnBack = true
            onBackPressed()
        }
    }
}
package frog.company.restrauntapp.ui.cook_list_menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import frog.company.restrauntapp.R
import frog.company.restrauntapp.database.UtilsCategory
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.database.UtilsDetails
import frog.company.restrauntapp.database.UtilsProduct
import frog.company.restrauntapp.databinding.ActivityOrderBinding
import frog.company.restrauntapp.databinding.FragmentCookListMenuBinding
import frog.company.restrauntapp.enum.EnumPresence
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerCategory
import frog.company.restrauntapp.inter.IListenerProduct
import frog.company.restrauntapp.model.Category
import frog.company.restrauntapp.model.Product
import frog.company.restrauntapp.model.User
import frog.company.restrauntapp.ui.order.AdapterProduct
import frog.company.restrauntapp.ui.order.AdapterTab
import io.paperdb.Paper


class CookListMenuFragment : Fragment(), IListenerProduct, IListenerCategory {

    private lateinit var _binding : FragmentCookListMenuBinding
    private val binding get() = _binding

    private lateinit var arrayProductAll : ArrayList<Product>
    private lateinit var arrayProductSelect : ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentCookListMenuBinding.inflate(inflater, container, false)

        val user = Paper.book().read(UtilsDB.USER, User())!!
        UtilsProduct().onLoadKitchen(user.user_kitchen, this)

        binding.listProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.imgSave.setOnClickListener {
            UtilsProduct().onUpdate(arrayProductSelect, this)
        }

        binding.imgAll.setOnClickListener {
            for(i in arrayProductSelect)
                i.prod_presence = EnumPresence.Yes.num

            binding.listProduct.adapter =
                AdapterCookProduct(arrayProductSelect, this)
        }

        binding.imgNotAll.setOnClickListener {
            for(i in arrayProductSelect)
                i.prod_presence = EnumPresence.No.num

            binding.listProduct.adapter =
                AdapterCookProduct(arrayProductSelect, this)
        }
        return binding.root
    }

    override fun onArrayProducts(result: ArrayList<Product>?) {
        if(result != null){
            arrayProductAll = result

            UtilsCategory().onLoadCategory(this)
        }
    }

    override fun onSelectProduct(result: Product, boolean: Boolean) {

    }

    override fun onProductCount(count: Int, comment : String, id: Int) {

    }

    override fun onIndexResult(index: Int) {
        when(index){
            UtilsEnum.PRODUCT_DEFAULT ->{
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Произошла ошибка!", Toast.LENGTH_SHORT).show()
                }
            }UtilsEnum.PRODUCT_UPDATE ->{
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Данные успешно обновлены!", Toast.LENGTH_SHORT).show()
                }
            }else ->{
            arrayProductSelect[index].prod_presence =
                    if (arrayProductSelect[index].prod_presence == EnumPresence.Yes.num)
                        EnumPresence.No.num
                    else
                        EnumPresence.Yes.num
            }
        }
    }

    override fun onResultCategory(category: Category) {
        if(arrayProductAll.size > 0){

            onSortCategory(category)
        }
    }

    override fun onResultCategoryAll(category: ArrayList<Category>?) {
        if(category != null){
            requireActivity().runOnUiThread {
                binding.listTab.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                binding.listTab.adapter = AdapterTab(category, this)
            }
            onSortCategory(category[0])
        }
    }

    private fun onSortCategory(category: Category){
        arrayProductSelect = ArrayList()

        for(i in arrayProductAll)
            if(i.prod_category == category.category_id)
                arrayProductSelect.add(i)

        requireActivity().runOnUiThread {
            binding.listProduct.adapter =
                AdapterCookProduct(arrayProductSelect, this)
        }
    }
}
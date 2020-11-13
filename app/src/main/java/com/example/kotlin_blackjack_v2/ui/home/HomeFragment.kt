package com.example.kotlin_blackjack_v2.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_blackjack_v2.R
import com.example.kotlin_blackjack_v2.SharedViewModel
import com.example.kotlin_blackjack_v2.ui.dashboard.DashboardFragment
import com.example.kotlin_blackjack_v2.ui.notifications.NotificationsFragment
import androidx.navigation.fragment.findNavController
import com.example.kotlin_blackjack_v2.MainActivity

class HomeFragment : Fragment() {
    // Main Menu / Home
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var SharedVM: SharedViewModel

    lateinit var ET_Name: EditText
    lateinit var ET_MinBet: EditText
    lateinit var TV_CashM: TextView
    lateinit var TOG_UseCash: Switch
    lateinit var B_GotoTable: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        SharedVM = ViewModelProvider(this.requireActivity()).get(SharedViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)

        ET_Name = root.findViewById<EditText>(R.id.et_PlayerName)
        ET_MinBet = root.findViewById<EditText>(R.id.et_MinBet)
        TV_CashM = root.findViewById<TextView>(R.id.tv_Cash_Menu)
        TOG_UseCash = root.findViewById<Switch>(R.id.tog_UseCash)
        B_GotoTable = root.findViewById<Button>(R.id.b_gotoGame) // Goto Table Fragment

        SharedVM._Cash.observe(viewLifecycleOwner, Observer {
            // On cash change
            TV_CashM.text = it.toString() ?: "<IDK>"
        })

        SharedVM._MinBet.observe(viewLifecycleOwner, Observer {
            // On cash change
            ET_MinBet.setText( (Math.max(it, 100)).toString() )
        })

        SharedVM._PName.observe(viewLifecycleOwner, Observer {
            // On name change
            ET_Name.setText(it)
        })

        SharedVM._UseCash.observe(viewLifecycleOwner, Observer {
            // On Use Cash changed
            TOG_UseCash.isChecked = it ?: true
        })

        SharedVM._CashScaler.observe(viewLifecycleOwner, Observer {
            // On cash scalar change
            // Stubbed
        })


        //homeViewModel.
        //homeViewModel.text.observe(viewLifecycleOwner, Observer {

       // })

        ET_MinBet?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?){

                if (s != null){
                    updateSHARED()
                } else {
                    println("[!] Returned null editable")
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        ET_Name?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?){

                if (s != null){
                    updateSHARED()
                } else {
                    println("[!] Returned null editable")
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        return root
    }

    fun updateCash(){
        SharedVM._Cash.value = SharedVM.Cash
    }

    fun updateSHARED(){
        println("[+] Updating Main SHARED data")
        updateCash()

        SharedVM.PName = ET_Name.getText().toString()
        SharedVM.UseCash = TOG_UseCash.isChecked()
        try{
            SharedVM.MinBet = ET_MinBet.text.toString().toInt()
        } catch(e: NumberFormatException ){
            // Never mind then
            SharedVM.MinBet = SharedVM.MinBet
        }
    }

    fun clickGotoTable(view: View){
        updateSHARED()

        findNavController().navigate(R.id.navigation_dashboard)
    }

    fun clickReset(view: View){
        println("[i] Reseting money to Minbet")

        // Reset money
        SharedVM.Cash = ( SharedVM.CashScaler * SharedVM.MinBet )

        updateSHARED()
        resetCash()
    }

    fun resetCash(){
        // Reset cash

        //val t:String = ET_MinBet!!.text.toString()
        val t:Int = SharedVM.MinBet
        var amt = 0

        if (t != null){
            amt = t * SharedVM.CashScaler
        }else{
            amt = SharedVM.MinBet
        }

        SharedVM.Cash = amt
        updateCash()
    }

}
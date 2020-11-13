package com.example.kotlin_blackjack_v2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_blackjack_v2.MainActivity
import com.example.kotlin_blackjack_v2.R
import com.example.kotlin_blackjack_v2.SharedViewModel
import androidx.navigation.fragment.findNavController

class DashboardFragment : Fragment() {
    // The Table
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var SharedVM: SharedViewModel

    var isInitd = false // Make it easy to tell if we've started yet

    // Define finals
    val GAME_SCORE_CEIL = 10 // Dont let value be too much
    val HOLDER_NONE = 0
    val HOLDER_DEALER = 1
    val HOLDER_PLAYER = 2

    //var Cash = 400 // The table uses money the most, therefore base it in here
    //val CashScaler = 4
    var BetDoubled = false

    // Track static elements
    lateinit var B_Hit: Button
    lateinit var B_Double: Button
    lateinit var B_Stay: Button

    lateinit var TV_Main: TextView
    lateinit var TV_CPlayer: TextView
    lateinit var TV_CDealer: TextView
    lateinit var TV_Cash: TextView // At table
    lateinit var TV_CashM: TextView // At menu
    lateinit var TV_Name: TextView

    lateinit var ET_CurBet: EditText
    lateinit var ET_MinBet:EditText

    lateinit var TOG_UseCash:Switch

    // Create resource tables
    val AllSuits = listOf("Diamonds", "Clubs", "Hearts", "Spades")
    val AllSuitsC = listOf('♦', '♣', '♥', '♠')
    val AllFaces = listOf("Ace", "Jack", "Queen", "King", "Joker")
    val AllColors = listOf("Red" , "Black")

    // Define a custom type for cards
    class Card(val index:Int, val value:Int, val Suit:Int, var Holder:Int) // 0 = deck, 2 = player, 1 = dealer

    lateinit var Deck: Array<Card>
    var Hand_Player: Array<Card?> = arrayOfNulls<Card>(13)
    var Hand_Dealer: Array<Card?> =  arrayOfNulls<Card>(13)


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        SharedVM = ViewModelProvider(this.requireActivity()).get(SharedViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        //val textView: TextView = root.findViewById(R.id.text_dashboard)

        // Init Game
        if (isInitd == false) {
            println("[i] Initializing the Table")
            isInitd = true

            createDeck()

            // Start the game with reset
            println("[i] INIT done, resetting Game state")
        }


        B_Hit = root.findViewById<Button>(R.id.b_GHit)
        B_Double = root.findViewById<Button>(R.id.b_GDouble)
        B_Stay = root.findViewById<Button>(R.id.b_GStay)

        TV_Main = root.findViewById<TextView>(R.id.tv_MainText)
        TV_CPlayer = root.findViewById<TextView>(R.id.tv_PlayerCards)
        TV_CDealer = root.findViewById<TextView>(R.id.tv_DealerCards)
        TV_Cash = root.findViewById<TextView>(R.id.tv_Cash_Table)
        TV_CashM = root.findViewById<TextView>(R.id.tv_Cash_Menu)
        TV_Name = root.findViewById<TextView>(R.id.tv_Name)

        ET_CurBet  = root.findViewById<EditText>(R.id.et_CurBet)
        ET_MinBet  = root.findViewById<EditText>(R.id.et_MinBet)
        TV_CashM = root.findViewById<TextView>(R.id.tv_Cash_Menu)
        TOG_UseCash = root.findViewById<Switch>(R.id.tog_UseCash)
        println("[i] Building element vars OK ")

        println("Buttons")
        println("IsNull? " + (B_Hit == null))
        println("IsNull? " + (B_Double == null))
        println("IsNull? " + (B_Stay == null))

        println("TextView")
        println("IsNull? " + (TV_Main == null))
        println("IsNull? " + (TV_CPlayer == null))
        println("IsNull? " + (TV_CDealer == null))
        println("IsNull? " + (TV_Cash == null))
        println("IsNull? " + (TV_CashM == null))
        println("IsNull? " + (TV_Name == null))

        println("Edit Text")
        println("IsNull? " + (ET_CurBet  == null))
        println("IsNull? " + (ET_MinBet  == null))
        println("IsNull? " + (TV_CashM == null))

        println("Switchs")
        println("IsNull? " + (TOG_UseCash == null))

        // Correct logic
        println("[i] Correct logic -> Reset Cash ")

        SharedVM._Cash.observe(viewLifecycleOwner, Observer {
            // On cash change
            TV_Cash.text = it.toString() ?: "<IDK>"
        })

        SharedVM._MinBet.observe(viewLifecycleOwner, Observer {
            // On cash change
            //ET_MinBet.text =  (Math.max(it, 100)).toString()
        })

        SharedVM._PName.observe(viewLifecycleOwner, Observer {
            // On name change
            TV_Name.text = it.toString() ?: "Player1"
        })

        SharedVM._UseCash.observe(viewLifecycleOwner, Observer {
            // On Use Cash changed
            ET_CurBet.isEnabled = it
        })

        SharedVM._CashScaler.observe(viewLifecycleOwner, Observer {
            // On cash scalar change

        })

        resetGame()
        println("[i] onCreate Complete!")

        return root
    }

    fun createDeck(){
        // Create all 13 values of cards
        // and create the suits as well
        println("[i] Creating the Deck!")

        Deck = emptyArray<Card>() // It really doesnt like null fill

        for (x in 0 until 13){
            // Make all 4

            val i1 = 0 + x
            val i2 = 1 + x
            val i3 = 2 + x
            val i4 = 3 + x
            println("[i] Creating cards @ index $i1 to $i4")

            Deck.plus(Card(i1, x + 1, 0, HOLDER_NONE))
            Deck.plus(Card(i2, x + 1, 1, HOLDER_NONE))
            Deck.plus(Card(i3, x + 1, 2, HOLDER_NONE))
            Deck.plus(Card(i4, x + 1, 3, HOLDER_NONE))
        }
    }

    fun setCash(amt: Int):Int{
        // Money needs to be exchanged
        // The game is over
        println("[i] Setting cash to $amt")

        val isWin = amt > 0

        if (useCash()) {
            val minb = getMinBet()
            if (amt < minb && isWin) {
                println("[!] User attempted to undervalue bets")
                SharedVM.Cash += minb
                SharedVM._Cash.value = SharedVM.Cash
            }else{
                SharedVM.Cash += amt
                SharedVM._Cash.value = SharedVM.Cash
            }

            if (TV_Cash != null){
                TV_Cash.setText("$ " + amt)
                //TV_CashM!!.setText(amt)
            }else{
                println("[!] TV_Cash is null?")
            }

        }

        endState(isWin, amt) // Round over

        return amt
    }

    fun endState(isWin:Boolean, amt:Int){
        // A hang state which unlocks the central text
        println("Hiding buttons, unhiding text with onClick")

        TV_Main.visibility = View.VISIBLE
        var mstr = "Tie Game"

        if (amt > 0) {
            if (isWin) {
                mstr = "You've won $ " + amt
            } else {
                var sadd = ""
                if (isBust(true)){
                    sadd = "Bust!"
                }
                mstr = sadd + "You've lost $ " + amt
            }
        }

        // Let them know the game will end
        if (isTooBroke()){
            mstr = "You're broke!"
        }

        TV_Main.setText(mstr)

        B_Double.visibility = View.INVISIBLE
        B_Hit.visibility = View.INVISIBLE
        B_Stay.visibility = View.INVISIBLE
    }

    fun checkGameStatus(DMove: Boolean){
        // Someone has won, who is it?
        println("[i] Evaluating game")

        var pt: Int = getPlayerTotal()
        var dt: Int = getDealerTotal()

        var pr = pt <= 21
        var dr = dt <= 21

        if (DMove && dr && dt < 16){
            // Draw a card if dealer is below 16
            // and give it to the dealer
            drawCardFromDeck(false)

            dt = getDealerTotal()
            dr = dt <= 21
        }

        // Show the dealers full hand
        TV_CDealer.setText(getDealerAll())

        if ((!pr && !dr) || (pr && dr && pt == dt)) {
            // Tie
            game_Tie()
            return
        }else if (!pr){ // Check Busts, then amounts
            // Bust Player
            // Lose
            game_Lose()

        }else if (!dr){
            // Bust Dealer
            // Win
            game_Win()

        }else if (dr && dt > pt){
            // Player Loss
            game_Lose()
        }else if (pr && pt > dt){
            // PLayer wins
            game_Win()
        }

    }

    fun drawCardFromDeck(side: Boolean): Card {
        // Take a number from the deck at random
        // and give it a side of the table
        var temp: Card

        // Get a random card thats not in a hand
        do {
            temp = Deck.random()!!
        } while (temp.Holder != HOLDER_NONE)

        // Mark as held
        // Add the card to the Hand
        if (side) {
            temp.Holder = HOLDER_PLAYER
            Hand_Player.plus(temp)
            // Update card string
            TV_CPlayer.setText(cardToStr(temp))
        } else {
            temp.Holder = HOLDER_DEALER
            Hand_Dealer.plus(temp)
        }

        return temp
    }

    fun resetGame(){
        // Rest sate to a start
        BetDoubled = false

        Hand_Dealer = arrayOfNulls<Card>(13)
        Hand_Player = arrayOfNulls<Card>(13)

        println("Clearing the deck")
        for (x in 0 until Deck.size){
            // Clean deck
            Deck[x].Holder = HOLDER_NONE
        }

        // Dealers Hand
        var damt = 0
        val peak: Card = drawCardFromDeck(false)
        TV_CDealer.setText(cardToStr(peak)) // Set the peak card

        drawCardFromDeck(false)
        damt = getDealerTotal()

        // Players Hand
        var pamt = 0
        drawCardFromDeck(true)
        drawCardFromDeck(true)
        pamt = getPlayerTotal()

        if (damt >= 21 || pamt >= 21){
            checkGameStatus(damt < 16)
            return
        }

        // Now wait for player to play
        B_Double.visibility = View.VISIBLE
        B_Hit.visibility = View.VISIBLE
        B_Stay.visibility = View.VISIBLE
        TV_Main.visibility = View.INVISIBLE
    }

    fun game_Win(){
        // Add money and change status
        println("[i] Win game")

        val amt = setCash(getTotalBet() * 2)
    }

    fun game_Lose(){
        // Subtract money and change status
        println("[i] Lose game")

        val amt = setCash(-1 * getTotalBet())
    }

    fun game_Tie(){
        println("[i] Tie game")
        endState(false, 0)
    }

    fun getPlayerTotal():Int{
        // Sum the cards

        var total = 0
        var str:String = "None"

        for (x in 0 until Hand_Player.count()){
            val cur: Card? = Hand_Player[x]!!

            if (cur != null) {
                if (str.length < 1) {
                    str = cardToStr(cur)
                } else {
                    str = str + " , " + cardToStr(cur)
                }

                total = total + Math.max(cur.value, GAME_SCORE_CEIL)
            }
        }

        TV_CPlayer.text = str.toString() // Players hand

        return total
    }

    fun getTotalBet():Int{
        // Calculate the bet in full

        var b = ET_CurBet.toString()
        var m = SharedVM.MinBet
        var bi:Int = 100
        var s:Int = 1

        if (BetDoubled){
            s = 2
        }
        if (b.length > 0){
            bi = b.toInt()
        }
        if (m != null) {
            bi = Math.max(bi, m)
        } else {
            bi = 100
            println("[!] Shared bet min came back null! 0,0")
        }

        val ans:Int = (s * bi)
        return ans
    }

    fun getDealerTotal():Int{
        // Calculate dealers cards
        var total = 0

        for (x in 0 until Hand_Dealer.count()){
            val cur: Card? = Hand_Dealer[x]!!
            if (cur != null){
                total = total + Math.max( cur.value, GAME_SCORE_CEIL )
            }
        }

        return total
    }

    fun getDealerAll():String{
        // Get the string for ALL of the dealers cards
        var str:String = "None"

        for (x in 0 until Hand_Dealer.size){
            val cur: Card = Hand_Dealer[x]!!

            if (str.length < 1) {
                str = cardToStr(cur)
            } else {
                str = str + " , " + cardToStr(cur)
            }
        }

        return str
    }

    fun getMinBet():Int{
        // Grab the current value
        // for Min Bet

        var arg = getMinBet()
        var ans: Int = 0

        ans = arg // Removed alot of test code here

        return ans
    }

    fun isBust(isP:Boolean):Boolean{
        // Over 21?
        var amt: Int

        if (isP) {
            // Is the player bust?
            amt = getPlayerTotal()
        } else {
            // Is the dealer bust?
            amt = getDealerTotal()
        }

        return amt > 21
    }

    fun isTooBroke():Boolean{
        // Is player too broke?
        val b1:Boolean = useCash()
        return b1 && SharedVM.Cash < getMinBet()
    }

    fun doTooBroke(){
        println("[i] Too broke, exiting..")
        doGotoMain()
    }

    fun useCash():Boolean{
        // Return the value of the slider
        return SharedVM.UseCash ?: true
    }

    fun getSuitName(s: Int):String{
        // Might not use, too long
        // see: Diamond Queen
        return AllSuits[s]
    }

    fun getSuitChar(s: Int):Char{
        return AllSuitsC[s]
    }

    fun getCardName(v: Int):String{
        // Value, not index
        // Returns the name of the card
        var ans = v.toString()
        val arg = v // Casting relic

        if (arg == 1 ) {
            // Search for alt
            ans = AllFaces[0]
        }else if (arg > 10) {
            ans = AllFaces[arg - 10]
        }

        return ans
    }

    fun getCardColor(s: Int):String{
        // Get colors of card
        // @@Todo Colored cards
        val arg = s % 2
        return AllColors[arg]
    }

    fun cardToStr(t: Card):String{
        // Build a sudo card
        var ans:String = "["

        ans = ans + getSuitChar(t.Suit) + getCardName(t.value) + "]"

        return ans
    }

    fun clickMain(view: View){
        // Reset for another round
        // Or boot player
        resetGame()

        if (isTooBroke()) {
            doTooBroke()
        }
    }

    fun clickHit(view: View){
        // Add a card to clients hand
        doHit(true)

    }
    fun clickDouble(view: View){
        // Double the bet
        doDouble()

    }
    fun clickStay(view: View){
        // Stay and end dealing
        doStay(true)

    }
    fun clickGotoMain(view: View){
        // Exit to the main menu
        doGotoMain()
    }

    fun doHit(side: Boolean){
        // Add a card to the hand
        drawCardFromDeck(side)
        if (isBust(true)) {
            checkGameStatus(false)
        }
    }

    fun doStay(side:Boolean){
        // Trigger the ending

        checkGameStatus(isBust(false))
    }

    fun doDouble(){
        // Dealer cant bet
        BetDoubled = true
        B_Double!!.visibility = View.INVISIBLE

    }

    public fun doGotoMain(){
        // Change fragment
        println("Leaving table...")
        //TV_CashM!!.setText("$ " + Cash)
        //startActivity(Intent(this, MainActivity::class.java))
        findNavController().navigate(R.id.navigation_home)
    }
}
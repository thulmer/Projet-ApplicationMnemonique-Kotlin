package com.example.jeumemonique.presentation.voirGroupe

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.VerifiedInputEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.jeumemonique.R
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirGroupes
import com.example.jeumemonique.presentation.voirGroupes.PrésentateurVoirGroupes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class VueVoirGroupe : Fragment(), IContratVuePrésentateurVoirGroupe.IVueVoirGroupe {
    lateinit var navController : NavController

    lateinit var présentateur : IContratVuePrésentateurVoirGroupe.IPrésentateurVoirGroupe

    lateinit var txtNomGroupe : TextView
    lateinit var btnRetour : ImageView

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager2
    lateinit var viewPagerAdapter: PagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_voir_groupe, container, false)


        présentateur = PrésentateurVoirGroupe(this)
        txtNomGroupe = vue.findViewById(R.id.txtNomGroupe)
        tabLayout = vue.findViewById(R.id.tabLayoutGroupe)
        viewPager = vue.findViewById(R.id.view_pager)
        viewPagerAdapter = PagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        attacherEcouteurOnglets()
        attacherEcouteurRetour(vue)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        présentateur.traiterInitialiser()
    }

    override fun afficherNomGroupe(nom : String) {
        txtNomGroupe.setText(nom)
    }

    override fun naviguerVersVoirGroupes() {
        navController.navigate(R.id.action_voirGroupe_to_voirGroupes)
    }

    private fun attacherEcouteurOnglets() {
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager.setCurrentItem(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

    }

    private fun attacherEcouteurRetour(vue : View){
        btnRetour = vue.findViewById<ImageView>(R.id.btnRetour)
        btnRetour.setImageResource(R.drawable.ic_retour)
        btnRetour.setOnClickListener {
            présentateur?.traiterRetour()
        }
    }

    override fun afficherMessageErreur(message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
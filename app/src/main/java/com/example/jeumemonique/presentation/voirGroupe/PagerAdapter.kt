package com.example.jeumemonique.presentation.voirGroupe

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jeumemonique.presentation.voirGroupes.VueVoirMembresGroupe
import com.example.jeumemonique.presentation.voirListesPartagées.VueVoirListesPartagées

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return VueVoirMembresGroupe()
            1 -> return VueVoirListesPartagées()
            else -> return VueVoirMembresGroupe()
        }
    }
}
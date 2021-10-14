package com.uitlab.nogadacompany.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.uitlab.nogadacompany.mainfragment.WebviewFg
import com.uitlab.nogadacompany.mainfragment.EmployeeCardFg
import com.uitlab.nogadacompany.mainfragment.NoticeFg
import com.uitlab.nogadacompany.user.User

class ViewpagerMainActivityAdapater(fa: FragmentActivity): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> EmployeeCardFg()
            1 -> NoticeFg()
            else -> WebviewFg()
        }
    }

}
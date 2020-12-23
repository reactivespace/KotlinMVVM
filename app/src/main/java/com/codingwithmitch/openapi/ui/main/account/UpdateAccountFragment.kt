package com.codingwithmitch.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_update_account.*


class UpdateAccountFragment : BaseAccountFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObserver()
    }


    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            Log.d(TAG, "UpdateAccountFragment: subscribeObserver: dataSate: ${dataState}")
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.accountProperties?.let {
                Log.d(TAG, "UpdatAccountFragment: subscribeObserver: ViewState: ${it}")
                setAccountDataFields(it)
            }
        })

    }

    private fun setAccountDataFields(accountProperties: AccountProperties) {
        input_email?.let {
            input_email.setText(accountProperties.email)
        }

        input_username?.let {
            input_username.setText(accountProperties.username)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                savChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savChanges() {
        viewModel.setStateEvent(
            AccountStateEvent.UpdateAccountPropertiesEvent(
                input_email.text.toString(),
                input_username.text.toString()
            )
        )
        stateChangeListener.hideSoftKeyboard()
    }


}
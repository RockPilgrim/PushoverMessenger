package my.rockpilgrim.pushovermessenger.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.send_fragment.*
import my.rockpilgrim.pushovermessenger.R
import my.rockpilgrim.pushovermessenger.databinding.SendFragmentBinding
import my.rockpilgrim.pushovermessenger.viewModel.SendViewModel
import my.rockpilgrim.pushovermessenger.viewModel.SendViewModel.State.*

class SendFragment : Fragment() {

    companion object{
        val TAG = SendFragment::class.java.simpleName
    }
    private lateinit var viewModel: SendViewModel
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate()")
        viewModel = ViewModelProvider(this).get(SendViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"onCreateView()")
        val binding = SendFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initUI()
    }

    private fun initUI() {
        historyButton.setOnClickListener {
            nextFragment()
        }
        sendButton.setOnClickListener {
            sendMessage()
        }
        scanButton.setOnClickListener {
            Log.i(TAG, "scanButton clicked()")
            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }

        messageEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                closeKeyBoard()
                sendMessage()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

    }

    private fun sendMessage() {
        clearSpace()
        viewModel.sendMessage(
            apiToken = getString(R.string.api_token),
            userKey = userKeyEditText.text.toString(),
            title = titleEditText.text.toString(),
            message = messageEditText.text.toString()
        )
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"onStart()")
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                Base -> clearSpace()
                Sent -> {
                    clearSpace()
                    makeToast(getString(R.string.sent))
                }
                Error -> makeToast(getString(R.string.error))
                is Edit -> fillFields(state.userKey,state.title,state.message)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG,"onActivityResult()")
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.contents == null) {
                    makeToast( "Cancelled")
                } else {
                    Log.i(TAG, "onActivityResult() ${result.contents}")
                    messageEditText.setText(result.contents)
                    saveState()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private fun fillFields(
        userKey: String?,
        title: String?,
        message: String?
    ) {
        userKeyEditText.setText(userKey)
        titleEditText.setText(title)
        messageEditText.setText(message)
    }

    private fun makeToast(line:String) {
        Toast.makeText(requireContext(), line, Toast.LENGTH_SHORT).show()
    }

    private fun clearSpace() {
        userKeyEditText.setText("")
        titleEditText.setText("")
        messageEditText.setText("")
    }

    private fun nextFragment() {
        val direction = SendFragmentDirections.actionSendFragmentToHistoryFragment()
        findNavController().navigate(direction)
    }

    private fun saveState() {
        viewModel.saveState(
            userKey = userKeyEditText.text.toString(),
            title = titleEditText.text.toString(),
            message = messageEditText.text.toString())
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop()")
        saveState()
    }

    private fun closeKeyBoard() {
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
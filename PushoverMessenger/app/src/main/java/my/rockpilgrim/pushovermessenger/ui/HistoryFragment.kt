package my.rockpilgrim.pushovermessenger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.history_fragment.*
import my.rockpilgrim.pushovermessenger.R
import my.rockpilgrim.pushovermessenger.adapter.MessageAdapter
import my.rockpilgrim.pushovermessenger.databinding.HistoryFragmentBinding
import my.rockpilgrim.pushovermessenger.viewModel.HistoryViewModel
import my.rockpilgrim.pushovermessenger.viewModel.HistoryViewModel.State.*

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        adapter = MessageAdapter(viewModel)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HistoryFragmentBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {state->
            when (state) {
                Empty -> empty(true)
                Error -> makeToast(getString(R.string.error))
                is History -> {
                    adapter.submitList(state.list)
                    empty(false)
                }
                Sent -> makeToast(getString(R.string.sent))
                else -> makeToast(getString(R.string.loading))
            }
        })
    }


    private fun makeToast(line: String) {
        Toast.makeText(requireContext(),line,Toast.LENGTH_SHORT).show()
    }

    private fun empty(isIt: Boolean) {
        if (isIt) {
            recyclerView.visibility = View.GONE
            informationTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            informationTextView.visibility = View.GONE
        }
    }

}
package ru.uomkri.tchktest.screens.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import io.reactivex.rxjava3.core.Observable
import ru.uomkri.tchktest.databinding.FragmentAuthBinding
import java.util.concurrent.TimeUnit

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAuthBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.buttonAuth.setOnClickListener {
            if (VK.isLoggedIn()) {
                requireView().findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToHomeFragment2())
            } else {
                VK.login(requireActivity(), arrayListOf(VKScope.WALL))
            }
        }

        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                Observable.timer(300, TimeUnit.MILLISECONDS)
                        .subscribe {
                            requireView().findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToHomeFragment2())
                        }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("dest", requireView().findNavController().currentDestination!!.label.toString())

        if (VK.isLoggedIn()) {
            requireView().findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToHomeFragment2())
        }
    }
}
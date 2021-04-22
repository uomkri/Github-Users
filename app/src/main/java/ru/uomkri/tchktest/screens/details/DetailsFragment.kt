package ru.uomkri.tchktest.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ru.uomkri.tchktest.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.getUser(args.username)

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.descUserName.text = it.name ?: ""
                binding.descUserLogin.text = it.login
                binding.descUserType.text = it.type
                binding.descUserCompany.text = it.company ?: ""

                Picasso.get()
                        .load(it.avatarUrl)
                        .fit()
                        .centerCrop()
                        .into(binding.userAvatar)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearSelectedUser()
    }
}
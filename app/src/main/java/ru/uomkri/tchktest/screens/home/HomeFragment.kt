package ru.uomkri.tchktest.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.requests.VKRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
import ru.uomkri.tchktest.R
import ru.uomkri.tchktest.databinding.FragmentHomeBinding
import ru.uomkri.tchktest.repo.net.VKAccount
import ru.uomkri.tchktest.utils.RecyclerItemClickListener
import ru.uomkri.tchktest.utils.UsersAdapter
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var listAdapter: UsersAdapter
    private val disposable = CompositeDisposable()


    @ExperimentalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        listAdapter = UsersAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        binding.searchText.doOnTextChanged { text, _, _, count ->
            if (count == 0) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyPlaceholder.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyPlaceholder.visibility = View.GONE

                val observable = viewModel.getSearchResults(text.toString())

                disposable.add(observable
                        .delaySubscription(600, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            listAdapter.submitData(lifecycle, it)
                            listAdapter.notifyDataSetChanged()
                        }
                )
            }
        }



        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        if (VK.isLoggedIn()) {
            getAccountData()
        }

        binding.recyclerView.addOnItemTouchListener(RecyclerItemClickListener(binding.recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val loginTextView: TextView = view.findViewById(R.id.user_login)
                        view.findNavController().navigate(HomeFragmentDirections.actionHomeFragment2ToDetailsFragment(loginTextView.text.toString()))
                    }
                }
        ))

        binding.drawer.setNavigationItemSelectedListener {
            if (it.itemId == R.id.logout) {
                VK.logout()
                parentFragmentManager.popBackStack()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        return binding.root
    }

    private fun getAccountData() {
        Observable.fromCallable {
            VK.executeSync(VKProfileRequest())
        }
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ a ->
                    if (a != null) {
                        val view = binding.drawer.getHeaderView(0)
                        val name: TextView = view.findViewById(R.id.name)
                        val screenName: TextView = view.findViewById(R.id.screen_name)
                        name.text = a.firstName + " " + a.lastName
                        screenName.text = a.screenName

                        getPhoto(a.id)

                    }
                }, { t ->
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                })
    }

    private fun getPhoto(id: Int) {
        Observable.fromCallable {
            VK.executeSync(VKPhotoRequest(uids = intArrayOf(id)))
        }
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { pic ->
                    if (pic != null) {
                        val img: ImageView = binding.drawer.getHeaderView(0).findViewById(R.id.userpic)

                        Picasso.get()
                                .load(pic)
                                .fit()
                                .centerCrop()
                                .into(img)
                    }
                }
    }

    class VKProfileRequest : VKRequest<VKAccount>("account.getProfileInfo") {
        override fun parse(r: JSONObject): VKAccount {
            val data = r.getJSONObject("response")
            return VKAccount(
                    firstName = data.getString("first_name"),
                    lastName = data.getString("last_name"),
                    screenName = data.getString("screen_name"),
                    id = data.getInt("id"),
                    profilePicture = null
            )
        }
    }

    class VKPhotoRequest(uids: IntArray = intArrayOf()) : VKRequest<String>("users.get") {
        init {
            if (uids.isNotEmpty()) {
                addParam("user_ids", uids.joinToString(","))
            }
            addParam("fields", "photo_100")
        }

        override fun parse(r: JSONObject): String {
            val data = r.getJSONArray("response")
            val usr = data.getJSONObject(0)
            return usr.getString("photo_100")
        }
    }
}
package com.example.warrantyreminder.ui.warranty

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentWarrantyBinding
import com.example.warrantyreminder.utils.Utils
import com.example.warrantyreminder.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_warranty.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WarrantyFragment : Fragment() {


    private var _binding: FragmentWarrantyBinding? = null
    private lateinit var itemId: String
    private lateinit var warrantyViewModel: WarrantyViewModel
    private val args: WarrantyFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val NOTIFICATION_ID = 0
    private val REQUEST_CODE = 0
    private val FLAGS = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWarrantyBinding.inflate(inflater, container, false)
        warrantyViewModel = ViewModelProvider(this).get(WarrantyViewModel::class.java)

        val warrantyItemId = args.warrantyItemId
        itemId = warrantyItemId


        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager

        createChannel(
            getString(R.string.egg_notification_channel_id),
            getString(R.string.egg_notification_channel_name)
        )


        viewLifecycleOwner.lifecycleScope.launch() {

            warrantyViewModel.apply {
                getWarrantyItem(itemId)
                warrantyItem.observe(viewLifecycleOwner, Observer {
                    tvItemName.text = it.itemName
                    tvItemDescription.text = it.itemDescription
                    tvExpiryDate.text = Utils.convertMillisToString(it.expirationDate)

                    Glide.with(requireContext())
                        .load(it.imageUrl)
                        .into(ivWarranty)
                })
            }

        }

        btn_notification.setOnClickListener {
            notificationManager.sendNotification("This is a notification", requireContext())
        }
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,

                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.breakfast_notification_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("TAG", "onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.edit_settings -> {
                editWarrantyItem()
                 true
            }
            else -> {
                findNavController().navigate(R.id.action_warrantyFragment_to_navigation_home)
                 true
            }
        }
    }



    private fun editWarrantyItem() {
        val bundle = Bundle().apply {
            putString("operationType", "EDITING")
            putString("warrantyItemId", itemId)
        }
        findNavController().navigate(
            R.id.action_warrantyFragment_to_addEditWarrantyFragment,
            bundle
        )
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
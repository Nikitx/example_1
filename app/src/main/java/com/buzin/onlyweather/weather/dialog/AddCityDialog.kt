package com.buzin.onlyweather.weather.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.buzin.onlyweather.R
import com.buzin.onlyweather.extensions.viewModelProvider
import com.buzin.onlyweather.util.MyUtil
import com.buzin.onlyweather.weather.ui.MainActivity
import dagger.android.support.DaggerDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


class AddCityDialog : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddCityDialogViewModel

    // ADAPTER
    private var btnOk: Button? = null
    private var btnCancel: Button? = null
    private var editTextCityName: EditText? = null
    private var tvTitle: TextView? = null
    private var isUserCloseDialog: Boolean = false


    // Use this instance of the interface to deliver action events
    private var mListener: AddCityDialogListener? = null

    // Override the Fragment.onAttach() method to instantiate the AddCityDialogListener
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AddCityDialogListener so we can send events to the host
            mListener = activity as AddCityDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                activity.toString()
                        + " must implement NoticeDialogListener"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
    }


    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_add_city, null)

        btnOk = v.findViewById(R.id.btn_ok)
        btnCancel = v.findViewById(R.id.btn_cancel)

        btnOk?.text = getString(R.string.btn_ok)
        btnCancel?.text = getString(R.string.btn_cancel)
        tvTitle = v.findViewById(R.id.title)
        tvTitle?.text = getString(R.string.ttl_new_city)

        editTextCityName = v.findViewById(R.id.editTextCityName)
        editTextCityName!!.requestFocus()
        isUserCloseDialog = true

        btnOk?.setOnClickListener {
            val cityName = editTextCityName!!.text.toString().trim { it <= ' ' }
            if (cityName.isEmpty()) {
                MyUtil.showToast(requireContext(), getString(R.string.toast_error_empty_field))
            } else {
                CoroutineScope(IO).launch {
                    viewModel.addNewCityByName(cityName)
                    mListener?.onDialogPositiveClick(this@AddCityDialog)
                    dialog?.dismiss()
                }
            }
        }
        btnCancel?.setOnClickListener {
            mListener?.onDialogNegativeClick(this@AddCityDialog)
            dialog?.dismiss()
        }

        val ad = AlertDialog.Builder(requireActivity())
        ad.setView(v)

        val d = ad.create()
        d.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.show()
        return d
    }

    companion object {
        const val CODE = R.id.DIALOG_ADD_CITY
        private var mActivity: FragmentActivity? = null


        fun newInstance(fragment: Fragment): AddCityDialog {
            val d = AddCityDialog()
            mActivity = fragment.activity
            d.setTargetFragment(fragment, CODE)
            val b = Bundle()
            d.arguments = b
            return d
        }

        fun newInstance(activity: MainActivity): AddCityDialog {
            val d = AddCityDialog()
            mActivity = activity
            val b = Bundle()
            d.arguments = b
            return d
        }
    }

    //https://developer.android.com/guide/topics/ui/dialogs?hl=ru
    interface AddCityDialogListener {
        fun onDialogPositiveClick(dialog: DaggerDialogFragment?)
        fun onDialogNegativeClick(dialog: DaggerDialogFragment?)
    }
}
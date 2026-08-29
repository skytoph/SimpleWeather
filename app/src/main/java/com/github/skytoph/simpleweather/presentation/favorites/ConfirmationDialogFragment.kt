package com.github.skytoph.simpleweather.presentation.favorites

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.github.skytoph.simpleweather.R

abstract class ConfirmationDialogFragment : DialogFragment() {

    //    @Suppress("UNCHECKED_CAST", "DEPRECATION")
//    private val callback: () -> Unit by lazy {
//        requireArguments().getSerializable(CALLBACK_KEY) as? () -> Unit ?: {}
//    }
//
//    @Suppress("UNCHECKED_CAST", "DEPRECATION")
//    private val cancelCallback: () -> Unit by lazy {
//        requireArguments().getSerializable(CANCEL_KEY) as? () -> Unit ?: {}
//    }
    @get:StringRes
    abstract val messageId: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(messageId))
            .setPositiveButton(getString(R.string.confirm)) { _, _ -> onConfirm() }
            .setNegativeButton(getString(R.string.dismiss)) { _, _ -> onCancel();dismiss() }
            .setCancelable(true)
            .create()

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancel()
    }

    abstract fun onConfirm()

    abstract fun onCancel()

    companion object {
        private const val CALLBACK_KEY = "key_callback"
        private const val CANCEL_KEY = "key_cancel"
        const val MESSAGE_ID_KEY = "key_message_id_res"

        fun newInstance(callback: () -> Unit, cancel: () -> Unit, @StringRes messageId: Int) =
            object : ConfirmationDialogFragment() {
                override val messageId: Int = messageId

                override fun onCancel() {
                    cancel()
                }

                override fun onConfirm() {
                    callback()
                }
            }
    }
}
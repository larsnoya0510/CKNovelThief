package com.example.cknovelthief.Model

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log


open class StatedFragment : Fragment() {

    internal var savedState: Bundle? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched()
        }
    }

    protected open fun onFirstTimeLaunched() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save State Here
        saveStateToArguments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Save State Here
        saveStateToArguments()
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private fun saveStateToArguments() {
        if (getView() != null)
            savedState = saveState()
        if (savedState != null) {
            //Log.d("watch","saveStateToArguments savedState: "+savedState)
            val b = getArguments()
                //Log.d("watch","saveStateToArguments b: "+b)
                b?.putBundle("internalSavedViewState_Target", savedState)
        }
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    fun restoreStateFromArguments(): Boolean {
        val b = getArguments()
        savedState = b?.getBundle("internalSavedViewState_Target")
        //Log.d("watch","restoreStateFromArguments savedState: "+savedState)
        if (savedState != null) {
            restoreState()
            return true
        }
        return false
    }

    /////////////////////////////////
    // Restore Instance State Here
    /////////////////////////////////

    private fun restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString(text));
            onRestoreState(savedState)
        }
    }

    protected open fun onRestoreState(savedInstanceState: Bundle?) {

    }

    //////////////////////////////
    // Save Instance State Here
    //////////////////////////////

    private fun saveState(): Bundle {
        val state = Bundle()
        // For Example
        //state.putString(text, tv1.getText().toString());
        onSaveState(state)
        return state
    }

    protected open fun onSaveState(outState: Bundle) {

    }
}
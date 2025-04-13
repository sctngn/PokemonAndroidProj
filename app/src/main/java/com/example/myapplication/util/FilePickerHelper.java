package com.example.myapplication.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

/**
 * Helper class for picking files for saving and loading Pokemon CSV data
 */
public class FilePickerHelper {
    private static final String TAG = "FilePickerHelper";
    
    // Interface for URI selection results
    public interface UriSelectionListener {
        void onUriSelected(Uri uri);
    }
    
    // ActivityResultLauncher for creating files (save)
    private ActivityResultLauncher<String> createDocumentLauncher;
    
    // ActivityResultLauncher for opening files (load)
    private ActivityResultLauncher<String[]> openDocumentLauncher;
    
    /**
     * Initialize the file picker with a Fragment
     * 
     * @param fragment Fragment to register launchers with
     * @param saveListener Listener for save URI selection
     * @param loadListener Listener for load URI selection
     */
    public FilePickerHelper(Fragment fragment, UriSelectionListener saveListener, UriSelectionListener loadListener) {
        // Register launcher for creating documents (saving)
        createDocumentLauncher = fragment.registerForActivityResult(
            new ActivityResultContracts.CreateDocument("text/csv"),
            uri -> {
                if (uri != null) {
                    Log.d(TAG, "Selected save URI: " + uri);
                    saveListener.onUriSelected(uri);
                }
            }
        );
        
        // Register launcher for opening documents (loading)
        openDocumentLauncher = fragment.registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) {
                    // Take persistable URI permission
                    int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    fragment.requireActivity().getContentResolver().takePersistableUriPermission(
                        uri, takeFlags);
                    
                    Log.d(TAG, "Selected load URI: " + uri);
                    loadListener.onUriSelected(uri);
                }
            }
        );
    }
    
    /**
     * Launch file picker for saving a Pokemon CSV file
     * 
     * @param defaultFileName Default file name suggestion
     */
    public void launchSaveFilePicker(String defaultFileName) {
        createDocumentLauncher.launch(defaultFileName);
    }
    
    /**
     * Launch file picker for loading a Pokemon CSV file
     */
    public void launchLoadFilePicker() {
        openDocumentLauncher.launch(new String[]{"text/csv"});
    }
}

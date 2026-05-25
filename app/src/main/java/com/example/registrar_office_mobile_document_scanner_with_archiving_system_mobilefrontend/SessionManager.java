package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "RegistrarAppSession";

    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ROLE = "role";
    private static final String KEY_STAFF_ID = "staff_id";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {

        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );

        editor = preferences.edit();
    }

    // Save Login Session
    public void saveLoginSession(
            String token,
            String role,
            String staffId
    ) {

        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_STAFF_ID, staffId);

        editor.apply();
    }

    // Check Login
    public boolean isLoggedIn() {
        return preferences.getBoolean(
                KEY_IS_LOGGED_IN,
                false
        );
    }

    // Logout
    public void logout() {

        editor.clear();
        editor.apply();
    }

    // Get Token
    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    // Get Role
    public String getRole() {
        return preferences.getString(KEY_ROLE, null);
    }

    // Get Staff ID
    public String getStaffId() {
        return preferences.getString(KEY_STAFF_ID, null);
    }
}
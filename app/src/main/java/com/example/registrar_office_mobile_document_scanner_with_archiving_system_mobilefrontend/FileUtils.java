package com.example.registrar_office_mobile_document_scanner_with_archiving_system_mobilefrontend;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileUtils {

    public static File copyUriToCache(Context context, Uri uri) throws Exception {
        String fileName = getFileName(context, uri);

        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "uploaded_document";
        }

        File file = new File(context.getCacheDir(), fileName);

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int length;

        while (inputStream != null && (length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        if (inputStream != null) {
            inputStream.close();
        }

        outputStream.close();

        return file;
    }

    public static String calculateSha256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        FileInputStream inputStream = new FileInputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        inputStream.close();

        byte[] hashBytes = digest.digest();

        StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;

        if ("content".equals(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    null
            );

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (result == null) {
            result = uri.getLastPathSegment();
        }

        return result;
    }
}
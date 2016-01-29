package com.zhaozihao.xunlian.zxing.activity;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class Utils {

                public static final boolean isChineseCharacter(String chineseStr) {
                char[] charArray = chineseStr.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                        // 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
                       if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')
                                || ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
                                continue;
                            } else {
                                return false;
                            }
                    }
                return true;
           }

                /**
     18.     * Get a file path from a Uri. This will get the the path for Storage Access
     19.     * Framework Documents, as well as the _data field for the MediaStore and
     20.     * other file-based ContentProviders.
     21.     *
     22.     * @param context
     23.     *            The context.
     24.     * @param uri
     25.     *            The Uri to query.
     26.     * @author paulburke
     27.     */
                public static String getPath(final Context context, final Uri uri) {

                final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

               // DocumentProvider
              if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                       // ExternalStorageProvider
                       if (isExternalStorageDocument(uri)) {
                                final String docId = DocumentsContract.getDocumentId(uri);
                               final String[] split = docId.split(":");
                              final String type = split[0];

                             if ("primary".equalsIgnoreCase(type)) {
                                     return Environment.getExternalStorageDirectory() + "/"
                                               + split[1];
                                    }

                               // TODO handle non-primary volumes
                         }
                    // DownloadsProvider
                      else if (isDownloadsDocument(uri)) {

                               final String id = DocumentsContract.getDocumentId(uri);
                               final Uri contentUri = ContentUris.withAppendedId(
                       Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                               return getDataColumn(context, contentUri, null, null);
                            }
                        // MediaProvider
                       else if (isMediaDocument(uri)) {
                              final String docId = DocumentsContract.getDocumentId(uri);
                                final String[] split = docId.split(":");
                              final String type = split[0];

                               Uri contentUri = null;
                                if ("image".equals(type)) {
                                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                   } else if ("video".equals(type)) {
                                       contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                   } else if ("audio".equals(type)) {
                                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                                    }

                               final String selection = "_id=?";
                               final String[] selectionArgs = new String[] { split[1] };

                               return getDataColumn(context, contentUri, selection,
                                               selectionArgs);
                            }
                    }
                // MediaStore (and general)
                else if ("content".equalsIgnoreCase(uri.getScheme())) {
                        return getDataColumn(context, uri, null, null);
                    }
               // File
                else if ("file".equalsIgnoreCase(uri.getScheme())) {
                        return uri.getPath();
                    }

               return null;
            }

               /**
     92.     * Get the value of the data column for this Uri. This is useful for
     93.     * MediaStore Uris, and other file-based ContentProviders.
     94.     *
     95.     * @param context
     96.     *            The context.
     97.     * @param uri
     98.     *            The Uri to query.
     99.     * @param selection
     100.     *            (Optional) Filter used in the query.
     101.     * @param selectionArgs
     102.     *            (Optional) Selection arguments used in the query.
     103.     * @return The value of the _data column, which is typically a file path.
     104.     */
               public static String getDataColumn(Context context, Uri uri,
                                                                   String selection, String[] selectionArgs) {

                Cursor cursor = null;
              final String column = "_data";
                final String[] projection = { column };

               try {
                        cursor = context.getContentResolver().query(uri, projection,
                                        selection, selectionArgs, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                final int column_index = cursor.getColumnIndexOrThrow(column);
                               return cursor.getString(column_index);
                            }
                  } finally {
                        if (cursor != null)
                                cursor.close();
                    }
                return null;
            }

                /**
     127.     * @param uri
     128.     *            The Uri to check.
     129.     * @return Whether the Uri authority is ExternalStorageProvider.
     130.     */
                public static boolean isExternalStorageDocument(Uri uri) {
                return "com.android.externalstorage.documents".equals(uri
                        .getAuthority());
            }

                /**
     137.     * @param uri
     138.     *            The Uri to check.
     139.     * @return Whether the Uri authority is DownloadsProvider.
     140.     */
                public static boolean isDownloadsDocument(Uri uri) {
               return "com.android.providers.downloads.documents".equals(uri
                       .getAuthority());
           }

               /**
     147.     * @param uri
     148.     *            The Uri to check.
     149.     * @return Whether the Uri authority is MediaProvider.
     150.     */
                public static boolean isMediaDocument(Uri uri) {
                return "com.android.providers.media.documents".equals(uri
                        .getAuthority());
            }

            }

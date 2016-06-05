package com.municipalidadavda.rn.global;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.municipalidadavda.modelo.global.Noticia;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio on 05/06/2016.
 */
public class NoticiasRN {

    private Context context;
    private HttpURLConnection con;

    public NoticiasRN(Context context) {
        this.context = context;
    }


}

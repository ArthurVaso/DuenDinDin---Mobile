package br.edu.ifsp.duendindin_mobile.utils;

import android.app.ProgressDialog;
import android.content.Context;

import br.edu.ifsp.duendindin_mobile.R;

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context, String titulo, String message, boolean permitirCancelar) {
        super(context, ProgressDialog.STYLE_SPINNER);
        setTitle(titulo);
        if (message != null)
            setMessage(message);
        else
            setMessage("Carregando...");

        setIndeterminate(true);
        setCancelable(permitirCancelar);

    }
}

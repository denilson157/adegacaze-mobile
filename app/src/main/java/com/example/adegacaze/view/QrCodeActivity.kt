package com.example.adegacaze.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import com.example.adegacaze.R
import com.example.adegacaze.databinding.ActivityQrCodeBinding
import com.example.adegacaze.showSnack
import com.google.zxing.BarcodeFormat

class QrCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityQrCodeBinding;
    lateinit var leitorQrCode: CodeScanner;
    var permissaoDeCameraConcedida = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        verificarPermissaoCamera()
    }


    private fun verificarPermissaoCamera() {
        if (!permissaoDeCameraConcedida)
            permissaoDeCameraConcedida = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED;


        if (permissaoDeCameraConcedida) {

            permissaoDeCameraConcedida = true;

            inicializarLeitor()

        } else {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1);

        }
    }


    private fun inicializarLeitor() {
        leitorQrCode = CodeScanner(this, binding.scannerView)
        configurarOpcoesCameraLeitor()
        iniciarAtividadeLeitor()
        tratarErroAtividadeLeitor()
    }

    private fun configurarOpcoesCameraLeitor() {
        leitorQrCode.camera = CodeScanner.CAMERA_BACK;
        leitorQrCode.formats = listOf(BarcodeFormat.QR_CODE);
        leitorQrCode.isAutoFocusEnabled = true;
        leitorQrCode.autoFocusMode = AutoFocusMode.SAFE;
        leitorQrCode.isFlashEnabled = false;
        leitorQrCode.scanMode = ScanMode.SINGLE;
    }

    private fun iniciarAtividadeLeitor() {
        leitorQrCode.setDecodeCallback {
            runOnUiThread {
                val respIntent = Intent();
                respIntent.putExtra("qrcode", it.text);
                setResult(RESULT_OK, respIntent);

                finish()
            }
        }
    }

    private fun tratarErroAtividadeLeitor() {
        leitorQrCode.setErrorCallback {
            runOnUiThread {
                val respIntent = Intent();
                respIntent.putExtra("errorText", "Não foi possível abrir a câmera");
                setResult(RESULT_CANCELED, respIntent);

                Log.e("QrCodeActivity", "tratarErroAtividade", it);

                finish()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            permissaoDeCameraConcedida = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissaoDeCameraConcedida)
                inicializarLeitor()
            else if (!shouldShowRequestPermissionRationale(permissions[0])) {

                mostrarDialogoPermissaoCamera()

            } else {

                val respIntent = Intent();
                respIntent.putExtra(
                    "errorText",
                    "Sem permissão de uso de câmera não é possível ler QR Codes"
                );
                setResult(RESULT_CANCELED, respIntent);

                finish()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (permissaoDeCameraConcedida)
            leitorQrCode.startPreview()
    }

    override fun onPause() {
        super.onPause()
        if (permissaoDeCameraConcedida)
            leitorQrCode.releaseResources()
    }

    private fun mostrarDialogoPermissaoCamera() {
        AlertDialog.Builder(this)
            .setTitle("Permissão da câmera")
            .setMessage("Habilite a permissão do uso da câmera em configurações para ler QR codes")
            .setCancelable(false)
            .setPositiveButton("Ir para configurações") { _, _ ->

                val respIntent = Intent();

                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                i.data = Uri.fromParts("package", packageName, null)

                startActivity(i)

                respIntent.putExtra("errorText", "Não foi possível abrir a câmera");

                setResult(RESULT_CANCELED)
                finish()
            }
            .setNegativeButton("Cancelar") { _, _ ->
                setResult(RESULT_CANCELED)
                finish()
            }
            .create()
            .show()
    }

}
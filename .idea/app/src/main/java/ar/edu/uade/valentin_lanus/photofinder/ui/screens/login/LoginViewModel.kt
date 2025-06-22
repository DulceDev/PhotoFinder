package ar.edu.uade.valentin_lanus.photofinder.ui.screens.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import ar.edu.uade.valentin_lanus.photofinder.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getGoogleSignInClient(context: android.content.Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return  GoogleSignIn.getClient(context, gso)
    }

    fun handleSignInResult(
        data: Intent?,
        context: android.content.Context,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val  account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure("Error al auntenticar con Firebase")
                    }
                }
        } catch (e: Exception) {
            onFailure(e.message ?: "Error desconocido")
        }
    }
}
package com.imadev.foody.ui.auth



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentLoginBinding
import com.imadev.foody.fcm.MyFirebaseMessagingService
import com.imadev.foody.model.Client
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.Constants.Companion.CLIENTS_COLLECTION
import com.imadev.foody.utils.Constants.Companion.RC_SIGN_IN
import com.imadev.foody.utils.moveTo

private const val TAG = "LoginFragment"

class LoginFragment : BaseFragment<FragmentLoginBinding, AuthViewModel>() {


    val db = Firebase.firestore


    override val viewModel: AuthViewModel by viewModels()

    // Configure Google Sign In
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private var callbackManager: CallbackManager? = null

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        callbackManager = CallbackManager.Factory.create();

        binding.facebookSignInBtn.setOnClickListener {
            signInWithFacebook()
        }


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)



        binding.googleSignInBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signInWithFacebook() {
        callbackManager?.let {
            LoginManager.getInstance().logInWithReadPermissions(this,
                it, listOf("email", "public_profile", "user_friends"))
        };
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {

            override fun onCancel() {
                TODO("Not yet implemented")
            }


            override fun onError(error: FacebookException) {
                Log.d(TAG, "onError: ${error.message}")
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }


            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "onSuccess: ${result?.accessToken?.token.toString()}")
                handleFacebookAccessToken(result.accessToken)
            }
        })
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener{task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    saveUser(
                        user?.uid,
                        username = user?.displayName,
                        phone = user?.phoneNumber,
                        email = user?.email
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed ${e.message}", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUser(
                        uid = user?.uid,
                        username = user?.displayName,
                        phone = user?.phoneNumber,
                        email = user?.email
                    )


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)

                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }

    override fun setToolbarTitle(activity: MainActivity) {
        TODO("Not yet implemented")
    }


    private fun saveUser(uid: String?, username: String?, phone: String?, email: String?) {
        val fcmToken = MyFirebaseMessagingService.getToken(requireContext())
        val client = Client(
            username = username,
            address = null,
            phone = phone,
            email = email,
            token = fcmToken
        )

        if (uid != null) {
            db.collection(CLIENTS_COLLECTION).document(uid).set(client).addOnFailureListener {
                Log.d(TAG, "saveUser: ${it.message}")
            }.addOnSuccessListener {
                moveTo(MainActivity::class.java)
            }
        }

    }


}
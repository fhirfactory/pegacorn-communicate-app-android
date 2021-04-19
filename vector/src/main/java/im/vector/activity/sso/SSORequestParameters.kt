package im.vector.activity.sso

import com.google.gson.annotations.SerializedName

data class SSORequestParameters (
        @JvmField
        @SerializedName("token")
        val token: String,
        @JvmField
        @SerializedName("type")
        val type: String = "m.login.token"
)
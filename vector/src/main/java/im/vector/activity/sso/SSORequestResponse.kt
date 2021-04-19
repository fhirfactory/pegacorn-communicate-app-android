package im.vector.activity.sso

import com.google.gson.annotations.SerializedName

data class SSORequestResponse(
        @JvmField
        @SerializedName("user_id")
        val userID: String,
        @JvmField
        @SerializedName("access_token")
        val accessToken: String,
        @JvmField
        @SerializedName("home_server")
        val homeServer: String,
        @JvmField
        @SerializedName("device_id")
        val deviceId: String
)
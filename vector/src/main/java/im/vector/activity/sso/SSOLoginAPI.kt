package im.vector.activity.sso

import org.matrix.androidsdk.rest.model.identityserver.IdentityAccountResponse
import org.matrix.androidsdk.rest.model.identityserver.IdentityServerRegisterResponse
import org.matrix.androidsdk.rest.model.openid.RequestOpenIdTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SSOLoginAPI {

    /**
     * register to the server
     *
     * @param body the body content
     */
    @POST("_matrix/client/r0/login")
    fun login(@Body SSOLoginParameters: SSORequestParameters): Call<SSORequestResponse>

}
package domain.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ClientResponse {
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(
        @Field("username") String username,
        @Field("password") String password
    );
}

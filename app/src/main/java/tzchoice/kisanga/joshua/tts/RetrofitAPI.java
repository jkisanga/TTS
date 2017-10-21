package tzchoice.kisanga.joshua.tts;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Url;
import tzchoice.kisanga.joshua.tts.Pojo.RefAction;
import tzchoice.kisanga.joshua.tts.Pojo.Tp;
import tzchoice.kisanga.joshua.tts.Pojo.TpInspection;
import tzchoice.kisanga.joshua.tts.Pojo.TransitPass;
import tzchoice.kisanga.joshua.tts.Pojo.User;

/**
 * Created by user on 2/27/2017.
 */

public interface RetrofitAPI {

    @POST("checkpoint_users")
    Call<User> postLogin(@Body User user);

    @GET("tpByCheckpoint/{checkpoint_id}")
    Call<List<Tp>> getTPasses(@Path("checkpoint_id") int checkpoint_id);

    @GET("tpByCheckpointExpired/{checkpoint_id}")
    Call<List<Tp>> getTPExpired(@Path("checkpoint_id") int checkpoint_id);
//


    @GET("tp_inspections/{tp_id}")
    Call<List<TpInspection>> getTpInspection(@Path("tp_id") int tp_id);


    @POST("tp_inspections")
    Call<TpInspection> sendTpInspection(@Body TpInspection inspection);

    @GET("search_query/{tp_no}")
    Call<TransitPass> sendTpNoToServer(@Path("tp_no") String tp_no);

    @GET("actions")
    Call<List<RefAction>> getActions();

    @GET("units")
    Call<List<RefAction>> getUnits();

    @GET("irregularities")
    Call<List<RefAction>> getIrregularities();

    @GET("forest_products")
    Call<List<RefAction>> getProducts();

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @GET("download_apk")
    Call<ResponseBody> downloadFileWithFixedUrl();
}

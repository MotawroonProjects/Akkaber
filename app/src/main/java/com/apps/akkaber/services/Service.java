package com.apps.akkaber.services;


import com.apps.akkaber.model.CartDataModel;
import com.apps.akkaber.model.DepartmentDataModel;
import com.apps.akkaber.model.OrderDataModel;
import com.apps.akkaber.model.PlaceMapDetailsData;
import com.apps.akkaber.model.ProductDataModel;
import com.apps.akkaber.model.NotificationDataModel;
import com.apps.akkaber.model.PlaceGeocodeData;
import com.apps.akkaber.model.SettingDataModel;
import com.apps.akkaber.model.ShipModel;
import com.apps.akkaber.model.SingleDepartmentDataModel;
import com.apps.akkaber.model.SingleOrderDataModel;
import com.apps.akkaber.model.SingleProductDataModel;
import com.apps.akkaber.model.SliderDataModel;
import com.apps.akkaber.model.StatusResponse;
import com.apps.akkaber.model.UserModel;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("geocode/json")
    Single<Response<PlaceGeocodeData>> getGeoData(@Query(value = "latlng") String latlng,
                                                  @Query(value = "language") String language,
                                                  @Query(value = "key") String key);


    @FormUrlEncoded
    @POST("api/login")
    Single<Response<UserModel>> login(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/register")
    Single<Response<UserModel>> signUp(@Field("first_name") String first_name,
                                       @Field("last_name") String last_name,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("register_by") String register_by


    );

    @GET("place/findplacefromtext/json")
    Single<Response<PlaceMapDetailsData>> searchOnMap(@Query(value = "inputtype") String inputtype,
                                                      @Query(value = "input") String input,
                                                      @Query(value = "fields") String fields,
                                                      @Query(value = "language") String language,
                                                      @Query(value = "key") String key);


    @Multipart
    @POST("api/register")
    Observable<Response<UserModel>> signUpwithImage(@Part("first_name") RequestBody first_name,
                                                    @Part("last_name") RequestBody last_name,
                                                    @Part("phone_code") RequestBody phone_code,
                                                    @Part("phone") RequestBody phone,
                                                    @Part("register_by") RequestBody register_by,
                                                    @Part MultipartBody.Part logo


    );

    @FormUrlEncoded
    @POST("api/updateProfile")
    Single<Response<UserModel>> editprofile(@Field("first_name") String first_name,
                                            @Field("last_name") String last_name,
                                            @Field("user_id") String user_id


    );

    @Multipart
    @POST("api/updateProfile")
    Observable<Response<UserModel>> editprofilewithImage(@Part("first_name") RequestBody first_name,
                                                         @Part("last_name") RequestBody last_name,
                                                         @Part("user_id") RequestBody user_id,
                                                         @Part MultipartBody.Part logo


    );

    @FormUrlEncoded
    @POST("api/logout")
    Single<Response<StatusResponse>> logout(@Field("user_id") String user_id,
                                            @Field("token") String token


    );

    @FormUrlEncoded
    @POST("api/store_user_token")
    Single<Response<StatusResponse>> updateFirebasetoken(@Field("token") String token,
                                                         @Field("user_id") String user_id,
                                                         @Field("type") String type


    );


    @GET("api/my_notifications")
    Single<Response<NotificationDataModel>> getNotifications(@Query(value = "user_id") String user_id);


    @GET("api/my_favourites")
    Single<Response<ProductDataModel>> getFavourites(@Header("lang") String lang,
                                                     @Query(value = "user_id") int user_id);

    @GET("api/banners")
    Single<Response<SliderDataModel>> getSlider();

    @GET("api/categories")
    Single<Response<DepartmentDataModel>> getDepartments(@Header("lang") String lang);

    @GET("api/getCategoryById")
    Single<Response<SingleDepartmentDataModel>> getSingleDepartment(@Header("lang") String lang,
                                                                    @Query(value = "id") String id);

    @GET("api/offers")
    Single<Response<ProductDataModel>> getOffers(@Header("lang") String lang);

    @GET("api/box")
    Single<Response<SingleProductDataModel>> getBox(@Header("lang") String lang);

    @GET("api/featured")
    Single<Response<DepartmentDataModel>> getFeatured(@Header("lang") String lang);

    @GET("api/product")
    Single<Response<SingleProductDataModel>> getSingleProduct(@Header("lang") String lang,
                                                              @Query(value = "user_id") String user_id,
                                                              @Query(value = "id") String id
    );

    @GET("api/settings")
    Single<Response<SettingDataModel>> getSetting(@Header("lang") String lang);

    @FormUrlEncoded
    @POST("api/contact_us")
    Single<Response<StatusResponse>> contactUs(@Field("name") String name,
                                               @Field("email") String email,
                                               @Field("title") String title,
                                               @Field("message") String message);

    @FormUrlEncoded
    @POST("api/make_favourite")
    Single<Response<StatusResponse>> addRemoveFav(@Field("user_id") String user_id,
                                                  @Field("product_id") String product_id);

    @GET("api/shipping_price")
    Single<Response<ShipModel>> getship(@Query("latitude") String latitude,
                                        @Query("longitude") String longitude);

    @POST("api/store_order")
    Single<Response<StatusResponse>> sendOrder(@Body CartDataModel cartDataModel
    );

    @GET("api/my_orders")
    Single<Response<OrderDataModel>> getMyOrders(@Query("user_id") String user_id);

    @GET("api/order_details")
    Single<Response<SingleOrderDataModel>> getSingleOrders(@Query(value = "order_id") String order_id);

    @GET("api/profile")
    Single<Response<UserModel>> getProfile(@Query("id") String id);
}
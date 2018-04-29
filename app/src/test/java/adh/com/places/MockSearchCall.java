package adh.com.places;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockSearchCall<T> implements Call<T> {

  private T mBody;

  MockSearchCall(T t) {
    mBody = t;
  }

  @Override
  public Response<T> execute() throws IOException {
    return Response.success(mBody);
  }

  @Override
  public void enqueue(Callback<T> callback) {
    Response<T> response = Response.success(mBody);
    callback.onResponse(this, response);
  }

  @Override
  public boolean isExecuted() {
    return false;
  }

  @Override
  public void cancel() {

  }

  @Override
  public boolean isCanceled() {
    return false;
  }

  @Override
  public Call<T> clone() {
    return null;
  }

  @Override
  public Request request() {
    return null;
  }
}
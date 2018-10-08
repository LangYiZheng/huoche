package utils;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dell003 on 2018/6/12.
 */

public abstract class NetWorkCallback<T> {
   public Type mType;
   public static Type getSuperclassTypeParameter(Class<?> subclass){
      Type superclass = subclass.getGenericSuperclass();
      if(superclass instanceof Class){
         throw new RuntimeException("Missing type parameter");
      }

      ParameterizedType parameterized = (ParameterizedType) superclass;
      return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
   }

   public NetWorkCallback() {
      this.mType = getSuperclassTypeParameter(getClass());
   }

   public abstract void onLoadingBefore(Request request);

   public abstract void onSuccess(Response response,T result);

   public abstract void onError(Response response);

   public abstract void onFailure(Request request,Exception e);
}

package com.gstore.gstoreapi.converters;

public interface ObjectConverter<T, U> {

    T convertSecondToFirst (U u);
    U convertFirstToSecond (T t);

}

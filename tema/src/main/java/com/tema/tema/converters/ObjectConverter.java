package com.tema.tema.converters;

public interface ObjectConverter<T, U> {

    T convertSecondToFirst (U u);
    U convertFirstToSecond (T t);

}

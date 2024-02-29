package com.gstore.gstoreapi.converters;

import com.gstore.gstoreapi.models.dtos.QuantityDTO;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CartConverter {

    public static List<QuantityDTO> convertCartStringToQuantityList(String cart) {
        if (cart == null || cart.isEmpty()) {
            return new ArrayList<>();
        }
        String[] cartArray = cart.split("[,:]+");
        List<QuantityDTO> quantityList = new ArrayList<>(1);

        for (int i = 0; i < cartArray.length; i += 2) {
            quantityList.add(new QuantityDTO(Long.parseLong(cartArray[i]), Integer.parseInt(cartArray[i + 1])));
        }

        return quantityList;
    }

    public static String convertQuantityArrayToCartString(List<QuantityDTO> quantities) {
        if (quantities.get(0).quantity() == null) {
            return null;
        }
        String cart = new String("");

        for (int i = 0; i < quantities.size(); i++) {
            if (i == quantities.size() - 1) {
                cart = cart.concat(String.format("%d:%d", quantities.get(i).productId(), quantities.get(i).quantity()));
                continue;
            }
            cart = cart.concat(String.format("%d:%d,", quantities.get(i).productId(), quantities.get(i).quantity()));
        }
        return cart;
    }

}

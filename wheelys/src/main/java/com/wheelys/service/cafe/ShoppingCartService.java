package com.wheelys.service.cafe;

import java.util.List;
import java.util.Map;

import com.wheelys.model.CartProduct;

public interface ShoppingCartService {

	List<CartProduct> getMemberCart(String memberKey);

	void addProductToCart(String memberKey, CartProduct productVo);

	void removeProductToCart(String memberKey, CartProduct productVo,int quantity);

	void reset(String memberKey);

	void remove(String memberKey, CartProduct productVo);

	Map getMemberCartMap(String memberKey, String cartkey);

}

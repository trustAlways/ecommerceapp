

package com.medassi.ecommerce.user.Activity.Model;

import com.medassi.ecommerce.user.Activity.Model.Search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CenterRepository {

    private static CenterRepository centerRepository;

    //private ArrayList<ProductCategoryModel> listOfCategory = new ArrayList<ProductCategoryModel>();
    private ConcurrentHashMap<String, ArrayList<Search>> mapOfProductsInCategory = new ConcurrentHashMap<String, ArrayList<Search>>();
    private List<Search> listOfProductsInShoppingList = Collections.synchronizedList(new ArrayList<Search>());
    private List<CartList> listOfProductsInCart = new ArrayList<CartList>();

    private List<Set<String>> listOfItemSetsForDataMining = new ArrayList<>();

    public static CenterRepository getCenterRepository() {

        if (null == centerRepository) {
            centerRepository = new CenterRepository();
        }
        return centerRepository;
    }


    public List<Search> getListOfProductsInShoppingList() {
        return listOfProductsInShoppingList;
    }

    public List<CartList> getListOfProductsInCart()
    {
        return listOfProductsInCart;
    }

    public void setListOfProductsInShoppingList(ArrayList<Search> getShoppingList) {
        this.listOfProductsInShoppingList = getShoppingList;
    }

    public Map<String, ArrayList<Search>> getMapOfProductsInCategory() {

        return mapOfProductsInCategory;
    }

    public void setMapOfProductsInCategory(ConcurrentHashMap<String, ArrayList<Search>> mapOfProductsInCategory) {
        this.mapOfProductsInCategory = mapOfProductsInCategory;
    }

    /*public ArrayList<ProductCategoryModel> getListOfCategory() {

        return listOfCategory;
    }*/

   /* public void setListOfCategory(ArrayList<ProductCategoryModel> listOfCategory) {
        this.listOfCategory = listOfCategory;
    }*/

    public List<Set<String>> getItemSetList() {

        return listOfItemSetsForDataMining;
    }

    public void addToItemSetList(HashSet list) {
        listOfItemSetsForDataMining.add(list);
    }

}

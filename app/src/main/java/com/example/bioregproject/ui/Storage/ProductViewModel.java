package com.example.bioregproject.ui.Storage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.bioregproject.Repositories.ProduitRepository;
import com.example.bioregproject.entities.Produit;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProduitRepository produitRepository;
    private LiveData<List<Produit>> allproduit;
    private LiveData<List<Produit>> getproduit;


    public ProductViewModel(@NonNull Application application) {
        super(application);
        produitRepository = new ProduitRepository(application);
        allproduit = produitRepository.getAllProduit();
    }


    public LiveData<List<Produit>> getAllproduit() {
        return allproduit;
    }
    public LiveData<List<Produit>> getProduitByCategorie(String categorie) {
        getproduit = produitRepository.getProduitByCategorie(categorie);

        return getproduit;
    }

    public void insert(Produit produit) {
        produitRepository.insert(produit);
    }
    public void update(Produit produit){produitRepository.update(produit);}
    public void delete(Produit produit) {
        produitRepository.delete(produit);
    }

}

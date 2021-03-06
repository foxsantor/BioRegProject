package com.example.bioregproject.ui.Settings.GeneralSettings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bioregproject.Repositories.CategorieRepository;
import com.example.bioregproject.Repositories.PostRepository;
import com.example.bioregproject.Repositories.SettingOilRepository;
import com.example.bioregproject.Repositories.SurfaceRepository;
import com.example.bioregproject.entities.CategorieTache;
import com.example.bioregproject.entities.Post;
import com.example.bioregproject.entities.Realtions.CategorywithSurfaces;
import com.example.bioregproject.entities.SettingOil;
import com.example.bioregproject.entities.Surface;

import java.util.List;

public class GeneralSettingsViewModel extends AndroidViewModel {
    private CategorieRepository repository;
    private SurfaceRepository repositorySurface;
    private PostRepository postRepository;
    private SettingOilRepository settingOilRepository;
    private LiveData<List<CategorieTache>> allCategorie;
    private LiveData<List<CategorywithSurfaces>>allSurface;
    private LiveData<List<CategorywithSurfaces>>categories;
    private LiveData<List<Post>> allPost;
    private LiveData<List<SettingOil>> allSetting;


    public GeneralSettingsViewModel(@NonNull Application application) {
        super(application);
        repository=new CategorieRepository(application);
        allCategorie=repository.getAllCategories();
        categories=repository.getCategoriesWithSurfaces();
        repositorySurface = new SurfaceRepository(application);
        allSurface=repository.getCategoriesWithSurfaces();
        postRepository = new PostRepository(application);
        allPost = postRepository.getAllPost();
        settingOilRepository = new SettingOilRepository(application);
        allSetting=settingOilRepository.getallSettingOils();

    }

    public void insert(CategorieTache cat){repository.insertOne(cat);}
    public void update(CategorieTache cat){repository.update(cat);}
    public void delete(CategorieTache cat){repository.delete(cat);}
    public LiveData<List<CategorieTache>>getAllCategories(){return allCategorie;}
    public void insert(Surface surface){repositorySurface.insertOne(surface);}
    public void update(Surface surface){repositorySurface.update(surface);}
    public void delete(Surface surface){repositorySurface.delete(surface);}
    public LiveData<List<CategorywithSurfaces>>getAllSurface(){return allSurface;}
    public LiveData<List<CategorywithSurfaces>>getCategories(){return categories;}

    public LiveData<List<Post>> getAllPost(){ return allPost; }
    public void insertOil(Post post) {
        postRepository.insert(post);
    }
    public void deleteOil(Post post) { postRepository.delete(post); }
    public void updateOil(Post post) {
        postRepository.update(post);
    }


    public LiveData<List<SettingOil>> getAllSetting(){ return allSetting; }
    public void updateSetting(SettingOil settingOil) {
        settingOilRepository.update(settingOil);
    }
    public void insertSetting(SettingOil settingOil) {
        settingOilRepository.insert(settingOil);
    }







}

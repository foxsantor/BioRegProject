package com.example.bioregproject.ui.Settings.AccountMangement;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.bioregproject.Repositories.AccountRepository;
import com.example.bioregproject.entities.Account;

import java.util.List;

public class UpdateAccountViewModel extends AndroidViewModel {

    private AccountRepository repository;
    private LiveData<List<Account>> allAccounts;



    public UpdateAccountViewModel(@NonNull Application application) {
        super(application);
        repository = new AccountRepository(application);
        allAccounts = repository.getAllAccounts();
    }

    public void insert(Account account)
    {
        repository.insert(account);
    }
    public LiveData<List<Account>> getAccount(Long id) {return repository.getAccount(id);}
    public void update(Account account)
    {
        repository.update(account);
    }
    public void delete(Account account)
    {
        repository.delete(account);
    }
    public void deleteAll()
    {
        repository.deleteAllAccounts();
    }
    public LiveData<List<Account>> getAllAccounts()
    {
        return allAccounts;
    }
}

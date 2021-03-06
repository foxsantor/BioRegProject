package com.example.bioregproject.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.bioregproject.DAOs.AccountDAO;
import com.example.bioregproject.entities.Account;
import com.example.bioregproject.DataBases.BioRegDB;

import java.util.List;

import io.reactivex.Flowable;

public class AccountRepository {
    private AccountDAO accountDAO;
    private LiveData<List<Account>> allAccounts;

    public AccountRepository (Application application)
    {
        BioRegDB database = BioRegDB.getInstance(application);
        accountDAO = database.accountDao();
        allAccounts = accountDAO.getAllAccounts();
    }

    public void insert(Account account){

        new InsertAccountAsynTask(accountDAO).execute(account);

    }
    public void delete(Account account){

        new DeleteAccountAsynTask(accountDAO).execute(account);
    }
    public void update(Account account){

        new UpdateAccountAsynTask(accountDAO).execute(account);
    }
    public void deleteAllAccounts(){

        new DeleteAllAccountsAsynTask(accountDAO).execute();
    }
    public LiveData<List<Account>> getAllAccounts(){

        return allAccounts;
    }
    public LiveData<List<Account>> getAccount(long id)
    {
        return accountDAO.loadAccountById(id);
    }

    public LiveData<List<Account>> getAccountByEmail(String email)
    {
        return accountDAO.loadAccountByEmail(email);
    }

    private static class InsertAccountAsynTask extends AsyncTask<Account,Void,Void>
    {
        private AccountDAO accountDAO;
        private InsertAccountAsynTask(AccountDAO accountDAO)
        {
            this.accountDAO=accountDAO;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.insert(accounts[0]);
            return null;
        }
    }
    private static class DeleteAccountAsynTask extends AsyncTask<Account,Void,Void>
    {
        private AccountDAO accountDAO;
        private DeleteAccountAsynTask(AccountDAO accountDAO)
        {
            this.accountDAO=accountDAO;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.delete(accounts[0]);
            return null;
        }
    }
    private static class UpdateAccountAsynTask extends AsyncTask<Account,Void,Void>
    {
        private AccountDAO accountDAO;
        private UpdateAccountAsynTask(AccountDAO accountDAO)
        {
            this.accountDAO=accountDAO;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.update(accounts[0]);
            return null;
        }
    }
    private static class DeleteAllAccountsAsynTask extends AsyncTask<Void,Void,Void>
    {
        private AccountDAO accountDAO;
        private DeleteAllAccountsAsynTask(AccountDAO accountDAO)
        {
            this.accountDAO=accountDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            accountDAO.deleteAllAccounts();
            return null;
        }
    }


}

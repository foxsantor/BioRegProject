package com.example.bioregproject.ui.History;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.bioregproject.DAOs.AccountDAO;
import com.example.bioregproject.DAOs.HistoryDAO;
import com.example.bioregproject.Repositories.AccountRepository;
import com.example.bioregproject.Repositories.HistoryRepository;
import com.example.bioregproject.entities.Account;
import com.example.bioregproject.entities.History;

import java.util.List;

public class DeviceHistoryViewModel extends AndroidViewModel {
    private AccountRepository repository;
    private HistoryRepository HistoryRepository;
    private LiveData<List<Account>> allAccounts;
    private LiveData<List<History>> allHistory;

    public DeviceHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new AccountRepository(application);
        allAccounts = repository.getAllAccounts();
        HistoryRepository = new HistoryRepository(application);
        allHistory = HistoryRepository.getAllHistory();
    }

    public void insert(History History)
    {
        HistoryRepository.insert(History);
    }
    public void update(History History)
    {
        HistoryRepository.update(History);
    }
    public void insert(Account account)
    {
        repository.insert(account);
    }
    public void deleteAllNotif()
    {
        HistoryRepository.deleteAllHistory();
    }
    public void delete(History History)
    {
        HistoryRepository.delete(History);
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
    public LiveData<List<History>> getAllHistorys()
    {
        return allHistory;
    }


    public LiveData<PagedList<History>> teamAllList;
    public MutableLiveData<String> filterTextAll = new MutableLiveData<>();
    public HistoryDAO teamDao;

    public void initAllTeams(final HistoryDAO teamDao) {
        this.teamDao = teamDao;
        final PagedList.Config config = (new PagedList.Config.Builder())
                .setPageSize(10)
                .build();
        teamAllList = Transformations.switchMap(filterTextAll, new Function<String, LiveData<PagedList<History>>>() {
            @Override
            public LiveData<PagedList<History>> apply(String input) {
                if (input == null || input.equals("") || input.equals("%%")) {
//check if the current value is empty load all data else search
                    return new LivePagedListBuilder<>(
                            teamDao.loadAllHistory(), config)
                            .build();
                } else {
                    //System.out.println("CURRENTINPUT: " + input);
                    return new LivePagedListBuilder<>(
                            teamDao.loadAllHistorybyName(input), config)
                            .build();
                }
            }
        });

    }


}

package Controller;

import java.util.List;

import DbHelper.TellerDAO;
import Model.TellersModel;

public class TellerController {
    

  

    private List<TellersModel> tellers;
    private int index = -1;

    public boolean hasNext(){
        return index < tellers.size() - 1;
    }
    public TellersModel next(){
        index++;
        return tellers.get(index);
    }

    public void fetchAllTeller(){
        tellers = TellerDAO.getAllTellers();
    }

    public TellersModel findTeller(int tellerId){
        return TellerDAO.getTellerById(tellerId);
    }

    public TellersModel verifyLogin(String username, int pass){
        return TellerDAO.getTellerByUserAndPass(username, pass);
    }

    public List<TellersModel> getTellers() {
        return tellers;
    }
    
    public void updateLogin(int id){
        TellerDAO.updateLoginDate(id);
    }

    public String getTellerLastLoginDate(int id){
        return TellerDAO.getFormattedDate(id);
    }


}

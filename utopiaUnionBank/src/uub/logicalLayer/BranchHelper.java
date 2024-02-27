package uub.logicalLayer;

import java.util.List;

import uub.model.Branch;
import uub.persistentLayer.BranchDao;
import uub.persistentLayer.IBranchDao;
import uub.staticLayer.CustomBankException;

public class BranchHelper {

	public List<Branch> getAllBranches() throws CustomBankException{
		
		IBranchDao branchDao = new BranchDao();
		
		return branchDao.getBranches();
	}
	
}

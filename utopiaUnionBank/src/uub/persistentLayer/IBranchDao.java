package uub.persistentLayer;

import java.util.List;

import uub.model.Branch;
import uub.staticLayer.CustomBankException;

public interface IBranchDao {

	
	

	public List<Branch> getBranches() throws CustomBankException;
	
	public void addBranch(List<Branch> branches) throws CustomBankException;

	int getLastId() throws CustomBankException;

	List<Branch> updateBranches(Branch branch) throws CustomBankException;
	
		
}

package uub.persistentinterfaces;

import java.util.List;

import uub.model.Branch;
import uub.staticLayer.CustomBankException;

public interface IBranchDao {

	
	

	public List<Branch> getBranches() throws CustomBankException;
	
	public void addBranch(List<Branch> branches) throws CustomBankException;

	int getLastId() throws CustomBankException;

	void updateBranches(Branch branch) throws CustomBankException;

	List<Branch> getBranchWithId(int id) throws CustomBankException;
	
		
}

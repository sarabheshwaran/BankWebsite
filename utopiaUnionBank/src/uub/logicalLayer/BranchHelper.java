package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uub.model.Branch;
import uub.persistentLayer.IBranchDao;
import uub.staticLayer.CustomBankException;

public class BranchHelper {

	private IBranchDao branchDao;

	public BranchHelper() throws CustomBankException {

		try {

			Class<?> BranchDao = Class.forName("uub.persistentLayer.BranchDao");
			Constructor<?> branDao = BranchDao.getDeclaredConstructor();

			branchDao = (IBranchDao) branDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			
			throw new CustomBankException("Error getting Data ! ", e);
			
		}

	}

	public List<Branch> getAllBranches() throws CustomBankException {

		return branchDao.getBranches();
	}

	public void addBranch(Branch branch) throws CustomBankException {

		int id = branchDao.getLastId();

		String ifsc = "UUB" + String.format("%04d", id);

		branch.setId(id);
		branch.setiFSC(ifsc);

		branchDao.addBranch(List.of(branch));

	}

}

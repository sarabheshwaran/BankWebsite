package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import uub.model.Branch;
import uub.persistentinterfaces.IBranchDao;
import uub.staticlayer.CustomBankException;

public class BranchHelper {

	private IBranchDao branchDao;

	public BranchHelper() throws CustomBankException {

		try {

			Class<?> BranchDao = Class.forName("uub.persistentlayer.BranchDao");
			Constructor<?> branDao = BranchDao.getDeclaredConstructor();

			branchDao = (IBranchDao) branDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new CustomBankException("Error getting Data ! ", e);

		}

	}

	public Map<Integer, Branch> getAllBranches() throws CustomBankException {

		return branchDao.getBranches();
	}



	public void addBranch(Branch branch) throws CustomBankException {

		int id = branch.getId();

		branch.setId(id);
		String ifsc = generateIFSC(id);
		branch.setiFSC(ifsc);

		branchDao.addBranch(List.of(branch));

	}

	private String generateIFSC(int id) {

		return "UUB" + String.format("%04d", id);
	}

}

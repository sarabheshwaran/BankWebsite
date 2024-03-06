package uub.persistentlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uub.model.Branch;
import uub.persistentinterfaces.IBranchDao;
import uub.staticLayer.ConnectionManager;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class BranchDao implements IBranchDao {

	private Connection connection;

	public BranchDao() throws CustomBankException {
		connection = ConnectionManager.getConnection();
	}
	@Override
	public List<Branch> getBranchWithId(int id) throws CustomBankException {
		String getQuery = "SELECT * FROM BRANCH WHERE ID = " + id;
		return getBranches(getQuery);
	}

	@Override
	public List<Branch> getBranches() throws CustomBankException {
		String getQuery = "SELECT * FROM BRANCH ";
		return getBranches(getQuery);
	}
	

	private List<Branch> getBranches(String query) throws CustomBankException {

		List<Branch> branches = new ArrayList<>();

		HelperUtils.nullCheck(query);

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement();) {

			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Branch branch = mapBranch(resultSet);
				branches.add(branch);
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return branches;

	}

	@Override
	public int getLastId() throws CustomBankException {

		int lastId = -1;

		String query = "SELECT MAX(ID) AS max_id FROM BRANCH";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				lastId = resultSet.getInt("max_id");
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return lastId;
	}

	@Override
	public void addBranch(List<Branch> branches) throws CustomBankException {
		HelperUtils.nullCheck(branches);

		String addQuery = "INSERT INTO BRANCH (IFSC,NAME,ADDRESS) VALUES (?,?,?)";

		try (Connection connection = ConnectionManager.getConnection();
			PreparedStatement statement = connection.prepareStatement(addQuery);) {
			
			
			for (Branch branch : branches) {

				statement.setObject( 1, branch.getiFSC());
				statement.setObject( 2, branch.getName());
				statement.setObject( 3, branch.getAddress());

				statement.addBatch();
			}

			statement.executeBatch();
		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}

	@Override
	public void updateBranches(Branch branch) throws CustomBankException {

		HelperUtils.nullCheck(branch);

		StringBuilder getQuery1 = new StringBuilder("SELECT * FROM BRANCH ");

		getQuery1.append(getFieldList(branch)).append("WHERE ID = ?");

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(getQuery1.toString())) {

			setValues(statement, branch);
			statement.executeUpdate();

		

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}


	private Branch mapBranch(ResultSet resultSet) throws SQLException {

		Branch branch = new Branch();
		branch.setId(resultSet.getInt("id"));
		branch.setiFSC(resultSet.getString("ifsc"));
		branch.setName(resultSet.getString("name"));
		branch.setAddress(resultSet.getString("address"));

		return branch;
	}

	private String getFieldList(Branch branch) {

		StringBuilder queryBuilder = new StringBuilder("  ");

		if (branch.getName() != null) {
			queryBuilder.append("NAME = ? , ");
		}
		if (branch.getiFSC() != null) {
			queryBuilder.append("IFSC = ? , ");
		}
		if (branch.getAddress() != null) {
			queryBuilder.append("ADDRESS = ? , ");
		}

		queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
		return queryBuilder.toString();

	}

	private void setValues(PreparedStatement statement, Branch branch) throws SQLException {

		int index = 1;

		if (branch.getName() != null) {
			statement.setObject(index++, branch.getName());
		}
		if (branch.getiFSC() != null) {
			statement.setObject(index++, branch.getiFSC());
		}
		if (branch.getAddress() != null) {
			statement.setObject(index++, branch.getAddress());
		}
		if (branch.getId() != 0) {
			statement.setObject(index++, branch.getId());
		}

	}

}

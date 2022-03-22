package settlers;

import javax.sql.DataSource;
import java.sql.*;

public class SettlerRepository {
    private DataSource dataSource;

    public SettlerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    public long saveNewSettler(Settler settler) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into settlers (name_of_settler, amount_of_tobacco, expected_income) values (?, ?, ?);",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, settler.getNameOfSettler());
            stmt.setInt(2, settler.getAmountOfTobacco());
            stmt.setInt(3,settler.getExpectedIncome());
            stmt.executeUpdate();
            return getIdByStatement(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert.", sqle);
        }
    }
    private long getIdByStatement(PreparedStatement stmt)  {
        try (
                ResultSet rs = stmt.getGeneratedKeys()
        )
        {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IllegalStateException("Cannot get id");
        }
        catch (SQLException sqle) {
            throw new IllegalStateException("Cannot get id", sqle);
        }
    }

     public Settler findSettlerById(long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("select * from settlers where id = ?");
        ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return returnSettlerIfFound(rs);
        }
        catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query", sqle);
        }
    }
    private Settler returnSettlerIfFound(ResultSet rs) throws SQLException {
        if (rs.next()) {
            String nameOfSettler = rs.getString("name_of_settler");
            int amountOfTobacco = rs.getInt("amount_of_tobacco");

            return new Settler(nameOfSettler, amountOfTobacco);
        }
        throw new IllegalStateException("Settler with this id not found.");
    }


    public void updateGrowthAndIncome(long id, int amount){
        int ammountOld = findSettlerById(id).getAmountOfTobacco();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE settlers SET amount_of_tobacco = ? WHERE id = ?;");
        ) {
            ps.setInt(1, ammountOld-amount);
            ps.setLong(2, id);
            ps.executeQuery();
        }
        catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query", sqle);
        }
    }
}

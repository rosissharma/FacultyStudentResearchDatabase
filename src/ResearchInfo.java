import java.util.*;

public class ResearchInfo {

    // Attributes for ResearchInfo
    private String userType;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String website;
    private String researchInterests;
    private String hashedPass;

    //Student
    private String major;
    private String minor;

    //Professor
    private String college;

    private MySQLDatabase database;

    /**
     * Default Constructor
     */
    ResearchInfo() {

    }

    /**
     * Takes the username and hashed password for the user to login to the database.
     *
     * @param username   The user's username
     * @param hashedPass The user's hashed password
     * @return null
     */
    public String login(String username, String hashedPass) {
        DBConnect();
        String sql = "SELECT Count(*) FROM users WHERE username = ? and hashedPassword = ?;";
        ArrayList<String> values = new ArrayList<String>();
        values.add(username);
        values.add(hashedPass);
        ArrayList<ArrayList<String>> result = database.getData(sql, values);
        try {
            if (Integer.parseInt(result.get(0).get(0)) == 1) {
                this.username = username;
                fetch();
                DBCloseConn();
                return userType;
            }
        } catch (Exception e) {
        }
        DBCloseConn();

        return null;
    }

    /**
     * Lists everyone in the database depending on the user. Lists professors for
     * students and public users. Lists students, for professors.
     *
     * @return list The array list
     */
    public ArrayList<ArrayList<String>> listAll() {
        DBConnect();
        ArrayList<ArrayList<String>> list;
        if (userType.equals("Admin")) {
            list = database.getData("SELECT userType, fullName, phoneNumber, email, website, interests FROM Persons LEFT JOIN professor ON persons.username=professor.username LEFT JOIN student ON student.username=persons.username JOIN userType on persons.userTypeID=userType.userTypeID;", null);
        } else if (userType.equals("Professor")) {
            list = database.getData("SELECT userType, fullName, phoneNumber, email, website, interests, major, minor FROM persons JOIN student ON student.username=persons.username JOIN userType on persons.userTypeID=userType.userTypeID;", null);
        } else if (userType.equals("Student")) {
            list = database.getData("SELECT userType, fullName, phoneNumber, email, website, interests, college FROM persons JOIN professor ON professor.username=persons.username JOIN userType on persons.userTypeID=userType.userTypeID;", null);
        } else {
            System.out.println("UserType not set");
            DBCloseConn();
            return null;
        }
        DBCloseConn();
        return list;
    }

    /**
     * Lists all projects in the database
     *
     * @return list The array list
     */
    public ArrayList<ArrayList<String>> listAllProjects() {
        DBConnect();
        ArrayList<ArrayList<String>> list = database.getData("SELECT projects.projectID, projectname, description FROM projects;", null);
        DBCloseConn();
        return list;
    }

    /**
     * Lists projects user is invovled in
     *
     * @return list The array list
     */
    public ArrayList<ArrayList<String>> listMyProjects() {
        DBConnect();
        ArrayList<String> values = new ArrayList<String>();
        values.add(username);
        ArrayList<ArrayList<String>> list = database.getData("SELECT projects.projectID, projectname, description FROM projects JOIN ProjectInvolvment ON projects.projectID=ProjectInvolvment.projectID WHERE username = ?;", values);
        DBCloseConn();
        return list;
    }

    /**
     * Lists projects user is not invovled in
     *
     * @return list The array list
     */
    public ArrayList<ArrayList<String>> listNotMyProjects() {
        DBConnect();
        ArrayList<String> values = new ArrayList<String>();
        values.add(username);
        ArrayList<ArrayList<String>> list = database.getData("SELECT projects.projectID, projectname, description FROM projects LEFT JOIN ProjectInvolvment ON projects.projectID=ProjectInvolvment.projectID WHERE NOT EXISTS (SELECT * FROM ProjectInvolvment WHERE projectID = projects.projectID and username=?);", values);
        DBCloseConn();
        return list;
    }

    /**
     * Method to add a user to the project
     *
     * @param projectID The id that will be added to the project
     */
    public void addToProject(int projectID) {
        DBConnect();
        ArrayList<String> values = new ArrayList<String>();
        values.add(username);
        values.add(Integer.toString(projectID));
        database.setData("INSERT INTO projectInvolvment (username, projectID) VALUES (?,?);", values);
        DBCloseConn();
    }

    /**
     * Method to remove user from the project
     *
     * @param projectID The id that will be used to remove the user
     */
    public void removeFromProject(int projectID) {
        DBConnect();
        ArrayList<String> values = new ArrayList<String>();
        values.add(username);
        values.add(Integer.toString(projectID));
        database.setData("DELETE FROM projectInvolvment WHERE username = ? AND projectID =?;", values);
        DBCloseConn();
    }

    /**
     * Lets the user search the database for professors or students depending on who
     * is logged in.
     *
     * @param keywordString String to search through the databse
     * @return tableArray
     */
    public ArrayList<ArrayList<String>> searchPeople(String keywordString) {
        int counter = 0;
        String SQLString = "";
        String SQLParameter = "";
        String SQLFirstParameter = "";
        int columns = 0;
        if (userType.equals("Admin")) {
            SQLString = "SELECT userType, fullName, phoneNumber, email, website, interests FROM Student RIGHT JOIN persons on persons.username = student.username LEFT JOIN professor on persons.username=professor.username JOIN userType on userType.userTypeID = persons.userTypeID WHERE ";
            SQLParameter = "or fullName like ? OR phoneNumber like ? OR email like ? OR website like ? or interests like ? ";
            SQLFirstParameter = " fullName like ? OR phoneNumber like ? OR email like ? OR website like ? or interests like ? ";
            columns = 5;
        } else if (userType.equals("Professor")) {
            SQLString = "SELECT userType, fullName, phoneNumber, email, website, interests, major, minor FROM persons JOIN student ON student.username=persons.username JOIN userType on persons.userTypeID=userType.userTypeID WHERE ";
            SQLParameter = "or fullName like ? OR phoneNumber like ? OR email like ? OR website like ? or interests like ? or major like ? or minor like ? ";
            SQLFirstParameter = " fullName like ? OR phoneNumber like ? OR email like ? OR website like ? or interests like ? or major like ? or minor like ? ";
            columns = 7;
        } else if (userType.equals("Student")) {
            SQLString = "SELECT userType, fullName, phoneNumber, email, website, interests, college FROM persons JOIN professor ON professor.username=persons.username JOIN userType on persons.userTypeID=userType.userTypeID WHERE ";
            SQLParameter = "or fullName like ? OR phoneNumber like ? OR email like ? OR website like ? or interests like ? or college like ? ";
            SQLFirstParameter = " fullName like ? OR phoneNumber like ? OR email like ? OR website like ? or interests like ? or college like ? ";
            columns = 6;
        } else {
            System.out.println("User Type not set");
            return null;
        }

        ArrayList<String> preparedParameter = new ArrayList<>();
        DBConnect();
        //split keywords string
        for (String retval : keywordString.split("\\s+")) {
            for (int i = 0; i < columns; i++) {
                preparedParameter.add("%" + retval + "%");
            }
            if (counter == 0) {
                SQLString += SQLFirstParameter;
            } else {
                SQLString += SQLParameter;
            }
            counter += 1;
        }
        SQLString += " ;";
        ArrayList<ArrayList<String>> tableArray = database.getData(SQLString, preparedParameter);
        DBCloseConn();

        return tableArray;
    }

    /**
     * Lets the user search for projects
     *
     * @param keywordString String that will be used for searching
     * @return tableArray
     */
    public ArrayList<ArrayList<String>> searchProjects(String keywordString) {
        int counter = 0;
        String SQLString = "SELECT projects.projectID, projectname, description FROM ProjectInvolvment RIGHT JOIN Projects on ProjectInvolvment.projectID = Projects.projectID WHERE ";
        String SQLParameter = "or projects.projectID like ? OR projectname like ? OR description like ? OR username like ? ";
        String SQLFirstParameter = "projects.projectID like ? OR projectname like ? OR description like ? OR username like ? ";

        ArrayList<String> preparedParameter = new ArrayList<>();
        DBConnect();
        //split keywords string
        for (String retval : keywordString.split("\\s+")) {
            for (int i = 0; i < 4; i++) {
                preparedParameter.add("%" + retval + "%");
            }
            if (counter == 0) {
                SQLString += SQLFirstParameter;
            } else {
                SQLString += SQLParameter;
            }
            counter += 1;
        }
        SQLString += ";";

        ArrayList<ArrayList<String>> tableArray = database.getData(SQLString, preparedParameter);
        DBCloseConn();

        return tableArray;
    }

    /**
     * Accessor for Phone Number
     *
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the phone number of the user.
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Accessor for Personal Info
     *
     * @return array
     */
    public ArrayList<String> getPersonalInfo() {
        ArrayList<String> array = new ArrayList<String>();
        array.add(userType);
        array.add(name);
        array.add(phoneNumber);
        array.add(email);
        array.add(website);
        array.add(researchInterests);
        if (userType.equals("Professor")) {
            array.add(college);
        } else if (userType.equals("Student")) {
            array.add(major);
            array.add(minor);
        }
        return array;
    }

    /**
     * Accessor for User Name
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the users password
     *
     * @param hashedPasss
     */
    public void setPassword(String hashedPasss) {
        this.hashedPass = hashedPasss;
    }

    /**
     * Accessor for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor for Email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the users name based on user type
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the user's email
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Accessor for website
     *
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the users website based on user type
     *
     * @param website
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Accessor for Research Interests
     *
     * @return researchInterests
     */
    public String getResearchInterests() {
        return researchInterests;
    }

    /**
     * Sets the Research Interests based on user type
     *
     * @param researchInterests
     */
    public void setResearchInterests(String researchInterests) {
        this.researchInterests = researchInterests;
    }

    /**
     * Sets the user type from the userType table - used internnally only - user can't change this
     *
     * @param userType
     */
    private void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Gets the user type
     *
     * @return userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Sets the student's major
     *
     * @param major
     */
    public void setMajor(String major) {
        if (userType.equals("Student")) {
            this.major = major;
        } else {
            System.out.println("Not a student, can't set major");
        }
    }

    /**
     * Gets a student's major
     *
     * @return major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Sets a students minor
     *
     * @param minor
     */
    public void setMinor(String minor) {
        if (userType.equals("Student")) {
            this.minor = minor;
        } else {
            System.out.println("Not a student, can't set minor");
        }
    }

    /**
     * Gets a students minor
     *
     * @return minor
     */
    public String getMinor() {
        return minor;
    }

    /**
     * Sets a professors college
     *
     * @param college
     */
    public void setCollege(String college) {
        if (userType.equals("Professor")) {
            this.college = college;
        } else {
            System.out.println("Not a professor, can't set college");
        }
    }

    /**
     * Gets a professors college
     *
     * @return college
     */
    public String getCollege() {
        return college;
    }

    /**
     * Updates internal variables
     *
     * @return boolean - true if successful, false if error
     */
    public boolean fetch() {
        DBConnect();
        ArrayList<String> values = new ArrayList<String>();
        values.add(username);
        ArrayList<ArrayList<String>> result = database.getData("SELECT fullName, phoneNumber, email, website, userType, interests, major, minor, college from persons JOIN userType ON persons.userTypeID=userType.userTypeID LEFT JOIN Student ON student.username=persons.username LEFT JOIN Professor ON professor.username=persons.username WHERE persons.username = ?;", values);
        DBCloseConn();
        try {
            if (result != null) {
                setName(result.get(0).get(0));
                setPhoneNumber(result.get(0).get(1));
                setEmail(result.get(0).get(2));
                setWebsite(result.get(0).get(3));
                setUserType(result.get(0).get(4));
                setResearchInterests(result.get(0).get(5));
                if (userType.equals("Student")) {
                    setMajor(result.get(0).get(6));
                    setMinor(result.get(0).get(7));
                } else if (userType.equals("Professor")) {
                    setCollege(result.get(0).get(8));
                }
                return true;
            } else {
                setName(null);
                setPhoneNumber(null);
                setEmail(null);
                setWebsite(null);
                setUserType(null);
                setResearchInterests(null);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setName(null);
            setPhoneNumber(null);
            setEmail(null);
            setWebsite(null);
            setUserType(null);
            setResearchInterests(null);
            return false;
        }
    }

    /**
     * Updates the information in the database itself
     *
     * @return rows Lines affected (-1 if error)
     */
    public int put() {
        DBConnect();
        database.startTrans();
        ArrayList<String> values = new ArrayList<String>();
        values.add(name);
        values.add(email);
        values.add(phoneNumber);
        values.add(website);
        values.add(researchInterests);
        values.add(username);
        int rows = database.setData("UPDATE persons SET fullName = ?, email = ?, phoneNumber = ?, website = ?, interests = ? WHERE username = ?", values);

        ArrayList<String> values2 = new ArrayList<String>();
        if (userType.equals("Student")) {
            values2.add(major);
            values2.add(minor);
            values2.add(username);
            database.setData("UPDATE student SET major = ?, minor = ? WHERE username = ?;", values2);
        } else if (userType.equals("Professor")) {
            values2.add(college);
            values2.add(username);
            database.setData("UPDATE professor SET college = ? WHERE username = ?;", values2);
        }
        database.endTrans();
        DBCloseConn();
        return rows;
    }

    /**
     * Connect to the databse
     */
    private void DBConnect() {
        database = new MySQLDatabase();
        if (!database.connect()) {
            System.out.println("Failed to connect to database");
            System.exit(1);
        }
    }

    /**
     * Close connection to the database
     */
    private void DBCloseConn() {
        if (!database.close()) {
            System.out.println("Failed to close connection to database");
            System.exit(2);
        }
    }
}

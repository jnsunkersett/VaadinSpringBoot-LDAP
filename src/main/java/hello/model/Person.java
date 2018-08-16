package hello.model;

import javax.validation.constraints.NotEmpty;

public class Person {

	@NotEmpty 
    private String uid;
	@NotEmpty
    private String fullName;
	@NotEmpty
    private String lastName;
	@NotEmpty
    private String orgUnit;
	@NotEmpty
    private static String baseDN;
	@NotEmpty
    private String password;
	private String confirmPassword;

    public Person() {
    }

    public Person(String fullName, String lastName) {
        this.fullName = fullName;
        this.lastName = lastName;
    }

    public Person(String uid, String fullName, String lastName) {
        this.uid = uid;
        this.fullName = fullName;
        this.lastName = lastName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public String getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}

	public String getBaseDN() {
		return baseDN;
	}

	public void setBaseDN(String baseDN) {
		this.baseDN = baseDN;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orgUnit == null) ? 0 : orgUnit.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (orgUnit == null) {
			if (other.orgUnit != null)
				return false;
		} else if (!orgUnit.equals(other.orgUnit))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [uid=" + uid + ", fullName=" + fullName + ", lastName="
				+ lastName + ", orgUnit=" + orgUnit + ", baseDN=" + baseDN
				+ "]";
	}
}

package hello.repository;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import hello.model.Person;

@Service
public class PersonRepository implements BaseLdapNameAware {

    @Autowired
    private LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;
    
    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }


    public void create(Person p) {
        Name dn = buildDn(p.getOrgUnit(), p.getUid());
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public List<Person> findAll() {
    	EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new PersonContextMapper());
    }

    public Person findOne(String uid, String ou) {
        Name dn = LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", ou) 
                .add("uid", uid)
                .build();
        Person p = ldapTemplate.lookup(dn, new PersonContextMapper());
        return p;
    }

    public List<Person> findByName(String name) {
        LdapQuery q = query()
                .where("objectclass").is("person")
                .and("cn").whitespaceWildcardsLike(name);
        return ldapTemplate.search(q, new PersonContextMapper());
    }

    public void update(Person p, String oldOrgUnit) {
    	String orgUnit = p.getOrgUnit();
    	//String oldOrgUnit = null;
    	//if(orgUnit.contains(",")) {
    	if(!orgUnit.equals(oldOrgUnit)) {
    		//oldOrgUnit = orgUnit.substring(0, orgUnit.indexOf(","));
    		Name oldDn = buildDn(oldOrgUnit, p.getUid());
    		
    		//orgUnit = orgUnit.substring(orgUnit.indexOf(",")+1, orgUnit.length());
    		Name newDn = buildDn(orgUnit, p.getUid());
    		
    		ldapTemplate.rename(oldDn, newDn);
    		//Change attributes - FullName, Last Name...
    		ldapTemplate.rebind(buildDn(orgUnit, p.getUid()), null, buildAttributes(p));
    	} else {
    		ldapTemplate.rebind(buildDn(p.getOrgUnit(), p.getUid()), null, buildAttributes(p));
    	}
    }

    public void updateLastName(Person p) {
        Attribute attr = new BasicAttribute("sn", p.getLastName());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(p.getOrgUnit(), p.getUid()), new ModificationItem[] {item});
    }

    public void delete(Person p) {
        ldapTemplate.unbind(buildDn(p.getOrgUnit(), p.getUid()));
    }

    private Name buildDn(String ou, String uid) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", ou)
                .add("uid", uid)
                .build();
    }

    private Attributes buildAttributes(Person p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        ocAttr.add("organizationalPerson");
        ocAttr.add("inetOrgPerson");
        attrs.put(ocAttr);
        attrs.put("cn", p.getFullName());
        attrs.put("sn", p.getLastName());
        attrs.put("userPassword", p.getPassword()); //TODO:Encrypt password
        return attrs;
    }


    private static class PersonContextMapper extends AbstractContextMapper<Person> {
        public Person doMapFromContext(DirContextOperations context) {
            Person person = new Person();
            person.setFullName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            person.setUid(context.getStringAttribute("uid"));
            String name = context.getNameInNamespace();
            person.setOrgUnit(name.substring(name.indexOf("ou=")+3, name.indexOf("dc")-1));
            person.setBaseDN(name.substring(name.indexOf("dc")));
            
            //Fetch userPassword
            Object o = context.getObjectAttribute("userPassword");
            byte[] bytes = (byte[]) o;
            String hash = new String(bytes);
            person.setPassword(hash);
            return person;
        }
    }
}

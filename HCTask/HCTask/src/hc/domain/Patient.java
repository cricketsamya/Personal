/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package healthcarion.domain;

/**
 *
 * @author Sameer
 */
public class Patient {

    public Patient() {
    }

    public Patient(String firstName, String lastName) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Patient(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    private Integer id;
    private String firstName;
    private String lastName;
}

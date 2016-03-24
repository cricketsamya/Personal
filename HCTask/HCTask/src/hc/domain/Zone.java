/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package healthcarion.domain;

/**
 *
 * @author Sameer
 */
public class Zone {

    public Zone() {
    }
    public Zone(String name) {
        this.id = null;
        this.name = name;
    }
    public Zone(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    private Integer id;
    private String name;
}

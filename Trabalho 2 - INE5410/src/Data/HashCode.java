/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

public class HashCode {
    public String hashCode;
    public int índex;
    public boolean found;
    public int numGerador;
    
    public boolean equals(HashCode hashCode) {
        return this.hashCode.equals(hashCode.hashCode);
    }
}

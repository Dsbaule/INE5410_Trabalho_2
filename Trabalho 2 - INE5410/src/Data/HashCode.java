/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

public class HashCode {
    private String hashCode;
    private boolean found;
    private int numGerador;

    public HashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public int getNumGerador() {
        return numGerador;
    }

    public void setNumGerador(int numGerador) {
        this.numGerador = numGerador;
    }
    
    
}

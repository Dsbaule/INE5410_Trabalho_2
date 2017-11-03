package Data;

import java.io.Serializable;

public class ClientParameters implements Serializable{
    String codigo;
    int initialValue;
    int finalValue;

    public ClientParameters(String codigo, int initialValue, int finalValue) {
        this.codigo = codigo;
        this.initialValue = initialValue;
        this.finalValue = finalValue;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public boolean matchesCodigo(String codigo) {
        return codigo.equals(this.codigo);
    }

    public int getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    public int getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(int finalValue) {
        this.finalValue = finalValue;
    }
    
}

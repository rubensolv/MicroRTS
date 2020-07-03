/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author rubens
 */
@Entity
@Table(name = "ucb", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Ucb.findAll", query = "SELECT u FROM Ucb u")
    , @NamedQuery(name = "Ucb.findByIdRule", query = "SELECT u FROM Ucb u WHERE u.idRule = :idRule")
    , @NamedQuery(name = "Ucb.findByQtdUsed", query = "SELECT u FROM Ucb u WHERE u.qtdUsed = :qtdUsed")})
public class Ucb implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_rule")
    private Integer idRule;
    @Column(name = "qtdUsed")
    private Integer qtdUsed;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "idRule", fetch = FetchType.EAGER)
    private List<LogUCB> logUCBList;

    public Ucb() {
    }

    public Ucb(Integer idRule) {
        this.idRule = idRule;
    }

    public Integer getIdRule() {
        return idRule;
    }

    public void setIdRule(Integer idRule) {
        this.idRule = idRule;
    }

    public Integer getQtdUsed() {
        return qtdUsed;
    }

    public void setQtdUsed(Integer qtdUsed) {
        this.qtdUsed = qtdUsed;
    }
    
    public List<LogUCB> getLogUCBList() {
        return logUCBList;
    }

    public void setLogUCBList(List<LogUCB> logUCBList) {
        this.logUCBList = logUCBList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRule != null ? idRule.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ucb)) {
            return false;
        }
        Ucb other = (Ucb) object;
        if ((this.idRule == null && other.idRule != null) || (this.idRule != null && !this.idRule.equals(other.idRule))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ucb{" + "idRule=" + idRule + ", qtdUsed=" + qtdUsed + '}';
    }


    
}

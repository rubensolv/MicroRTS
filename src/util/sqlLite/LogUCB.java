/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author rubens
 */
@Entity
@Table(name = "logUCB", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "LogUCB.findAll", query = "SELECT l FROM LogUCB l")
    , @NamedQuery(name = "LogUCB.findById", query = "SELECT l FROM LogUCB l WHERE l.id = :id")
    , @NamedQuery(name = "LogUCB.findByReward", query = "SELECT l FROM LogUCB l WHERE l.reward = :reward")})
public class LogUCB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(generator="sqliteLog")
    //@TableGenerator(name="sqliteLog", table="sqlite_sequence",
    //pkColumnName="name", valueColumnName="seq",
    //pkColumnValue="logUCB", initialValue=1, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "reward", nullable = false)
    private int reward;
    @JoinColumn(name = "id_rule", referencedColumnName = "id_rule", nullable = false)
    @ManyToOne(cascade = CascadeType.REFRESH,optional = false, fetch = FetchType.EAGER)
    private Ucb idRule;

    public LogUCB() {
    }

    public LogUCB(Integer id) {
        this.id = id;
    }

    public LogUCB(Integer id, int reward) {
        this.id = id;
        this.reward = reward;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public Ucb getIdRule() {
        return idRule;
    }

    public void setIdRule(Ucb idRule) {
        this.idRule = idRule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogUCB)) {
            return false;
        }
        LogUCB other = (LogUCB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "util.sqlLite.LogUCB[ id=" + id + " ]";
    }
    
}

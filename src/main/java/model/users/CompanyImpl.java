package model.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CompanyImpl implements Company {
    
    private static final CompanyImpl SINGLETON = new CompanyImpl();
    private final List<Staff> staff = new ArrayList<>();
    private final List<Clients> clients = new ArrayList<>();
    private final List<Products> products = new ArrayList<>();
    
    public CompanyImpl () {}
    
    public static CompanyImpl getSingleton() {
        return SINGLETON;
    }

    @Override
    public void addStaff(Staff s) {
        this.staff.add(s);
    }

    @Override
    public void removeStaff(Staff s) {
        this.staff.remove(s);
    }

    @Override
    public Optional<Staff> searchStaff(String emailStaff) {
        for (final Staff s : this.staff) {
            if (s.getEmail().equals(emailStaff)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Staff> getStaff() {
        return Collections.unmodifiableList(this.staff);
    }

    @Override
    public void addClient(Clients c) {
        this.clients.add(c);
    }

    @Override
    public void removeClient(Clients c) {
        this.clients.remove(c);
    }

    @Override
    public Optional<Clients> searchClient(String CF_PIVA) {
        for (final Clients c : this.clients) {
            if (c.getCFPIVA().equals(CF_PIVA)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Clients> getClient() {
        return Collections.unmodifiableList(this.clients);
    }

    @Override
    public void addProduct(Products p) {
        this.products.add(p);
    }

    @Override
    public void removeProduct(Products p) {
        this.products.remove(p);        
    }

    @Override
    public Optional<Products> searchProduct(String codeProduct) {
        for (final Products p : this.products) {
            if (p.getCode().equals(codeProduct)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Products> getProduct() {
        return Collections.unmodifiableList(this.products);
    }
    
}
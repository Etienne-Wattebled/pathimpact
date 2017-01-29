package org.univlille1.pathimpact.grammaire;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.univlille1.pathimpact.element.AbstractElement;
import org.univlille1.pathimpact.element.ElementItf;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Grammaire {
	protected S s;
	protected List<Regle> regles;
	protected int prochainIdRegle;
	protected Table<Regle,ElementItf,Integer> utilisationsRegles;
	
	public Grammaire() {
		reset();
	}
	
	protected void reset() {
		this.s = new S();
		this.regles = new LinkedList<Regle>();
		this.prochainIdRegle = 0;
		this.utilisationsRegles = HashBasedTable.create();
	}
	
	/**
	 * Ajout de la règle
	 * @param strackTrace de la règle
	 *
	 */
	public Regle ajouterRegle(List<ElementItf> elements) {
		// to do : mettre à jour utilisationsRegles si nécessaire
		String id = String.valueOf(prochainIdRegle);
		prochainIdRegle = prochainIdRegle + 1;
		Regle regle = new Regle(elements,id);
		regles.add(regle);
		return regle;
	}
	
	public void supprimerRegle(Regle regle) {
		regles.remove(regle);
		Map<ElementItf,Integer> m = utilisationsRegles.row(regle);
		
		Map<Regle,Map<ElementItf,Integer>> m2 = utilisationsRegles.rowMap();
		m2.remove(regle);		
		
		Set<ElementItf> elements = m.keySet();
		for (ElementItf e : elements) {
			if (e instanceof Regle) {
				((Regle) e).supprimerElement(regle);
			}
			if (e instanceof S) {
				((S) e).supprimerElement(regle);
			}
		}
	}
	
	public void appliquerRegle(Regle regleAAppliquer, Regle regle) {
		int nb = regleAAppliquer.appliquer(regle.getElements());
		// to do mettre à jour utilisationsRegles
	}
	
	public void appliquerRegleSurS(Regle regle) {
		int nb = regle.appliquer(s.getElements());
		// to do mettre à jour utilisationsRegles
	}
	
	public int getNbRegles() {
		return regles.size();
	}
	
	public S getS() {
		return s;
	}
	
	public List<Regle> getRegles() {
		return regles;
	}
	
	public void ajouterElementDansS(ElementItf element) {
		s.ajouterElement(element);
	}
	
	public void supprimerElementDansS(ElementItf element) {
		s.supprimerElement(element);
	}
	
	public Regle getRegleQuiProduit(List<ElementItf> elements) {
		for (Regle regle : regles) {
			if (regle.produit(elements)) {
				return regle;
			}
		}
		return null;
	}
	
	public void print() {
		System.out.println("S =");
		Iterator<ElementItf> itE = s.listIterator();
		while (itE.hasNext()) {
			ElementItf element = itE.next();
			System.out.print(element.getNom());
			System.out.print(" ");
		}
		System.out.println("R = {");
		Iterator<Regle> itR = regles.listIterator();
		while (itR.hasNext()) {
			Regle regle = itR.next();
			regle.print();
		}
		System.out.println("}");
	}
	
	public class Regle extends AbstractElement {
		
		protected List<ElementItf> elements;
		
		protected Regle(List<ElementItf> elements, String nom) {
			super(nom);
			this.elements = elements;
		}
		
		public List<ElementItf> getElements() {
			return this.elements;
		}
		public int getNbElements() {
			return elements.size();
		}
		
		public ElementItf getElement(int i) {
			return elements.get(i);
		}
		
		protected void ajouterElement(ElementItf element) {
			elements.add(element);
		}
		
		protected void supprimerElement(ElementItf element) {
			elements.removeIf(new PredicateSameInstance(element));
		}
		
		protected void supprimerElements(List<ElementItf> elements) {
			this.elements.removeAll(elements);
		}
		
		public void print() {
			System.out.print(getNom());
			System.out.print(" =>");
			Iterator<ElementItf> it = elements.listIterator();
			while (it.hasNext()) {
				System.out.print(" ");
				System.out.print(it.next());
			}
		}
		
		public boolean contientElement(ElementItf element) {
			return elements.contains(element);
		}
		
		public boolean produit(List<ElementItf> elements) {
			if (this.elements.size() != elements.size()) {
				return false;
			}
			Iterator<ElementItf> it1 = this.elements.listIterator();
			Iterator<ElementItf> it2 = elements.listIterator();
			while (it1.hasNext() && it2.hasNext()) {
				ElementItf e1 = it1.next();
				ElementItf e2 = it2.next();
				if (e1 != e2) {
					return false;
				}
			}
			return true;
		}
		protected int desappliquer(List<ElementItf> elements) {
			int nb = 0;
			ListIterator<ElementItf> it = elements.listIterator();
			ElementItf element = null;
			while (it.hasNext()) {
				element = it.next();
				if (element == this) {
					it.remove();
					for (ElementItf e : elements) {
						it.add(e);
					}
					nb = nb+1;
				}
			}
			return nb;
		}
		protected int appliquer(List<ElementItf> elements) {
			if (this.elements.size() > elements.size()) {
				return 0;
			}
			int nb = 0;
			ListIterator<ElementItf> itE = elements.listIterator();
			ListIterator<ElementItf> itR = null;
			if (itE.hasNext() && !this.elements.isEmpty()) {
				ElementItf elementR = null;
				ElementItf elementE = null;
				while (itE.hasNext()) {
					itR = this.elements.listIterator();
					elementR = itR.next();
					elementE = itE.next();
					int n = 0;
					int rollback = itE.previousIndex();
					while ((elementR == elementE) && (itE.hasNext()) && (itR.hasNext())) {
						n = n+1;
						elementE = itE.next();
						elementR = itR.next();
					}
					if (elementR == elementE) {
						n = n+1;
					}
					boolean ok = (n == this.elements.size());
					if (n != 0) {
						while (itE.previousIndex() != rollback) {
							if (ok) {
								itE.remove();
							} else {
								itE.previous();
							}
						}
						if (ok) {
							itE.add(this);
							nb = nb+1;
						}
					}
				}
			}
			return nb;
		}
	}
	
	protected class S extends AbstractElement {
		private List<ElementItf> elements;
		
		protected S() {
			super("S");
		}
		
		protected void ajouterElement(ElementItf element) {
			elements.add(element);
		}
		
		protected void supprimerElement(ElementItf element) {
			elements.removeIf(new PredicateSameInstance(element));
		}
		
		protected List<ElementItf> getElements() {
			return elements;
		}
		
		protected ListIterator<ElementItf> listIterator() {
			return elements.listIterator();
		}
	}
	
	protected class PredicateSameInstance implements Predicate<ElementItf> {
		private ElementItf element;
		
		public PredicateSameInstance(ElementItf element) {
			this.element = element;
		}
		
		@Override
		public boolean test(ElementItf e) {
			return (e == element);
		}
	}
}

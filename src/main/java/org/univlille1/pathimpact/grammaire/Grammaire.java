package org.univlille1.pathimpact.grammaire;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.univlille1.pathimpact.element.AbstractElement;
import org.univlille1.pathimpact.element.ElementItf;

public class Grammaire {
	protected Regle s;
	protected List<Regle> regles;
	protected int prochainIdRegle;

	public Grammaire() {
		reset();
	}

	protected void reset() {
		this.s = new Regle("S");
		this.regles = new LinkedList<Regle>();
		this.prochainIdRegle = 0;
	}

	/**
	 * Ajout de la règle
	 * 
	 * @param strackTrace
	 *            de la règle
	 *
	 */
	public Regle ajouterRegle(LinkedList<ElementItf> elements) {
		String id = String.valueOf(prochainIdRegle);
		prochainIdRegle = prochainIdRegle + 1;
		Regle regle = new Regle(elements, id);
		regles.add(regle);
		return regle;
	}

	public void supprimerRegle(Regle regle) {
		desappliquerRegleSurGrammaire(regle);
		regles.remove(regle);
	}

	public int appliquerRegleSurGrammaire(Regle regle) {
		int nb = 0;
		nb = nb + appliquerRegleSurS(regle);
		for (Regle r : regles) {
			if (regle != r) {
				nb = nb + appliquerRegle(regle, r);
			}
		}
		return nb;
	}
	
	public int desappliquerRegleSurGrammaire(Regle regle) {
		int nb = 0;
		nb = nb + desappliquerRegleSurS(regle);
		for (Regle r : regles) {
			if (regle != r) {
				nb = nb + desappliquerRegle(regle, r);
			}
		}
		return nb;
	}
	
	
	public int appliquerRegle(Regle regleAAppliquer, Regle regle) {
		return regleAAppliquer.appliquer(regle.getElementsListeModifiable());
	}

	public int desappliquerRegle(Regle regleADesappliquer, Regle regle) {
		return regleADesappliquer.desappliquer(regle.getElementsListeModifiable());
	}

	public int appliquerRegleSurS(Regle regle) {
		return appliquerRegle(regle, s);
	}

	public int desappliquerRegleSurS(Regle regle) {
		return desappliquerRegle(regle, s);
	}

	public int getNbRegles() {
		return regles.size();
	}

	public Regle getS() {
		return s;
	}

	public List<Regle> getRegles() {
		return Collections.unmodifiableList(regles);
	}

	public void ajouterElementDansS(ElementItf element) {
		s.ajouterElement(element);
	}

	public void supprimerElementDansS(ElementItf element) {
		s.supprimerElement(element);
	}

	public void simplierRegles() {
		Map<Regle, Integer> map = new HashMap<Regle, Integer>();
		Set<Regle> reglesNonUtilisees = new HashSet<Regle>(regles);
		for (ElementItf e : s.getElements()) {
			if (e instanceof Regle) {
				incrementer(map, (Regle) e);
				reglesNonUtilisees.remove((Regle) e);
			}
		}
		for (Regle r : regles) {
			for (ElementItf e : r.getElements()) {
				if (e instanceof Regle) {
					incrementer(map, (Regle) e);
					reglesNonUtilisees.remove((Regle) e);
				}
			}
		}
		for (Entry<Regle, Integer> e : map.entrySet()) {
			if (e.getValue() < 2) {
				supprimerRegle(e.getKey());
			}
		}

		for (Regle regle : reglesNonUtilisees) {
			// La règle n'est pas utilisée, cela ne sert à rien de la désappliquer
			regles.remove(regle);
		}
	}

	private void incrementer(Map<Regle, Integer> map, Regle element) {
		Integer i = map.get(element);
		if (i == null) {
			map.put(element, 1);
		} else {
			map.put(element, i + 1);
		}
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
		s.print();
		System.out.println("R = {");
		for (Regle regle : regles) {
			regle.print();
		}
		System.out.println("}");
	}

	public class Regle extends AbstractElement {

		protected LinkedList<ElementItf> elements;

		protected Regle(String nom) {
			super(nom);
			this.elements = new LinkedList<ElementItf>();
		}

		protected Regle(LinkedList<ElementItf> elements, String nom) {
			super(nom);
			this.elements = elements;
		}

		public ListIterator<ElementItf> getListIteratorEnd() {
			return Collections.unmodifiableList(elements).listIterator(elements.size());
		}

		public ListIterator<ElementItf> listIterator() {
			return Collections.unmodifiableList(elements).listIterator();
		}

		public ElementItf getLastElement() {
			return elements.getLast();
		}

		private List<ElementItf> getElementsListeModifiable() {
			return this.elements;
		}

		public List<ElementItf> getElements() {
			return Collections.unmodifiableList(this.elements);
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
			System.out.print("\n");
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
					for (ElementItf e : this.elements) {
						it.add(e);
					}
					nb = nb + 1;
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
						n = n + 1;
						elementE = itE.next();
						elementR = itR.next();
					}
					if (elementR == elementE) {
						n = n + 1;
					}
					boolean ok = (n == this.elements.size());
					if (n != 0) {
						while (itE.previousIndex() != rollback) {
							itE.previous();
							if (ok) {
								itE.remove();
							}
						}
						if (ok) {
							itE.previous();
							itE.remove();
							itE.add(this);
							nb = nb + 1;
						}
					}
				}
			}
			return nb;
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
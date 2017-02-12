PathImpact - Analyseur d'impacts

But :
Si une m�thode est modifi�e, on souhaite pouvoir lister les m�thodes susceptibles d'�tre impact�es.

Fonctionnement de PathImpact :
- On d�termine et on concat�ne toutes les stacktraces possibles
- On compresse la concat�nation des stacktraces via Sequitur (qui produit une grammaire sans doublon de digramme)
- On construit une structure d'arbre appel�e DAG � partir de la grammaire g�n�r�e par Sequitur et on peut ensuite via cet arbre d�terminer les impacts.

Structure d'une stacktrace (une stacktrace est une liste ElementItf) :
- ElementItf : interface repr�sentant tous les �l�ments d�une stacktrace. Seules les m�thodes getNom() et print() sont pr�sentes dans cette interface.
- AbstractElement : Classe abstraite impl�mentant ElementItf et ne poss�dant uniquement que le nom.
- Evenement : Type �num�r� impl�mentant ElementItf. Les seules valeurs possibles sont � RETURN � dont le nom est � r � ainsi que � END_OF_PROGRAM � dont le nom est � x �.
- Methode : Classe h�ritant de AbstractElement.

Classes importantes :
- StackTracesProcessor : Processor Spoon permettant de d�terminer les stacktraces.
- Grammaire : Classe permettant de repr�senter des r�gles grammaticales. Grammaire est utilis�e par Sequitur.
- Sequitur : Algorithme de compression utilisant Grammaire.

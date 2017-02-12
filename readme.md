PathImpact - Analyseur d'impacts

But :
Si une méthode est modifiée, on souhaite pouvoir lister les méthodes susceptibles d'être impactées.

Fonctionnement de PathImpact :
- On détermine et on concatène toutes les stacktraces possibles
- On compresse la concaténation des stacktraces via Sequitur (qui produit une grammaire sans doublon de digramme)
- On construit une structure d'arbre appelée DAG à partir de la grammaire générée par Sequitur et on peut ensuite via cet arbre déterminer les impacts.

Structure d'une stacktrace (une stacktrace est une liste ElementItf) :
- ElementItf : interface représentant tous les éléments d’une stacktrace. Seules les méthodes getNom() et print() sont présentes dans cette interface.
- AbstractElement : Classe abstraite implémentant ElementItf et ne possédant uniquement que le nom.
- Evenement : Type énuméré implémentant ElementItf. Les seules valeurs possibles sont « RETURN » dont le nom est « r » ainsi que « END_OF_PROGRAM » dont le nom est « x ».
- Methode : Classe héritant de AbstractElement.

Classes importantes :
- StackTracesProcessor : Processor Spoon permettant de déterminer les stacktraces.
- Grammaire : Classe permettant de représenter des règles grammaticales. Grammaire est utilisée par Sequitur.
- Sequitur : Algorithme de compression utilisant Grammaire.

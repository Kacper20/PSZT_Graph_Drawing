###Zadanie: Rysowanie grafu na płaszczyźnie

####Treść:
Program powinien przestawić zadany graf na płaszczyźnie w jak najbardziej czytelny sposób.
Rysowane krawędzie są łukami trój-przegubowymi, a wierzchołki kołami o zadanej średnicy.
Kryteria czytelności:
* krawędzie się nie przecinają
* krawędzie są krótkie
* krawędzie nie przechodzą przez wierzchołki ani w ich bardzo bliskim sąsiedztwie
* wierzchołki są rozmieszczone równomiernie na całym obszarze rysowania
* kąty pomiędzy krawędziami incydentnymi z danymi wierzchołkiem są jak największe
* krawędzie są jak najprostsze (mogą być łukami, ale najlepiej jak będą proste)
Grafy wczytywane są z plików w formacie GraphML (http://thinkaurelius.github.io/titan/).
Użytkownik określa średnicę wierzchołka, wielkość obszaru rysowania oraz jakiś limit
czasowy lub inne kryterium stopu. Wizualizacja powinna być pokazana oraz zapisana (jako
specjalne atrybuty poszczególnych wierzchołków i krawędzi).


####Autorzy:

Tomasz Nazarewicz
Konrad Sikorski
Kacper Harasim

####Opis działania algorytmu:


####Opis przeprowadzonych testów:

#### Sposób budowania i uruchomienia:
Projekt budowany jest za pomocą narzędzia maven. Wszystkie używane biblioteki wyspecyfikowane są w pliku pom.xml.
Głównym plikiem programu jest GraphMain.java, który odpowiada za wyświetlenie okna użytkownikowi.

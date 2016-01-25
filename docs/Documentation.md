###Zadanie: Rysowanie grafu na płaszczyźnie

####Autorzy:

Tomasz Nazarewicz
Konrad Sikorski
Kacper Harasim

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




####Opis działania algorytmu:

Algorytm używany do wykonania projektu do lekko zmodyfikowany algorytm ewolucyjny.


######Generacja
Pierwszym krokiem jest wygenerowanie populacji startowej. Generowana ona jest za pomocą rozkładu jednostajnego, z obszaru dozwolonego. Generacja obejmuje wylosowanie dla każdego wierzchołka startowych wartości współrzędnych X oraz Y.


######Selekcja
Drugim krokiem jest selekcja. Zastosowane zostały dwa rodzaje selekcji:
Na początku, osobnik z najlepszą wartością funkcji fitness dostaje awans do następnej populacji.
Kolejnym krokiem jest selekcja części z reszty osobników do nowej populacji z użyciem Fitness proportionate selection. Według tej reguły, dla każdego osobnika obliczane jest prawdopodobieństwo bycia wybranym jako:  P(x) = f(x) / (suma_fitness_dla_populacji),
gdzie f - funkcja jakościowa
x - osobnik z populacji.


######Krzyżowanie
Następnie, dopóki nie wypełnimy całej nowej populacji, znajdujemy losowo(za pomocą rozkładu normalnego) dwa osobniki ze starej populacji i krzyżujemy je.
Zastosowaliśmy krzyżowanie, w którym graf wynikowy powstaje następująco:
Przyjmijmy, że r1 oraz r2 to rodzice grafu c.
Dla każdego wierzchołka w c ustawiamy x wierzchołka jako wartość wylosowaną generatorem liczb z rozkładem normalnym o średniej równej średniej arytmetycznej odpowiadających współrzędnych x rodziców, oraz wariancji w. Analogicznie postępujemy dla zmiennej y.


######Mutacja
Kolejnym etapem jest zmutowanie nowej populacji.
Mutacji podlegają wszystkie osobniki oprócz najlepszego osobnika, który został wybrany w drodze selekcji elitarnej.

Dla wszystkich osobników, które podlegają mutacji przechodzimy po ich wierzchołkach.
Z prawdopodobieństwem zadanym w aplikacji wierzchołek podlega następującej operacji:
Do współrzędnej dodawany jest mały jitter uzyskany za pomocą zmiennej o rozkładzie normalnym o wariancji zadanej w aplikacji.


####Opis przeprowadzonych testów:

Testy przeprowadzone zostały głównie na grafach o strukturze grid.


#### Sposób budowania i uruchomienia:
Projekt budowany jest za pomocą narzędzia maven. Wszystkie używane biblioteki wyspecyfikowane są w pliku pom.xml.
Głównym plikiem programu jest GraphMain.java, który odpowiada za wyświetlenie okna użytkownikowi.
Wyświetlanie odbywa się za pomocą generowania pliku SVG na podstawie wewnętrznej struktury grafu w programie.


####Opis  parametrów:

#####Modyfikowalne:
* promień wierzchołka
* szerokość ekranu wizualizującego graf
* wysokość ekranu wizualizującego graf
* limit czasowy działania algorytmu
* wielkość populacji


#####Niemodyfikowalne(nastawy dla algorytmu):
* współczynnik długości - współczynnik, który jest używany do obliczenia nagrody za prawidłową (jak najmniej odbiegającą od zadanej przez użytkownika) długości krawędzi
* współczynnik krzyżowania krawędzi - współczynnik który jest używany do obliczenia nagrody za jak najmniej krawędzi oraz  wierzchołków które się przecinają
* współczynnik kątów - współczynnik używany do obliczenia nagrody za najbardziej odpowiednie kąty(tj. najrówniej rozłożone)
* prawdopodobieństwo mutacji - prawdopodobieństwo, z jakim osobnik będzie poddany mutacji.
* wariancja krzyżowania - wartość wariancji używana do losowania nowych współrzędnych dziecka.


####Opis mocnych i słabych stron projektu:



####Podział pracy:

Podział pracy był następujący:

Tomasz Nazarewicz - panel użytkownika,
Konrad Sikorski - generator SVG, obliczenia geometryczne.
Kacper Harasim - stworzenie struktury modelu, konwersji pomiędzy

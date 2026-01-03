To jest symulator świata, w którym agenci nie mają wbudowanej wiedzy o obiektach ani pojęć semantycznych. Nie istnieją klasy typu „wilka”, „owcy” czy „pożywienia”. Istnieją wyłącznie cechy świata, potrzeby organizmu oraz mechanizmy percepcji i działania. Znaczenie obiektów wyłania się wyłącznie z doświadczenia.

Świat składa się z bytów będących kontenerami cech. Każda encja jest zbiorem właściwości wizualnych, chemicznych i fizycznych, bez hierarchii dziedziczenia. Agent nie widzi obiektu, tylko strumień wykrywalnych cech filtrowanych przez zmysły. Percepcja jest niepełna i zależna od budowy ciała.

Agent składa się z ciała i mózgu. Ciało definiuje potrzeby życiowe oraz dostępne akcje. Mózg jest wymiennym komponentem decyzyjnym, co pozwala stosować różne strategie sterowania: od automatów reaktywnych, przez uczenie asocjacyjne, po modele predykcyjne. Najważniejszym typem jest mózg uczący się, który buduje wiedzę o relacjach między cechami, akcjami i skutkami.

Proces decyzyjny przebiega w pętli. Agent skanuje otoczenie, przewiduje skutki potencjalnych działań w kontekście aktualnych potrzeb, wybiera akcję, a następnie konfrontuje predykcję z rzeczywistym wynikiem. Nowa wiedza jest zapisywana w postaci asocjacji ważonych siłą efektu. Ukryte cechy obiektów ujawniają się dopiero w wyniku interakcji, co wymusza eksplorację.

Uczenie nie jest scentralizowane wyłącznie na poziomie jednostki. Agenci raportują doświadczenia do wspólnego systemu konsolidacji wiedzy, który filtruje szum, wzmacnia powtarzalne zależności i stopniowo wygasza informacje słabe lub nieaktualne. Dzięki temu populacja uczy się szybciej, ale zachowuje lokalne różnice zachowań.

Całość działa w środowisku LibGDX, które pełni rolę powłoki symulacyjnej. Renderowanie i UI umożliwiają bezpośrednią inspekcję procesów poznawczych: aktualnych potrzeb, wag skojarzeń, ścieżek decyzyjnych i historii uczenia. System jest zaprojektowany jako narzędzie do obserwowania emergencji znaczeń, a nie do maksymalizacji abstrakcyjnego rewardu.

W skrócie: to platforma do badania, jak z prostych mechanizmów percepcji, potrzeb i doświadczenia powstaje zachowanie, które z zewnątrz wygląda na „inteligentne”.
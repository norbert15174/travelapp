# travelapp
Głównym celem pracy jest stworzenie aplikacji internetowej, która umożliwi integrację osób o zainteresowaniach podróżniczych. Pozwoli ona na utworzenie profilu osobowego, w którym będą przechowywane wspomnienia z odbytych podróży w formie albumów. Profil ten będzie także zawierać podstawowe informacje o użytkowniku, co sprawi, że inni podróżnicy o podobnych zainteresowaniach będą mogli nawiązać komunikację w celu zawarcia znajomości - będzie ona realizowana przy użyciu komunikatora tekstowego. Dodatkowym aspektem pracy jest umożliwienie użytkownikowi udostępniania albumów publicznie oraz tworzenia ich wspólnie ze znajomymi. 

#	Proces tworzenia aplikacji
##	Backend
#####	Stworzenie struktury bazy danych oraz implementacja mechanizmów integrujących bazę danych z aplikacją
#####	Połączenie aplikacji z Google Cloud Platform, które umożliwi na bezpieczne  przechowywanie danych użytkownika
#####	Autoryzacja użytkownika
#####	Stworzenie architektury funkcjonowania serwisu oraz wystawienie punktów końcowych pozwalających na komunikację z serwerem.
#####	Połączenie aplikacji z mechanizmem kolejkowania wiadomości
##	Frontend
#####	Stworzenie schematu wyglądu aplikacji przy użyciu narzędzi graficznych
#####	Implementacja wyglądu aplikacji, który umożliwi w dogodny sposób użytkownikowi korzystanie z platformy
#####	Implementacja mechanizmów pozwalających na przekazywanie danych od użytkownika
#####	Połączenie aplikacji z usługą Google Maps
##	Wdrażanie aplikacji na serwer
#####	Implementacja certyfikatu SSL
##### Instalacja:
##### - bazy danych
#####	- systemu kolejkowania wiadomości
#####	- serwera Tomcat oraz Node
##### - Dodanie plików uwierzytelniających, które pozwalają na łączenie aplikacji z Google Cloud Platform
##### - Utworzenie zmiennych środowiskowych, w celu bezpiecznego przechowywania informacji wrażliwych

#	Technologie:

## Backend
	Spring Boot
	Hibernate
	MySQL
	Kafka/RabbitMQ
	Google Cloud Platform
##	Frontend
	HTML, CSS( styled-components ), Javascript
	React
	Google Maps Platform
	Figma

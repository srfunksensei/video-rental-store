## Video Rental Store

Implementation of a system which provides following operations available:
* get inventory of films
* calculate the price for rentals
* renting one or several films
* returning films and calculating surcharges
* keep track of customer bonus bonus

The system is build using Spring Boot and Java 1.8. The following modules are included to build the app:
* Web
* JPA
* H2
* Lombok

The focus for this assignment is clean, extensible, robust and modular architecture. Besides this the system was implemented to use as much as possible design patterns. 

As this is simple demo use-case system, no test are written, nor data validation or error handling is performed.  

## Structure overview

```
|____dto
|____repository
| |____film
| | |____specification
| |____rent
| |____price
| |____customer
|____assembler
| |____resource
|____controller
|____model
|____service
```

### Model

The parent class of all entities is `AbstractEntity` which contains common properties (such as `id`, `createdDate`, etc.). Every entity class is subclass of this one.

#### Price

`Price` is modeled with a single table inheritance strategy. There is specific price for basic and premium type of films, as well as price for rental. The price is modeled this way to give us the ability for easy manipulation with price values. If there is requirement to update price or add a new one, we can simply add a migration script or execute update on data. This will not affect current rentals, only the future one (calculated and persisted).

#### Bonus point

`Bonus point` is modeled with a single table inheritance strategy. There is one bonus point type for new film releases and one for the rest. Bonus points are modeled in same fashion as price to give us opportunity to change values for types. Also we have the flexibility to add new ones to meet new requirements.

#### Film

`Film` is a simple representation of film abstraction consisting of basic film  information, plus relationship to bonus point as well as relationship to rental.

#### Rental

`Rental` is representing one customer order. It has two price fields - one is actual price and the other one is the price charged to the customer. Rental has one or more films associated with it and customer for whom order has been created. Rental has it's own price which is calculated before persisting it and is independent of film price. If film price is updated in meantime we will have information how much actual rental cost and how much was charged to the customer without loosing history.

#### RentalFilm

As rental can have many films, and each film can be rented for any number of days, `RentalFilm` is a interconnection between film and rental with the number of days rented. This is basically many-to-many relationship with additional parameter.

#### Customer

`Customer` consists of simple properties which define a customer abstraction. Each customer can have many rentals. Customer object itself contains number of bonus point which are accumulated every time when the rent is checked in. This way we keep the track of bonus points and providing the ability to independently update bonus point without loosing information.

### Data manipulation

To handle the tedious database interactions Spring Data JPA is turned on. Spring Data repositories are interfaces with methods supporting records manipulation against a back end data store. Each repository is implemented keeping in mind number of data which can be returned to the user. E.g. `FilmRepository` extends `JpaRepository` in order to limit number of data returned to the operator.

#### Film specification

To find films more efficiently there are two custom specifications:
* `FilmWithTitleLike` specification which finds all films using SQLs `LIKE` operator
* `FilmWithTypeEqual` specification which find all films where type is equal the given one

On top of this there is a specification builder which can combine multiple specification for easier data manipulation. The builder is implemented in a brute fashion.

#### Database initalization

An application with no data is not very interesting, so database initializor is brought to action at startup. As `H2` is used the data will be lost and recreated every time the application is killed.

The application is preloaded with films, prices and customer.

### Services

Each service contains business logic for the system separates Controller from Model.

#### Price calculation

To have the flexibility to calculate the rental price easily, there is a `FilmCalculationStrategy` interface which represents contracts for actual calculations. As price is calculated differently for each film type, it was decided that each film type will have its own calculation strategy.

There is additional abstraction `FilmPriceCalculator<P extends Price>` in between `FilmCalculationStrategy` and actual calculator implementation as the prices are related to film type.

`RentCalculator` is component responsible for calculating total rental price as well as surcharges. It receives films for which price needs to be determined and then delegates the job of actual calculation to specific calculator, aggregates the prices and returns it back.

The surcharges are calculated based on the following premise: use the price calculation for the films with number of days for which film has been rented (as this is a new order) and then subtract the actual price (paid upon check out).

### Controllers

The controllers expose API endpoints to accomplish operations stated at the beginning. There are three major endpoint roots:
* `/films` - operations related to films
	* `GET /` - returns list of films in inventory paginated
	* `GET /{filmId}` - returns film specified with id
* `/rents` - operations related to rentals
 	* `GET /` - returns list of rentals in system paginated
	* `GET /{rentId}` - returns rental specified with id
	* `POST /calculate` - calculates rental price based on movies and number of days which customer wants to rent
	* `POST /checkIn` - creates a new rental assigned to the customer
	* `PUT /checkOut/{rentId}` - updates price and rental status upon check out
	* `DELETE /{rentId}` - deletes rental specified with id
* `/customers` - operations related to customers
	* `GET /` - returns list of customer in inventory paginated
	* `GET /{customerId}` - returns customer specified with id
	* `DELETE /{customerId}` - deletes customer specified with id

The return type is HAL mediatype object(s).

## Building, Running and Testing

```bash
$ ./mvnw clean spring-boot:run
```
or alternatively using your installed maven version
```bash
mvn clean spring-boot:run
```

To see the application in action you can use `curl` or `postman` collection provided.

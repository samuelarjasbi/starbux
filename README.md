# Starbux

## Installation and Configuration

Clone the repository by the following command:

```bash
$> git clone https://github.com/mohammadarjasbi/starbux.git
```

Run the following command in terminal or CMD prompt to run dockerized package:

```bash
$> docker-compose up
```

or for background running:
```bash
$> docker-compose up -d
```


You'll must see the following details:

```bash
2021-12-01 02:02:12.955 DEBUG 28636 --- [  restartedMain] ySourcesPropertyResolver$DefaultResolver : Found key 'spring.liveBeansView.mbeanDomain' in PropertySource 'systemProperties' with value of type String
2021-12-01 02:02:12.957  INFO 28636 --- [  restartedMain] c.s.starbuxapi.StarbuxapiApplication     : Started StarbuxapiApplication in 6.607 seconds (JVM running for 8.407)
2021-12-01 02:02:12.958 DEBUG 28636 --- [  restartedMain] o.s.b.a.ApplicationAvailabilityBean      : Application availability state LivenessState changed to CORRECT
2021-12-01 02:02:12.959 DEBUG 28636 --- [  restartedMain] o.s.boot.devtools.restart.Restarter      : Creating new Restarter for thread Thread[main,5,main]
2021-12-01 02:02:12.959 DEBUG 28636 --- [  restartedMain] o.s.boot.devtools.restart.Restarter      : Immediately restarting application
2021-12-01 02:02:12.959 DEBUG 28636 --- [  restartedMain] o.s.boot.devtools.restart.Restarter      : Starting application com.starbux.starbuxapi.StarbuxapiApplication with URLs [file:~/starbux/target/classes/]
2021-12-01 02:02:12.960 DEBUG 28636 --- [  restartedMain] o.s.b.a.ApplicationAvailabilityBean      : Application availability state ReadinessState changed to ACCEPTING_TRAFFIC

```

After that in order to run test cases you have to use the following command **( In order to run tests properly you have to run docker containers first):**

```bash
$> mvn clean test
```

You must see
```bash
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```


**It means application is running and ready to use.**

## Usage

**First for using API EndPoint you need to Authorized for example:**


In order to create a user with Rolles, you can Post a JSON string with custom value to the following URL:


URL (accepted method = post)
```bash
http://localhost:6868/api/auth/signup
```
JSON
```bash
{
    "username":"test",
    "password":"1234",
    "email":"test@gmail.com",
    "role":["admin","user","mod"] //user for regular users, mod for moderators
}
```

and as a result, if your sent JSON was correct, you will get:

```bash
{
    "message": "User registered successfully!"
}
```

**(also you can use POSTMAN or any other tools to send requests through the web methods)**

![alt text](https://i.postimg.cc/2S0DgRfL/2.jpg)



**After signup now you can log in with the following data**

URL (accepted method = post)
```bash
http://localhost:8089/api/auth/signin
```
JSON
```bash
{
    "username":"test",
    "password":"1234",
}
```

**After signin you will get some data as well as access token we need it for using other Endpoints**

```bash
{
    "id": 1,
    "username": "test",
    "email": "test@gmail.com",
    "roles": [
        "ROLE_USER",
        "ROLE_ADMIN",
        "ROLE_MODERATOR"
    ],
    "tokenType": "Bearer",
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjM4MzEyOTI3LCJleHAiOjE2MzgzOTkzMjd9.BM07G8DPaTSwacYMNhDSrPyZ25j8VM1HtvLv8xzMtmfaJL7-gna-1lkqKvItGMQFnB4BxNVMxhTMYmNzO1Mu6w"
}
```

**We will copy the access token as a string and add ("Bearer ") at the first**
```bash
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjM4MzEyOTI3LCJleHAiOjE2MzgzOTkzMjd9.BM07G8DPaTSwacYMNhDSrPyZ25j8VM1HtvLv8xzMtmfaJL7-gna-1lkqKvItGMQFnB4BxNVMxhTMYmNzO1Mu6w
```

**Now for gaining access to other Endpoints using token we have to set authorization parameter in our request header, to do that in Postman, for each request tab, you have to go in the headers tab and add a new parameter at the end of the list. as key, you have to enter Authorization and as value, you have to enter accessToken string (Image Below)**

![alt text](https://i.postimg.cc/SNNqdNBb/3.jpg)



>All of the Endpoints with required roles and required URL method and data attached.

>`https://localhost:port/(EndPoints)`   <URL Method>  `{(data required)}` "role required" 


**Drink EndPoints**

Add Drink
>`http://localhost:6868/api/products/add/drink` <POST> `{ "name" : string ,"quantity" : int ,"price": Double }` admin - moderator

Add list of drinks
>`http://localhost:6868/api/products/add/drinks` <POST> `[{ "name" : string ,"quantity" : int ,"price": Double },{ "name" : string ,"quantity" : int ,"price": Double }]` admin - moderator

Get all drinks
>`http://localhost:6868/api/products/Drinks` <GET> {} any role

Find a drink by Id
>`http://localhost:6868/api/products/DrinksById/{id}` <GET> `data will get from URL` any role

Find a drink by name
>`http://localhost:6868/api/products/Drinks/{name}` <GET> `data will get from URL` any role

Update a drink
>`http://localhost:6868/api/products/update/drink` <PUT> `{ "name" : string ,"quantity" : int ,"price": Double }` admin - moderator

Delete a drink by id
>`http://localhost:6868/api/products/delete/Drink/{id}` <DELETE> `data will get from URL`  admin 

**Toppings EndPoints**

Add a topping
>`http://localhost:6868/api/products/add/topping` <POST> `{ "name" : string ,"quantity" : int ,"price": Double }` admin - moderator

Add list of toppings
>`http://localhost:6868/api/products/add/toppings` <POST> `[{ "name" : string ,"quantity" : int ,"price": Double },{ "name" : string ,"quantity" : int ,"price": Double }]` admin - moderator

Get all toppings
>`http://localhost:6868/api/products/toppings` <GET> {} any role

Find a topping by Id
>`http://localhost:6868/api/products/toppingsById/{id}` <GET> `data will get from URL` any role

Find a topping by name
>`http://localhost:6868/api/products/topping/{name}` <GET> `data will get from URL` any role

Update a topping
>`http://localhost:6868/api/products/update/topping` <PUT> `{ "name" : string ,"quantity" : int ,"price": Double }` admin - moderator

Delete a topping by id
>`http://localhost:6868/api/products/delete/topping/{id}` <DELETE> `data will get from URL`  admin 


**Add products to cart**: Any drink will add with a topping or null

Add a drink to cart
>`http://localhost:6868/api/addtocart/addProduct` <POST> `{"drinks_id":string,"toppings_id":string,"userId":string,"qty":string}` any role

Update a product quantity
>`http://localhost:6868/api/addtocart/updateQtyForCart` <PUT> `{"cartId":string,"userId":string,"qty":string}` any role

Delete a product form cart
>`http://localhost:6868/api/addtocart/removeProductFromCart` <DELETE> `{"cartId":string,"userId":string}` any role


**Check out order EndPoints**

Check out order
>`http://localhost:6868/api/order/checkout_order` <POST> `{"userId":string,"deliveryAddress":string}` any role

Get orders by user id
>`http://localhost:6868/api/order/getOrdersByUserId` <POST> `{"userId":string}` any role

Delete data by order id
>`http://localhost:6868/api/addtocart/deleteOrderById/{id}` <DELETE> `data will get from URL` admin role


**Reports**

The total amount of orders per customer.
>`http://localhost:6868/api/reports/orders` <get> `{}` Admin role

Most used toppings for drinks.
>`http://localhost:6868/api/reports/toppings` <get> `{}` Admin role

Delete reports by user-id
>`http://localhost:6868/api/reports/DeleteReportByUserId/{userId}` <DELETE> `data will get from URL` Admin role


## Description

It's almost my first app with Spring boot framework so do a favor and report any bug you'll find. I'm sure there is a bunch of it. 

## Test Coverage Results

**Total (68%)**


![alt text](https://i.postimg.cc/25cbTK6C/r.jpg)

**Per Packages**


![alt text](https://i.postimg.cc/D0sSvfcz/5.jpg)

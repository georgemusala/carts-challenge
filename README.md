# carts-challenge

How to start the app:

1. Make sure docker and docker compose are installed
2. In a terminal CD into repo root folder
3. Execute: docker compose up
    
    The following actions will be performed: 
     - app will build with maven and docker image created (see Dockerfile)
     - using app image and postgresql image, the app will start on port 8080 and database on port 5432

How to use the app:

1. To create a cart:
   
    Make a POST call to http://localhost:8080/carts/create
    payload example:

   ```
   {
      "code": "2428381",
      "creationDate": "2024-09-13",
      "currency": "RON",
      "totalPrice": 103
   }
   ```

2. To update a cart:
    
    Make a POST call to http://localhost:8080/carts/update
    payload example:

   ```
   {
      "code": "2428381",
      "creationDate": "2024-09-13",
      "currency": "RON",
      "totalPrice": 103
   }
   ```
